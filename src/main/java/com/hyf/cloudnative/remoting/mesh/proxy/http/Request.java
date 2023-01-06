package com.hyf.cloudnative.remoting.mesh.proxy.http;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Request {

    private String url;
    private HttpMethod httpMethod;
    private Map<String, List<String>> headers;
    private Map<String, List<String>> params;
    private Object body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(url, request.url) && httpMethod == request.httpMethod && Objects.equals(headers, request.headers) && Objects.equals(params, request.params) && Objects.equals(body, request.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod, headers, params, body);
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", httpMethod=" + httpMethod +
                ", headers=" + headers +
                ", params=" + params +
                ", body=" + body +
                '}';
    }
}
