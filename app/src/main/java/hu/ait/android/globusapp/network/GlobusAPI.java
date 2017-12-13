package hu.ait.android.globusapp.network;

import java.util.List;
import java.util.Map;

import hu.ait.android.globusapp.data.EndList;
import hu.ait.android.globusapp.data.File;
import hu.ait.android.globusapp.data.FileObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Zachary on 12/12/2017.
 */

public interface GlobusAPI {
    @GET("/transfer/{endpointID}/{filepath}")
    Call<String> getSubState(@Path("endpointID") String endpointID,
                             @Path("filepath") String filepath);

    @GET("/test")
    Call<String> getTest();

    @GET("/owner")
    Call<String> getOwner();

    @GET("/endpoints")
    Call<Map<String, List<EndList>>> getEndpoints();

    @GET("/files")
    Call<Map<String, List<File>>> getFiles();
}
