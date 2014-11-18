package wwairport.gpop.us.wearableworldconnectedairport.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

public class WWWeatherModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CLASS VARIABLES
    private static final long serialVersionUID = -8959832007991513854L;

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

    // fromJson(): Retrieves the strings from the JSON object and returns a WWWeatherModel object.
    public static WWWeatherModel fromJson(JSONObject jsonObject) {

        WWWeatherModel model = new WWWeatherModel();

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

    // ARRAY LIST OF MOVIES of BoxOfficeMovie type.
    // Decodes array of box office movie json results into business model objects
    // BoxOfficeMovie.fromJson(jsonArrayOfMovies)
    public static ArrayList<WWWeatherModel> fromJson(JSONArray jsonArray) {
        ArrayList<WWWeatherModel> businesses = new ArrayList<WWWeatherModel>(jsonArray.length());


        // Processes each result in json array, decode and convert to business objects.
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject businessJson = null;

            try { businessJson = jsonArray.getJSONObject(i); }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            WWWeatherModel business = WWWeatherModel.fromJson(businessJson);

            if (business != null) { businesses.add(business); }
        }

        return businesses;
    }

}