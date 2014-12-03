package gpop.us.sfoexplorer.Model;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */

public class WWFlightModel implements Serializable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // JSON VARIABLES
    private String destination;
    private String departure_time;
    private String departure_gate;
    private int timeToDeparture;

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // GET METHODS:
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departure_time; }
    public String getDepartureGate() { return departure_gate; }
    public int getTimeToDeparture() { return timeToDeparture; }

    // SET METHODS:
    public void setDestination(String dest) { destination = dest; }
    public void setDepartureTime(String time) { departure_time = time; }
    public void setDepartureGate(String gate) { departure_gate = gate; }
    public void setTimeToDeparture(int time) { timeToDeparture = time; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWFlightModel object.
    public static WWFlightModel fromJson(JSONObject jsonObject) {

        WWFlightModel model = new WWFlightModel();

        // Deserializes the JSON string into object fields.
        try {
            model.destination = jsonObject.getString("arrivalAirport"); // Destination.
            model.departure_time = jsonObject.getString("departureTime"); // Departure time.
            model.departure_gate = jsonObject.getString("departureGate"); // Gate number.
            model.timeToDeparture = jsonObject.getInt("minutes"); // Time to departure value.
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
