package parmar.nishikant.livechatapp.Model;

public class userinfo {
    private String id;
    private String fullname;
    private String dpURL;
    public userinfo(String id,String fullname,String dpURL) {
        this.id= id;
        this.fullname = fullname;
        this.dpURL = dpURL;
    }
    public userinfo()
    {

    }
    public String getId(){
        return id;
    }
    public String getFullname(){
        return fullname;
    }
    public String getDpURL(){
        return dpURL;
    }
    public void setId(String id){
        this.id= id;
    }
    public void setFullname(String fullname){
        this.fullname= fullname;
    }
    public void setDpURL(String dpURL){
        this.dpURL= dpURL;
    }
}
