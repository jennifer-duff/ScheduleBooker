package Models;

public class AppType {
    private String typeValue;
    private int typeCount;

    /**
     * @param typeValue the type category of the target appointment(s)
     * @param typeCount the number of appointments currently scheduled of the given type
     */
    public AppType(String typeValue, int typeCount) {
        this.typeValue = typeValue;
        this.typeCount = typeCount;
    }

    /**
     * @return the type of the target appointment(s)
     */
    public String getTypeValue() {
        return typeValue;
    }

    /**
     * @param typeValue the type to set for the target appointments
     */
    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    /**
     * @return the number of currently-scheduled appointments of the given type
     */
    public int getTypeCount() {
        return typeCount;
    }

    /**
     * @param typeCount the number of currently-scheduled appointments to set as being of the given type
     */
    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }
}
