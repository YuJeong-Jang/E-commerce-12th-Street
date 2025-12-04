package com.commerce.common;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import java.util.List;
import java.util.Arrays;

import java.io.IOException;

@WebFilter("/*")
public class CustomConfiguration implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");

        String origin = req.getHeader("Origin");
        List<String> allowedOrigins = Arrays.asList("http://localhost:7070", "http://localhost:8080");

        // CORS 관련 헤더 추가
        if (origin != null) {
            res.setHeader("Access-Control-Allow-Origin", origin); // 부모 도메인 주소로 변경
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        }
        // OPTIONS 요청에 대해 바로 응답 (필요시)
        /*
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        */

        chain.doFilter(request, response);
    }
}
