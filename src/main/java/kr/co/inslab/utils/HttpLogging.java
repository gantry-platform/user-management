package kr.co.inslab.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpLogging {
    void logRequest(HttpServletRequest request, Object body);
    void logResponse(HttpServletRequest request, HttpServletResponse response, Object body);
}
