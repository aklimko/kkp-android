package pl.adamklimko.kkpandroid.model;

public class UserData {
    private String username;
    private Profile profile;
    private BoughtProducts boughtProducts;
    private CleanedRooms cleanedRooms;

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

    public BoughtProducts getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(BoughtProducts boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public CleanedRooms getCleanedRooms() {
        return cleanedRooms;
    }

    public void setCleanedRooms(CleanedRooms cleanedRooms) {
        this.cleanedRooms = cleanedRooms;
    }
}
