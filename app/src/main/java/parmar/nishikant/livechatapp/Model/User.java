package parmar.nishikant.livechatapp.Model;

public class User {
    private String id;
    private String fullname;
    private String dpURL;
    private String points;
    public User(String id,String fullname,String dpURL,String points) {
        this.id= id;
        this.fullname = fullname;
        this.dpURL = dpURL;
        this.points = points;
    }
    public User()
    {

    }

    public String getId() {
        return id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setUsername(String fullname) {
        this.fullname = fullname;
    }

    public String getDpURL() {
        return dpURL;
    }

    public void setDpURL(String dpURL) {
        this.dpURL = dpURL;
    }
}
