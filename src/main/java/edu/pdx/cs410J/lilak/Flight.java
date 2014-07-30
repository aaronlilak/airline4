package edu.pdx.cs410J.lilak;


import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.lilak.myAirportnames;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/** Flight class implements details of an airline flight*/
public class Flight extends AbstractFlight implements Comparable<Flight> {
    Class c = AbstractFlight.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    /** Constructor default */
    public Flight() {
        super();
    }


    /** Alternative constructor which takes in flight information
     *
     * @param flightnumber  integer value of flight number
     * @param origin    3 letter origin code
     * @param depart_timedate time/date string of departure
     * @param destination 3 letter destination code
     * @param arrive_timedate time/date string of arrival
     */
    public Flight(int flightnumber, String origin, String depart_timedate, String destination, String arrive_timedate) {
        super();
        depart_time_date=depart_timedate;
        arrive_time_date=arrive_timedate;
        origin_code=origin;
        destination_code=destination;
        flight_number=flightnumber;
        settimedate();
    }

    public int compareTo(Flight compareFlight) {
        if (compareFlight==null) return 0;
        if (this==null) return 0;
        //first sort alphabetically
        String thisorigincode=origin_code;
        String compareorigincode=compareFlight.origin_code;
        int returnval=thisorigincode.compareTo(compareorigincode);
        if (returnval!=0) return returnval;
        //next sort numerically if you haven't yet decided
        returnval=departdate.compareTo(compareFlight.departdate);
        if (returnval !=0) return returnval;
        //then return 0 since they are equal
        return 0;
    }

    /*
    public int compareTo(AbstractFlight compareFlight) {
        return 0;
    }
    */

    /**printflight() method prints out the details of the flight */
    public void PrintFlight() {
        System.out.print("Flight number "+ flight_number + " departs from " +
                origin_code + " at "+ depart_time_date + " and arrives at " +
                destination_code + " at " + arrive_time_date + "\n");
    }

    public void settimedate() {
        String [] tokensarrive=arrive_time_date.split(" ");
        String [] datearrive=tokensarrive[0].split("/");
        String [] timearrive=tokensarrive[1].split(":");
        String [] tokensdepart=depart_time_date.split(" ");
        String [] timedepart=tokensdepart[1].split(":");
        String [] datedepart=tokensdepart[0].split("/");




        Calendar calendararrive = Calendar.getInstance();
        calendararrive.set(Calendar.DATE, Integer.parseInt(datearrive[1]));
        calendararrive.set(Calendar.MONTH, Integer.parseInt(datearrive[0]));
        calendararrive.set(Calendar.YEAR, Integer.parseInt(datearrive[2]));
        calendararrive.set(Calendar.HOUR, Integer.parseInt(timearrive[0]));
        calendararrive.set(Calendar.MINUTE, Integer.parseInt(timearrive[1]));
        calendararrive.set(Calendar.SECOND, 00);
        if (tokensarrive[2].contentEquals("PM")) {
            calendararrive.set(Calendar.AM_PM, 1);
        } else {
            calendararrive.set(Calendar.AM_PM,0);
        }
        arrivedate =new Date(calendararrive.getTimeInMillis());


        Calendar calendardepart = Calendar.getInstance();
        calendardepart.set(Calendar.DATE, Integer.parseInt(datedepart[1]));
        calendardepart.set(Calendar.MONTH, Integer.parseInt(datedepart[0]));
        calendardepart.set(Calendar.YEAR, Integer.parseInt(datedepart[2]));
        calendardepart.set(Calendar.HOUR, Integer.parseInt(timedepart[0]));
        calendardepart.set(Calendar.MINUTE, Integer.parseInt(timedepart[1]));
        calendardepart.set(Calendar.SECOND, 00);
        if (tokensdepart[2].contentEquals("PM")) {
            calendardepart.set(Calendar.AM_PM, 1);
        } else {
            calendardepart.set(Calendar.AM_PM,0);
        }
        departdate =new Date(calendardepart.getTimeInMillis());

        duration_mins=(int) Math.ceil((calendararrive.getTimeInMillis()-calendardepart.getTimeInMillis())/1000.0/60.0);
/*
        System.out.print("The depart date construct is "+ departdate.toString() + "\n");
        System.out.print("The arrive date constructn is "+ arrivedate.toString() + "\n");
        System.out.print("The duration of the flight is "+ duration_mins+ " minutes \n");
        System.out.print("The arrival string is "+ getArrivalString() + "\n");
        System.out.print("The departure string is "+getDepartureString() +"\n");
        System.out.print("The pretty string is "+GetPrettyString() +"\n");
        */
    }

    public String GetFlightString() {
        if (this==null) return null;
        StringBuilder sb= new StringBuilder("");
        sb.append(flight_number).append(" ").append(origin_code).append(" ").append(depart_time_date).append(" ");
        sb.append(destination_code).append(" ").append(arrive_time_date).append("\n");
        return sb.toString();
    }

    /**GetPrettyString() returns the prettified string for the flight...including the substitution into the map of code/airport cities */
    public String GetPrettyString() {
        myAirportnames an= new myAirportnames();
        if (this==null) return null;
        StringBuilder sb=new StringBuilder("");
        sb.append("flight number ").append(flight_number).append(" departs from ").append(an.getName(origin_code)).append(" at ").append(getDepartureString()).append(" to ").append(an.getName(destination_code)).append(" arriving at ").append(getArrivalString()).append(" with a duration of ").append(getDuration()).append(" minutes. \n");
        return sb.toString();
    }

    /** getNumber() returns the integer flight number */
    public int getNumber() {
        return flight_number;
    }

    /**getsource() returns the 3 character origin city code */
    public String getSource() {
        return origin_code;
    }

    /** getdeparturestring() returns the time/date departure string */
    public String getDepartureString() {

        //return depart_time_date;
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(departdate);
    }

    /**getdestination() returns the string 3 character code for destination */
    public String getDestination() {
        return destination_code;
    }

    /**getarrivalstring() returns the time/date of the arrival */
    public String getArrivalString() {

        //return arrive_time_date;
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(arrivedate);
    }

    public int getDuration() {
        return duration_mins;
    }


    /** depart_time_date is the string for departure information */
    private String depart_time_date;
    /** arrive_time_date is the string for arrival information */
    private String arrive_time_date;
    /** origin_code is the 3 character origin code */
    private String origin_code;
    /** destination_code is the 3 character destination code */
    private String destination_code;
    /** flight_number is the integer flight number */
    private int flight_number;

    private Date departdate;
    private Date arrivedate;

    int duration_mins;
}
