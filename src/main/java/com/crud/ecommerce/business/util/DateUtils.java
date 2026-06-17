package com.crud.ecommerce.business.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

final public class DateUtils {

    private DateUtils() {}

    public static LocalDateTime databaseNow() {
        return LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}