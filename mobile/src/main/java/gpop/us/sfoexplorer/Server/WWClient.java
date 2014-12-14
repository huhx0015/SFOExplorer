package gpop.us.sfoexplorer.Server;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWClient {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // API VARIABLES:
    private String SERVER_URL = "http://gpop-server.com/sfo/"; // Server URL string.
    private String BASE_URL = ""; // Base URL string.
    private String API_TYPE = "NONE"; // API type string.

    // CLIENT VARIABLES:
    private AsyncHttpClient client; // AsyncHttpClient object.

    // FLIGHT VARIABLES:
    private String airlineCarrier; // Referenes the name of the airline carrier.
    private String flightNumber; // References the flight number.

    // LOGGING VARIABLES:
    private static final String TAG = WWClient.class.getSimpleName(); // Retrieves the simple name of the class.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // WWClient(): Sets up the HTTP Client.
    public WWClient(String api_type) {

        // Initializes the class variables.
        this.client = new AsyncHttpClient();
        API_TYPE = api_type;

        selectApi(); // Selects the API based on the specified API type.
    }

    // WWClient(): Sets up the HTTP Client for getting the flight status.
    public WWClient(String carrier, String flight) {

        // Initializes the class variables.
        this.client = new AsyncHttpClient();
        airlineCarrier = carrier;
        flightNumber = flight;

        API_TYPE = "FLIGHT_STATUS"; // Sets the type to FLIGHT_STATUS.
        selectApi(); // Selects the API based on the specified API type.
    }

    /** SERVER FUNCTIONALITY ___________________________________________________________________ **/

    // selectApi(): Selects an API based on the API type.
    private void selectApi() {

        // WEATHER UNDERGROUND API (VIA GPOP SERVER):
        if (API_TYPE.equals("WEATHER")) { BASE_URL = SERVER_URL + "weather.php"; }

        // WEATHER UNDERGROUND API - AIRPORT (VIA GPOP SERVER):
        else if (API_TYPE.equals("WEATHER-AIRPORT")) { BASE_URL = SERVER_URL + "weather-airport.php?airline=" + airlineCarrier + "&flight_number=" + flightNumber; }

        // FLYSFO API:
        else if (API_TYPE.equals("FLYSFO")) { BASE_URL = SERVER_URL; }

        // FLIGHT STATUS API:
        else if (API_TYPE.equals("FLIGHT_STATUS")) {
            BASE_URL = SERVER_URL + "stats.php?airline=" + airlineCarrier + "&flight_number=" + flightNumber;
        }

        // NULL (NO API SELECTED):
        else { BASE_URL = null; }

        Log.d(TAG, "BASE_URL: " + BASE_URL); // Logging.
    }

    // getJsonData(): Retrieves the JSON data string.
    public void getJsonData(JsonHttpResponseHandler handler) {

        // If the URL is not null, it retrieves the JSON data string.
        if (BASE_URL != null) {
            client.get(BASE_URL, handler); // Retrieves JSON data.
            Log.d(TAG, "getJsonData(): Retrieving JsonData from " + BASE_URL); // Logging.
        }

        else { Log.d(TAG, "getJsonData(): URL was invalid."); } // Logging.
    }
}
