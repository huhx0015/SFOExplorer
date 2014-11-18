package wwairport.gpop.us.wearableworldconnectedairport.Server;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWClient {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // API VARIABLES
    private final String API_KEY = "au9t49ts6j2t5ngpfhrydkcu"; // API key string.
    private final String BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/"; // Base URL string.

    // CLIENT VARIABLES
    private AsyncHttpClient client; // AsyncHttpClient object.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // WWClient(): Sets up the HTTP Client.
    public WWClient() {
        this.client = new AsyncHttpClient();
    }

    /** SERVER FUNCTIONALITY ___________________________________________________________________ **/

    // createApiUrl(): Creates the full API URL string.
    private String createApiUrl(String apiURL) { return BASE_URL + apiURL; }

    // getJsonData(): Retrieves the JSON data string.
    public void getJsonData(JsonHttpResponseHandler handler) {

        String url = createApiUrl("lists/movies/box_office.json"); // Creates the full API URL string.

        // Creates an asynchronous HTTP request.
        RequestParams params = new RequestParams("apikey", API_KEY);
        client.get(url, params, handler);
    }
}
