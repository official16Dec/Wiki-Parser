package com.sundroid.wikiparser.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaseApiService {

//    @POST("get_city")
//    Call<ResponseBody> getCity();
//
//
//    @FormUrlEncoded
//    @POST("signup_user")
//    Call<ResponseBody> signUpUser(@Field("user_mobile") String user_mobile,
//                                  @Field("user_name") String user_name);

    @GET("api.php?")
    Call<ResponseBody> getCategories(@Query("format") String format,
                                     @Query("action") String action,
                                     @Query("list") String list,
                                     @Query("acprefix") String acprefix,
                                     @Query("formatversion") String formatversion);

    @GET("api.php?")
    Call<ResponseBody> getArticles(@Query("format") String format,
                                   @Query("action") String action,
                                   @Query("generator") String generator,
                                   @Query("grnnamespace") String grnnamespace,
                                   @Query("prop") String prop,
                                   @Query("rvprop") String rvprop,
                                   @Query("grnlimit") String grnlimit);
}
