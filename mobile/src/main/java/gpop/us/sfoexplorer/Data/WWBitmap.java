package gpop.us.sfoexplorer.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.FileOutputStream;

/**
 * Created by Michael Yoon Huh on 12/15/2014.
 */
public class WWBitmap {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String TAG = WWBitmap.class.getSimpleName(); // Retrieves the simple name of the class.

    /** BITMAP FUNCTIONALITY ___________________________________________________________________ **/

    // saveBitmapToFile(): Saves the bitmap to a PNG image file.
    public static int saveBitmapToFile(Bitmap bitmap, Context context, String filename, int downloads) {

        int successCode = 0; // Used for returning the response code.

        try {

            String filepath = context.getFilesDir() + "/";

            Log.d(TAG, "Saving File As: " + filename + " | Directory: " + filepath);

            FileOutputStream fOut = new FileOutputStream(filepath + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            // Checks to see if the number of downloads has reached 6.
            if (downloads == 6) { return 1; } // Indicates that the download of all images have been completed.

            return successCode;

        }

        catch (Exception e) {

            e.printStackTrace();
            Log.i(null, "Save file error!");
            return successCode;
        }
    }
}
