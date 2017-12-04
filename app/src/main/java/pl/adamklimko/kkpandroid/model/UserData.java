package pl.adamklimko.kkpandroid.model;

public class UserData {
    private String username;
    private Profile profile;
    private Products boughtProducts;
    private Rooms cleanedRooms;

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Products getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(Products boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public Rooms getCleanedRooms() {
        return cleanedRooms;
    }

    public void setCleanedRooms(Rooms cleanedRooms) {
        this.cleanedRooms = cleanedRooms;
    }
}
