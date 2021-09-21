package com.example.g_summerassignmnet.Common;

import com.example.g_summerassignmnet.Testbooth_location.Result;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APiInterface {

    @GET("maps/api/distancematrix/json")
    Single<Result> getDistance(@Query("key") String key,
                               @Query("origins") String origion,
                               @Query("destinations") String destination,
                               @Query("mode") String driving);


}
