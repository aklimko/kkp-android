package pl.adamklimko.kkpandroid.model;

public class Profile {
    private final String fullName;
    private final String facebookId;

    public Profile(String fullName, String facebookId) {
        this.fullName = fullName;
        this.facebookId = facebookId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Profile)) {
            return false;
        }
        final Profile other = (Profile) obj;
        if (!fullName.equals(other.getFullName())) {
            return false;
        }
        if (!facebookId.equals(other.getFacebookId())) {
            return false;
        }
        return true;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFacebookId() {
        return facebookId;
    }

}
