package gpop.us.sfoexplorer.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import gpop.us.sfoexplorer.Device.SFODisplay;
import gpop.us.sfoexplorer.R;
import gpop.us.sfoexplorer.UI.SFOFont;

/**
 * Created by Michael Yoon Huh on 12/13/2014.
 */
public class SFOSearchFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private View search_view; // References the layout for the fragment.

    // LOGGING VARIABLES
    private static final String TAG = SFOFlightNumberFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // LAYOUT VARIABLES
    private Button eatButton, electronicsButton, coffeeButton, booksButton, shopButton,
            entertainmentButton, atmButton, restroomButton, rentalButton, mapButton;

    // SEARCH VARIABLES
    private String selectedCategory; // Used for referencing the selected category string.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.
    private Point resolutionDimens; // Used to determine the device's full resolution parameters.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int displaySize; // Stores the device's display size.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static SFOSearchFragment search_fragment = new SFOSearchFragment();

    // WWSearchFragment(): Deconstructor for the WWFlightNumberFragment.
    public SFOSearchFragment() {}

    // getInstance(): Returns the details_fragment instance.
    public static SFOSearchFragment getInstance() { return search_fragment; }

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
        search_view = (ViewGroup) inflater.inflate(R.layout.ww_search_fragment, container, false);
        setUpLayout(); // Sets up the layout for the fragment.

        return search_view;
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
    private void updateActivity(Boolean isReturn, String category) {

        // Signals to the attached activity that the card details fragment needs to be closed.
        try { ((OnSearchSelectedListener) currentActivity).updateFromSearchFragment(isReturn, category); }
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
        setUpText(); // Sets up the TextView objects for the fragment.
    }

    // setUpButtons(): Sets up the button objects for the fragment.
    private void setUpButtons() {

        // References the Button objects in the layout.
        eatButton = (Button) search_view.findViewById(R.id.search_eat_button);
        electronicsButton = (Button) search_view.findViewById(R.id.search_electronics_button);
        coffeeButton = (Button) search_view.findViewById(R.id.search_coffee_button);
        booksButton = (Button) search_view.findViewById(R.id.search_books_button);
        shopButton = (Button) search_view.findViewById(R.id.search_shop_button);
        entertainmentButton = (Button) search_view.findViewById(R.id.search_entertainment_button);
        atmButton = (Button) search_view.findViewById(R.id.search_atm_button);
        restroomButton = (Button) search_view.findViewById(R.id.search_restroom_button);
        rentalButton = (Button) search_view.findViewById(R.id.search_rental_button);
        mapButton = (Button) search_view.findViewById(R.id.search_airport_button);

        // References the ImageButton objects in the layout.
        ImageButton back_button = (ImageButton) search_view.findViewById(R.id.search_back_button);

        // Sets up the listener and the actions for the back button.
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, null); // Signals to the attached activity to hide the fragment.
            }
        });

        // Sets up the listener and the actions for the EAT button.
        eatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "EAT"); // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the ELECTRONICS button.
        electronicsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "ELECTRONICS");  // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the COFFEE button.
        coffeeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "COFFEE");  // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the BOOKS button.
        booksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "BOOKS"); // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the SHOPS button.
        shopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "SHOPS");  // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the ENTERTAINMENT button.
        entertainmentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "ENTERTAINMENT"); // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the ATM button.
        atmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "ATM"); // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the RESTROOM button.
        restroomButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "RESTROOM"); // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the RENTAL button.
        rentalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "RENTAL"); // Sets the category and hides the fragment.
            }
        });

        // Sets up the listener and the actions for the MAP button.
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateActivity(true, "MAP"); // Sets the category and hides the fragment.
            }
        });
    }

    // setUpText(): Sets up the TextView objects for the fragment.
    private void setUpText() {

        // Sets the custom font face for the Button objects.
        eatButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        electronicsButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        coffeeButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        booksButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        shopButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        entertainmentButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        atmButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        restroomButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        rentalButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.
        mapButton.setTypeface(SFOFont.getInstance(currentActivity).setRobotoLight()); // Sets the custom font face.

        // Sets up a shadow effect for the TextView objects.
        eatButton.setShadowLayer(8, 4, 4, Color.BLACK);
        electronicsButton.setShadowLayer(8, 4, 4, Color.BLACK);
        coffeeButton.setShadowLayer(8, 4, 4, Color.BLACK);
        booksButton.setShadowLayer(8, 4, 4, Color.BLACK);
        shopButton.setShadowLayer(8, 4, 4, Color.BLACK);
        entertainmentButton.setShadowLayer(8, 4, 4, Color.BLACK);
        atmButton.setShadowLayer(8, 4, 4, Color.BLACK);
        restroomButton.setShadowLayer(8, 4, 4, Color.BLACK);
        rentalButton.setShadowLayer(8, 4, 4, Color.BLACK);
        mapButton.setShadowLayer(8, 4, 4, Color.BLACK);
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
     *  [OnSearchSelectedListener] INTERFACE
     *  DESCRIPTION: An interface that is used by activities attached to this fragment to determine
     *  if buttons in the fragment have been clicked and if preference values have been changed.
     *  --------------------------------------------------------------------------------------------
     */

    public interface OnSearchSelectedListener {
        public void updateFromSearchFragment(Boolean isReturn, String category);
    }
}
