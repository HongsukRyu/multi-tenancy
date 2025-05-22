package com.multitenancy.back.common.filter;

import com.multitenancy.back.common.config.TenantContext;
import jakarta.servlet.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String tenantId = httpServletRequest.getHeader("X-TENANT-ID");

        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId);
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            TenantContext.clear();
        }
    }
}
