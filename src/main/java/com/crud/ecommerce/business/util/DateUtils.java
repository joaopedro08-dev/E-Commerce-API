package com.crud.ecommerce.business.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private static final ZoneId BRAZIL_ZONE = ZoneId.of("America/Sao_Paulo");

    private DateUtils() {}

    public static LocalDateTime databaseNow() {
        return LocalDateTime.now(BRAZIL_ZONE)
                .truncatedTo(ChronoUnit.SECONDS);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(BRAZIL_ZONE);
    }
}