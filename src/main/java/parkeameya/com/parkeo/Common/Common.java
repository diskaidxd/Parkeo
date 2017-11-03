package parkeameya.com.parkeo.Common;

import parkeameya.com.parkeo.Remote.IGoogleAPI;
import parkeameya.com.parkeo.Remote.RetrofitClient;

/**
 * Created by johnalexquispelucas on 2/11/17.
 */

public class Common {
    public static final String baseURL ="https://maps.googleapis.com";
    public static IGoogleAPI getGoogleAPI ()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
