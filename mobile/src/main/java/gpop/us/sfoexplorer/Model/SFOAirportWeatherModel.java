package gpop.us.sfoexplorer.Model;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Yoon Huh on 12/13/2014.
 */
public class SFOAirportWeatherModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON (TEMPERATURE) VARIABLES
    private String origin_weather_status;
    private String destination_weather_status;
    private double origin_temperature;
    private double destination_temperature;

    // JSON (FORECAST) VARIABLES
    private static ArrayList<SFOForecastModel> origin_forecast = new ArrayList<>();
    private static ArrayList<SFOForecastModel> destination_forecast = new ArrayList<>();

    // JSON (RADAR) VARIABLES
    private ArrayList<String> origin_radar_urls = new ArrayList<String>();
    private ArrayList<String> destination_radar_urls = new ArrayList<String>();

    // LOGGING VARIABLES
    private static final String TAG = SFOAirportWeatherModel.class.getSimpleName(); // Retrieves the simple name of the class.

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // getOriginWeatherStatus(): Retrieves the current arrival weather status.
    public String getOriginWeatherStatus() { return origin_weather_status; }

    // getDestinationWeatherStatus(): Retrieves the current destination weather status.
    public String getDestinationWeatherStatus() { return destination_weather_status; }

    // getOriginTemperature(): Retrieves the current arrival temperature.
    public double getOriginTemperature() { return origin_temperature; }

    // getDestinationTemperature(): Retrieves the current destination temperature.
    public double getDestinationTemperature() { return destination_temperature; }

    // getOriginForecast(): Retrieves the arrival forecast.
    public ArrayList<SFOForecastModel> getOriginForecast() { return origin_forecast; }

    // getDestinationForecast(): Retrieves the destination forecast.
    public ArrayList<SFOForecastModel> getDestinationForecast() { return destination_forecast; }

    // getOriginRadarUrls(): Retrieves the arrival radar URLs.
    public ArrayList<String> getOriginRadarUrls() { return origin_radar_urls; }

    // getDestinationRadarUrls(): Retrieves the destination radar URLs.
    public ArrayList<String> getDestinationRadarUrls() { return destination_radar_urls; }

    // setOriginWeatherStatus(): Sets the current arrival weather status.
    public void setOriginWeatherStatus(String status) { origin_weather_status = status; }

    // setDestinationWeatherStatus(): Sets the current destination weather status.
    public void setDestinationWeatherStatus(String status) { destination_weather_status = status; }

    // setOriginTemperature(): Sets the current arrival temperature.
    public void setOriginTemperature(double temp) {
        origin_temperature = temp; }

    // setDestinationTemperature(): Sets the current destination temperature.
    public void setDestinationTemperature(double temp) { destination_temperature = temp; }

    // setOriginForecast(): Sets the arrival forecast list.
    public void setOriginForecast(ArrayList<SFOForecastModel> forecast) { origin_forecast = forecast; }

    // setDestinationForecast(): Sets the destination forecast list.
    public void setDestinationForecast(ArrayList<SFOForecastModel> forecast) { destination_forecast = forecast; }

    // setOriginRadarUrls(): Sets the arrival radar url list.
    public void setOriginRadarUrls(ArrayList<String> urls) { origin_radar_urls = urls; }

    // setDestinationRadarUrls(): Sets the destination radar url list.
    public void setDestinationRadarUrls(ArrayList<String> urls) { destination_radar_urls = urls; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWAirportWeatherModel object.
    public static SFOAirportWeatherModel fromJson(JSONObject jsonObject) {

        SFOAirportWeatherModel model = new SFOAirportWeatherModel();

        // Deserializes the JSON string into object fields.
        try {

            model.origin_weather_status = jsonObject.getString("origin_subjective_temp"); // Weather status.
            model.origin_temperature = jsonObject.getDouble("origin_temp"); // Temperature.
            model.destination_weather_status = jsonObject.getString("destination_subjective_temp"); // Weather status.
            model.destination_temperature = jsonObject.getDouble("destination_temp"); // Temperature.

            // Retrieves the array of arrival forecasts from the JSON string.
            JSONArray arrivalForecasts = jsonObject.getJSONArray("origin_forecast");
            origin_forecast = SFOForecastModel.fromJson(arrivalForecasts);

            // Retrieves the array of destination forecasts from the JSON string.
            JSONArray destinationForecasts = jsonObject.getJSONArray("destination_forecast");
            destination_forecast = SFOForecastModel.fromJson(destinationForecasts);

            // Builds the array of image URLs for the origin radar.
            JSONArray arrivalImages = jsonObject.getJSONArray("origin_radar");
            for (int i = 0; i < arrivalImages.length(); i++) {
                String str_image_url = arrivalImages.getString(i);
                model.origin_radar_urls.add(str_image_url);
            }

            // Builds the array of image URLs for the destination radar.
            JSONArray destinationImages = jsonObject.getJSONArray("destination_radar");
            for (int i = 0; i < destinationImages.length(); i++) {
                String str_image_url = destinationImages.getString(i);
                model.destination_radar_urls.add(str_image_url);
            }
        }

        // Exception handler.
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "fromJson(): JSONException occurred"); // Logging.
            return null;
        }

        // Returns the new model object.
        return model;
    }
}
