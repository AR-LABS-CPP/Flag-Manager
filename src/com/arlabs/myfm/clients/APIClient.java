package com.arlabs.myfm.clients;

import com.arlabs.myfm.services.APIService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author AR-LABS
 */
public class APIClient {
    private static APIClient instance = null;
    private final APIService apiService;
    
    public static APIClient getInstance() {
        if(instance == null) {
            instance = new APIClient();
        }
        
        return instance;
    }
    
    private APIClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        
        Retrofit retrofitClient = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://placeholder")
                .build();
        
        this.apiService = retrofitClient.create(APIService.class);
    }
    
    public APIService getAPIService() {
        return this.apiService;
    }
}
