package com.example.neyser.emailtracking.io;

import com.example.neyser.emailtracking.io.response.ClientsBySellersResponse;
import com.example.neyser.emailtracking.io.response.ClientsBySourceResponse;
import com.example.neyser.emailtracking.io.response.LoginResponse;
import com.example.neyser.emailtracking.io.response.QuantityResponse;
import com.example.neyser.emailtracking.io.response.SellersResponse;
import com.example.neyser.emailtracking.model.Seller;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiService {

    @GET("login.php")
    Call<LoginResponse> getLogin(
            @Query("usuario") String username,
            @Query("clave") String password
    );

    @GET("cant_vistos.php")
    Call<QuantityResponse> getViewedEmails();

    @GET("cant_enviados.php")
    Call<QuantityResponse> getSentEmails();

    @GET("cant_clicks.php")
    Call<QuantityResponse> getClickedEmails();

    @GET("cant_clientes.php")
    Call<QuantityResponse> getClientsCount();


    @GET("vendedores.php")
    Call<SellersResponse> getSellers();

    @GET("clientes_por_vendedor.php")
    Call<ClientsBySellersResponse> getClientsBySellers();

    @GET("clientes_por_medio.php")
    Call<ClientsBySourceResponse> getClientsBySource();
    /*

    @FormUrlEncoded
    @POST("upload/photo")
    Call<SimpleResponse> postPhoto(
            @Field("image") String base64,
            @Field("extension") String extension,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("product")
    Call<SimpleResponse> postNewProduct(
            @Field("code") String code,
            @Field("name") String name,
            @Field("description") String description
    );
*/
}
