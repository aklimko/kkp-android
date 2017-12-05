package pl.adamklimko.kkpandroid.model;

import pl.adamklimko.kkpandroid.model.types.ActionType;

public class History {
    private String time;
    private User user;
    private Products productsEntry;
    private Rooms roomsEntry;
    private ActionType actionType;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Products getProductsEntry() {
        return productsEntry;
    }

    public void setProductsEntry(Products productsEntry) {
        this.productsEntry = productsEntry;
    }

    public Rooms getRoomsEntry() {
        return roomsEntry;
    }

    public void setRoomsEntry(Rooms roomsEntry) {
        this.roomsEntry = roomsEntry;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder(user.getUsername());
        if (actionType == ActionType.DONE) {
            if (productsEntry != null) {
                text.append(" bought: ");
            } else {
                text.append(" cleaned: ");
            }
        } else {
            if (productsEntry != null) {
                text.append(" marked as missing: ");
            } else {
                text.append(" marked as dirty: ");
            }
        }
        if (productsEntry != null) {
            text.append(productsEntry.getProductsNamesWhenFieldValueIsOne());
        } else {
            text.append(roomsEntry.getRoomsNamesWhenFieldValueIsOne());
        }

        return text.toString();
    }

    public String getTimeToString() {
        if (time.length() != 16) {
            return "NaD";
        }
        StringBuilder time = new StringBuilder();
        String[] datetime = this.time.split(" ");
        if (datetime.length != 2) {
            return "NaD";
        }
        return time.append(datetime[1]).append("\n").append(datetime[0]).toString();
    }
}
