package pl.adamklimko.kkpandroid.model;

import java.lang.reflect.Field;

public class Rooms {
    private int bathroom;
    private int kitchen;
    private int floor;

    public static int getNumberOfRooms() {
        int count = 0;
        Field[] fields = Rooms.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == int.class) {
                count++;
            }
        }
        return count;
    }

    public int getFieldValue(int field) {
        switch (field) {
            case 0:
                return bathroom;
            case 1:
                return kitchen;
            case 2:
                return floor;
            default:
                return 0;
        }
    }

    public void setFieldValueToOne(int field) {
        switch (field) {
            case 0:
                bathroom = 1;
                break;
            case 1:
                kitchen = 1;
                break;
            case 2:
                floor = 1;
                break;
        }
    }

    public String getRoomsNamesWhenFieldValueIsOne() {
        StringBuilder names = new StringBuilder();
        if (bathroom == 1) names.append("bathroom, ");
        if (kitchen == 1) names.append("kitchen, ");
        if (floor == 1) names.append("floor, ");
        names.delete(names.length() - 2, names.length());
        return names.toString();
    }

    public int getSumValues() {
        int count = 0;
        Field[] fields = Rooms.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    count += field.getInt(this);
                } catch (IllegalAccessException e) {
                }
            }
        }
        return count;
    }
}
