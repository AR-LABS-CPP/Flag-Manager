package com.arlabs.myfm.services;

import com.arlabs.myfm.pojos.APIResponse;
import com.arlabs.myfm.pojos.FlagUpdateRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 *
 * @author AR-LABS
 */
public interface APIService {
    @POST
    public Call<APIResponse> postUpdate(
        @Url String requestURL,
        @Body FlagUpdateRequest flagUpdateRequest
    );
}
