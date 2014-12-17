package gpop.us.sfoexplorer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import gpop.us.sfoexplorer.Data.SFOWeather;
import gpop.us.sfoexplorer.Memory.SFOMemory;
import gpop.us.sfoexplorer.Model.SFOWeatherModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.Server.SFOClient;
import gpop.us.sfoexplorer.UI.SFOFont;
import gpop.us.sfoexplorer.UI.SFOImages;
import gpop.us.sfoexplorer.UI.SFOMenu;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class SFOTitleActivity extends Activity implements AdapterView.OnItemSelectedListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // FLIGHT VARIABLES
    private String flightNumber; // References the flight number.

    // LAYOUT VARIABLES
    private EditText flight_input; // References the EditText input fields.
    private ImageView weather_image; // References the ImageView object for the weather icon.
    private Spinner airlineSpinner; // Spinner object for the main airline spinner.
    private TextView weather_status; // References the TextView object for the weather status.

    // LOGGING VARIABLES
    private static final String TAG = SFOTitleActivity.class.getSimpleName(); // Retrieves the simple name of the class.

    // SERVER VARIABLES
    private SFOClient client; // Custom AsyncHttpClient client object for accessing JSON data.
    private SFOWeatherModel weatherModel; // The model object for JSON weather data.

    // THREAD VARIABLES
    private Handler updateThread = new Handler(); // A thread that handles the updating of the weather bar.
    private int updateTimer = 60000; // Time (in milliseconds) used by the updateThread.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpLayout(); // Sets up the layout for the activity.
    }

    // onResume(): This function runs when the activity is visible and is actively running.
    @Override
    public void onResume() {
        super.onResume();
        startStopAllThreads(true); // Starts all threads.
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    protected void onPause() {
        super.onPause();
        startStopAllThreads(false); // Stops all threads.
    }

    // onStop(): This function runs when screen is no longer visible and the activity is in a
    // state prior to destruction.
    @Override
    protected void onStop() {
        super.onStop();
        finish(); // The activity is terminated at this point.
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    protected void onDestroy() {

        super.onDestroy();
        recycleMemory(); // Recycles all ImageView and View objects to free up memory resources.
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {
        finish(); // The activity is terminated at this point.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {

        setContentView(R.layout.ww_title_activity); // Sets the XML file.
        setUpImages(); // Sets up the ImageView objects.
        setUpInput(); // Sets up the EditText objects.
        setUpButtons(); // Sets up the Button objects.
        setUpSpinner(); // Sets up the spinner dropdown object.
        setUpWeatherBar(); // Sets up the weather bar objects.
    }

    // setUpButtons(): Sets up the buttons for the layout.
    private void setUpButtons() {

        // References the Button objects in the layout.
        Button exploreButton = (Button) findViewById(R.id.explore_button);
        Button skipButton = (Button) findViewById(R.id.skip_button);

        // Sets custom font styles for the buttons.
        exploreButton.setTypeface(SFOFont.getInstance(this).setBigNoodleTypeFace());
        skipButton.setTypeface(SFOFont.getInstance(this).setBigNoodleTypeFace());

        // Sets up the listener and the actions for the EXPLORE button.
        exploreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Retrieves the string from the EditText input.
                flightNumber = flight_input.getText().toString();

                // Sets up the intent to launch the next activity.
                Intent i = new Intent("gpop.us.sfoexplorer.MAINACTIVITY");
                i.putExtra("flight_number", flightNumber); // Passes the flight number to the next activity.
                i.putExtra("flight_skip", false); // Passes the flight number skip value to the next activity.
                startActivity(i); // Begins the next activity.
            }
        });

        // Sets up the listener and the actions for the SKIP button.
        skipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Sets up the intent to launch the next activity.
                Intent i = new Intent("gpop.us.sfoexplorer.MAINACTIVITY");
                i.putExtra("flight_number", "NULL"); // Passes an invalid flight number to the next activity.
                i.putExtra("flight_skip", true); // Passes the flight number skip value to the next activity.
                startActivity(i); // Begins the next activity.
            }
        });
    }

    // setUpImages(): Sets up the images for the layout.
    private void setUpImages() {

        // References the ImageView objects for the layout.
        ImageView title_activity_logo = (ImageView) findViewById(R.id.title_activity_logo);
        ImageView title_background_image = (ImageView) findViewById(R.id.title_activity_background);

        // Sets the image resource for the logo.
        //Picasso.with(this).load(R.drawable.explorer_logo).into(title_activity_logo);

        // Sets the image resource for the background.
        Picasso.with(this).load(R.drawable.sfo_bg).fit().centerCrop().into(title_background_image);
    }

    // setUpInput(): Sets up the EditText objects for the layout.
    private void setUpInput() {

        // References the EditText objects.
        flight_input = (EditText) findViewById(R.id.flight_number_input);

        // Sets custom font styles for the input.
        flight_input.setTypeface(SFOFont.getInstance(this).setBigNoodleTypeFace());
    }


    // setUpWeatherBar(): Sets up the weather bar.
    private void setUpWeatherBar() {

        // Sets up the ImageView objects.
        weather_image = (ImageView) findViewById(R.id.title_weather_icon);

        // Sets up the TextView objects.
        weather_status = (TextView) findViewById(R.id.title_weather_text);
        weather_status.setTypeface(SFOFont.getInstance(this).setBigNoodleTypeFace()); // Sets the custom font face.
        weather_status.setShadowLayer(4, 0, 0, Color.BLACK);

        getWeatherStatus(); // Updates the weather status on the notification bar.
    }

    /** SPINNER FUNCTIONALITY __________________________________________________________________ **/

    // setUSpinner(): Sets up the map spinner list for the map selection menu.
    private void setUpSpinner() {

        // SPINNER VARIABLES:
        airlineSpinner = (Spinner) findViewById(R.id.title_airline_dropdown_spinner);

        String[] airlineList = SFOMenu.setAirlineList(this, "terminal_2"); // Sets up the lists for the Spinner object.
        SFOMenu.createAirlineSpinner(this, airlineSpinner, airlineList); // Creates a customized spinner for airlineSpinner.
        airlineSpinner.setOnItemSelectedListener(this); // Sets a listener to the mapSpinner object.
        airlineSpinner.setOnTouchListener(spinnerOnTouch); // Sets up an onClick-like event for the mapSpinner object.
    }

    // spinnerOnTouch(): Captures the touch events for mapSpinner and is primarily used to play the
    // sound effect when the spinner is pressed. This is a workaround for onClick events for spinners,
    // as spinner objects do not support onClick events natively.
    private final View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

         // When the spinner is pressed, it plays a sound effect.
         if (event.getAction() == MotionEvent.ACTION_UP) { }

         return false;
        }
    };

    // onItemSelected(): Override function for setOnTouchListener for the airlineSpinner object. When
    // a airline is selected from the spinner drop down list, the airline is selected.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // DO SOMETHING HERE.

        //int spinner_choice = airlineSpinner.getSelectedItemPosition(); // Saves the spinner position for onResume events.
    }

    // onNothingSelected(): Override function for setOnTouchListener for the airlineSpinner object.
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /** SERVER FUNCTIONALITY ___________________________________________________________________ **/

    // getWeatherStatus(): Retrieves the current weather status.
    private void getWeatherStatus() {

        client = new SFOClient("WEATHER"); // Sets up the JSON client for retrieving weather data.

        // Attempts to retrieve the JSON data from the server.
        client.getJsonData(new JsonHttpResponseHandler() {

            // onSuccess(): Run when JSON request was successful.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "Weather Handshake successful! " + response.toString()); // Logging.
                weatherModel = SFOWeatherModel.fromJson(response); // Attempts to retrieve a JSON string from the server.

                String current_weather = weatherModel.getWeather(); // Sets the weather status from the JSON string.
                double current_temperature = weatherModel.getTemperature(); // Sets the current temperature from the JSON string.

                // Creates a new Time object.
                Time currentTime = new Time(); // Initializes the Time object.
                currentTime.setToNow(); // Sets the current time.
                int newTime = (int) (currentTime.toMillis(true) / 1000); // Converts the time into hours.

                // Retrieves the appropriate weather image based on the value from the JSON string.
                int weather_graphic = SFOWeather.weatherGraphicSelector(current_weather, newTime);

                // Sets the weather icon for the ImageView object.
                Picasso.with(getApplicationContext())
                        .load(weather_graphic)
                        .withOptions(SFOImages.setBitmapOptions())
                        .resize(48, 48)
                        .centerCrop()
                        .into(weather_image);

                // Sets the weather status for the TextView object.
                weather_status.setText(current_temperature + "° " + current_weather);
            }

            // onFailure(): Run when JSON request was a failure.
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d(TAG, "Weather Handshake failure! | Status Code: " + statusCode); // Logging.
                //Toast.makeText(getApplicationContext(), "Weather Handshake failure! | Status Code: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** THREAD FUNCTIONALITY ________________________________________________________________ **/

    // runningUpdates(): Updates the weather and time to flight boarding.
    private Runnable runningUpdates = new Runnable() {

        public void run() {

            getWeatherStatus(); // Updates the current weather status on the notification bar.

            updateThread.postDelayed(this, updateTimer); // Thread loops for every 60000 milliseconds.
        }
    };

    // startStopAllThreads(): Starts or stops all threads.
    private void startStopAllThreads(Boolean isStart) {

        // Starts all threads.
        if (isStart) { updateThread.postDelayed(runningUpdates, updateTimer); }

        // Stops all threads.
        else { updateThread.removeCallbacks(runningUpdates); }
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles ImageView and View objects to clear up memory resources prior to
    // Activity destruction.
    private void recycleMemory() {

        // NullPointerException error handling.
        try {

            // Unbinds all Drawable objects attached to the current layout.
            SFOMemory.unbindDrawables(findViewById(R.id.ww_title_activity_layout));
        }

        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
        }
    }
}
