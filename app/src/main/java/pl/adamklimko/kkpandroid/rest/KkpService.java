package pl.adamklimko.kkpandroid.rest;

import pl.adamklimko.kkpandroid.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

import java.util.List;

public interface KkpService {
    @POST("login")
    Call<Token> login(@Body User user);

    @GET("user/profile")
    Call<Profile> getProfile();

    @PATCH("user/profile")
    Call<Profile> patchProfile(@Body Profile profile);

    @GET("users/all")
    Call<List<UserData>> getUsersData();

    @PATCH("bought_products")
    Call<BoughtProducts> addBoughtProducts(@Body BoughtProducts boughtProducts);
}
