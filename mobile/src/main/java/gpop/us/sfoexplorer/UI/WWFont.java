package gpop.us.sfoexplorer.UI;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */

import android.content.Context;
import android.graphics.Typeface;

public class WWFont {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private final Context context;
    private static WWFont instance;

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    // WWFont(): Constructor for the WWFont class.
    private WWFont(Context context) {
        this.context = context;
    }

    // getInstance(): Creates an instance of the WWFont class.
    public static WWFont getInstance(Context context) {
        synchronized (WWFont.class) {
            if (instance == null)
                instance = new WWFont(context);
            return instance;
        }
    }

    // setBigNoodleTypeFace(): Retrieves the custom font family (Big Noodle Titling) from resources.
    public Typeface setBigNoodleTypeFace() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/big_noodle_titling.ttf");
    }

    // setRobotoLight(): Retrieves the custom font family (Roboto Light) from resources.
    public Typeface setRobotoLight() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/roboto_light.ttf");
    }

    // setRobotoRegular(): Retrieves the custom font family (Roboto Regular) from resources.
    public Typeface setRobotoRegular() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/roboto_regular.ttf");
    }

    // setYanoneKaffeeSatzTypeFace(): Retrieves the custom font family (Yanone Kaffee Satz)from resources.
    public Typeface setYanoneKaffeeSatzTypeFace() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/yanonekaffeesatz_bold.ttf");
    }
}
