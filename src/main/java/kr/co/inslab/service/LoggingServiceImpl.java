package kr.co.inslab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoggingServiceImpl implements LoggingService {

    private final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);

    @Override
    public void logRequest(HttpServletRequest request, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(request);

        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(request.getMethod()).append("] ");
        stringBuilder.append("path=[").append(request.getRequestURI()).append("] ");
        stringBuilder.append("headers=[").append(buildHeadersMap(request)).append("] ");

        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        }

        if (body != null) {
            stringBuilder.append("body=[" + body + "]");
        }

        logger.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RESPONSE ");
        stringBuilder.append("method=[").append(request.getMethod()).append("] ");
        stringBuilder.append("path=[").append(request.getRequestURI()).append("] ");
        stringBuilder.append("responseHeaders=[").append(buildHeadersMap(response)).append("] ");
        stringBuilder.append("responseBody=[").append(body).append("] ");

        logger.info(stringBuilder.toString());
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}