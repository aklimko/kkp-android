package pl.adamklimko.kkpandroid.model;

public class History {
    private int[] time;
    private User user;
    private BoughtProducts productsEntry;
    private CleanedRooms roomsEntry;
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

    public BoughtProducts getProductsEntry() {
        return productsEntry;
    }

    public void setProductsEntry(BoughtProducts productsEntry) {
        this.productsEntry = productsEntry;
    }

    public CleanedRooms getRoomsEntry() {
        return roomsEntry;
    }

    public void setRoomsEntry(CleanedRooms roomsEntry) {
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
                text.append(" bought ");
            } else {
                text.append(" cleaned ");
            }
        } else {
            if (productsEntry != null) {
                text.append(" marked as missing ");
            } else {
                text.append(" marked as dirty ");
            }
        }
        return text.toString();
    }
}
