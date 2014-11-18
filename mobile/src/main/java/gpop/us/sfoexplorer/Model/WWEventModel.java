package gpop.us.sfoexplorer.Model;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWEventModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON VARIABLES
    private int proximity;
    private String event_title;
    private String event_description;
    private String event_summary;
    private String phone_number;
    private String event_hours;
    private String tags;
    private String terminal_location;
    private String boarding_area;
    private String security_location;
    private String event_image_URL;
    private String event_map;
    private String event_website;
    private String event_type;
    private String gate_location;

    // LOGGING VARIABLES
    private static final String TAG = WWEventModel.class.getSimpleName(); // Retrieves the simple name of the class.

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // GET FUNCTIONS:
    public int getProximity() { return proximity; }
    public String getEventTitle() { return event_title; }
    public String getEventDescription() { return event_description; }
    public String getEventSummary() { return event_summary; }
    public String getPhoneNumber() { return phone_number; }
    public String getEventHours() { return event_hours; }
    public String getTags() { return tags; }
    public String getTerminalLocation() { return terminal_location; }
    public String getBoardingArea() { return boarding_area; }
    public String getSecurityLocation() { return security_location; }
    public String getEventImageURL() { return event_image_URL; }
    public String getEventMap() { return event_map; }
    public String getEventWebsite() { return event_website; }
    public String getEventType() { return event_type; }
    public String getGateLocation() { return gate_location; }

    // SET FUNCTIONS:
    public void setProximity(int prox) { proximity = prox; }
    public void setEventTitle(String name) { event_title = name; }
    public void setEventBody(String body) { event_description = body; }
    public void setEventSummary(String summary) { event_summary = summary; }
    public void setPhoneNumber(String number) { phone_number = number; }
    public void setEventHours(String hours) { event_hours = hours; }
    public void setTags(String tag) { tags = tag; }
    public void setTerminalLocation(String location) { terminal_location = location; }
    public void setBoardingArea(String area) { boarding_area = area; }
    public void setSecurityLocation(String location) { security_location = location; }
    public void setEventImageURL(String image) { event_image_URL = image; }
    public void setEventMap(String map) { event_map = map; }
    public void setEventWebsite(String site) { event_website = site; }
    public void setEventType(String type) { event_type = type; }
    public void setGateLocation(String gate) { gate_location = gate; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWWeatherModel object.
    public static WWEventModel fromJson(JSONObject jsonObject) {

        WWEventModel model = new WWEventModel();

        // Deserializes the JSON string into object fields.
        try {
            model.proximity = jsonObject.getInt("proximity"); // Proximity.
            model.event_title = jsonObject.getString("title"); // Event title.
            model.event_description = jsonObject.getString("description"); // Description.
            model.terminal_location = jsonObject.getString("terminal"); // Terminal.
            model.boarding_area = jsonObject.getString("boardingArea"); // Boarding Area.
            model.event_image_URL = jsonObject.getString("image"); // Image URL.
            model.event_map = jsonObject.getString("map"); // Map URL.
            model.event_type = jsonObject.getString("type"); // Event type.
            //model.event_summary = jsonObject.getString("location_summary"); // Location Summary.
            //model.phone_number = jsonObject.getString("phone"); // Phone.
            model.event_hours = jsonObject.getString("hours"); // Hours.
            model.tags = jsonObject.getString("tags"); // Tags.
            model.security_location = jsonObject.getString("security"); // Security.
            model.event_website = jsonObject.getString("website"); // Website URL.
            model.gate_location = jsonObject.getString("gate"); // Gate location.
        }

        // Exception handler.
        catch (JSONException e) {
            e.printStackTrace(); // Prints error stack trace.
            Log.d(TAG, "fromJson(): JSONException occurred"); // Logging.

            return null;
        }

        return model; // Returns the new model object.
    }

    // fromJson(): Creates an ArrayList of WWEventModel objects from the jsonArray object.
    public static ArrayList<WWEventModel> fromJson(JSONArray jsonArray) {

        ArrayList<WWEventModel> events = new ArrayList<WWEventModel>(jsonArray.length());

        // Processes each result in the JSON array, decoding and converting each to a WWEventModel
        // object.
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject eventJson = null;

            // Attempts to retrieve the JSONObject from the JSONArray.
            try { eventJson = jsonArray.getJSONObject(i); }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            WWEventModel event = WWEventModel.fromJson(eventJson); // Creates the WWEventModel object.

            if (event != null) { events.add(event); } // Adds the WWEventModel object to the ArrayList.
        }

        return events;
    }
}