package hu.ait.android.globusapp.data;

/**
 * Created by Zachary on 12/13/2017.
 */

public class Endpoint {

    private String name;
    private String id;

    public Endpoint(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
