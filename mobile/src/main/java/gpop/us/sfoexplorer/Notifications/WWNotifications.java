package gpop.us.sfoexplorer.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import gpop.us.sfoexplorer.Activities.WWMainActivity;
import gpop.us.sfoexplorer.R;

/**
 * Created by Michael Yoon Huh on 11/17/2014.
 */

public class WWNotifications {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Context ww_context; // Context for the instance in which this class is used.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // WWNotifications(): Constructor for the WWNotifications class.
    public final static WWNotifications wwNotify = new WWNotifications();

    // WWNotifications(): Deconstructor for the SBNotifications class.
    public WWNotifications() {
    }

    // getInstance(): Returns the wwNotify instance.
    public static WWNotifications getInstance() {
        return wwNotify;
    }

    // initializeWW(): Initializes the WWNotifications class variables.
    public void initializeWW(Context con) {
        ww_context = con; // Context for the instance in which this class is used.
    }

    /** NOTIFICATION FUNCTIONALITY _____________________________________________________________ **/

    // Creates the notification.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void createNotification(Context con, String notiText) {

        int notificationId = 001; // Notification ID tag.

        // Intent to launch the splash.
        Intent sb_intent = new Intent(con, WWMainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(con, 0, sb_intent, 0);

        // ANDROID WEAR:
        // Specify the 'big view' content to display the long event description that may not fit the
        // normal content text.
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(notiText);

        // ANDROID WEAR SPECIFIC ACTION:
        // Create the action.
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher, "FLY", pIntent).build();

        // ANDROID WEAR NOTIFICATION FOR ANDROID WEAR ONLY ACTIONS:
        // Builds the notification and add the action via the WearableExtender.
        Notification noti =
                new NotificationCompat.Builder(con)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("FLY TO-DO")
                        .setContentText(notiText).setSmallIcon(R.drawable.ic_launcher)
                        .setStyle(bigStyle)
                        .extend(new NotificationCompat.WearableExtender().addAction(action))
                        .build();

        // Get an instance of the NotificationManager service.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(con);

        // ANDROID WEAR SPECIFIC MANAGER NOTIFICATION:
        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, noti);
    }
}