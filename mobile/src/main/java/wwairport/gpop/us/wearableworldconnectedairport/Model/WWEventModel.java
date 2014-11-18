package wwairport.gpop.us.wearableworldconnectedairport.Model;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWEventModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON VARIABLES
    private String event_title;
    private String event_body;
    private String event_summary;
    private String phone_number;
    private String event_hours;
    private String tags;
    private String terminal_location;
    private String security_location;
    private String event_image_URL;
    private String event_map;
    private String event_website;

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // GET FUNCTIONS:
    public String getEventTitle() { return event_title; }
    public String getEventBody() { return event_body; }
    public String getEventSummary() { return event_summary; }
    public String getPhoneNumber() { return phone_number; }
    public String getEventHours() { return event_hours; }
    public String getTags() { return tags; }
    public String getTerminalLocation() { return terminal_location; }
    public String getSecurityLocation() { return security_location; }
    public String getEventImageURL() { return event_image_URL; }
    public String getEventMap() { return event_map; }
    public String getEventWebsite() { return event_website; }

    // SET FUNCTIONS:
    public void setEventTitle(String name) { event_title = name; }
    public void setEventBody(String body) { event_body = body; }
    public void setEventSummary(String summary) { event_summary = summary; }
    public void setPhoneNumber(String number) { phone_number = number; }
    public void setEventHours(String hours) { event_hours = hours; }
    public void setTags(String tag) { tags = tag; }
    public void setTerminalLocation(String location) { terminal_location = location; }
    public void setSecurityLocation(String location) { security_location = location; }
    public void setEventImageURL(String image) { event_image_URL = image; }
    public void setEventMap(String map) { event_map = map; }
    public void setEventWebsite(String site) { event_website = site; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWWeatherModel object.
    public static WWEventModel fromJson(JSONObject jsonObject) {

        WWEventModel model = new WWEventModel();

        // Deserializes the JSON string into object fields.
        try {
            model.event_title = jsonObject.getString("subjective"); // Weather status.
            model.event_body = jsonObject.getString("body"); // Temperature.
            model.event_summary = jsonObject.getString("location_summary"); // Temperature.
            model.phone_number = jsonObject.getString("phone"); // Temperature.
            model.event_hours = jsonObject.getString("hours"); // Temperature.
            model.tags = jsonObject.getString("tags"); // Temperature.
            model.terminal_location = jsonObject.getString("terminal"); // Temperature.
            model.security_location = jsonObject.getString("security"); // Temperature.
            model.event_image_URL = jsonObject.getString("image"); // Temperature.
            model.event_map = jsonObject.getString("map"); // Temperature.
            model.event_website = jsonObject.getString("website"); // Temperature.
        }

        // Exception handler.
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return model; // Returns the new model object.
    }
}
