package models;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UserData {
    @JsonAlias({"Name"})
    private String username;
    @JsonAlias({"Bio"})
    private String bio;
    @JsonAlias({"Image"})
    private String image;

    public UserData(){}

    public UserData(String bio, String image) {
        this.bio = bio;
        this.image = image;
    }

    public UserData(String username, String bio, String image) {
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
