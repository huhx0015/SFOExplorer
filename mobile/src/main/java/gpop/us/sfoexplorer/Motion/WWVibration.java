package gpop.us.sfoexplorer.Motion;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class WWVibration {

    /** VIBRATION FUNCTIONALITY ________________________________________________________________ **/

    // vibrateDevice(): Used to make the Android device vibrate.
    public static void vibrateDevice(int value, Context context) {
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(value); // Vibrates for "value" milliseconds.
    }
}
