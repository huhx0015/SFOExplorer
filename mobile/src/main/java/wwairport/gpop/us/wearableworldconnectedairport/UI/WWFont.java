package wwairport.gpop.us.wearableworldconnectedairport.UI;

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

    // getTypeFace(): Retrieves the custom font family (Big Noodle Titling) from resources.
    public Typeface getTypeFace() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/big_noodle_titling.ttf");
    }
}
