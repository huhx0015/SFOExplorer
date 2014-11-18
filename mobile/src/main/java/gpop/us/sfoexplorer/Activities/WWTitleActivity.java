package gpop.us.sfoexplorer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import gpop.us.sfoexplorer.R;
import it.sephiroth.android.library.picasso.Picasso;
import gpop.us.sfoexplorer.Memory.WWMemory;
import gpop.us.sfoexplorer.UI.WWFont;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class WWTitleActivity extends Activity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // FLIGHT VARIABLES
    private String flightNumber = "NULL"; // References the flight number.

    // LAYOUT VARIABLES
    private EditText flight_input; // References the EditText input fields.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        finish(); // The activity is terminated at this point.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {

        setContentView(R.layout.ww_title_activity); // Sets the XML file.
        setUpImages(); // Sets up the ImageView objects.
        setUpInput(); // Sets up the EditText objects.
        setUpButtons(); // Sets up the Button objects.
    }

    // setUpButtons(): Sets up the buttons for the layout.
    private void setUpButtons() {

        // References the Button objects in the layout.
        Button exploreButton = (Button) findViewById(R.id.explore_button);

        // Sets custom font styles for the buttons.
        exploreButton.setTypeface(WWFont.getInstance(this).setBigNoodleTypeFace());

        // Sets up the listener and the actions for the EXPLORE button.
        exploreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Retrieves the string from the EditText input.
                flightNumber = flight_input.getText().toString();

                // Sets up the intent to launch the next activity.
                Intent i = new Intent("gpop.us.sfoexplorer.MAINACTIVITY");
                i.putExtra("flight_number", flightNumber); // Passes the flight number to the next activity.
                startActivity(i); // Begins the next activity.
            }
        });
    }

    // setUpImages(): Sets up the images for the layout.
    private void setUpImages() {

        // References the ImageView objects for the layout.
        ImageView title_activity_logo = (ImageView) findViewById(R.id.title_activity_logo);
        ImageView title_background_image = (ImageView) findViewById(R.id.title_activity_background);

        // Sets the image resource for the logo.
        //Picasso.with(this).load(R.drawable.explorer_logo).into(title_activity_logo);

        // Sets the image resource for the background.
        Picasso.with(this).load(R.drawable.sfo_bg).fit().centerCrop().into(title_background_image);
    }

    // setUpInput(): Sets up the EditText objects for the layout.
    private void setUpInput() {

        // References the EditText objects.
        flight_input = (EditText) findViewById(R.id.flight_number_input);

        // Sets custom font styles for the input.
        flight_input.setTypeface(WWFont.getInstance(this).setBigNoodleTypeFace());
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles ImageView and View objects to clear up memory resources prior to
    // Activity destruction.
    private void recycleMemory() {

        // NullPointerException error handling.
        try {

            // Unbinds all Drawable objects attached to the current layout.
            WWMemory.unbindDrawables(findViewById(R.id.ww_title_activity_layout));
        }

        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
        }
    }
}
