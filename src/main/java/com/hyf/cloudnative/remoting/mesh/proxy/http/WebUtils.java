package com.hyf.cloudnative.remoting.mesh.proxy.http;

import java.util.List;

public class WebUtils {

    public static String concat(List<String> uris) {
        return concat(uris, true);
    }

    public static String concat(List<String> uris, boolean keepLastSlash) {
        StringBuilder sb = new StringBuilder();
        for (String uri : uris) {
            if (uri == null || uri.trim().length() == 0) {
                continue;
            }
            if (!(uri.startsWith("http") || uri.startsWith("/"))) { // prefix
                sb.append("/");
            }
            if (uri.endsWith("/")) { // suffix
                uri = uri.substring(0, uri.length() - 1);
            }
            sb.append(uri);
        }

        if (keepLastSlash && uris.get(uris.size() - 1).endsWith("/")) {
            sb.append("/");
        }

        return sb.toString();
    }
}
