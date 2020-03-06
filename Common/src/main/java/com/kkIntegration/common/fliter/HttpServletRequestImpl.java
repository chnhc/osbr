package com.kkIntegration.common.fliter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * description:
 * author: ckk
 * create: 2020-01-14 19:06
 */
public class HttpServletRequestImpl extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders;

    public HttpServletRequestImpl(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);

        if (headerValue != null){
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<>(customHeaders.keySet());

        // now add the headers from the wrapped request object
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }

        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }
}
