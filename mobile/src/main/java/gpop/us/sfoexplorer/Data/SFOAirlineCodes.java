package gpop.us.sfoexplorer.Data;

import gpop.us.sfoexplorer.R;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class SFOAirlineCodes {

    /** AIRLINE CODE FUNCTIONALITY _____________________________________________________________ **/

    // decipherAirlineCodes(): Returns the full name of the airline based on the airline code given.
    public static String decipherAirlineCodes(String code) {

        code = code.toUpperCase(); // Converts the airline code to upper case.
        String airline = "Unknown Airlines";

        // AMERICAN AIRLINES:
        if (code.equals("AA")) { airline = "American Airlines"; }

        // 40-MILE AIR:
        else if (code.equals("Q5")) { airline = "40-Mile Air"; }

        // AER LINGUS:
        else if (code.equals("EL")) { airline = "Aer Lingus"; }

        // AMEERIJET:
        else if (code.equals("M6")) { airline = "Amerijet International"; }

        // ALASKA AIRLINES:
        else if (code.equals("AS")) { airline = "Alaska Airlines"; }

        // AMERICAN EAGLE AIRLINES:
        else if (code.equals("MQ")) { airline = "American Eagle Airlines"; }

        // AIRTRAN AIRWAYS:
        else if (code.equals("FL")) { airline = "AirTran Airways"; }

        // ALOHA AIRLINES:
        else if (code.equals("AQ")) { airline = "Aloha Airlines"; }

        // CHINA EASTERN AIRLINES:
        else if (code.equals("MU")) { airline = "China Eastern Airlines"; }

        // CONTINENTAL AIRLINES:
        else if (code.equals("CO")) { airline = "Continental Airlines"; }

        // DELTA AIRLINES:
        else if (code.equals("DL")) { airline = "Delta Air Lines"; }

        // EMIRATES:
        else if (code.equals("EK")) { airline = "Emirates"; }

        // ETIHAD AIRWAYS:
        else if (code.equals("EY")) { airline = "Etihad Airways"; }

        // FRONTIER AIRLINES:
        else if (code.equals("F9")) { airline = "Frontier Airlines"; }

        // HAWAIIAN AIRLINES:
        else if (code.equals("HA")) { airline = "Hawaiian Airlines"; }

        // JETBLUE AIRWAYS:
        else if (code.equals("B6")) { airline = "JetBlue Airways"; }

        // KOREAN AIR:
        else if (code.equals("KE")) { airline = "Korean Air"; }

        // SUN COUNTRY AIRLINES:
        else if (code.equals("SY")) { airline = "Sun Country Airlines"; }

        // SOUTHWEST AIRLINES:
        else if (code.equals("WN")) { airline = "Southwest Airlines"; }

        // UNITED AIRLINES:
        else if (code.equals("UA")) { airline = "United Airlines"; }

        // US AIRWAYS:
        else if (code.equals("US")) { airline = "US Airways"; }

        // VIRGIN AMERICA:
        else if (code.equals("VX")) { airline = "Virgin America"; }

        // UNKNOWN:
        else { airline = "Unknown Airlines"; }

        return airline;
    }

    // getAirlineIcon(): Returns the image resource of the airline icon.
    public static int getAirlineIcon(String code) {

        code = code.toUpperCase(); // Converts the airline code to upper case.
        int icon = R.drawable.plane_icon; // Default airline icon.

        // AMERICAN AIRLINES:
        if (code.equals("AA")) { icon = R.drawable.aa_icon; }

        // 40-MILE AIR:
        //else if (code.equals("Q5")) { airline = "40-Mile Air"; }

        // AER LINGUS:
        else if (code.equals("EL")) { icon = R.drawable.el_icon; }

        // AMEERIJET:
        //else if (code.equals("M6")) { airline = "Amerijet International"; }

        // ALASKA AIRLINES:
        //else if (code.equals("AS")) { airline = "Alaska Airlines"; }

        // AMERICAN EAGLE AIRLINES:
        else if (code.equals("MQ")) { icon = R.drawable.aa_icon; }

        // AIRTRAN AIRWAYS:
        //else if (code.equals("FL")) { airline = "AirTran Airways"; }

        // ALOHA AIRLINES:
        //else if (code.equals("AQ")) { airline = "Aloha Airlines"; }

        // CHINA EASTERN AIRLINES:
        else if (code.equals("MU")) { icon = R.drawable.mu_icon; }

        // CONTINENTAL AIRLINES:
        else if (code.equals("CO")) { icon = R.drawable.co_icon; }

        // DELTA AIRLINES:
        else if (code.equals("DL")) { icon = R.drawable.dl_icon; }

        // ETIHAD AIRWAYS:
        else if (code.equals("EY")) { icon = R.drawable.ey_icon; }

        // FRONTIER AIRLINES:
        else if (code.equals("F9")) { icon = R.drawable.f9_icon; }

        // HAWAIIAN AIRLINES:
        //else if (code.equals("HA")) { airline = "Hawaiian Airlines"; }

        // JETBLUE AIRWAYS:
        //else if (code.equals("B6")) { airline = "JetBlue Airways"; }

        // KOREAN AIR:
        else if (code.equals("KE")) { icon = R.drawable.ke_icon; }

        // SUN COUNTRY AIRLINES:
        //else if (code.equals("SY")) { airline = "Sun Country Airlines"; }

        // SOUTHWEST AIRLINES:
        else if (code.equals("WN")) { icon = R.drawable.wn_icon; }

        // UNITED AIRLINES:
        //else if (code.equals("UA")) { airline = "United Airlines"; }

        // US AIRWAYS:
        else if (code.equals("US")) { icon = R.drawable.us_icon; }

        // VIRGIN AMERICA:
        else if (code.equals("VX")) { icon = R.drawable.vx_icon; }

        // UNKNOWN:
        //else { airline = "Unknown Airlines"; }

        return icon;
    }

}
