package pl.adamklimko.kkpandroid.model;

public class History {
    private int[] time;
    private User user;
    private Products productsEntry;
    private Rooms roomsEntry;
    private ActionType actionType;

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
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
            text.append(roomsEntry.toString());
        }

        return text.toString();
    }

    public String getTimeToString() {
        if (time.length != 5 && time.length != 6) {
            return "NaD";
        }
        StringBuilder time = new StringBuilder();
        for (int i = 3; i <= 4; i++) {
            time.append(this.time[i]);
            if (i != 4) {
                time.append(":");
            }
        }
        time.append("\n");
        for (int i = 2; i >= 0; i--) {
            time.append(this.time[i]);
            if (i != 0) {
                time.append("-");
            }
        }
        return time.toString();
    }
}
