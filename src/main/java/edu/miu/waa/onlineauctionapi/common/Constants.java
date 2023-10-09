package edu.miu.waa.onlineauctionapi.common;

public class Constants {
    public static final String API_URL_v1 = "/api/v1";

    public static final String ENCODER_ID = "bcrypt";
    public static final String API_URL_PREFIX = "/api/v1/**";

    public static final String SIGNUP_URL = API_URL_v1 + "/users";
    public static final String TOKEN_URL = API_URL_v1 + "/auth/token";

    public static final String PRODUCTS_URLs = API_URL_v1 + "/products/**";

    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String ROLE_CLAIM = "roles";
    public static final String AUTHORITY_PREFIX = "ROLE_";
}
