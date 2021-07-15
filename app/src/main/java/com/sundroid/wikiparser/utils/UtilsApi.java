package com.sundroid.wikiparser.utils;

public class UtilsApi {
    private static final String BASE_URL_API = "https://en.wikipedia.org/w/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
