package com.uni.impact.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHeaderRequestWrapper extends HttpServletRequestWrapper {
    private final String headerName;
    private final String headerValue;

    public CustomHeaderRequestWrapper(HttpServletRequest request,
                                      String headerName,
                                      String headerValue) {
        super(request);
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public String getHeader(String name) {
        if (headerName.equalsIgnoreCase(name)) {
            return headerValue;
        }
        return super.getHeader(name);
    }
}