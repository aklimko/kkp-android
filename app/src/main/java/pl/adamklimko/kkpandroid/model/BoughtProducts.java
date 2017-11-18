package pl.adamklimko.kkpandroid.model;

import java.lang.reflect.Field;

public class BoughtProducts {
    private int toiletPaper;
    private int dishSoap;
    private int trashBag;
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
        return new String[] {"Toilet paper", "Dish soap", "Trash bag", "Soap", "Sugar"};
    }

    public int getFieldValue(int field) {
        switch (field) {
            case 0:
                return toiletPaper;
            case 1:
                return dishSoap;
            case 2:
                return trashBag;
            case 3:
                return soap;
            case 4:
                return sugar;
            default:
                return 0;
        }
    }

    public int getSumValues() {
        return toiletPaper + dishSoap + trashBag + soap + sugar;
    }

    public int getToiletPaper() {
        return toiletPaper;
    }

    public void setToiletPaper(int toiletPaper) {
        this.toiletPaper = toiletPaper;
    }

    public int getDishSoap() {
        return dishSoap;
    }

    public void setDishSoap(int dishSoap) {
        this.dishSoap = dishSoap;
    }

    public int getTrashBag() {
        return trashBag;
    }

    public void setTrashBag(int trashBag) {
        this.trashBag = trashBag;
    }

    public int getSoap() {
        return soap;
    }

    public void setSoap(int soap) {
        this.soap = soap;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }
}
