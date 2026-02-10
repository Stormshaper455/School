package itech3150.hw2;

import java.io.Serializable;

public class Website implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String url;

    public Website() {
        this("", "");
    }

    public Website(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Website [name=" + name + ", URL=" + url + "]";
    }
}
