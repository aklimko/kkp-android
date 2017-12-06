package pl.adamklimko.kkpandroid.model;

import java.util.List;

public class Data {
    private List<UserData> usersData;
    private List<History> history;
    private Products missingProducts;
    private Rooms dirtyRooms;

    public List<UserData> getUsersData() {
        return usersData;
    }

    public List<History> getHistory() {
        return history;
    }

    public Products getMissingProducts() {
        return missingProducts;
    }

    public Rooms getDirtyRooms() {
        return dirtyRooms;
    }
}
