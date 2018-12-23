package parmar.nishikant.livechatapp.Model;

public class User {
    private String id;
    private String fullname;
    private String dpURL;
    private String points;
    private String status;
    private String show_status;
    public User(String id,String fullname,String dpURL,String points,String status,String show_status) {
        this.id= id;
        this.fullname = fullname;
        this.dpURL = dpURL;
        this.points = points;
        this.status=status;
        this.show_status=show_status;
    }
    public User()
    {

    }

    public String getShow_status() {
        return show_status;
    }

    public void setShow_status(String show_status) {
        this.show_status = show_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
