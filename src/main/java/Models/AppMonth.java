package Models;

public class AppMonth {
    private String appMonthValue;
    private String year;
    private int appMonthCount;

    public AppMonth(String appMonthValue, int appMonthCount) {
        this.appMonthValue = appMonthValue;
        this.appMonthCount = appMonthCount;
    }

    public String getAppMonthValue() {
        return appMonthValue;
    }

    public void setAppMonthValue(String appMonthValue) {
        this.appMonthValue = appMonthValue;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getAppMonthCount() {
        return appMonthCount;
    }

    public void setAppMonthCount(int appMonthCount) {
        this.appMonthCount = appMonthCount;
    }
}
