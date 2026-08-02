package com.minhtan.qlptclient.service;

import java.math.BigDecimal;

public class MethodAmenity {
    private static final BigDecimal PRICE_MULTIPLIER = BigDecimal.valueOf(1000);

    public static BigDecimal convertToVnd(BigDecimal value) {
        return value == null ? null : value.multiply(PRICE_MULTIPLIER);
    }

    public static String resolveMediaUrl(String url) {

        if (url == null || url.isBlank()) {
            return "";
        }

        if (url.startsWith("http://")
                || url.startsWith("https://")
                || url.startsWith("file:")) {
            return url;
        }

        if (url.startsWith("/uploads/")) {
            return ApiClient.getInstance().getBaseUrl() + url;
        }

        return ApiClient.getInstance().getBaseUrl() + "/uploads/" + url;
    }
}
