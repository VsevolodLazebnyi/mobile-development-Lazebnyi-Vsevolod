package com.example.massenger.network;

import com.example.massenger.model.ApiMessage;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageApiService {
    @GET("posts")
    Call<List<ApiMessage>> getMessages();
}