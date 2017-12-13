package hu.ait.android.globusapp.data;

/**
 * Created by Zachary on 12/13/2017.
 */

public class FileObject {

    private String name;
    private String size;

    public FileObject(String name, String size) {
        this.name = name;
        this.size= size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
