package edu.pdx.cs410J.lilak;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** Airline class is implementation of Airline. In project1 airline is given a name */
public class Airline extends AbstractAirline {
    Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath

    /**Constructor */
    public Airline() {
        super();
        sizefl=0;
    }

    /**Constructor which takes a String argument for the name
    * @param thisname is name of airline
    */

     public Airline(String thisname) {
        super();
        List flightlist = new ArrayList<Flight>();
        //ArrayList<AbstractFlight> flightlist = new ArrayList<AbstractFlight>();
        name=thisname;
         sizefl=0;
    }

    /**PrintAirline prints out the airline name
     */
    public void PrintAirline() {
        System.out.println("Airline is named " + name + "\n");
        for (Iterator it=flightlist.iterator(); it.hasNext();) {
            //it.next();
            Object thisone=it.next();
            Flight temp=(Flight) thisone;
            temp.PrintFlight();
        }
        Flight lastflight=null;
        for (Iterator it=flightlist.iterator(); it.hasNext();) {
            Object thisone=it.next();
            Flight temp=(Flight) thisone;
            int myval=temp.compareTo(lastflight);
            if (temp!=null && lastflight!=null) {
                //System.out.print("comparison is " + myval + " between flights of " + temp.GetFlightString() + " and " + lastflight.GetFlightString() + "\n");
            }
            lastflight=temp;
        }
        return;
    }

    /**getName returns the name of the airline */
    public String getName() {
        return name;
    }

    public Integer getSize() { return sizefl; }



    /**addFlight adds in a flight the flight collection */

    public void addFlight(Flight newFlight) {
        //System.out.print("Adding flight in the flight way \n");
        flightlist.add(newFlight);
        sizefl=flightlist.size();

        Collections.sort(flightlist);
        return;
    }

    public void addFlight(AbstractFlight newFlight) {
        System.out.print("Do you ever see this \n");
        return;
    }


    /**getFlights() will return the list of flights in future */
    public java.util.Collection getFlights() {
        return null;
    }

    /**setName sets the name of the airline */
    public void setName(String namestring) {
        name=namestring;
    }

    //An airline has a name and contains a list of flights
    /**name is a String with name of the airline */
    private String name;
    /**flightlist is a list of flights */

    private Integer sizefl;

    public List<Flight> flightlist = new ArrayList<Flight>();
    //public List<Flight> flightlist = new ArrayList<Flight>();
    //public List<AbstractFlight> flightlist;


}
