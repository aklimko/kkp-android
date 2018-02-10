package pl.adamklimko.kkpandroid.network;

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

    @GET("users")
    Call<List<UserData>> getUsersData();

    @GET("data")
    Call<Data> getData();

    @PATCH("products/bought")
    Call<Products> addBoughtProducts(@Body Products boughtProducts);

    @PATCH("products/missing")
    Call<Products> selectProductsAsMissing(@Body Products missingProducts);

    @PATCH("rooms/cleaned")
    Call<Rooms> addCleanedRooms(@Body Rooms cleanedRooms);

    @PATCH("rooms/dirty")
    Call<Rooms> selectRoomsAsDirty(@Body Rooms dirtyRooms);

    @GET("history")
    Call<List<History>> getHistory();
}
