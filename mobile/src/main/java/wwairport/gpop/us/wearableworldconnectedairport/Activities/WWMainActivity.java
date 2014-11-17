package wwairport.gpop.us.wearableworldconnectedairport.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import wwairport.gpop.us.wearableworldconnectedairport.Fragments.WWCardFragment;
import wwairport.gpop.us.wearableworldconnectedairport.R;


public class WWMainActivity extends FragmentActivity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SLIDER VARIABLES
    private PagerAdapter dgPageAdapter; // Used to reference the PagerAdapter object.
    private ViewPager dgTitleScreenPager; // Used to reference the ViewPager object.


    /** ACTIVITY FUNCTIONALITY _________________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ww_main_activity);
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

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

    /** SLIDER FUNCTIONALITY ___________________________________________________________________ **/

    // createSlideFragments(): Sets up the slide fragments for the PagerAdapter object.
    private List<Fragment> createSlideFragments() {

        final List<Fragment> fragments = new Vector<Fragment>(); // List of fragments in which the fragments is stored.

        // Initialize fragment objects.
        WWCardFragment cardFragment_1 = new WWCardFragment();
        WWCardFragment cardFragment_2 = new WWCardFragment();
        WWCardFragment cardFragment_3 = new WWCardFragment();
        cardFragment_1.initializeFragment(0);
        cardFragment_2.initializeFragment(1);
        cardFragment_3.initializeFragment(2);

        // Sets up the fragment list for the PagerAdapter object.
        fragments.add(cardFragment); // Adds the cardFragment fragment to the List<Fragment> object.
        fragments.add(cardFragment1); // Adds the cardFragment1 fragment to the List<Fragment> object.
        fragments.add(cardFragment2); // Adds the cardFragment2 fragment to the List<Fragment> object.

        return fragments;
    }

    // setUpSlider(): Initializes the slides for the PagerAdapter object.
    private void setUpSlider(Boolean isChanged) {

        // Resets the ViewPager object if the Page Adapter object has experienced a screen change.
        if (isChanged == true) { dgTitleScreenPager.setAdapter(null); }

        // Initializes and creates a new FragmentListPagerAdapter objects using the List of slides
        // created from createSlideFragments.
        dgPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments());

        dgTitleScreenPager = (ViewPager) super.findViewById(R.id.dg_main_activity_fragment_pager);
        dgTitleScreenPager.setAdapter(this.dgPageAdapter); // Sets the PagerAdapter object for the activity.
        setPageListener(dgTitleScreenPager); // Sets up the listener for the pager object.

        // If the activity has experienced a screen change, the page is set to the game series that
        // was previously being displayed.
        if (isChanged == true) { dgTitleScreenPager.setCurrentItem(series); } // Loads the selected slider page.

        // Checks the dq_current_series value in temporary preferences to load the proper slider page,
        // if DQTitleScreen activity is loaded after leaving DQWorldView activity.
        else {
            DQ_temps = getSharedPreferences(DQ_TEMPS, MODE_PRIVATE); // Temporary preferences object.
            int selectedSeries = DQ_temps.getInt("dq_current_series", 0); // Retrieves the selected series value from temporary preferences.
            dgTitleScreenPager.setCurrentItem(selectedSeries); // Loads the selected slider page.
        }
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
