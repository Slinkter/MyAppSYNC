package com.vidrieriachaloreyes.mysqlsycn.Retrofit;

import com.vidrieriachaloreyes.mysqlsycn.Contact;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("syncinfo.php")
    Call<Contact> insert(@Query("name") String name);


}
