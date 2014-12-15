package gpop.us.sfoexplorer.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

public class WWWeatherRadarModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON VARIABLES
    private ArrayList<String> arrival_radar_urls = new ArrayList<String>();
    private ArrayList<String> departure_radar_urls = new ArrayList<String>();

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // getArrivalRadarUrl(): Retrieves the arrival radar URL list.
    public ArrayList<String> getArrivalRadarUrl() { return arrival_radar_urls; }

    // getDepartureRadarURL(): Retrieves the departure radar URL list.
    public ArrayList<String> getDepartureRadarURL() { return departure_radar_urls; }

    // setArrivalRadarUrl(): Sets the arrival radar URL list.
    public void setArrivalRadarUrl(ArrayList<String> arrival_radar) { arrival_radar_urls = arrival_radar; }

    // setDepartureRadarUrl(): Sets the departure radar URL list.
    public void setDepartureRadarUrl(ArrayList<String> departure_radar) { departure_radar_urls = departure_radar; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWWeatherRadarModel object.
    public static WWWeatherRadarModel fromJson(JSONObject jsonObject) {

        WWWeatherRadarModel model = new WWWeatherRadarModel();

        // Deserializes the JSON string into object fields.
        try {

            // Builds the array of image URLs for the arrival radar.
            JSONArray arrivalImages = jsonObject.getJSONArray("arrival_radar");
            for (int i = 0; i < arrivalImages.length(); i++) {
                String str_image_url = arrivalImages.getString(i);
                model.arrival_radar_urls.add(str_image_url);
            }

            // Builds the array of image URLs for the departure radar.
            JSONArray departureImages = jsonObject.getJSONArray("departure_radar");
            for (int i = 0; i < departureImages.length(); i++) {
                String str_image_url = departureImages.getString(i);
                model.departure_radar_urls.add(str_image_url);
            }
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