package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.proxy.http.file.MultipartFileAdapter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

public class RequestAnnotationMetadataUtils {

    private static final List<MetadataParser> metadataParsers = new ArrayList<>();
    private static final List<MetadataApplier> metadataAppliers = new ArrayList<>();

    static {
        addMetadataProcessor(new RequestMappingMetadataProcessor());
        addMetadataProcessor(new MatrixVariableMetadataProcessor());
        addMetadataProcessor(new PathVariableMetadataProcessor());
        addMetadataProcessor(new RequestParamMetadataProcessor());
        addMetadataProcessor(new RequestHeaderMetadataProcessor());
        addMetadataProcessor(new CookieValueMetadataProcessor());
        addMetadataProcessor(new RequestPartMetadataProcessor());
        addMetadataProcessor(new RequestBodyMetadataProcessor());
    }

    public static void parse(Class<?> clazz) {
        for (MetadataParser metadataParser : metadataParsers) {
            metadataParser.parseClass(clazz);

            for (Method method : clazz.getDeclaredMethods()) {
                metadataParser.parseMethod(clazz, method);

                for (int i = 0; i < method.getParameters().length; i++) {
                    Parameter parameter = method.getParameters()[i];
                    metadataParser.parseParameter(clazz, method, i, parameter);
                }
            }
        }
    }

    public static void apply(Request request, Method method, Object[] args) {
        metadataAppliers.forEach(applier -> applier.applyRequest(request, method, args));
    }

    public static void addMetadataProcessor(MetadataProcessor metadataProcessor) {
        metadataParsers.add(metadataProcessor);
        metadataAppliers.add(metadataProcessor);
    }

    public static void addMetadataParser(MetadataParser metadataParser) {
        metadataParsers.add(metadataParser);
    }

    public static void addMetadataProcessor(MetadataApplier metadataApplier) {
        metadataAppliers.add(metadataApplier);
    }

    private static String convert(Object o) {
        if (o == null) {
            return "";
        }
        return String.valueOf(o);
    }

    private static void iterateObject(Object o, Consumer<Object> iteratorConsumer) {
        if (o instanceof Iterable) {
            for (Object item : ((Iterable<?>) o)) {
                iteratorConsumer.accept(item);
            }
        } else if (o instanceof Object[]) {
            for (Object item : ((Object[]) o)) {
                iteratorConsumer.accept(item);
            }
        } else {
            iteratorConsumer.accept(o);
        }
    }

    public interface MetadataParser {
        void parseClass(Class<?> clazz);

        void parseMethod(Class<?> clazz, Method method);

        void parseParameter(Class<?> clazz, Method method, int index, Parameter parameter);
    }

    public interface MetadataApplier {
        void applyRequest(Request request, Method method, Object[] args);
    }

    public interface MetadataProcessor extends MetadataParser, MetadataApplier {
        default void parseClass(Class<?> clazz) {
        }

        default void parseMethod(Class<?> clazz, Method method) {
        }

        default void parseParameter(Class<?> clazz, Method method, int index, Parameter parameter) {
        }

        default void applyRequest(Request request, Method method, Object[] args) {
        }
    }

    public static abstract class AbstractMetadataProcessor<T> implements MetadataProcessor {

        protected final Map<MetadataKey, List<IndexItem<T>>> indexItemMap = new HashMap<>();

        @Override
        public void parseParameter(Class<?> clazz, Method method, int index, Parameter parameter) {
            T t = parseIndexItem(parameter);
            if (t == null) {
                return;
            }

            MetadataKey mapKey = new MetadataKey(clazz, method);
            indexItemMap.putIfAbsent(mapKey, new ArrayList<>());
            IndexItem<T> indexItem = new IndexItem<>(index, t);
            indexItemMap.get(mapKey).add(indexItem);
        }

        @Override
        public void applyRequest(Request request, Method method, Object[] args) {
            List<IndexItem<T>> indexItems = indexItemMap.get(new MetadataKey(method.getDeclaringClass(), method));
            if (indexItems == null) {
                return;
            }
            applyIndexItem(request, indexItems, args);
        }

        protected abstract T parseIndexItem(Parameter parameter);

        protected abstract void applyIndexItem(Request request, List<IndexItem<T>> indexItems, Object[] args);

    }

    private static class RequestMappingMetadataProcessor implements MetadataProcessor {

        private final Map<MetadataKey, RequestMappingMetadata> metadataMap = new HashMap<>();

