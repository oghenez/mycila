package com.mycila.inject.web;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 * @date 2013-01-30
 */
@Singleton
public final class HttpContextFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpContext.change((HttpServletRequest) request, (HttpServletResponse) response);
        try {
            chain.doFilter(request, response);
        } finally {
            HttpContext.end();
        }
    }
}
