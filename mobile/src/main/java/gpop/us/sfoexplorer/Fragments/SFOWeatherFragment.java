package gpop.us.sfoexplorer.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import gpop.us.sfoexplorer.Data.SFOBitmap;
import gpop.us.sfoexplorer.Data.SFOWeather;
import gpop.us.sfoexplorer.Device.SFODisplay;
import gpop.us.sfoexplorer.Model.SFOAirportWeatherModel;
import gpop.us.sfoexplorer.Model.SFOForecastModel;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.SFOFont;
import gpop.us.sfoexplorer.UI.SFOImages;
import it.sephiroth.android.library.picasso.Picasso;
import it.sephiroth.android.library.picasso.Target;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class SFOWeatherFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private View weather_view; // References the layout for the fragment.

    // LAYOUT VARIABLES
    private LinearLayout destination_weather_container, origin_weather_container;
    private ImageView forecast_day_1_image, forecast_day_2_image, forecast_day_3_image,
            forecast_day_4_image, forecast_day_5_image;
    private TextView forecast_day_1_status, forecast_day_2_status, forecast_day_3_status,
            forecast_day_4_status, forecast_day_5_status, forecast_day_1_temp, forecast_day_2_temp,
    forecast_day_3_temp, forecast_day_4_temp, forecast_day_5_temp;

    // LOGGING VARIABLES
    private static final String TAG = SFOWeatherFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // RADAR VARIABLES
    private AnimationDrawable radar_animation;
    private int successfulDownloads = 0; // Used to determine the number of successful radar image downloads.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    // WEATHER VARIABLES
    private String currentLocation; // References the current location.
    private String destinationLocation; // References the destination location.
    //private WWWeatherModel weatherModel; // References the event model object for this fragment.
    private SFOAirportWeatherModel weatherModel; // References the event model object for this fragment.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SFOWeatherFragment weather_fragment = new SFOWeatherFragment();

    // WWWeatherFragment(): Deconstructor for the WWWeatherFragment.
    public SFOWeatherFragment() {}

    // getInstance(): Returns the details_fragment instance.
    public static SFOWeatherFragment getInstance() { return weather_fragment; }

    // initializeFragment(): Initializes the fragment with the event properties.
    public void initializeFragment(SFOAirportWeatherModel weather, String location, String destination) {
        weatherModel = weather;
        currentLocation = location;
        destinationLocation = destination;
    }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
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
        setUpForecastWeek(true); // Sets up the five day forecast for the origin location.
        setUpRadar(true); // Sets up the weather radar for the origin location.
    }

    // setUpButtons(): Sets up the button objects for the fragment.
    private void setUpButtons() {

        // References the origin and destination weather containers.
        origin_weather_container = (LinearLayout) weather_view.findViewById(R.id.ww_origin_weather_container);
        destination_weather_container = (LinearLayout) weather_view.findViewById(R.id.ww_destination_weather_container);

        // References the ImageButton objects in the layout.
        ImageButton back_button = (ImageButton) weather_view.findViewById(R.id.weather_back_button);

        // Sets up the listener and the actions for the back button.
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true); // Signals to the attached activity that the details fragment should be shown.
            }
        });

        // Sets up the listener and the actions for the origin location weather container.
        origin_weather_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setUpForecastWeek(true); // Sets up the five day forecast for the origin location.
                setUpRadar(true); // Sets up the weather radar for the origin location.
            }
        });

        // Sets up the listener and the actions for the destination location weather container.
        destination_weather_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setUpForecastWeek(false); // Sets up the five day forecast for the destination location.
                setUpRadar(false); // Sets up the weather radar for the destination location.
            }
        });
    }

    /** WEATHER FUNCTIONALITY __________________________________________________________________ **/

    // getWeatherGraphics(): Returns a weather image resource based on the current weather status
    // string.
    private int getWeatherGraphics(String currentWeather) {

        // Creates a new Time object.
        Time currentTime = new Time(); // Initializes the Time object.
        currentTime.setToNow(); // Sets the current time.
        int newTime = currentTime.hour;

        Log.d(TAG, "Current Time: " + newTime); // Logging.

        // Retrieves the appropriate weather image resource based on the current weather status string.
        int weather_image_resource = SFOWeather.weatherGraphicSelector(currentWeather, newTime);

        return weather_image_resource;
    }

    // setUpForecastWeek(): Sets up the image resources for displaying the five day weather
    // forecast.
    private void setUpForecastWeek(Boolean isOrigin) {

        ArrayList<SFOForecastModel> forecastArray; // References the ArrayList of the WWForecastModel object.

        // References the forecast ImageView objects.
        forecast_day_1_image = (ImageView) weather_view.findViewById(R.id.ww_forecast_day_1_image);
        forecast_day_2_image = (ImageView) weather_view.findViewById(R.id.ww_forecast_day_2_image);
        forecast_day_3_image = (ImageView) weather_view.findViewById(R.id.ww_forecast_day_3_image);
        forecast_day_4_image = (ImageView) weather_view.findViewById(R.id.ww_forecast_day_4_image);
        forecast_day_5_image = (ImageView) weather_view.findViewById(R.id.ww_forecast_day_5_image);

        // References the forecast TextView objects.
        forecast_day_1_status = (TextView) weather_view.findViewById(R.id.ww_forecast_day_1_status);
        forecast_day_2_status = (TextView) weather_view.findViewById(R.id.ww_forecast_day_2_status);
        forecast_day_3_status = (TextView) weather_view.findViewById(R.id.ww_forecast_day_3_status);
        forecast_day_4_status = (TextView) weather_view.findViewById(R.id.ww_forecast_day_4_status);
        forecast_day_5_status = (TextView) weather_view.findViewById(R.id.ww_forecast_day_5_status);
        forecast_day_1_temp = (TextView) weather_view.findViewById(R.id.ww_forecast_day_1_temp);
        forecast_day_2_temp = (TextView) weather_view.findViewById(R.id.ww_forecast_day_2_temp);
        forecast_day_3_temp = (TextView) weather_view.findViewById(R.id.ww_forecast_day_3_temp);
        forecast_day_4_temp = (TextView) weather_view.findViewById(R.id.ww_forecast_day_4_temp);
        forecast_day_5_temp = (TextView) weather_view.findViewById(R.id.ww_forecast_day_5_temp);

        // If the origin location is currently selected, the forecast for the origin is retrieved.
        if (isOrigin) { forecastArray = weatherModel.getOriginForecast(); }

        // Otherwise, the forecast for the destination is retrieved.
        else { forecastArray = weatherModel.getDestinationForecast(); }

        // Recursively loops through the array, retrieving the forecast elements for each day for the
        // next five days.
        //for (int i = 0; i < forecastArray.size(); i++) {
        for (int i = 0; i < 5; i++) {
            Log.d(TAG, "Current Forecast Day: " + i); // Logging.
            SFOForecastModel forecast = forecastArray.get(i); // Retrieves the current forecast day model.
            setUpForecastDay(forecast, i); // Sets the weather image and text for each of the forecast days.
        }
    }

    // setUpForecastDay(): Sets the weather image and text for each of the forecast days.
    private void setUpForecastDay(SFOForecastModel model, int day) {

        String forecastDay = model.getForecastDay(); // Retrieves the current forecast day.
        String forecastWeather = model.getWeatherIcon(); // Retrieves the current forecast weather.
        String forecastTemp = model.getWeatherPop(); // Retrieves the current forecast temperature.

        Log.d(TAG, "- Forecast Day: " + forecastDay); // Logging.
        Log.d(TAG, "- Forecast Weather: " + forecastWeather); // Logging.
        Log.d(TAG, "- Forecast Temperature: " + forecastTemp); // Logging.

        // Retrieves the weather resource.
        int forecastWeatherResource = getWeatherGraphics(forecastWeather);

        // References the ImageView and TextView objects for the current forecast day.
        ImageView currentImage = null;
        TextView currentTemp = null, currentStatus = null;

        // Selects the appropriate ImageView and TextView objects to modify for the forecast day.
        switch (day) {

            case 0:
                currentImage = forecast_day_1_image;
                currentStatus = forecast_day_1_status;
                currentTemp = forecast_day_1_temp;
                break;

            case 1:
                currentImage = forecast_day_2_image;
                currentStatus = forecast_day_2_status;
                currentTemp = forecast_day_2_temp;
                break;

            case 2:
                currentImage = forecast_day_3_image;
                currentStatus = forecast_day_3_status;
                currentTemp = forecast_day_3_temp;
                break;

            case 3:
                currentImage = forecast_day_4_image;
                currentStatus = forecast_day_4_status;
                currentTemp = forecast_day_4_temp;
                break;

            case 4:
                currentImage = forecast_day_5_image;
                currentStatus = forecast_day_5_status;
                currentTemp = forecast_day_5_temp;
                break;
        }

        if (currentStatus != null) { currentStatus.setText(forecastDay); } // Sets the current forecast day title.
        if (currentTemp != null) { currentTemp.setText(forecastTemp + "°"); } // Sets the temperature text.

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(forecastWeatherResource)
                .withOptions(SFOImages.setBitmapOptions())
                .into(currentImage);
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
        origin_text.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        origin_location.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        origin_temperature_text.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.
        origin_weather_status_text.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.
        destination_text.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        destination_location.setTypeface(SFOFont.getInstance(currentActivity).setBigNoodleTypeFace()); // Sets the custom font face.
        destination_temperature_text.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace());// Sets the custom font face.
        destination_weather_status_text.setTypeface(SFOFont.getInstance(currentActivity).setYanoneKaffeeSatzTypeFace()); // Sets the custom font face.

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

        // Retrieves the appropriate weather image based on the value from the JSON string.
        int origin_weather_image_resource = getWeatherGraphics(origin_current_weather);
        int destination_weather_image_resource = getWeatherGraphics(departure_current_weather);

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(origin_weather_image_resource)
                .withOptions(SFOImages.setBitmapOptions())
                .into(origin_weather_image);

        // Sets the weather icon for the ImageView object.
        Picasso.with(currentActivity)
                .load(destination_weather_image_resource)
                .withOptions(SFOImages.setBitmapOptions())
                .into(destination_weather_image);
    }

    /** RADAR FUNCTIONALITY ____________________________________________________________________ **/

    private void setUpRadar(Boolean isOrigin) {

        ArrayList<String> radarUrls; // References the list of URLs for the radar image.
        successfulDownloads = 0; // Resets the number of successful downloads to 0.

        // If the origin location is currently selected, the URLs for the origin is retrieved.
        if (isOrigin) { radarUrls = weatherModel.getOriginRadarUrls(); }

        // Otherwise, the URLs for the destination is retrieved.
        else { radarUrls = weatherModel.getDestinationRadarUrls(); }

        // Recursively loops through the array, retrieving the radar URLs and downloading the URLs
        // to local storage.
        for (int i = 0; i < radarUrls.size(); i++) {

            String currentRadarUrl = radarUrls.get(i);
            Log.d(TAG, "Current Radar URL: " + currentRadarUrl); // Logging.

            // Attempt to download the image URL to local storage.
            if (null != currentRadarUrl) {

                // Creates a new Target to create a circular image and set it into the ImageView object.
                final int currentRadarID = i;
                Target target = new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        Log.d(TAG, "Bitmap loaded for radar image " + currentRadarID + ".");

                        // Saves the Bitmap to local storage.
                        SFOBitmap.saveBitmapToFile(bitmap, currentActivity, "radar_" + currentRadarID + ".png", successfulDownloads);
                        successfulDownloads++; // Increments the number of successful downloads.

                        // If all images have been downloaded, the radar animation sequence is started.
                        if (successfulDownloads == 6) { setUpAnimations(); }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d(TAG, "Bitmap download failed for radar image " + currentRadarID + ".");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                };

                Picasso.with(currentActivity).load(currentRadarUrl).into(target);
            }
        }
    }

    // setUpAnimations(): Sets up the animations for the radar animation.
    private void setUpAnimations() {

        String fileName = "radar_"; // Prefix for the radar images.

        // Initializes the AnimationDrawable object for the radar image.
        radar_animation = new AnimationDrawable();

        // Builds the AnimationDrawable list from the downloaded radar images.
        for (int i = 0; i < 6; i++) {

            Bitmap radar_bitmap = BitmapFactory.decodeFile(currentActivity.getFilesDir() + "/" + fileName + i + ".png");

            BitmapDrawable radar_frame = new BitmapDrawable(radar_bitmap);
            radar_animation.addFrame(radar_frame, 400);
        }

        radar_animation.setOneShot(false); // Sets the animation to loop infinitely.

        // Initializes and sets up the references for the animated radar.
        ImageView weather_radar = (ImageView) weather_view.findViewById(R.id.ww_radar_map);
        weather_radar.setBackgroundDrawable(radar_animation);

        // Null pointer exception handling.
        try {

            // Animation is loaded as a runnable to address a bug present in Android 2.3 where
            // animations will not start normally.
            weather_radar.post(new Runnable(){

                public void run(){
                    radar_animation.start(); // Attempts to begin the animation for the weather radar.
                }
            });
        }

        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
            Log.d(TAG, "Weather radar animation failed to start.");
        }
    }

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // setUpDisplayParameters(): Sets up the device's display parameters.
    private void setUpDisplayParameters() {

        // References the display parameters for the device.
        Display deviceWindow = currentActivity.getWindowManager().getDefaultDisplay();

        currentOrientation = SFODisplay.updateDisplayLayout(currentActivity, deviceWindow); // Retrieves the device's display attributes.
        resolutionDimens = SFODisplay.getResolution(deviceWindow);
        displaySize = SFODisplay.getDisplaySize(resolutionDimens, currentOrientation);
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
