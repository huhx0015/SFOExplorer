package gpop.us.sfoexplorer.Activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import gpop.us.sfoexplorer.Fragments.WWDetailsFragment;
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
        WWDetailsFragment.OnDetailsSelectedListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CARD VARIABLES
    private ArrayList<WWEventModel> models; // References the ArrayList of WWEventModel objects.

    // FRAGMENT VARIABLES
    private Boolean isDetailsOn = false; // Used to determine if the card details fragment is currently being shown.
    private Boolean isDetailsFragmentMade = false; // Used to determine if the card details fragment has already been created.
    private WWDetailsFragment details_fragment; // References the WWDetailsFragment object.

    // LAYOUT VARIABLES
    private FrameLayout card_fragment_details_container; // References the card fragment details container object.

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

    // SYSTEM VARIABLES
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.
    private static WeakReference<WWMainActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    // THREAD VARIABLES
    private Handler updateThread; // A thread that handles the updating of the notification bar.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weakRefActivity = new WeakReference<WWMainActivity>(this); // Creates a weak reference of this activity.
        setUpLayout(); // Sets up the layout for the activity.
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    protected void onPause() { super.onPause(); }

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
            displayFragment(false); // Hides the card details fragment.
        }

        else { finish(); } // The activity is terminated at this point.
    }

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setUpDisplayParameters(); // Sets up the device's display parameters.
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
            if (isDetailsFragmentMade) { displayFragment(true); }
            else {

                // Initializes the WWDetailsFragment object.
                details_fragment = new WWDetailsFragment();
                details_fragment.initializeFragment(currentCardNumber, currentEvent);

                setUpFragment(details_fragment); // Sets up the fragment for the card details.
            }
        }
    }

    // updateFromDetailsFragment(): This function is called whenever the user interacts with the fragment
    // buttons. Called directly by the interface methods in WWDetailsFragment.
    @Override
    public void updateFromDetailsFragment(Boolean hideDetails) {

        // If showDetails value is true, the card details fragment is removed.
        if (hideDetails) {
            isDetailsOn = false; // Indicates that the details fragment is currently not being shown.
            displayFragment(false); // Hides the card details fragment.
        }
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {

        setUpDisplayParameters(); // Sets up the device's display parameters.

        setContentView(R.layout.ww_main_activity); // Sets the XML file.

        // References the card fragment details layout container.
        card_fragment_details_container = (FrameLayout) findViewById(R.id.card_fragment_details);

        setUpNotificationBar(); // Sets up the notification bar for the activity.
        setUpCardEvents(); // Sets up the event cards for the slider fragments.
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
    private void displayFragment(Boolean show) {

        if ((weakRefActivity.get() != null) && (weakRefActivity.get().isFinishing() != true)) {

            // Initializes the manager and transaction objects for the fragments.
            final FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
            final FragmentTransaction fragTrans = fragMan.beginTransaction();
            fragTrans.setCustomAnimations(R.anim.slide_down, R.anim.slide_up);

            // Displays the fragment.
            if (show == true) { fragTrans.show(details_fragment); }

            // Hides the fragment.
            else { fragTrans.hide(details_fragment); } // Hides the fragment.

            // Makes the changes to the fragment manager and transaction objects.
            fragTrans.addToBackStack(null);
            fragTrans.commitAllowingStateLoss();
        }
    }

    // removeFragment(): Removes the fragment from the container.
    private void removeFragment() {

        // Initializes the manager and transaction objects for the fragments.
        FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out); // Sets the custom animation.
        card_fragment_details_container.setVisibility(View.INVISIBLE); // Hides the fragment container.
        fragTrans.show(details_fragment); // Shows the fragment.
        fragMan.popBackStack(); // Pops the fragment from the stack.
        card_fragment_details_container.removeAllViews(); // Removes all views in the layout.
    }

    // setUpFragment(): Updates the content of the fragment container with the proper fragment.
    private void setUpFragment(WWDetailsFragment fragment) {

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

            isDetailsFragmentMade = true; // Indicates that the card details fragment has been made.
        }
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
    }

    // getWeatherStatus(): Retrieves the current weather status.
    private void getWeatherStatus() {

        client = new WWClient("WEATHER"); // Sets up the JSON client for retrieving weather data.

        // Attempts to retrieve the JSON data from the server.
        client.getJsonData(new JsonHttpResponseHandler() {

            // onSuccess(): Run when JSON request was successful.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                WWWeatherModel model; // The model object for JSON weather data.

                Log.d(TAG, "Weather Handshake successful! " + response.toString()); // Logging.
                //Toast.makeText(getApplicationContext(), "Weather Handshake successful! " + response.toString(), Toast.LENGTH_SHORT).show();
                model = WWWeatherModel.fromJson(response); // Attempts to retrieve a JSON string from the server.

                String current_weather = model.getWeather(); // Sets the weather status from the JSON string.
                double current_temperature = model.getTemperature(); // Sets the current temperature from the JSON string.

                // Retrieves the appropriate weather image based on the value from the JSON string.
                int weather_image = R.drawable.weather_partly_cloudy;

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

        int airline_logo = R.drawable.aa_icon;
        String flight_number = "FLIGHT 390";
        String gate_number = "GATE A12";

        // Sets the weather icon for the ImageView object.
        Picasso.with(this)
                .load(airline_logo)
                .placeholder(R.drawable.dark_transparent_tile)
                .resize(48, 48)
                .centerCrop()
                .into(notification_flight_image);

        // Sets the flight and gate number for the TextView objects.
        notification_flight_number.setText(flight_number);
        notification_gate_number.setText(gate_number);
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
                    removeFragment(); // Removes the fragment.
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
