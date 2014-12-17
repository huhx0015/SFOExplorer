package gpop.us.sfoexplorer.Memory;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class SFOMemory {

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles View objects to clear up memory resources.
    public static void recycleMemory(View layout, Activity activity) {

        // Unbinds all Drawable objects attached to the current layout.
        try { unbindDrawables(layout); }

        // Null pointer exception catch.
        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
            String errorString = "WWMEMORY\nNULL POINTER EXCEPTION OCCURRED WHILE ATTEMPTING TO RECYCLE MEMORY!";
        }
    }

    // unbindDrawables(): Unbinds all Drawable objects attached to the view layout by setting them
    // to null, freeing up memory resources and preventing Context-related memory leaks. This code
    // is borrowed from Roman Guy at www.curious-creature.org.
    public static void unbindDrawables(View view) {

        // If the View object's background is not null, a Callback is set to render them null.
        if (view.getBackground() != null) { view.getBackground().setCallback(null); }

        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }

            ((ViewGroup) view).removeAllViews(); // Removes all View objects in the ViewGroup.
        }
    }
}
