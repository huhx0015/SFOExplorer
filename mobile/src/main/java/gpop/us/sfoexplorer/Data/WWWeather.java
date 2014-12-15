package gpop.us.sfoexplorer.Data;

import gpop.us.sfoexplorer.R;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class WWWeather {

    /** WEATHER GRAPHICS FUNCTIONALITY _________________________________________________________ **/

    // weatherGraphicSelector(): Returns a weather icon based on the weather type.
    public static int weatherGraphicSelector(String type, int time) {

        int weatherIcon = R.drawable.na; // References the weather icon.
        type = type.toLowerCase(); // Converts the String object into a String with all lowercase characters.

        // MILD SNOW:
        if ( (type.equals("mild snow")) || (type.equals("Mild Snow")) ) {
            weatherIcon = R.drawable.clouds_mild_snow;
        }

        // CLOUDY:
        else if ( (type.equals("cloudy")) || (type.equals("Cloudy") ||
                (type.equals("mostly cloudy")) || (type.equals("Mostly Cloudy")) ) ||
                (type.equals("overcast")) || (type.equals("Overcast")) ) {
            weatherIcon = R.drawable.cloudy;
        }

        // HAIL:
        else if ( (type.equals("hail")) || (type.equals("Hail")) ) {
            weatherIcon = R.drawable.clouds_mild_snow;
        }

        // RAIN:
        else if ( (type.equals("heavy rain")) || (type.equals("light rain")) ||
                (type.equals("rain")) || (type.equals("rainy"))) {

            // NIGHT:
            if ( (time > 21) || ( (time > 0) && (time < 7) ) ) {
                weatherIcon = R.drawable.heavy_rain_moon;
            }

            // DAY:
            else { weatherIcon = R.drawable.heavy_rain; }
        }

        // HEAVY RAIN (STORM):
        else if ( (type.equals("heavy rain storm")) || (type.equals("Heavy Rainstorm")) ) {

            // NIGHT:
            if ( (time > 21) || ( (time > 0) && (time < 7) ) ) {
                weatherIcon = R.drawable.heavy_rain_storm_moon;
            }

            // DAY:
            else { weatherIcon = R.drawable.heavy_rain_storm; }
        }

        // THUNDERSTORM:
        else if ( (type.equals("thunderstorm")) || (type.equals("Thunderstorm")) ) {

            // NIGHT:
            if ( (time > 21) || ( (time > 0) && (time < 7) ) ) {
                weatherIcon = R.drawable.lighning_storm_moon;
            }

            // DAY:
            else { weatherIcon = R.drawable.lightning_storm; }
        }

        // CLEAR:
        else if ( (type.equals("clear")) || (type.equals("moon")) || (type.equals("night")) || (type.equals("clear skies"))) {

            // NIGHT:
            if ( (time > 21) || ( (time > 0) && (time < 7) ) ) {
                weatherIcon = R.drawable.moon;
            }

            // DAY:
            else { weatherIcon = R.drawable.sun; }
        }

        // MOSTLY SUNNY:
        else if ( (type.equals("mostly sunny")) || (type.equals("Mostly Sunny")) ) {
            weatherIcon = R.drawable.mostly_sunny;
        }

        // PARTLY CLOUDY:
        else if ( (type.equals("partly cloudy")) || (type.equals("Partly Cloudy")) ) {
            weatherIcon = R.drawable.partly_cloudy;
        }

        // WINDY:
        else if ( (type.equals("windy")) || (type.equals("Windy")) ) {
            weatherIcon = R.drawable.wind;
        }

        // SNOW:
        else if ( (type.equals("snow")) || (type.equals("Snow")) || (type.equals("Snowy")) ) {
            weatherIcon = R.drawable.snow;
        }

        // SLEET:
        else if ( (type.equals("sleet")) || (type.equals("Sleet")) ) {
            weatherIcon = R.drawable.sleet;
        }

        // SNOW RAIN:
        else if ( (type.equals("snow rain")) || (type.equals("Snow Rain")) ) {
            weatherIcon = R.drawable.snow_rain;
        }

        // NOT AVAILABLE:
        else {
            weatherIcon = R.drawable.na;
        }

        return weatherIcon;
    }
}
