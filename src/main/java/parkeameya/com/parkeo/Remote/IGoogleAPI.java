package parkeameya.com.parkeo.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by johnalexquispelucas on 2/11/17.
 */

public interface IGoogleAPI {
    @GET
    Call<String> getPath(@Url String url);
}
