package pl.adamklimko.kkpandroid.rest;

import pl.adamklimko.kkpandroid.model.Profile;
import pl.adamklimko.kkpandroid.model.Token;
import pl.adamklimko.kkpandroid.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;


public interface KkpService {
    @POST("login")
    Call<Token> login(@Body User user);

    @GET("user/profile")
    Call<Profile> getProfile();

    @PATCH("user/profile")
    Call<Profile> patchProfile(@Body Profile profile);
}