        @Override
        public void parseMethod(Class<?> clazz, Method method) {
            RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(clazz, RequestMapping.class);
            RequestMapping methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
            if (classAnnotation == null && methodAnnotation == null) {
                return;
            }

            MetadataKey mapKey = new MetadataKey(clazz, method);
            RequestMappingMetadata metadata = new RequestMappingMetadata();
            metadataMap.putIfAbsent(mapKey, metadata);

            setPath(metadata, classAnnotation, methodAnnotation);
            setRequestMethod(metadata, classAnnotation, methodAnnotation);
            setParams(metadata, classAnnotation, methodAnnotation);
            setHeaders(metadata, classAnnotation, methodAnnotation);
        }

        @Override
        public void applyRequest(Request request, Method method, Object[] args) {
            RequestMappingMetadata metadata = metadataMap.get(new MetadataKey(method.getDeclaringClass(), method));
            if (metadata == null) {
                return;
            }

            if (StringUtils.hasText(request.getUrl())) {
                request.setUrl(WebUtils.concat(Arrays.asList(request.getUrl(), metadata.getPath())));
            } else {
                request.setUrl(metadata.getPath());
            }
            if (request.getHttpMethod() == null) {
                request.setHttpMethod(metadata.getHttpMethod());
            }
            request.getHeaders().putAll(metadata.getHeaders());
            request.getParams().putAll(metadata.getParams());
        }

        private void setPath(RequestMappingMetadata metadata, RequestMapping classAnnotation, RequestMapping methodAnnotation) {

            List<String> paths = new ArrayList<>();

            if (classAnnotation != null) {
                String[] value = classAnnotation.value();
                paths.addAll(Arrays.asList(value));
            }
            if (methodAnnotation != null) {
                String[] value = methodAnnotation.value();
                paths.addAll(Arrays.asList(value));
            }

            metadata.setPath(WebUtils.concat(paths));
        }

        private void setRequestMethod(RequestMappingMetadata metadata, RequestMapping classAnnotation, RequestMapping methodAnnotation) {
            HttpMethod httpMethod;
            RequestMethod[] method = {};

            if (methodAnnotation != null && methodAnnotation.method().length > 0) {
                method = methodAnnotation.method();
            } else if (classAnnotation != null) {
                method = classAnnotation.method();
            }

            if (method.length == 0) {
                httpMethod = HttpMethod.GET; // default get
            } else {
                httpMethod = HttpMethod.valueOf(method[0].name());
            }
            metadata.setHttpMethod(httpMethod);
        }

        private void setParams(RequestMappingMetadata metadata, RequestMapping classAnnotation, RequestMapping methodAnnotation) {
            Map<String, List<String>> params = new HashMap<>();

            if (methodAnnotation != null && methodAnnotation.params().length > 0) {
                params.putAll(parseList(methodAnnotation.params()));
            } else if (classAnnotation != null) {
                params.putAll(parseList(classAnnotation.params()));
            }

            if (!params.isEmpty()) {
                metadata.getParams().putAll(params);
            }
        }

        private void setHeaders(RequestMappingMetadata metadata, RequestMapping classAnnotation, RequestMapping methodAnnotation) {
            Map<String, List<String>> headers = new HashMap<>();

            if (methodAnnotation != null && methodAnnotation.produces().length > 0) {
                headers.put(HttpHeaders.ACCEPT, Arrays.asList(methodAnnotation.produces()));
            } else if (classAnnotation != null && classAnnotation.produces().length > 0) {
                headers.put(HttpHeaders.ACCEPT, Arrays.asList(classAnnotation.produces()));
            }

            if (methodAnnotation != null && methodAnnotation.consumes().length > 0) {
                headers.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(methodAnnotation.consumes()));
            } else if (classAnnotation != null && classAnnotation.consumes().length > 0) {
                headers.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(classAnnotation.consumes()));
            }

            if (methodAnnotation != null && methodAnnotation.headers().length > 0) {
                headers.putAll(parseList(methodAnnotation.headers()));
            } else if (classAnnotation != null) {
                headers.putAll(parseList(classAnnotation.headers()));
            }

