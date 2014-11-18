package wwairport.gpop.us.wearableworldconnectedairport.Server;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWClient {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // API VARIABLES
    //private final String API_KEY = "au9t49ts6j2t5ngpfhrydkcu"; // API key string.
    private String BASE_URL = ""; // Base URL string.
    private String API_TYPE = "NONE"; // API type string.

    // CLIENT VARIABLES
    private AsyncHttpClient client; // AsyncHttpClient object.

    // LOGGING VARIABLES
    private static final String TAG = WWClient.class.getSimpleName(); // Retrieves the simple name of the class.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // WWClient(): Sets up the HTTP Client.
    public WWClient(String api_type) {

        // Initializes the class variables.
        this.client = new AsyncHttpClient();
        API_TYPE = api_type;

        selectApi(); // Selects the API based on the specified API type.
    }

    /** SERVER FUNCTIONALITY ___________________________________________________________________ **/

    // createApiUrl(): Creates the full API URL string.
    private String createApiUrl(String apiURL) { return BASE_URL + apiURL; }

    // selectApi(): Selects an API based on the API type.
    private void selectApi() {

        // WEATHER UNDERGROUND API:
        if (API_TYPE.equals("WEATHER")) { BASE_URL = "http://gpop-server.com/sfo/weather.php"; }

        // FLYSFO API:
        else if (API_TYPE.equals("FLYSFO")) { BASE_URL = "http://gpop-server.com/sfo/"; }

        // FLIGHT STATUS API:
        else if (API_TYPE.equals("FLIGHT_STATUS")) {
            BASE_URL = "";
        }

        // NULL (NO API SELECTED):
        else {
            BASE_URL = null;
        }

        Log.d(TAG, "BASE_URL: " + BASE_URL); // Logging.
    }

    // getJsonData(): Retrieves the JSON data string.
    public void getJsonData(JsonHttpResponseHandler handler) {

        //String url = createApiUrl("lists/movies/box_office.json"); // Creates the full API URL string.

        // Creates an asynchronous HTTP request.
        //RequestParams params = new RequestParams();
        //RequestParams params = new RequestParams("apikey", API_KEY);

        // If the URL is not null, it retrieves the JSON data string.
        if (BASE_URL != null) {
            client.get(BASE_URL, handler); // Retrieves JSON data.
            Log.d(TAG, "getJsonData(): Retrieving JsonData..."); // Logging.
            //client.get(BASE_URL, params, handler);
        }

        else { Log.d(TAG, "getJsonData(): URL was invalid."); } // Logging.
    }
}
