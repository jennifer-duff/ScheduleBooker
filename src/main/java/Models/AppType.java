package Models;

public class AppType {
    private String typeValue;
    private int typeCount;

    public AppType(String typeValue, int typeCount) {
        this.typeValue = typeValue;
        this.typeCount = typeCount;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }
}
