package org.farrellcrafts.snowwatch;

import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused")
class Report {
    private int BaseDepth;
    private String Weather;
    private String WeatherIcon;
    private String Forecast12;
    private String Forecast24;
    private String Forecast36;
    private int GroomedTrails;
    private int Last24;
    private int Last48;
    private int OpenLifts;
    private int OpenTrails;
    private Date ReportTime;
    private String Resort;
    private int SummitDepth;
    private int Temperature;
    private int TotalDepth;

    public Report(){

    }

    public String formatDepth(int depth){
        return String.format(Locale.US, "%d\"", depth);
    }

    String formatTemp(int temp){
        return String.format(Locale.US, "%d\u00B0", temp);
    }

    public int getBaseDepth() {
        return BaseDepth;
    }

    public String getForecast12() {
        return Forecast12;
    }

    public String getForecast24() {
        return Forecast24;
    }

    public String getForecast36() {
        return Forecast36;
    }

    public int getGroomedTrails() {
        return GroomedTrails;
    }

    public int getLast24() {
        return Last24;
    }

    public int getLast48() {
        return Last48;
    }

    public int getOpenLifts() {
        return OpenLifts;
    }

    public int getOpenTrails() {
        return OpenTrails;
    }

    public Date getReportTime() {
        return ReportTime;
    }

    public String getResort() {
        return Resort;
    }

    public int getSummitDepth() {
        return SummitDepth;
    }

    public int getTemperature() {
        return Temperature;
    }

    public int getTotalDepth() {
        return TotalDepth;
    }

    public String getWeather(){
        return Weather;
    }

    public String getWeatherIcon(){
        return WeatherIcon;
    }
}
