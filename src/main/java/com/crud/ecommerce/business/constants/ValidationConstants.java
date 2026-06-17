package com.crud.ecommerce.business.constants;

import java.math.BigDecimal;
import java.util.regex.Pattern;

final public class ValidationConstants {

    private ValidationConstants() {}

    final public static class Product {
        public static final Pattern NAME_PATTERN =
                Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s\\-.,()&]{3,120}$");
        public static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^.{20,500}$");
        public static final int STOCK_MAX_LIMIT = 10000;
        public static final BigDecimal PRICE_MAX_LIMIT = BigDecimal.valueOf(10000);
    }

    final public static class Client {
        public static final Pattern FULL_NAME_PATTERN =
                Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{3,100}$");
        public static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
        public static final Pattern PHONE_PATTERN =
                Pattern.compile("^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$");
        public static final Pattern CPF_PATTERN =
                Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
        public static final Pattern RG_PATTERN =
                Pattern.compile("^\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Xx]$");
    }

    final public static class Address {
        public static final Pattern STATE_PATTERN = Pattern.compile("^[A-Z]{2}$");
        public static final Pattern ADDRESS_NUMBER_PATTERN =
                Pattern.compile("^[0-9]{1,6}[A-Za-z]?$");
        public static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^\\d{5}-\\d{3}$");
    }

    final public static class Review {
        public static final int RATING_MIN = 1;
        public static final int RATING_MAX = 5;
        public static final Pattern TITLE_PATTERN =
                Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s\\-.,!?()]{3,100}$");
        public static final int COMMENT_MIN = 10;
        public static final int COMMENT_MAX = 1000;
    }
}