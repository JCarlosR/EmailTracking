package com.example.neyser.emailtracking.io;

import com.example.neyser.emailtracking.io.response.CategoriesResponse;
import com.example.neyser.emailtracking.io.response.ClientsBySellersResponse;
import com.example.neyser.emailtracking.io.response.ClientsBySourceResponse;
import com.example.neyser.emailtracking.io.response.ClientsResponse;
import com.example.neyser.emailtracking.io.response.LinkTypesResponse;
import com.example.neyser.emailtracking.io.response.LinksResponse;
import com.example.neyser.emailtracking.io.response.OpenedEmailsResponse;
import com.example.neyser.emailtracking.io.response.SimpleResponse;
import com.example.neyser.emailtracking.io.response.QuantityResponse;
import com.example.neyser.emailtracking.io.response.SellersResponse;
import com.example.neyser.emailtracking.io.response.SubCategoriesByCategoryResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApiService {

    @GET("login.php")
    Call<SimpleResponse> getLogin(
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

    @GET("categories/index.php")
    Call<CategoriesResponse> getCategories();

    @GET("clients/index.php")
    Call<ClientsResponse> getClients();

    @GET("links/index.php")
    Call<LinksResponse> getLinks();

    @GET("links/types.php")
    Call<LinkTypesResponse> getLinkTypes();

    @GET("subcategories/index.php")
    Call<SubCategoriesByCategoryResponse> getSubCategories(@Query("category_id") int category_id);

    @FormUrlEncoded
    @POST("clients/store.php")
    Call<SimpleResponse> postNewClient(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("sub_category_id") int sub_category_id
    );

    @FormUrlEncoded
    @POST("links/store.php")
    Call<SimpleResponse> postNewLink(
            @Field("name") String name,
            @Field("url") String url,
            @Field("type_id") int type_id
    );

    @GET("emails/index.php")
    Call<OpenedEmailsResponse> getOpenedEmails(@Query("last_name") String last_name);

}
