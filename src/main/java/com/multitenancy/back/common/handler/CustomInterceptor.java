package com.multitenancy.back.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Spring Framework v5.0 부터는 HandlerInterceptor interface 사용
// HandlerInterceptorAdapter -> HandlerInterceptor
//import org.springframework.web.servlet.handler.HandlerInterceptor;

@Slf4j
public class CustomInterceptor extends HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // This method is called before the actual handler is executed.
        // You can perform pre-processing here.
        return true; // Return true to proceed with the execution chain.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        // This method is called after the handler is executed but before the view is rendered.
        // You can perform post-processing here.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        // This method is called after the view has been rendered.
        // You can perform cleanup or logging here.
    }
}

