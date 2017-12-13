package hu.ait.android.globusapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Zachary on 12/12/2017.
 */

public class EndList {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

