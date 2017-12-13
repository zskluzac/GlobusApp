package hu.ait.android.globusapp.data;

/**
 * Created by Zachary on 12/12/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("size")
    @Expose
    public String size;

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }
}