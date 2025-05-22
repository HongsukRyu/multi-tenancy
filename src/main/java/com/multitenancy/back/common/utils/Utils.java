package com.multitenancy.back.common.utils;

import com.multitenancy.back.common.model.Const;
import com.multitenancy.back.common.model.RequestModel;
import com.multitenancy.back.common.model.Success;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Utils {

    private static final String[] HEADERS_TO_TRY = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

    public String getClientIpAddress(HttpServletRequest request) {

        for (String header : HEADERS_TO_TRY) {
            if (header == "X-Forwarded-For") {
                System.out.println("X-Forwarded-For" + " :: [TEST]");
            }
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private static final String CHAR_REGULAR_EXPRESSION =
            "!\\\"#$%&'()*+,.:;<=>?@[]^_`{]+$|.*select.*|.*create.*|.*update.*|.*alter.*|.*delete.*|.*insert.*|.*drop.*|.*--.*|.*union.*|.*join.*";

    private static final Pattern unsecuredCharPattern =
            Pattern.compile(CHAR_REGULAR_EXPRESSION, Pattern.CASE_INSENSITIVE);

    public static <T> boolean isEmptyOrNull(T collection) {
        if (collection instanceof Map) {
            return ((Map<?, ?>) collection).isEmpty();
        }
        if (collection instanceof List) {
            return ((List<?>) collection).isEmpty();
        }
        if (collection instanceof Set) {
            return ((Set<?>) collection).isEmpty();
        }
        return collection == null;
    }

    public RequestModel getAttributeRequestModel(HttpServletRequest request) {

        RequestModel requestModel = new RequestModel();
        String userId = request.getAttribute("userId").toString();
        int userType = Integer.parseInt(request.getAttribute("userType").toString());

        requestModel.setUserId(userId);
        requestModel.setType(userType);

        return requestModel;
    }

    /**
     * @return String
     */
    public String randNumCreate() {
        StringBuilder tempNum = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int rndVal = (int) (Math.random() * 62);
            if (rndVal < 10) {
                tempNum.append(rndVal);
            } else if (rndVal > 35) {
                tempNum.append((char) (rndVal + 61));
            } else {
                tempNum.append((char) (rndVal + 55));
            }
        }
        return tempNum.toString();
    }

    public ResponseEntity<Success> returnEntity(Success success) {
        if (success.isSuccess()) {
            return ResponseEntity.ok().body(success);
        } else if (success.getErrorCode() != null && success.getErrorCode().equals(Const.UNAUTHORIZED)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
        }
        return ResponseEntity.badRequest().body(success);
    }
}