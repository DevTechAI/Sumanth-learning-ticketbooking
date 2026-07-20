package com.handson.Oauth.util;

import org.springframework.http.HttpHeaders;

public final class ResponseHeaderBuilder {

    private ResponseHeaderBuilder() {
    }

    public static HttpHeaders jsonHeaders(String resourceName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Application", "Oauth");
        headers.add("X-Resource", resourceName);
        return headers;
    }
}
