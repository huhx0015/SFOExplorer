package gpop.us.sfoexplorer.Activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import gpop.us.sfoexplorer.Fragments.WWDepartureFragment;
import gpop.us.sfoexplorer.Fragments.WWDetailsFragment;
import gpop.us.sfoexplorer.Fragments.WWWeatherFragment;
import gpop.us.sfoexplorer.Model.WWFlightModel;
import gpop.us.sfoexplorer.Motion.WWVibration;
import gpop.us.sfoexplorer.Notifications.WWNotifications;
import gpop.us.sfoexplorer.Data.WWWeather;
import it.sephiroth.android.library.picasso.Picasso;
import gpop.us.sfoexplorer.Device.WWDisplay;
import gpop.us.sfoexplorer.Fragments.WWCardFragment;
import gpop.us.sfoexplorer.Memory.WWMemory;
import gpop.us.sfoexplorer.Model.WWEventModel;
import gpop.us.sfoexplorer.Model.WWWeatherModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.Server.WWClient;
import gpop.us.sfoexplorer.UI.WWFont;
import gpop.us.sfoexplorer.UI.WWImages;

public class WWMainActivity extends FragmentActivity implements WWCardFragment.OnCardSelectedListener,
        WWDetailsFragment.OnDetailsSelectedListener, WWDepartureFragment.OnFlightSelectedListener,
        WWWeatherFragment.OnWeatherSelectedListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CARD VARIABLES
    private ArrayList<WWEventModel> models; // References the ArrayList of WWEventModel objects.
    private WWFlightModel flightModel; // The model object for JSON flight data.
    private WWWeatherModel weatherModel; // The model object for JSON weather data.

    // FLIGHT VARIABLES
    private int timeToBoard = 31; // Used to reference the remaining time (in minutes) to flight boarding.
    private String airlineCode = "AA"; // Used to reference the airline code.
    private String airlineCarrier = "American Airlines"; // Used to reference the airline carrier.
    private String flightDestination = "Minneapolis Saint Paul"; // Used to reference the passenger's destination.
    private String flightNumber = "AA 1482"; // Used to reference the passenger's flight number.
    private String departureGate = "A20"; // Used to reference the passenger's departure gate.
    private String currentLocation = "San Francisco International Airport"; // Used to reference the traveler's current location.

    // FRAGMENT VARIABLES
    private Boolean isDetailsOn = false; // Used to determine if the card details fragment is currently being shown.
    private Boolean isDetailsFragmentMade = false; // Used to determine if the card details fragment has already been created.
    private Boolean isFlightOn = false; // Used to determine if the flight fragment is currently being shown.
    private Boolean isFlightFragmentMade = false; // Used to determine if the flight fragment has already been created.
    private Boolean isWeatherOn = false; // Used to determine if the weather fragment is currently being shown.
    private Boolean isWeatherFragmentMade = false; // Used to determine if the weather fragment has already been created.
    private WWDetailsFragment details_fragment; // References the WWDetailsFragment object.
    private WWDepartureFragment flight_fragment; // References the WWDepartureFragment object.
    private WWWeatherFragment weather_fragment; // References the WWWeatherFragment object.

    // LAYOUT VARIABLES
    private FrameLayout card_fragment_details_container; // References the card fragment details container object.
    private LinearLayout notification_flight_container; // References the flight container on the notification bar.
    private LinearLayout notification_weather_container; // References the weather container on the notification bar.

    // LOGGING VARIABLES
    private static final String TAG = WWMainActivity.class.getSimpleName(); // Retrieves the simple name of the class.

    // NOTIFICATION VARIABLES
    private ImageView notification_flight_image, notification_weather_image;
    private TextView notification_countdown_timer, notification_flight_number, notification_gate_number, notification_weather_status;

    // SERVER VARIABLES
    private WWClient client; // Custom AsyncHttpClient client object for accessing JSON data.

    // SLIDER VARIABLES
    private int currentCardNumber = 0; // Used to determine which card fragment is currently being displayed.
    private int numberOfCards = 1; // Used to determine how many card fragments are to be displayed.
    private PagerAdapter wwPageAdapter; // Used to reference the PagerAdapter object.
    private ViewPager wwTitleScreenPager; // Used to reference the ViewPager object.

    // SPEECH VARIABLES
    private TextToSpeech speechInstance; // Used to reference the TTS instance for the class.

    // SYSTEM VARIABLES
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.
    private static WeakReference<WWMainActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    // THREAD VARIABLES
    private Handler updateThread = new Handler(); // A thread that handles the updating of the notification bar and .
    private int updateTimer = 60000; // Time (in milliseconds) used by the updateThread.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weakRefActivity = new WeakReference<WWMainActivity>(this); // Creates a weak reference of this activity.
        getIntentBundle(); // Retrieves data from the previous activity.
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

        // If the details fragment is currently being shown, the fragment is removed.
        if (isDetailsOn) {
            isDetailsOn = false; // Indicates that the details fragment is currently not being shown.
            displayFragment(false, details_fragment); // Hides the card details fragment.
        }

        // If the weather fragment is currently being shown, the fragment is removed.
        else if (isWeatherOn) {
            isWeatherOn = false; // Indicates that the weather fragment is currently not being shown.
            displayFragment(false, weather_fragment); // Hides the weather fragment.
        }

        else { finish(); } // The activity is terminated at this point.
    }

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setUpLayout(); // Sets up the layout for the fragment.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ww_main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** FRAGMENT INTERFACE FUNCTIONALITY _______________________________________________________ **/

    // updateFromCardFragment(): This function is called whenever the user interacts with the fragment
    // buttons. Called directly by the interface methods in WWCardFragment.
    @Override
    public void updateFromCardFragment(Boolean showDetails) {

        // If showDetails value is true, the card details fragment is setup.
        if (showDetails) {

            isDetailsOn = true; // Indicates that the details fragment is currently being shown.

            // Retrieves the WWEventModel, based on the card that is currently displayed.
            WWEventModel currentEvent = models.get(currentCardNumber);

            // If the card details fragment has already been made, the fragment is shown instead of
            // being created.
            if (isDetailsFragmentMade) { displayFragment(true, details_fragment); }
            else {

                // Initializes the WWDetailsFragment object.
                details_fragment = new WWDetailsFragment();
                details_fragment.initializeFragment(currentCardNumber, currentEvent);

                setUpFragment(details_fragment, "FLIGHT"); // Sets up the fragment for the card details.
            }

            // Disables flight fragment if currently active.
            if (isFlightOn) {
                isFlightOn = false;
                isFlightFragmentMade = false;
            }

            // Disables weather fragment if currently active.
            if (isWeatherOn) {
                isWeatherOn = false;
                isWeatherFragmentMade = false;
            }
        }
    }

    // updateFromDetailsFragment(): This function is called whenever the user interacts with the fragment
    // buttons. Called directly by the interface methods in WWDetailsFragment.
    @Override
    public void updateFromDetailsFragment(Boolean hideDetails) {

        // If hideDetails value is true, the card details fragment is removed.
        if (hideDetails) {
            isDetailsOn = false; // Indicates that the details fragment is currently not being shown.
            displayFragment(false, details_fragment); // Hides the card details fragment.
        }
    }

    // updateFromFlightFragment(): This function is called whenever the user interacts with the fragment
    // buttons. Called directly by the interface methods in WWDepartureFragment.
    @Override
    public void updateFromFlightFragment(Boolean hideFlight) {

        // If hideFlight value is true, the departure fragment is removed.
        if (hideFlight) {
            isFlightOn = false; // Indicates that the details fragment is currently not being shown.
            displayFragment(false, flight_fragment); // Hides the weather fragment.
        }
    }

    // updateFromWeatherFragment(): This function is called whenever the user interacts with the fragment
    // buttons. Called directly by the interface methods in WWWeatherFragment.
    @Override
    public void updateFromWeatherFragment(Boolean hideWeather) {

        // If hideWeather value is true, the weather fragment is removed.
        if (hideWeather) {
            isWeatherOn = false; // Indicates that the details fragment is currently not being shown.
            displayFragment(false, weather_fragment); // Hides the weather fragment.
        }
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {

        setUpDisplayParameters(); // Sets up the device's display parameters.

        setContentView(R.layout.ww_main_activity); // Sets the XML file.

        // References the layout containers.
        card_fragment_details_container = (FrameLayout) findViewById(R.id.card_fragment_details);
        notification_flight_container = (LinearLayout) findViewById(R.id.notification_flight_container);
        notification_weather_container = (LinearLayout) findViewById(R.id.notification_weather_container);

        setUpNotificationBar(); // Sets up the notification bar for the activity.
        setUpCardEvents(); // Sets up the event cards for the slider fragments.
        setUpButtons(); // Sets up the clickable Button objects for the activity.
    }

    // getIntentBundle(): Retrieves the data from the previous activity.
    private void getIntentBundle() {

        Bundle extras = getIntent().getExtras();

        // Tries to retrieve the additional information from the bundle.
        if (extras != null) {

            flightNumber = extras.getString("flight_number");
            String[] flightStrip = flightNumber.split(" ");

            // If the array size is of two elements, the airline code and flight number is extracted.
            if (flightStrip.length == 2) {
                airlineCode = flightStrip[0];
                flightNumber = flightStrip[1];
            }
        }
    }

    // setUpButtons(): Sets up the buttons for the activity.
    private void setUpButtons() {

        // Sets up the listener and the actions for the weather container button.
        notification_flight_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Checks to see if the flightModel object has been initialized first or not.
                if (flightModel != null) {

                    // Sets up the weather fragment.
                    isFlightOn = true; // Indicates that the flight fragment is currently being shown.

                    // If the card details fragment has already been made, the fragment is shown instead of
                    // being created.
                    if (isFlightFragmentMade) {
                        displayFragment(true, flight_fragment);
                    } else {

                        // Initializes the WWFlightFragment object.
                        flight_fragment = new WWDepartureFragment();
                        flight_fragment.initializeFragment(flightModel, airlineCarrier);

                        setUpFragment(flight_fragment, "FLIGHT"); // Sets up the fragment for the weather details.
                    }
                }
            }
        });

        // Sets up the listener and the actions for the weather container button.
        notification_weather_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Checks to see if the weatherModel object has been initialized first or not.
                if (weatherModel != null) {

                    // Sets up the weather fragment.
                    isWeatherOn = true; // Indicates that the weather fragment is currently being shown.

                    // If the card details fragment has already been made, the fragment is shown instead of
                    // being created.
                    if (isWeatherFragmentMade) {
                        displayFragment(true, weather_fragment);
                    } else {

                        // Initializes the WWWeatherFragment object.
                        weather_fragment = new WWWeatherFragment();
                        weather_fragment.initializeFragment(weatherModel, currentLocation);

                        setUpFragment(weather_fragment, "WEATHER"); // Sets up the fragment for the weather details.
                    }
                }
            }
        });

    }

    /** CARD FUNCTIONALITY _____________________________________________________________________ **/

    // setUpCardEvents(): Sets up the event cards for the slider fragments. Queries the server to
    // retrieve the list of events.
    private void setUpCardEvents() {

        client = new WWClient("FLYSFO"); // Sets up the JSON client for retrieving SFO data.

        // Attempts to retrieve the JSON data from the server.
        client.getJsonData(new JsonHttpResponseHandler() {

            // onSuccess(): Run when JSON request was successful for JSONArray.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.d(TAG, "Fly SFO API Handshake Success (JSONArray Response)" + response.toString()); // Logging.

                models = WWEventModel.fromJson(response); // Builds an ArrayList of WWEventModel objects from the JSONArray.
                numberOfCards = models.size(); // Retrieves the number of card fragments to build.

                setUpSlider(false); // Initializes the fragment slides for the PagerAdapter.
            }

            // onSuccess(): Run when JSON request was successful for JSONObject.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "Fly SFO API Handshake Success (JSONObject Response) | Status Code: " + statusCode); // Logging.
            }

            // onFailure(): Run when JSON request was a failure.
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Fly SFO API Handshake failure! | Status Code: " + statusCode); // Logging.
            }
        });
    }

    /** FRAGMENT FUNCTIONALITY _________________________________________________________________ **/

    // displayFragment(): Displays/hides the fragment container.
    private void displayFragment(Boolean show, Fragment frag) {

        if ((weakRefActivity.get() != null) && (weakRefActivity.get().isFinishing() != true)) {

            // Initializes the manager and transaction objects for the fragments.
            final FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
            final FragmentTransaction fragTrans = fragMan.beginTransaction();
            fragTrans.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);

            // Displays the fragment.
            if (show == true) { fragTrans.show(frag); }

            // Hides the fragment.
            else { fragTrans.hide(frag); } // Hides the fragment.

            // Makes the changes to the fragment manager and transaction objects.
            fragTrans.addToBackStack(null);
            fragTrans.commitAllowingStateLoss();
        }
    }

    // removeFragment(): Removes the fragment from the container.
    private void removeFragment(Fragment frag) {

        // Initializes the manager and transaction objects for the fragments.
        FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out); // Sets the custom animation.
        card_fragment_details_container.setVisibility(View.INVISIBLE); // Hides the fragment container.
        fragTrans.show(frag); // Shows the fragment.
        fragMan.popBackStack(); // Pops the fragment from the stack.
        card_fragment_details_container.removeAllViews(); // Removes all views in the layout.
    }

    // setUpFragment(): Updates the content of the fragment container with the proper fragment.
    private void setUpFragment(Fragment fragment, String fragmentType) {

        if ((weakRefActivity.get() != null) && (weakRefActivity.get().isFinishing() != true)) {

            card_fragment_details_container.setVisibility(View.VISIBLE); // Displays the fragment.

            // Initializes the manager and transaction objects for the fragments.
            FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
            FragmentTransaction fragTrans = fragMan.beginTransaction();

            fragTrans.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);
            fragTrans.replace(R.id.card_fragment_details, fragment);

            // Makes the changes to the fragment manager and transaction objects.
            fragTrans.addToBackStack(null);
            fragTrans.commitAllowingStateLoss();

            // DETAILS FRAGMENT:
            if (fragmentType.equals("DETAILS")) {
                isDetailsFragmentMade = true; // Indicates that the card details fragment has been made.

            }

            // FLIGHT:
            else if (fragmentType.equals("FLIGHT")) {
                isFlightFragmentMade = true; // Indicates that the flight fragment has been made.
            }

            // WEATHER:
            else if (fragmentType.equals("WEATHER")) {
                isWeatherFragmentMade = true; // Indicates that the weather fragment has been made.
            }
        }
    }

    /** NARRATION FUNCTIONALITY ________________________________________________________________ **/

    // startSpeech(): Activates voice functionality to say something.
    private void startSpeech(final String script) {

        // Creates a new instance to begin TextToSpeech functionality.
        speechInstance = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                speechInstance.speak(script, TextToSpeech.QUEUE_FLUSH, null); // The script is spoken by the TTS system.
            }
        });
    }

    /** NOTIFICATION BAR FUNCTIONALITY _________________________________________________________ **/

    // setUpNotificationBar(): Sets up the notification bar for the activity.
    private void setUpNotificationBar() {

        // Sets up the ImageView objects.
        notification_flight_image = (ImageView) findViewById(R.id.notification_flight_icon);
        notification_weather_image = (ImageView) findViewById(R.id.notification_weather_image);

        // Sets up the TextView objects.
        notification_countdown_timer = (TextView) findViewById(R.id.notification_countdown_text);
        notification_flight_number = (TextView) findViewById(R.id.notification_flight_number);
        notification_gate_number = (TextView) findViewById(R.id.notification_gate_number);
        notification_weather_status = (TextView) findViewById(R.id.notification_weather_status);

        // Sets up the custom font type for the TextView objects.
        notification_countdown_timer.setTypeface(WWFont.getInstance(this).setBigNoodleTypeFace());// Sets the custom font face.
        notification_flight_number.setTypeface(WWFont.getInstance(this).setBigNoodleTypeFace()); // Sets the custom font face.
        notification_gate_number.setTypeface(WWFont.getInstance(this).setBigNoodleTypeFace()); // Sets the custom font face.
        notification_weather_status.setTypeface(WWFont.getInstance(this).setBigNoodleTypeFace()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        notification_countdown_timer.setShadowLayer(4, 0, 0, Color.BLACK);
        notification_flight_number.setShadowLayer(4, 0, 0, Color.BLACK);
        notification_gate_number.setShadowLayer(4, 0, 0, Color.BLACK);
        notification_weather_status.setShadowLayer(4, 0, 0, Color.BLACK);

        getFlightStatus(); // Updates the flight status on the notification bar.
        getWeatherStatus(); // Updates the weather status on the notification bar.
        updateTimeToBoard(); // Updates the time to board value on the notification bar.
    }

    // getWeatherStatus(): Retrieves the current weather status.
    private void getWeatherStatus() {

        client = new WWClient("WEATHER"); // Sets up the JSON client for retrieving weather data.

        // Attempts to retrieve the JSON data from the server.
        client.getJsonData(new JsonHttpResponseHandler() {

            // onSuccess(): Run when JSON request was successful.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "Weather Handshake successful! " + response.toString()); // Logging.
                //Toast.makeText(getApplicationContext(), "Weather Handshake successful! " + response.toString(), Toast.LENGTH_SHORT).show();
                weatherModel = WWWeatherModel.fromJson(response); // Attempts to retrieve a JSON string from the server.

                String current_weather = weatherModel.getWeather(); // Sets the weather status from the JSON string.
                double current_temperature = weatherModel.getTemperature(); // Sets the current temperature from the JSON string.

                // Creates a new Time object.
                Time currentTime = new Time(); // Initializes the Time object.
                currentTime.setToNow(); // Sets the current time.
                int newTime = (int) (currentTime.toMillis(true) / 1000); // Converts the time into hours.

                // Retrieves the appropriate weather image based on the value from the JSON string.
                int weather_image = WWWeather.weatherGraphicSelector(current_weather, newTime);

                // Sets the weather icon for the ImageView object.
                Picasso.with(getApplicationContext())
                        .load(weather_image)
                        .withOptions(WWImages.setBitmapOptions())
                        .resize(48, 48)
                        .centerCrop()
                        .into(notification_weather_image);

                // Sets the weather status for the TextView object.
                notification_weather_status.setText(current_temperature + "° " + current_weather);
            }

            // onFailure(): Run when JSON request was a failure.
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d(TAG, "Weather Handshake failure! | Status Code: " + statusCode); // Logging.
                //Toast.makeText(getApplicationContext(), "Weather Handshake failure! | Status Code: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // getFlightStatus(): Retrieves the current flight status.
    private void getFlightStatus() {

        client = new WWClient(airlineCode, flightNumber); // Sets up the JSON client for retrieving weather data.

        // Attempts to retrieve the JSON data from the server.
        client.getJsonData(new JsonHttpResponseHandler() {

            // onSuccess(): Run when JSON request was successful.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "Flight Handshake successful! " + response.toString()); // Logging.
                flightModel = WWFlightModel.fromJson(response); // Attempts to retrieve a JSON string from the server.

                timeToBoard = flightModel.getTimeToDeparture(); // Gets the time to board value from the JSON string.
                departureGate = flightModel.getDepartureGate(); // Gets the departure gate from the JSON string.
                String departure_time = flightModel.getDepartureTime(); // Gets the departure time from the JSON string.

                Log.d(TAG, "Time to departure value: " + timeToBoard); // Logging.

                int airline_logo = R.drawable.plane_icon; // Image to use as the airline flag.

                // AMERICAN AIRLINES:
                if ( (airlineCode.equals("AA")) || (airlineCode.equals("aa")) ) {
                    airline_logo = R.drawable.aa_icon;
                }

                // Sets the weather icon for the ImageView object.
                Picasso.with(getApplicationContext())
                        .load(airline_logo)
                        .placeholder(R.drawable.dark_transparent_tile)
                        .resize(48, 48)
                        .centerCrop()
                        .into(notification_flight_image);

                // Sets the flight and gate number for the TextView objects.
                notification_countdown_timer.setText(timeToBoard + " MINUTES");
                notification_flight_number.setText(flightNumber);
                notification_gate_number.setText(departureGate);

            }

            // onFailure(): Run when JSON request was a failure.
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d(TAG, "Flight Handshake failure! | Status Code: " + statusCode); // Logging.
                //Toast.makeText(getApplicationContext(), "Weather Handshake failure! | Status Code: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // updateTimeToBoard(): Updates the time to board value.
    private void updateTimeToBoard() {

        // Updates the countdown timer value.
        notification_countdown_timer.setText(timeToBoard + " MINUTES");
    }

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // setUpDisplayParameters(): Sets up the device's display parameters.
    private void setUpDisplayParameters() {

        // References the display parameters for the device.
        Display deviceWindow = this.getWindowManager().getDefaultDisplay();

        currentOrientation = WWDisplay.updateDisplayLayout(this, deviceWindow); // Retrieves the device's display attributes.
        resolutionDimens = WWDisplay.getResolution(deviceWindow);
        displaySize = WWDisplay.getDisplaySize(resolutionDimens, currentOrientation);
    }

    /** SLIDER FUNCTIONALITY ___________________________________________________________________ **/

    // createSlideFragments(): Sets up the slide fragments for the PagerAdapter object.
    private List<Fragment> createSlideFragments(int numberOfSlides) {

        final List<Fragment> fragments = new Vector<Fragment>(); // List of fragments in which the fragments is stored.

        // Creates the card deck for the slider.
        for (int i = 0; i < numberOfSlides; i++) {

            // Initializes the card fragment and adds it to the deck.
            WWCardFragment cardFragment = new WWCardFragment();
            cardFragment.initializeFragment(i, models.get(i));
            fragments.add(cardFragment);
        }

        return fragments;
    }

    // setPageListener(): Sets up the listener for the PagerAdapter object.
    private void setPageListener(ViewPager page) {

        // Defines the action to take when the page is changed.
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // onPageScrollStateChanged(): Called the page scroll state is changed.
            public void onPageScrollStateChanged(int state) { }

            // onPageScrolled(): Called when the pages are scrolled.
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            // onPageSelected(): Called when a new page is selected.
            public void onPageSelected(int position) {

                currentCardNumber = position; // Sets the current card ID value.

                // Removes the card details fragment if it was created. This is to fix the double
                // animation effect when the fragment is re-created.
                if (isDetailsFragmentMade) {
                    isDetailsFragmentMade = false; // Indicates that the details fragment is no longer made.
                    removeFragment(details_fragment); // Removes the details fragment.
                }

                // Removes the weather fragment if it was created. This is to fix the double
                // animation effect when the fragment is re-created.
                if (isWeatherFragmentMade) {
                    isWeatherFragmentMade = false; // Indicates that the weather fragment is no longer made.
                    removeFragment(weather_fragment); // Removes the weather fragment.
                }
            }
        });
    }

    // setUpSlider(): Initializes the slides for the PagerAdapter object.
    private void setUpSlider(Boolean isChanged) {

        // Resets the ViewPager object if the Page Adapter object has experienced a screen change.
        if (isChanged == true) { wwTitleScreenPager.setAdapter(null); }

        // Initializes and creates a new FragmentListPagerAdapter objects using the List of slides
        // created from createSlideFragments.
        wwPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments(numberOfCards));

        wwTitleScreenPager = (ViewPager) super.findViewById(R.id.card_fragment_pager);
        wwTitleScreenPager.setAdapter(this.wwPageAdapter); // Sets the PagerAdapter object for the activity.
        setPageListener(wwTitleScreenPager); // Sets up the listener for the pager object.

        // If the activity has experienced a screen change, the page is set to the game series that
        // was previously being displayed.
        if (isChanged == true) { wwTitleScreenPager.setCurrentItem(currentCardNumber); } // Loads the selected slider page.
    }

    // FragmentListPagerAdapter(): A subclass that extends upon the FragmentPagerAdapter class object,
    // granting the ability to load slides from a List of Fragments.
    class FragmentListPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments; // Used to store the List of Fragment objects.

        // FragmentListPagerAdapter(): Constructor method for the FragmentListPagerAdapter subclass.
        public FragmentListPagerAdapter(final FragmentManager fragmentManager, final List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        // getCount(): Returns the number of fragments in the PagerAdapter object.
        @Override
        public int getCount() {
            return fragments.size();
        }

        // getItem(): Returns the fragment position in the PagerAdapter object.
        @Override
        public Fragment getItem(final int position) {
            return fragments.get(position);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /** THREAD FUNCTIONALITY ________________________________________________________________ **/

    // checkBoardingTime(): Checks on the remaining time to boarding the flight and activates voice,
    // notification and vibration alerts accordingly.
    private void checkBoardingTime() {

        Boolean notifyTraveler = false; // Used to determine if the traveler should be notified or not.
        String message = ""; // Used to build a message which voice and notification systems will use.

        // 30 MINUTES:
        if ( (timeToBoard == 30) || (timeToBoard == 20)) {

            message = "Your flight to " + flightDestination + " on " + airlineCarrier + " " +
                    flightNumber + " at Gate " + departureGate + " will be boarding in " + timeToBoard + " minutes.";

            notifyTraveler = true;
        }

        // 10 - 15 MINUTES:
        else if ( (timeToBoard == 15) || (timeToBoard == 10)) {

            message = "Your flight to " + flightDestination + " on " + airlineCarrier + " " +
                    flightNumber + " at Gate " + departureGate +
                    " is boarding in " + timeToBoard + " minutes. Please make your way to Gate "
                    + departureGate + " now.";

            notifyTraveler = true;
        }

        // 1 - 5 MINUTES:
        else if ( (timeToBoard <= 5) && (timeToBoard > 0))  {

            message = "Your flight to " + flightDestination + " on " + airlineCarrier + " " +
                    flightNumber + " at Gate " + departureGate +
                    " is boarding in " + timeToBoard + " minutes. Please make your way to Gate "
                    + departureGate + " immediately!";

            notifyTraveler = true;
        }

        // 0 MINUTES:
        else if (timeToBoard == 0)  {

            message = "Final boarding call! Your flight to " + flightDestination + " on " + airlineCarrier + " " +
                    flightNumber + " at Gate " + departureGate +
                    " is now boarding. Please make your way to Gate " + departureGate + " immediately!";

            notifyTraveler = true;
        }

        // If the notification conditions have been met, send out a voice and notification message.
        if (notifyTraveler) {
            startSpeech(message); // Creates a speech instance.
            WWNotifications.createNotification(this, message); // Creates a notification instance.
            WWVibration.vibrateDevice(500, this); // Vibrates the device.
        }
    }

    // runningUpdates(): Updates the weather and time to flight boarding.
    private Runnable runningUpdates = new Runnable() {

        public void run() {

            checkBoardingTime(); // Checks the boarding time and updates the time accordingly.
            getWeatherStatus(); // Updates the current weather status on the notification bar.
            updateTimeToBoard(); // Updates the time to board counter on the notification bar.
            timeToBoard--; // Decrements the boarding time value.
            getFlightStatus(); // Updates the flight status on the notification bar.

            updateThread.postDelayed(this, updateTimer); // Thread loops for every 60000 milliseconds.
        }
    };

    // startStopAllThreads(): Starts or stops all threads.
    private void startStopAllThreads(Boolean isStart) {

        // Starts all threads.
        if (isStart) {
            updateThread.postDelayed(runningUpdates, updateTimer);
        }

        // Stops all threads.
        else {
            updateThread.removeCallbacks(runningUpdates);
        }
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles ImageView and View objects to clear up memory resources prior to
    // Activity destruction.
    private void recycleMemory() {

        // NullPointerException error handling.
        try {

            // Unbinds all Drawable objects attached to the current layout.
            WWMemory.unbindDrawables(findViewById(R.id.ww_main_activity_layout));
        }

        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
        }
    }
}
