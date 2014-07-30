package edu.pdx.cs410J.lilak;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    /** validate_input_param is function which takes in
     *
     * @param thisarg   String value of the argument
     * @param index index in the input stream in which it arrived exclusive of options
     * @return a boolean value if the input is in a valid format. This is determined dependent upon the argument. F
     * For instance the date must be in mm/dd/yy format, time in military format must have hour <25, etc..
     */
    public static boolean validate_input_param(String thisarg, int index) {
        //System.out.print("calling input validation on index of "+ index + " and arg of " + thisarg + "\n");
        switch (index) {
            case 0:
                //just needs to be a String
                return true;
            //break;
            case 1:
                //flight number...needs to be integer
                if (thisarg.matches("\\d+")) {
                    //have a positive integer now..
                    return true;
                } else {
                    System.out.print("Flight number " + thisarg + " is not a positive integer \n");
                }
                break;
            case 2:
            case 6:
                myAirportnames an= new myAirportnames();
                //System.out.print("the name of the airport is "+an.getName(thisarg)+"\n");
                if (an.getName(thisarg)==null) {
                    System.out.print("The airport with code letters of " + thisarg+ " is not recognized"+"\n");
                    break;
                }
                //three letter depart code
                if (thisarg.length()!=3) {
                    System.out.print("Origin/Destination code is not three letters \n");
                    break;
                }
                if (!thisarg.matches("[a-zA-Z]+")) {
                    System.out.print("Origin/Destination code must be letters only \n");
                    break;
                }
                return true;
            //break;
            case 3:
            case 7:
                if (!thisarg.matches("\\d+/\\d+/\\d+")) {
                    System.out.print("Date not formatted correctly....should be m/d/year \n");
                    break;
                }

                String [] tokensdate = thisarg.split("/");
                if (Integer.parseInt(tokensdate[0])>12) {
                    System.out.print("Month must be in range of 0-12 \n");
                    break;
                }

                if (Integer.parseInt(tokensdate[1])>31) {
                    System.out.print("Day must be in range of 0-31 \n");
                    break;
                }

                if (Integer.parseInt(tokensdate[2])<1903) {
                    System.out.print("Year must be >1903 when Wright Brothers First Flew \n");
                    break;
                }
                //depart date
                return true;
            //break;
            case 4:
            case 8:
                if (!thisarg.matches("\\d+:\\d+")) {
                    System.out.print("Time not formatted correctly....should be hh:mm \n");
                    break;
                }

                String [] tokens = thisarg.split(":");
                if (Integer.parseInt(tokens[0])>24) {
                    System.out.print("Hour can not be greater than 24 \n");
                    break;
                }
                if (Integer.parseInt(tokens[1])>60) {
                    System.out.print("Minute can not be greater than 60 \n");
                    break;
                }
                if ((Integer.parseInt(tokens[0])==24) && (Integer.parseInt(tokens[1])!=0)) {
                    System.out.print("Time must be less than 24:00 \n");
                    break;
                }
                return true;
            case 5:
            case 9:
                if (!thisarg.contentEquals("AM") && (!thisarg.contentEquals("PM") && (!thisarg.contentEquals("am")) && (!thisarg.contentEquals("pm")))) {
                    System.out.print("Expect an AM or a PM on the time argument \n");
                    System.out.print("Invalid input for argument " + thisarg + "\n");
                    break;
                }
                //depart time
                return true;
        }
        return false;
    }

    public static String Readme_Message = "Program written by Aaron Lilak. This is implementation of CS410J program4. It allows for input of an" +
            " airline and flight information and query through HTTP access. This program will be built upon in future projects. \n" +
            "usage: java edu.pdx.cs410J.lilak.Project4 [options] <args>\n" +
            "args are (in this order):\n" +
            "name The name of the airline\n" +
            "flightNumber The flight number\n" +
            "src Three-letter code of departure airport\n" +
            "departTime Departure date and time (12-hour time)\n" +
            "dest Three-letter code of arrival airport\n" +
            "arriveTime Arrival date and time (12-hour time)\n" +
            "options are (options may appear in any order):\n" +
            "-host the host of webclient (ie localhost):\n"+
            "-port the port number of the webclient (ie 8080 for HTTP): \n"+
            "-search will invoke a search of flights matching src and destination: \n"+
            "-print Prints a description of the new flight\n" +
            "-README Prints a README for this project and exits\n" +
            "Date and time should be in the format: mm/dd/yyyy hh:mm am/pm";

    /**printreadme() is method to print out the readme text */
    public static void printreadme() {
        System.out.println(Readme_Message);
        return;
    }

    /** printairline() will print out the airline information */
    public static void printairline() {
        return;
    }

    public static void main(String... args) {
        String hostName = null;
        String portString = null;
        String key = null;
        String value = null;
        String [] plist = new String[10];
        Boolean portnext=false;
        Boolean hostnext=false;
        Boolean printit=false;
        Boolean search=false;
        int argcount=0;
        Boolean havesomepartofflight=false;
        Boolean posttest=false;

        for (String arg : args) {
            if (arg.contentEquals("-search")) {
                search=true;
                continue;
            }
        }

        for (String arg : args) {
            if (arg.contentEquals("-README")) {
                printreadme();
                System.exit(0);
            }
        }

        for (String arg : args) {
            if (portnext==true) {
                portnext=false;
                portString=arg;
                continue;
            }
            if (hostnext==true) {
                hostnext=false;
                hostName=arg;
                continue;
            }
            //System.out.println(arg);
            if (arg.contentEquals("-README")) {
                printreadme();
                System.exit(0);
            }
            if (arg.contentEquals("-port")) {
                portnext=true;
                continue;
            }
            if (arg.contentEquals("-host")) {
                hostnext=true;
                continue;
            }
            if (arg.contentEquals("-print")) {
                printit=true;
                continue;
            }
            if (arg.contentEquals("-search")) {
                //already populated the search boolean
                continue;
            }
            if (arg.contentEquals("-posttest")) {
                posttest=true;
                continue;
            }

            if (search==false) {
                //if you got here you should validate the input now
                boolean goodinput = validate_input_param(arg, argcount);
                if (goodinput==false) {
                  System.err.print("Problem with input stream for arg "+ arg);
                  System.exit(1);
                }
            } else {
                if (argcount >0) {
                    boolean goodinput = validate_input_param(arg, 2);
                    if (goodinput == false) {
                        System.err.print("The source/destination code is not proper \n");
                        System.exit(1);
                    }
                } else {
                    boolean goodinput = validate_input_param(arg, 0);
                    if (goodinput == false) {
                        System.err.print("The airline name is incorrectly formatted for "+ arg +"\n");
                        System.exit(1);
                    }
                }
            }
            plist[argcount]=arg;
            if (argcount>0) havesomepartofflight=true;
            argcount++;

        }

        if (havesomepartofflight==true) {
            if (search==false) {
                if (argcount!=10) {
                    System.err.print("Input is not correctly formatted with your "+ argcount + " number of parameters in addition to the options. \n");
                    System.exit(1);
                }
            } else {
                if (search==true) {
                    if (argcount !=3) {
                        System.err.print("There are not exactly 3 arguments (airline and source city and destination) for the search in addition to any options. \n");
                        System.exit(1);
                    }
                }
            }
        }

        if (hostName == null) {
            System.out.println("Missing the hostname in configuration line \n");

        } else if ( portString == null) {
            System.out.println("Missing the port number in the configuration line \n");
        }

        int port;
        try {
            port = Integer.parseInt( portString );
            
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AirlineRestClient client = new AirlineRestClient(hostName, port);

        if (search==true) {
            //key is a search key
            StringBuilder sb=new StringBuilder();
            sb.append(plist[0]).append("?").append(plist[1]).append("?").append(plist[2]);
            key=sb.toString();
            value="search";
        } else {
            if (argcount==10) {
                //key is a key for posting new flight
                StringBuilder sb=new StringBuilder();
                sb.append(plist[0]).append("?").append(plist[1]).append("?").append(plist[2]).append("?").append(plist[3]).append("?");
                sb.append(plist[4]).append("?").append(plist[5]).append("?").append(plist[6]).append("?").append(plist[7]).append("?");
                sb.append(plist[8]).append("?").append(plist[9]);
                key=sb.toString();
                value="add";
                if (printit) {
                    myAirportnames an= new myAirportnames();
                    System.out.println("New Flight on airline " + plist[0]+" number "+plist[1]+" leaves from " + an.getName(plist[2])+ " at " + plist[3]+ " " +plist[4]+ " " +plist[5] +
                            " and arrives at " + an.getName(plist[6]) + " at " + plist[7]+" "+plist[8]+" "+plist[9]+"\n");
                }
            } else {
                if (!posttest) {
                    System.out.println("You have not fully specified the flight....need 10 params or need to query with -search with 3\n");
                    System.exit(1);
                }
            }
        }

        if (posttest) {
            System.out.print("You will try to do something smart here now \n");
            System.exit(1);
        }


        //System.out.print("key is "+key+ " and the value is " + value +"\n");

        HttpRequestHelper.Response response;
        try {
            if (key == null) {
                // Print all key/value pairs
                //System.out.print("calling the getallkeysandvalues "+ "\n");
                response = client.getAllKeysAndValues();

            } else if (value == null) {
                // Print all values of key
                //System.out.print("calling the getvalues "+ "\n");

                response = client.getValues(key);

            } else {
                // Post the key/value pair
                //System.out.print("calling the addkeyvaluepair "+ "\n");

                response = client.addKeyValuePair(key, value);
            }

            checkResponseCode( HttpURLConnection.HTTP_OK, response);

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.out.println(response.getContent());

        System.exit(0);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }
}