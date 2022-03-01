package Models;

public class AppMonth {
    private String appMonthValue;
    private String year;
    private int appMonthCount;

    /**
     * @param appMonthValue     the month in which the target appointment(s) occur
     * @param appMonthCount     the number of appointment(s) that occur in the given month
     */
    public AppMonth(String appMonthValue, int appMonthCount) {
        this.appMonthValue = appMonthValue;
        this.appMonthCount = appMonthCount;
    }

    /**
     * @return the String value of the month in which the target appointment(s) occur
     */
    public String getAppMonthValue() {
        return appMonthValue;
    }


    /**
     * @param appMonthValue the value to set for the month in which the target appointment(s) occur
     */
    public void setAppMonthValue(String appMonthValue) {
        this.appMonthValue = appMonthValue;
    }

    /**
     * @return the year in which the target appointment(s) occur
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year the year to set for when the target appointment(s) occur
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the number of appointments that occur in the given month
     */
    public int getAppMonthCount() {
        return appMonthCount;
    }

    /**
     * @param appMonthCount the number of appointments to set as occuring in the given month
     */
    public void setAppMonthCount(int appMonthCount) {
        this.appMonthCount = appMonthCount;
    }
}
