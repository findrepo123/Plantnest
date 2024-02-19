package com.project.api.utilities;

import org.springframework.stereotype.Component;

import java.util.UUID;

public class StringUtils {
    public static String convertToSlug(String productName) {
        if (productName == null) return null;

        String slug = productName.toLowerCase().replaceAll("\\s+", "-");
        String uuidPart = UUID.randomUUID().toString().substring(0, 6);
        return slug + "-" + uuidPart;
    }
}
