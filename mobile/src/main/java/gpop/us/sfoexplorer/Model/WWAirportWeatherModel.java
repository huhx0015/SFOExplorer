package gpop.us.sfoexplorer.Model;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by Michael Yoon Huh on 12/13/2014.
 */
public class WWAirportWeatherModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON VARIABLES
    private String weather_status;
    private double temperature;

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // getTemperature(): Retrieves the current temperature.
    public double getTemperature() { return temperature; }

    // getWeather(): Retrieves the current weather status.
    public String getWeather() { return weather_status; }

    // setTemperature(): Sets the current temperature.
    public void setTemperature(double temp) { temperature = temp; }

    // setWeather(): Sets the current weather status.
    public void setWeather(String weather) { weather_status = weather; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWAirportWeatherModel object.
    public static WWAirportWeatherModel fromJson(JSONObject jsonObject) {

        WWAirportWeatherModel model = new WWAirportWeatherModel();

        // Deserializes the JSON string into object fields.
        try {
            model.weather_status = jsonObject.getString("subjective"); // Weather status.
            model.temperature = jsonObject.getDouble("temp"); // Temperature.
        }

        // Exception handler.
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        // Returns the new model object.
        return model;
    }
}
