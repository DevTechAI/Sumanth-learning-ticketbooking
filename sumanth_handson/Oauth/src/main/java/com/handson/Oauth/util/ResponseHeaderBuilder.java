package com.handson.Oauth.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public final class ResponseHeaderBuilder
{
    private ResponseHeaderBuilder() {
    }

    public static HttpHeaders jsonHeaders(String resourceName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Application", "JWT-admin");
        headers.add("X-Resource", resourceName);
        return headers;
    }
}
