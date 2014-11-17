package wwairport.gpop.us.wearableworldconnectedairport.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWCardFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private int cardNumber = 0; // References the fragment number.
    private View card_view; // References the layout for the fragment.

    // SYSTEM VARIABLES
    private static final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.
    private int seriesNumber = 0; // References the fragment number.
    
    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static WWCardFragment card_fragment = new WWCardFragment();

    // WWCardFragment(): Deconstructor for the WWCardFragment.
    public WWCardFragment() {}

    // getInstance(): Returns the profile_fragment instance.
    public static WWCardFragment getInstance() { return card_fragment; }

    // initializeFragment(): Initializes the fragment with the slide number ID.
    public void initializeFragment(int fragmentValue ) {
        cardNumber = fragmentValue;
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

        updateDisplayLayout(); // Retrieves the device's display attributes.

        // Sets the view to the specified XML layout file.
        card_view = (ViewGroup) inflater.inflate(R.layout.dg_game_select_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return card_view;
    }

    // onActivityCreated(): Runs after the attached activity's onCreate() function has been completed.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initiates a first-time call through the interface to initialize the connection between
        // the fragment and the activity.
        updateActivity();
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

    /** FRAGMENT EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        updateDisplayLayout(); // Retrieves the device's display attributes.
        setUpLayout(); // Sets up the layout for the fragment.
    }

    /** ACTIVITY FUNCTIONALITY _________________________________________________________________ **/

    // updateActivity(): Interfaces with the attached activity class and signals that the map
    // properties have been changed.
    private void updateActivity() {

        // Signals to the attached activity that a button was pressed and that the map properties
        // need to be checked.
        try { ((OnCardSelectedListener) currentActivity).updatePreferences(); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // getDisplaySize(): Used for retrieving the display size of the device.
    public int getDisplaySize(Point resolution, int currentOrientation) {

        int size = 0;

        // PORTRAIT MODE: Determines the display size from the resolution.x value.
        if (currentOrientation == 0) { size = resolution.x; }

        // LANDSCAPE MODE: Determines the display size from the resolution.y value.
        else if (currentOrientation == 1) { size = resolution.y; }

        return size;
    }

    // getResolution(): Retrieves the device's screen resolution (width and height).
    @SuppressLint("NewApi")
    public Point getResolution(Display display) {

        Point displayDimen = new Point(); // Used for determining the device's display resolution.

        // If API Level is 13 and higher (HONEYCOMB_MR2>), use the new method.
        if (api_level > 12) { display.getSize(displayDimen); }

        // If API Level is less than 13 (HONEYCOMB_MR1<), use the depreciated method.
        else {
            displayDimen.x = display.getWidth();
            displayDimen.y = display.getHeight();
        }

        return displayDimen;
    }

    // updateDisplayLayout(): Retrieves the device's screen resolution and current orientation.
    private void updateDisplayLayout() {

        // Used for retrieving the device's screen resolution values.
        Display display = currentActivity.getWindowManager().getDefaultDisplay();
        Point displayDimen = getResolution(display);

        // Retrieves the device's current screen orientation.
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            currentOrientation = 0;
        }

        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentOrientation = 1;
        }

        displaySize = getDisplaySize(displayDimen, currentOrientation);
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    /** --------------------------------------------------------------------------------------------
     *  [OnCardSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnCardSelectedListener{
        public void updatePreferences();
    }

}
