package pl.adamklimko.kkpandroid.model;

import java.lang.reflect.Field;

public class BoughtProducts {
    private int toiletPaper;
    private int dishSoap;
    private int trashBags;
    private int soap;
    private int sugar;

    public static int getNumberOfProducts() {
        int count = 0;
        Field[] fields = BoughtProducts.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == int.class) {
                count++;
            }
        }
        return count;
    }

    public static String[] getProductsNames() {
        return new String[] {"Toilet paper", "Dish soap", "Trash bags", "Soap", "Sugar"};
    }

    public int getFieldValue(int field) {
        switch (field) {
            case 0:
                return toiletPaper;
            case 1:
                return dishSoap;
            case 2:
                return trashBags;
            case 3:
                return soap;
            case 4:
                return sugar;
            default:
                return 0;
        }
    }

    public void setFieldValueToOne(int field) {
        switch (field) {
            case 0:
                toiletPaper = 1;
                break;
            case 1:
                dishSoap = 1;
                break;
            case 2:
                trashBags = 1;
                break;
            case 3:
                 soap = 1;
                break;
            case 4:
                sugar = 1;
                break;
            default:
                break;
        }
    }

    public int getSumValues() {
        int count = 0;
        Field[] fields = BoughtProducts.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    count += field.getInt(this);
                } catch (IllegalAccessException e) {}
            }
        }
        return count;
    }
}
