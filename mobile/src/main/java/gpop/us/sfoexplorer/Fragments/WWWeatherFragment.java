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
import gpop.us.sfoexplorer.Model.WWAirportWeatherModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.WWFont;
import gpop.us.sfoexplorer.UI.WWImages;
import gpop.us.sfoexplorer.Data.WWWeather;
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
    private String destinationLocation; // References the destination location.
    //private WWWeatherModel weatherModel; // References the event model object for this fragment.
    private WWAirportWeatherModel weatherModel; // References the event model object for this fragment.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static WWWeatherFragment weather_fragment = new WWWeatherFragment();

    // WWWeatherFragment(): Deconstructor for the WWWeatherFragment.
    public WWWeatherFragment() {}

    // getInstance(): Returns the details_fragment instance.
    public static WWWeatherFragment getInstance() { return weather_fragment; }

    // initializeFragment(): Initializes the fragment with the event properties.
    public void initializeFragment(WWAirportWeatherModel weather, String location, String destination) {
        weatherModel = weather;
        currentLocation = location;
        destinationLocation = destination;
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
        setUpOriginDestWeather(); // Sets up the ImageView objects for the fragment.
        setUpOriginDestText(); // Sets up the TextView objects for the fragment.

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

    // setUpWeekForecast(): Sets up the image resources for displaying the five day weather
    // forecast.
    private void setUpWeekForecastImages() {


    }

    // setUpOriginDestText(): Sets up the TextView objects for the current origin and destination
    // city weather.
    private void setUpOriginDestText() {

        // References the TextView objects.
        TextView origin_text = (TextView) weather_view.findViewById(R.id.ww_origin_text);
        TextView origin_location = (TextView) weather_view.findViewById(R.id.ww_origin_location_text);
        TextView origin_temperature_text = (TextView) weather_view.findViewById(R.id.ww_origin_weather_temp);
        TextView origin_weather_status_text = (TextView) weather_view.findViewById(R.id.ww_origin_weather_text);
        TextView destination_text = (TextView) weather_view.findViewById(R.id.ww_destination_text);
        TextView destination_location = (TextView) weather_view.findViewById(R.id.ww_destination_location_text);
        TextView destination_temperature_text = (TextView) weather_view.findViewById(R.id.ww_destination_weather_temp);
        TextView destination_weather_status_text = (TextView) weather_view.findViewById(R.id.ww_destination_weather_text);

        // Sets up the custom font type for the TextView objects.
        origin_text.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        origin_location.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        origin_temperature_text.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.
        origin_weather_status_text.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.
        destination_text.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        destination_location.setTypeface(WWFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        destination_temperature_text.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());// Sets the custom font face.
        destination_weather_status_text.setTypeface(WWFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        origin_text.setShadowLayer(8, 4, 4, Color.BLACK);
        origin_location.setShadowLayer(8, 4, 4, Color.BLACK);
        origin_temperature_text.setShadowLayer(8, 4, 4, Color.BLACK);
        origin_weather_status_text.setShadowLayer(8, 4, 4, Color.BLACK);
        destination_text.setShadowLayer(8, 4, 4, Color.BLACK);
        destination_location.setShadowLayer(8, 4, 4, Color.BLACK);
        destination_temperature_text.setShadowLayer(8, 4, 4, Color.BLACK);
        destination_weather_status_text.setShadowLayer(8, 4, 4, Color.BLACK);

        // Retrieves the weather details from the WWAirportWeatherModel object.
        double arrival_temperature_value = weatherModel.getOriginTemperature(); // Gets the temperature from the JSON string.
        double departure_temperature_value = weatherModel.getDestinationTemperature(); // Gets the temperature from the JSON string.
        String arrival_weather_status_string = weatherModel.getOriginWeatherStatus(); // Gets the weather from the JSON string.
        String departure_weather_status_string = weatherModel.getDestinationWeatherStatus(); // Gets the weather from the JSON string.

        // Sets the weather status for the TextView object.
        origin_temperature_text.setText(arrival_temperature_value + "°"); // Sets the temperature text.
        destination_temperature_text.setText(departure_temperature_value + "°"); // Sets the temperature text.
        origin_weather_status_text.setText(arrival_weather_status_string); // Sets the weather status text.
        destination_weather_status_text.setText(departure_weather_status_string + "°"); // Sets the temperature text.

        // Sets the location for the TextView objects.
        origin_location.setText(currentLocation); // Sets the current location text.
        destination_location.setText(destinationLocation); // Sets the destination location text.
    }

    // setUpOriginDestWeather(): Sets up the ImageView objects for the current origin & destination
    // city weather.
    private void setUpOriginDestWeather() {

        // References the ImageView objects.
        ImageView origin_weather_image = (ImageView) weather_view.findViewById(R.id.ww_origin_weather_image);
        ImageView destination_weather_image = (ImageView) weather_view.findViewById(R.id.ww_destination_weather_image);

        // Retrieves the current weather status for the origin and destination locations.
        String origin_current_weather = weatherModel.getOriginWeatherStatus(); // Sets the weather status from the JSON string.
        String departure_current_weather = weatherModel.getDestinationWeatherStatus(); // Sets the weather status from the JSON string.

        // Creates a new Time object.
        Time currentTime = new Time(); // Initializes the Time object.
        currentTime.setToNow(); // Sets the current time.
        int newTime = (int) (currentTime.toMillis(true) / 1000); // Converts the time into hours.

        // Retrieves the appropriate weather image based on the value from the JSON string.
        int origin_weather_image_resource = WWWeather.weatherGraphicSelector(origin_current_weather, newTime);
        int destination_weather_image_resource = WWWeather.weatherGraphicSelector(departure_current_weather, newTime);

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(origin_weather_image_resource)
                .withOptions(WWImages.setBitmapOptions())
                .into(origin_weather_image);

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(destination_weather_image_resource)
                .withOptions(WWImages.setBitmapOptions())
                .into(destination_weather_image);
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
