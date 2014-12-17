package gpop.us.sfoexplorer.Model;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

public class SFOForecastModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON VARIABLES
    private int period; // Period.
    private String weather_icon; // Weather Icon.
    private String weather_icon_url; // Weather Icon URL.
    private String forecast_day; // Forecast title.
    private String forecast_text; // Forecast text.
    private String forecast_metric; // Forecast metric.
    private String weather_pop; // Weather pop.

    // LOGGING VARIABLES
    private static final String TAG = SFOForecastModel.class.getSimpleName(); // Retrieves the simple name of the class.

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // getPeriod(): Retrieves the current period value.
    public int getPeriod() { return period; }

    // getWeatherIcon(): Retrieves the current weather icon.
    public String getWeatherIcon() { return weather_icon; }

    // getWeatherIconUrl(): Retrieves the current weather icon URL.
    public String getWeatherIconUrl() { return weather_icon_url; }

    // getForecastDay(): Retrieves the forecast day.
    public String getForecastDay() { return forecast_day; }

    // getForecastText(): Retrieves the forecast text.
    public String getForecastText() { return forecast_text; }

    // getForecastMetric(): Retrieves the forecast metric.
    public String getForecastMetric() { return forecast_metric; }

    // getWeatherPop(): Retrieves the weather pop value.
    public String getWeatherPop() { return weather_pop; }

    // setPeriod(): Sets the current period value.
    public void setPeriod(int per) { period = per; }

    // setWeatherIcon(): Sets the current weather icon.
    public void setWeatherIcon(String icon) { weather_icon = icon; }

    // setWeatherIconUrl(): Sets the current weather icon URL.
    public void setWeatherIconUrl(String url) { weather_icon_url = url; }

    // setForecastDay(): Sets the forecast day value.
    public void setForecastDay(String day) { forecast_day = day; }

    // setForecastText(): Sets the forecast text.
    public void setForecastText(String text) { forecast_text = text; }

    // setForecastMetric(): Sets the forecast metric.
    public void setForecastMetric(String metric) { forecast_metric = metric; }

    // setWeatherPop(): Sets the weather pop value.
    public void setWeatherPop(String pop) { weather_pop = pop; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWForecastModel object.
    public static SFOForecastModel fromJson(JSONObject jsonObject) {

        SFOForecastModel model = new SFOForecastModel();

        // Deserializes the JSON string into object fields.
        try {
            model.period = jsonObject.getInt("period"); // Period.
            model.weather_icon = jsonObject.getString("icon"); // Icon.
            model.weather_icon_url = jsonObject.getString("icon_url"); // Icon URL.
            model.forecast_day = jsonObject.getString("title"); // Forecast day.
            model.forecast_metric = jsonObject.getString("fcttext"); // Forecast text.
            model.forecast_text = jsonObject.getString("fcttext_metric"); // Forecast metric.
            model.weather_pop = jsonObject.getString("pop"); // Pop.
        }

        // Exception handler.
        catch (JSONException e) {
            e.printStackTrace(); // Prints error stack trace.
            Log.d(TAG, "fromJson(): JSONException occurred"); // Logging.
            return null;
        }

        return model; // Returns the new model object.
    }

    // fromJson(): Creates an ArrayList of WWForecastModel objects from the jsonArray object.
    public static ArrayList<SFOForecastModel> fromJson(JSONArray jsonArray) {

        ArrayList<SFOForecastModel> forecasts = new ArrayList<SFOForecastModel>(jsonArray.length());

        // Processes each result in the JSON array, decoding and converting each to a WWForecastModel
        // object.
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject eventJson = null;

            // Attempts to retrieve the JSONObject from the JSONArray.
            try { eventJson = jsonArray.getJSONObject(i); }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            SFOForecastModel forecast = SFOForecastModel.fromJson(eventJson); // Creates the WWForecastModel object.

            if (forecast != null) { forecasts.add(forecast); } // Adds the WWForecastModel object to the ArrayList.
        }

        return forecasts;
    }
}