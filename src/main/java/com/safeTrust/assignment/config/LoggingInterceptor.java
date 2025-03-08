package com.safeTrust.assignment.config;

import com.safeTrust.assignment.constant.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        logger.info(Message.REQUEST_LOG, request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex) {
        logger.info(Message.RESPONSE_LOG, request.getMethod(), request.getRequestURI(), response.getStatus());

        if (ex != null) {
            logger.error(Message.EXCEPTION_OCCURRED, ex.getMessage(), ex);
        }
    }
}