            if (!headers.isEmpty()) {
                metadata.getHeaders().putAll(headers);
            }
        }

        private Map<String, List<String>> parseList(String[] params) {
            Map<String, List<String>> map = new HashMap<>();
            for (String header : params) {
                int index = header.indexOf('=');
                if (!header.contains("!=") && index >= 0) {
                    map.put(header.substring(0, index), new ArrayList<>(Collections.singleton(header.substring(index + 1).trim())));
                }
            }
            return map;
        }

        private static class RequestMappingMetadata {
            private String path;
            private HttpMethod httpMethod;
            private Map<String, List<String>> headers;
            private Map<String, List<String>> params;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public HttpMethod getHttpMethod() {
                return httpMethod;
            }

            public void setHttpMethod(HttpMethod httpMethod) {
                this.httpMethod = httpMethod;
            }

            public Map<String, List<String>> getHeaders() {
                if (headers == null) {
                    headers = new HashMap<>();
                }
                return headers;
            }

            public void setHeaders(Map<String, List<String>> headers) {
                this.headers = headers;
            }

            public Map<String, List<String>> getParams() {
                if (params == null) {
                    params = new HashMap<>();
                }
                return params;
            }

            public void setParams(Map<String, List<String>> params) {
                this.params = params;
            }

            @Override
            public String toString() {
                return "RequestMappingMetadata{" +
                        "path='" + path + '\'' +
                        ", httpMethod=" + httpMethod +
                        ", headers=" + headers +
                        ", params=" + params +
                        '}';
            }
        }
    }

    private static class MatrixVariableMetadataProcessor extends AbstractMetadataProcessor<MatrixVariableMetadataProcessor.MatrixVariableMetadata> {

        @Override
        protected MatrixVariableMetadata parseIndexItem(Parameter parameter) {
            MatrixVariable matrixVariableAnnotation = findMergedAnnotation(parameter, MatrixVariable.class);
            if (matrixVariableAnnotation == null) {
                return null;
            }

            if (!StringUtils.hasText(matrixVariableAnnotation.value()) && (parameter.getType() != Map.class)) {
                throw new IllegalArgumentException("@MatrixVariable value() must has text");
            }

            // if (!StringUtils.hasText(matrixVariableAnnotation.pathVar()) || ValueConstants.DEFAULT_NONE.equals(matrixVariableAnnotation.pathVar())) {
            //     throw new IllegalArgumentException("@MatrixVariable pathVar() must has text");
            // }
            // ignore, not throw exception
            if (!StringUtils.hasText(matrixVariableAnnotation.pathVar()) || ValueConstants.DEFAULT_NONE.equals(matrixVariableAnnotation.pathVar())) {
                return null;
            }

            return new MatrixVariableMetadata("{" + matrixVariableAnnotation.pathVar() + "}", matrixVariableAnnotation.value());
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<MatrixVariableMetadata>> indexItems, Object[] args) {
            if (!StringUtils.hasText(request.getUrl())) {
                return;
            }

            // assemble matrix url
            Map<String, StringBuilder> matrixMap = new HashMap<>();
            for (IndexItem<MatrixVariableMetadata> indexItem : indexItems) {
                int i = indexItem.getIndex();
                MatrixVariableMetadata matrixVariableMetadata = indexItem.getItem();

                if (args[i] == null) {
                    continue;
                }

                String pathVarPlaceHolder = matrixVariableMetadata.getPathVarPlaceHolder();
                matrixMap.putIfAbsent(pathVarPlaceHolder, new StringBuilder());
                matrixMap.get(pathVarPlaceHolder).append(convertMatrix(matrixVariableMetadata.getName(), args[i]));
            }

            // insert matrix value
            StringBuilder sb = new StringBuilder(request.getUrl());
            matrixMap.forEach((k, v) -> {
                int idx = sb.indexOf(k);
                if (idx != -1) {
                    sb.insert(idx + k.length(), v);
                }
            });

            request.setUrl(sb.toString());
        }

        private String convertMatrix(String name, Object o) {
            if (o instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) o;
                return map.keySet().stream().filter(key -> map.get(key) != null)
                        .filter(key -> !StringUtils.hasText(name) || key.equals(name))
                        .map(key -> ";" + key + "=" + convert(map.get(key))).collect(Collectors.joining());
            } else {
                return ";" + name + "=" + convert(o);
            }
        }

        private static class MatrixVariableMetadata {
            private final String pathVarPlaceHolder;
            private final String name;

            public MatrixVariableMetadata(String pathVarPlaceHolder, String name) {
                this.pathVarPlaceHolder = pathVarPlaceHolder;
                this.name = name;
            }

            public String getPathVarPlaceHolder() {
                return pathVarPlaceHolder;
            }

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return "MatrixVariableMetadata: " + pathVarPlaceHolder + "#" + name;
            }
        }
    }

    private static class PathVariableMetadataProcessor extends AbstractMetadataProcessor<String> {

        @Override
        protected String parseIndexItem(Parameter parameter) {
            PathVariable pathVariableAnnotation = findMergedAnnotation(parameter, PathVariable.class);
            if (pathVariableAnnotation == null) {
                return null;
            }

            Assert.hasText(pathVariableAnnotation.value(), "@PathVariable value() must has text");
            return pathVariableAnnotation.value();
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<String>> indexItems, Object[] args) {
            for (IndexItem<String> indexItem : indexItems) {
                int i = indexItem.getIndex();
                String key = indexItem.getItem();

                if (args[i] == null) {
                    continue;
                }

                request.setUrl(request.getUrl().replaceFirst("\\{" + key + "}", convert(args[i])));
            }
        }
    }

    private static class RequestParamMetadataProcessor extends AbstractMetadataProcessor<String> {

        @Override
        protected String parseIndexItem(Parameter parameter) {
            RequestParam requestParamAnnotation = findMergedAnnotation(parameter, RequestParam.class);
            if (requestParamAnnotation == null) {
                return null;
            }

            check(requestParamAnnotation, parameter);

            return requestParamAnnotation.value();
        }

        private void check(RequestParam requestParamAnnotation, Parameter parameter) {
            Type parameterType = parameter.getParameterizedType();
            if (parameterType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) parameterType;
                if (parameterizedType.getRawType() == Map.class) {
                    Assert.isTrue(parameterizedType.getActualTypeArguments()[0] == String.class, "Map key must be a String: " + parameterizedType.getActualTypeArguments()[0].getTypeName());
                }
            }
            if (!StringUtils.hasText(requestParamAnnotation.value()) && (parameter.getType() != Map.class)) {
                throw new IllegalArgumentException("@RequestParam value() must has text");
            }
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<String>> indexItems, Object[] args) {
            for (IndexItem<String> indexItem : indexItems) {
                int i = indexItem.getIndex();
                String key = indexItem.getItem();

                if (args[i] == null) {
                    continue;
                }

                // Map 类型特殊处理，内嵌仅支持一层
                if (args[i] instanceof Map) {
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) args[i]).entrySet()) {
                        Object k = entry.getKey();
                        Object v = entry.getValue();

                        List<String> mapParams = new ArrayList<>();
                        String mapKey = convert(k);
                        iterateObject(v, o -> mapParams.add(convert(o)));
                        request.getParams().put(mapKey, mapParams);
                    }
                } else {
                    List<String> params = new ArrayList<>();
                    iterateObject(args[i], o -> params.add(convert(o)));
                    request.getParams().put(key, params);
                }
            }
        }
    }

    private static class RequestHeaderMetadataProcessor extends AbstractMetadataProcessor<String> {

        @Override
        protected String parseIndexItem(Parameter parameter) {
            RequestHeader requestHeaderAnnotation = findMergedAnnotation(parameter, RequestHeader.class);
            if (requestHeaderAnnotation == null) {
                return null;
            }

            Assert.hasText(requestHeaderAnnotation.value(), "@RequestHeader value() must has text");
            return requestHeaderAnnotation.value();
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<String>> indexItems, Object[] args) {
            for (IndexItem<String> indexItem : indexItems) {
                int i = indexItem.getIndex();
                String key = indexItem.getItem();

                if (args[i] == null) {
                    continue;
                }

                request.getHeaders().put(key, new ArrayList<>(Collections.singleton(convert(args[i]))));
            }
        }
    }

    private static class CookieValueMetadataProcessor extends AbstractMetadataProcessor<String> {

        @Override
        protected String parseIndexItem(Parameter parameter) {
            CookieValue cookieValueAnnotation = findMergedAnnotation(parameter, CookieValue.class);
            if (cookieValueAnnotation == null) {
                return null;
            }

            Assert.hasText(cookieValueAnnotation.value(), "@CookieValue value() must has text");
            return cookieValueAnnotation.value();
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<String>> indexItems, Object[] args) {
            for (IndexItem<String> indexItem : indexItems) {
                int i = indexItem.getIndex();
                String key = indexItem.getItem();

                if (args[i] == null) {
                    continue;
                }

                request.getHeaders().putIfAbsent(HttpHeaders.COOKIE, new ArrayList<>());
                request.getHeaders().get(HttpHeaders.COOKIE).add(key + "=" + convert(args[i]));
            }
        }
    }

    private static class RequestPartMetadataProcessor extends AbstractMetadataProcessor<String> {

        @Override
        protected String parseIndexItem(Parameter parameter) {
            RequestPart requestPartAnnotation = findMergedAnnotation(parameter, RequestPart.class);
            if (requestPartAnnotation == null) {
                return null;
            }

            Assert.hasText(requestPartAnnotation.value(), "@RequestPart value() must has text");
            return requestPartAnnotation.value();
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<String>> indexItems, Object[] args) {

            Map<String, List<MultipartFile>> tempMultipartFilesMap = new HashMap<>();

            for (IndexItem<String> indexItem : indexItems) {
                int i = indexItem.getIndex();
                String key = indexItem.getItem();

                if (args[i] == null) {
                    continue;
                }

                Object arg = args[i];
                addMultipartFiles(tempMultipartFilesMap, key, arg);
            }

            // origin
            // request.setBody(FormDataParser.parse(tempMultipartFilesMap)); // TODO parse multipart not dependent on SpringMVC

            // SpringMVC
            // FormHttpMessageConverter
            MultiValueMap<String, Resource> multipartFilesMap = new LinkedMultiValueMap<>();
            for (String name : tempMultipartFilesMap.keySet()) {
                multipartFilesMap.addAll(name, tempMultipartFilesMap.get(name).stream().map(MultipartFile::getResource).collect(Collectors.toList()));
            }
            request.setBody(multipartFilesMap);
        }

        private void addMultipartFiles(Map<String, List<MultipartFile>> multipartFilesMap, String partName, Object arg) {
            multipartFilesMap.putIfAbsent(partName, new ArrayList<>());

            iterateObject(arg, o -> {
                if (o instanceof Part) {
                    multipartFilesMap.get(partName).add(new MultipartFileAdapter((Part) o));
                } else if (o instanceof MultipartFile) {
                    multipartFilesMap.get(partName).add((MultipartFile) o);
                }
            });
        }
    }

    private static class RequestBodyMetadataProcessor extends AbstractMetadataProcessor<Boolean> {

        @Override
        protected Boolean parseIndexItem(Parameter parameter) {

            // 文件上传的body优先，此处不设置解析值
            if (parameter.getAnnotation(RequestPart.class) != null) {
                return null;
            }

            Annotation[] annotations = parameter.getAnnotations();
            if (hasOtherParameterAnnotation(annotations)) {
                return null;
            }

            return parameter.getAnnotation(RequestBody.class) != null;
        }

        @Override
        protected void applyIndexItem(Request request, List<IndexItem<Boolean>> indexItems, Object[] args) {
            if (indexItems.size() <= 0) {
                return;
            }

            boolean bodySet = false;
            for (int i = 0; i < indexItems.size(); i++) {
                IndexItem<Boolean> indexItem = indexItems.get(i);
                if (indexItem.getItem() != null && indexItem.getItem()) {
                    request.setBody(args[i]);
                    bodySet = true;
                }
            }
            if (!bodySet) {
                int i = indexItems.get(0).getIndex(); // default first
                request.setBody(args[i]);
            }
        }

        private boolean hasOtherParameterAnnotation(Annotation[] annotations) {
            for (Annotation annotation : annotations) {
                for (Class<?> httpClass : Arrays.asList(RequestParam.class, PathVariable.class, RequestHeader.class, CookieValue.class)) {
                    if (httpClass == annotation.annotationType()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class MetadataKey {
        private final Class<?> clazz;
        private final Method method;

        public MetadataKey(Class<?> clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MetadataKey that = (MetadataKey) o;
            return Objects.equals(clazz, that.clazz) && Objects.equals(method, that.method);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, method);
        }

        @Override
        public String toString() {
            return "MetadataKey: " + clazz + "#" + method;
        }
    }

    public static class IndexItem<T> {
        private final int index;
        private final T item;

        public IndexItem(int index, T item) {
            this.index = index;
            this.item = item;
        }

        public int getIndex() {
            return index;
        }

        public T getItem() {
            return item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexItem<T> that = (IndexItem<T>) o;
            return index == that.index && Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, item);
        }

        @Override
        public String toString() {
            return "IndexItem: " + index + "#" + item;
        }
    }
}
