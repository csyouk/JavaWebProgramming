package webserver.model;

/**
 * Created by changsuyouk on 10/24/16.
 */
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email){
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString(){
        return  " " + this.userId +
                " " + this.password +
                " " + this.name +
                " " + this.email;
    }
}
