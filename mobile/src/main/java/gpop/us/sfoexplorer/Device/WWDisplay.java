package gpop.us.sfoexplorer.Device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */
public class WWDisplay {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private static final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** RESOLUTION FUNCTIONALITY _______________________________________________________________ **/

    // getDisplaySize(): Used for retrieving the display size of the device.
    public static int getDisplaySize(Point resolution, int currentOrientation) {

        int size = 0;

        // PORTRAIT MODE: Determines the display size from the resolution.x value.
        if (currentOrientation == 0) { size = resolution.x; }

        // LANDSCAPE MODE: Determines the display size from the resolution.y value.
        else if (currentOrientation == 1) { size = resolution.y; }

        return size;
    }

    // getResolution(): Retrieves the device's screen resolution (width and height).
    @SuppressLint("NewApi")
    public static Point getResolution(Display display) {

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
    public static int updateDisplayLayout(Context context, Display display) {

        int currentOrientation = 0; // Return value for

        // Retrieves the device's current screen orientation.
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            currentOrientation = 0;
        }

        else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentOrientation = 1;
        }

        return currentOrientation;
    }
}
