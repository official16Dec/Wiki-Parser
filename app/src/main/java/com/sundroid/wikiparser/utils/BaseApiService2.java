package com.sundroid.wikiparser.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaseApiService2 {
    @GET("api.php?")
    Call<ResponseBody> getFeaturedImages(@Query("action") String action,
                                         @Query("prop") String prop,
                                         @Query("iiprop") String iiprop,
                                         @Query("generator") String generator,
                                         @Query("gcmtype") String gcmtype,
                                         @Query("gcmtitle") String gcmtitle,
                                         @Query("format") String format,
                                         @Query("utf8") String utf8);
}
