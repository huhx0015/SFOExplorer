package gpop.us.sfoexplorer.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import gpop.us.sfoexplorer.Device.WWDisplay;
import gpop.us.sfoexplorer.Model.WWWeatherModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.WWFont;
import gpop.us.sfoexplorer.UI.WWImages;
import gpop.us.sfoexplorer.Weather.WWWeather;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class WWWeatherFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private View weather_view; // References the layout for the fragment.

    // LOGGING VARIABLES
    private static final String TAG = WWWeatherFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    // WEATHER VARIABLES
    private String currentLocation; // References the current location.
    private WWWeatherModel weatherModel; // References the event model object for this fragment.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static WWWeatherFragment weather_fragment = new WWWeatherFragment();

    // WWWeatherFragment(): Deconstructor for the WWWeatherFragment.
    public WWWeatherFragment() {}

    // getInstance(): Returns the details_fragment instance.
    public static WWWeatherFragment getInstance() { return weather_fragment; }

    // initializeFragment(): Initializes the fragment with the event properties.
    public void initializeFragment(WWWeatherModel weather, String location) {
        weatherModel = weather;
        currentLocation = location;
    }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setUpDisplayParameters(); // Sets up the device's display parameters.

        // Sets the view to the specified XML layout file.
        weather_view = (ViewGroup) inflater.inflate(R.layout.ww_weather_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return weather_view;
    }

    // onActivityCreated(): Runs after the attached activity's onCreate() function has been completed.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initiates a first-time call through the interface to initialize the connection between
        // the fragment and the activity.
        //updateActivity();
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    public void onResume() {
        super.onResume();
    }

    // onPause(): This function is called whenever the current fragment is suspended.
    @Override
    public void onPause() {
        super.onPause();
    }

    /** ACTIVITY FUNCTIONALITY _________________________________________________________________ **/

    // updateActivity(): Interfaces with the attached activity class and signals that properties
    // have been changed.
    private void updateActivity(Boolean hideWeather) {

        // Signals to the attached activity that the card details fragment needs to be closed.
        try { ((OnWeatherSelectedListener) currentActivity).updateFromWeatherFragment(hideWeather); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    /** FRAGMENT EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setUpDisplayParameters(); // Sets up the device's display parameters.
        setUpLayout(); // Sets up the layout for the fragment.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {

        setUpButtons(); // Sets up the Button objects for the fragment.
        setUpImages(); // Sets up the ImageView objects for the fragment.
        setUpText(); // Sets up the TextView objects for the fragment.

    }

    // setUpButtons(): Sets up the button objects for the fragment.
    private void setUpButtons() {

        // References the ImageButton objects in the layout.
        ImageButton back_button = (ImageButton) weather_view.findViewById(R.id.weather_back_button);

        // Sets up the listener and the actions for the back button.
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true); // Signals to the attached activity that the details fragment should be shown.
            }
        });

    }

    // setUpImages(): Sets up the ImageView objects for the fragment.
    private void setUpImages() {

        // References the ImageView objects.
        ImageView weather_image = (ImageView) weather_view.findViewById(R.id.weather_image);

        String current_weather = weatherModel.getWeather(); // Sets the weather status from the JSON string.

        // Creates a new Time object.
        Time currentTime = new Time(); // Initializes the Time object.
        currentTime.setToNow(); // Sets the current time.
        int newTime = (int) (currentTime.toMillis(true) / 1000); // Converts the time into hours.

        // Retrieves the appropriate weather image based on the value from the JSON string.
        int weather_image_resource = WWWeather.weatherGraphicSelector(current_weather, newTime);

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(weather_image_resource)
                .withOptions(WWImages.setBitmapOptions())
                .into(weather_image);
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // References the TextView objects.
        TextView temperature_text = (TextView) weather_view.findViewById(R.id.temperature_text);
        TextView weather_status_text = (TextView) weather_view.findViewById(R.id.weather_status_text);
        TextView current_location = (TextView) weather_view.findViewById(R.id.current_location);

        // Sets up the custom font type for the TextView objects.
        temperature_text.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());// Sets the custom font face.
        weather_status_text.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.
        current_location.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        temperature_text.setShadowLayer(8, 4, 4, Color.BLACK);
        weather_status_text.setShadowLayer(8, 4, 4, Color.BLACK);
        current_location.setShadowLayer(8, 4, 4, Color.BLACK);

        // Retrieves the event details from the WWEventModel object.
        double temperature_value = weatherModel.getTemperature(); // Gets the temperature from the JSON string.
        String weather_status_string = weatherModel.getWeather(); // Gets the weather from the JSON string.

        // Sets the weather status for the TextView object.
        temperature_text.setText(temperature_value + "°"); // Sets the temperature text.
        weather_status_text.setText(weather_status_string); // Sets the weather status text.
    }

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // setUpDisplayParameters(): Sets up the device's display parameters.
    private void setUpDisplayParameters() {

        // References the display parameters for the device.
        Display deviceWindow = currentActivity.getWindowManager().getDefaultDisplay();

        currentOrientation = WWDisplay.updateDisplayLayout(currentActivity, deviceWindow); // Retrieves the device's display attributes.
        resolutionDimens = WWDisplay.getResolution(deviceWindow);
        displaySize = WWDisplay.getDisplaySize(resolutionDimens, currentOrientation);
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    /** --------------------------------------------------------------------------------------------
     *  [OnWeatherSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnWeatherSelectedListener{
        public void updateFromWeatherFragment(Boolean hideDetails);
    }
}
