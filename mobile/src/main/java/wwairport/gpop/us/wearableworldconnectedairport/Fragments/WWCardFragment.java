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
import android.widget.ImageView;

import wwairport.gpop.us.wearableworldconnectedairport.Device.WWDisplay;
import wwairport.gpop.us.wearableworldconnectedairport.R;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWCardFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private int cardNumber = 0; // References the fragment number.
    private View card_view; // References the layout for the fragment.

    // LAYOUT VARIABLES
    private ImageView card_background_image; // References the card background ImageView object.
    private ImageView main_card_image; // References the main card image ImageView object.
    private int main_card_image_resource; // Used to reference the main card image.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.
    
    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static WWCardFragment card_fragment = new WWCardFragment();

    // WWCardFragment(): Deconstructor for the WWCardFragment.
    public WWCardFragment() {}

    // getInstance(): Returns the profile_fragment instance.
    public static WWCardFragment getInstance() { return card_fragment; }

    // initializeFragment(): Initializes the fragment with the slide number ID.
    public void initializeFragment(int fragmentValue) {
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

        setUpDisplayParameters(); // Sets up the device's display parameters.

        // Sets the view to the specified XML layout file.
        card_view = (ViewGroup) inflater.inflate(R.layout.ww_card_fragment, container, false);
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

        setUpDisplayParameters(); // Sets up the device's display parameters.
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

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {

        setUpImages(); // Sets up the ImageView objects in the fragment.
    }

    // setUpImages(): Sets up the images for the fragment.
    private void setUpImages() {

        // References the ImageView objects.
        card_background_image = (ImageView) card_view.findViewById(R.id.card_background_image);
        main_card_image = (ImageView) card_view.findViewById(R.id.card_activity_image);

        // TODO

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
     *  [OnCardSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnCardSelectedListener{
        public void updatePreferences();
    }

}
