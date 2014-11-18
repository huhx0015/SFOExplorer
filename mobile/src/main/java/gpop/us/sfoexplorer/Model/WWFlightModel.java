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
    private String flight_number;
    private String gate_number;

    /** GET / SET FUNCTIONALITY ________________________________________________________________ **/

    // getFlightNumber(): Retrieves the flight number.
    public String getFlightNumber() { return flight_number; }

    // getGateNumber(): Retrieves the gate number.
    public String getGateNumber() { return gate_number; }

    // setFlightNumber(): Sets the current flight number.
    public void setFlightNumber(String flight) { flight_number = flight; }

    // setGateNumber(): Sets the gate number.
    public void getGateNumber(String gate) { gate_number = gate; }

    /** JSON FUNCTIONALITY _____________________________________________________________________ **/

    // fromJson(): Retrieves the strings from the JSON object and returns a WWFlightModel object.
    public static WWFlightModel fromJson(JSONObject jsonObject) {

        WWFlightModel model = new WWFlightModel();

        // Deserializes the JSON string into object fields.
        try {
            model.flight_number = jsonObject.getString("flight_number"); // Flight number.
            model.gate_number = jsonObject.getString("gate_number"); // Gate number.
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
