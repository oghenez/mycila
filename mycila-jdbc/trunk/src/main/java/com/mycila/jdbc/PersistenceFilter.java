package com.mycila.jdbc;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public final class PersistenceFilter implements Filter {

    private final UnitOfWork unitOfWork;

    @Inject
    public PersistenceFilter(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            unitOfWork.begin();
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            unitOfWork.end();
        }
    }
}
