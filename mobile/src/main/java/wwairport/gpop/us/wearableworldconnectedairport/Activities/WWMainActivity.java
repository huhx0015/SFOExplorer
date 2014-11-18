package wwairport.gpop.us.wearableworldconnectedairport.Activities;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Vector;
import wwairport.gpop.us.wearableworldconnectedairport.Device.WWDisplay;
import wwairport.gpop.us.wearableworldconnectedairport.Fragments.WWCardFragment;
import wwairport.gpop.us.wearableworldconnectedairport.R;
import wwairport.gpop.us.wearableworldconnectedairport.UI.WWFont;


public class WWMainActivity extends FragmentActivity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // NOTIFICATION VARIABLES
    private ImageView notification_flight_image, notification_weather_image;
    private TextView notification_flight_number, notification_gate_number, notification_weather_status;

    // SLIDER VARIABLES
    private int cardNumber = 0; // Used to determine which card fragment is being displayed.
    private PagerAdapter wwPageAdapter; // Used to reference the PagerAdapter object.
    private ViewPager wwTitleScreenPager; // Used to reference the ViewPager object.

    // SYSTEM VARIABLES
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    /** ACTIVITY FUNCTIONALITY _________________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpLayout(); // Sets up the layout for the activity.
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

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

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {

        setUpDisplayParameters(); // Sets up the device's display parameters.
        setContentView(R.layout.ww_main_activity); // Sets the XML file.
        setUpNotificationBar(); // Sets up the notification bar for the activity.
        setUpSlider(false); // Initializes the fragment slides for the PagerAdapter.
    }

    /** NOTIFICATION BAR FUNCTIONALITY _________________________________________________________ **/

    // setUpNotificationBar(): Sets up the notification bar for the activity.
    private void setUpNotificationBar() {

        // Sets up the ImageView objects.
        notification_flight_image = (ImageView) findViewById(R.id.notification_flight_icon);
        notification_weather_image = (ImageView) findViewById(R.id.notification_weather_image);

        // Sets up the TextView objects.
        notification_flight_number = (TextView) findViewById(R.id.notification_flight_number);
        notification_gate_number = (TextView) findViewById(R.id.notification_gate_number);
        notification_weather_status = (TextView) findViewById(R.id.notification_weather_status);

        // Sets up the custom font type for the TextView objects.
        notification_flight_number.setTypeface(WWFont.getInstance(this).getTypeFace()); // Sets the custom font face.
        notification_gate_number.setTypeface(WWFont.getInstance(this).getTypeFace()); // Sets the custom font face.
        notification_weather_status.setTypeface(WWFont.getInstance(this).getTypeFace()); // Sets the custom font face.

        getFlightStatus(); // Updates the flight status on the notification bar.
        getWeatherStatus(); // Updates the weather status on the notification bar.
    }

    // getWeatherStatus(): Retrieves the current weather status.
    private void getWeatherStatus() {

        int weather_image = R.drawable.weather_partly_cloudy;
        String current_temperature = "55°";
        String current_weather = "PARTLY CLOUDY";

        // Sets the weather icon for the ImageView object.
        Picasso.with(this)
                .load(weather_image)
                .placeholder(R.drawable.dark_transparent_tile)
                .resize(48, 48)
                .centerCrop()
                .into(notification_weather_image);

        // Sets the weather status for the TextView object.
        notification_weather_status.setText(current_temperature + " " + current_weather);
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
        for (int i = 0; i <= numberOfSlides; i++) {

            // Initializes the card fragment and adds it to the deck.
            WWCardFragment cardFragment = new WWCardFragment();
            cardFragment.initializeFragment(i);
            fragments.add(cardFragment);
        }

        return fragments;
    }

    // setPageListener(): Sets up the listener for the Pager Adapter object.
    private void setPageListener(ViewPager page) {

        // Defines the action to take when the page is changed.
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // onPageScrollStateChanged(): Called the page scroll state is changed.
            public void onPageScrollStateChanged(int state) {}

            // onPageScrolled(): Called when the pages are scrolled.
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            // onPageSelected(): Called when a new page is selected.
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        //series = 0;
                        break;
                    case 1:
                        //series = 1;
                        break;
                    case 2:
                        //series = 2;
                        break;
                    default:
                        //series = 0;
                        break;
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
        wwPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments(10));

        wwTitleScreenPager = (ViewPager) super.findViewById(R.id.card_fragment_pager);
        wwTitleScreenPager.setAdapter(this.wwPageAdapter); // Sets the PagerAdapter object for the activity.
        setPageListener(wwTitleScreenPager); // Sets up the listener for the pager object.

        // If the activity has experienced a screen change, the page is set to the game series that
        // was previously being displayed.
        if (isChanged == true) { wwTitleScreenPager.setCurrentItem(cardNumber); } // Loads the selected slider page.
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
}
