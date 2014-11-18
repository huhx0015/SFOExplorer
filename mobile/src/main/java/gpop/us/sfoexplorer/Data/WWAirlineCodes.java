package gpop.us.sfoexplorer.Data;

/**
 * Created by Michael Yoon Huh on 11/18/2014.
 */
public class WWAirlineCodes {

    /** AIRLINE CODE FUNCTIONALITY _____________________________________________________________ **/

    // decipherAirlineCodes(): Returns the full name of the airline based on the airline code given.
    public static String decipherAirlineCodes(String code) {

        String airline = "Unknown Airlines";

        // AMERICAN AIRLINES:
        if (code.equals("AA")) { airline = "American Airlines"; }

        // 40-MILE AIR:
        else if (code.equals("Q5")) { airline = "40-Mile Air"; }

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

        // CONTINENTAL AIRLINES:
        else if (code.equals("CO")) { airline = "Continental Airlines"; }

        // DELTA AIRLINES:
        else if (code.equals("DL")) { airline = "Delta Air Lines"; }

        // FRONTIER AIRLINES:
        else if (code.equals("F9")) { airline = "Frontier Airlines"; }

        // HAWAIIAN AIRLINES:
        else if (code.equals("HA")) { airline = "Hawaiian Airlines"; }

        // JETBLUE AIRWAYS:
        else if (code.equals("B6")) { airline = "JetBlue Airways"; }

        // SUN COUNTRY AIRLINES:
        else if (code.equals("SY")) { airline = "Sun Country Airlines"; }

        // SOUTHWEST AIRLINES:
        else if (code.equals("WN")) { airline = "Southwest Airlines"; }

        // UNITED AIRLINES:
        else if (code.equals("UA")) { airline = "United Airlines"; }

        // UNKNOWN:
        else { airline = "Unknown Airlines"; }

        return airline;
    }

}
