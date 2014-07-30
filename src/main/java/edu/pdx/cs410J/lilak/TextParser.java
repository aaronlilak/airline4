package edu.pdx.cs410J.lilak;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
/**TextParser class implements AirlineParser*/

/** TextParser will read an airline from a file and do error checking on it*/
public class TextParser implements AirlineParser {

    /**TextParser constructor which takes a String filename as an argument */
    public TextParser(String thisfilename) {
        filename=thisfilename;
    }

    /**parse method for TextParser. This will read the file and throws possibly a ParserException */
    public Airline parse()  throws ParserException {
        BufferedReader in=null;
        airlinename=null;
        Airline airlinetest=new Airline();
        File f = new File(filename);
        if(f.exists() && !f.isDirectory()) {

            try {
                in = new BufferedReader(new FileReader(filename));
                String line;
                Integer linecount=0;
                while ((line = in.readLine()) != null) {
                    String[] paramlist = new String[10];
                    StringTokenizer st = new StringTokenizer(line);
                    int countval = 0;
                    if (st.countTokens() == 0) continue; //windows is giving me a bunch of empty lines
                    if (st.countTokens() != 10) {
                        System.out.print("There is a problem in the input file " + filename + " which has " + st.countTokens());
                        System.out.print(" arguments instead of 10. Note that the options -print, -filename, and -README are not valid ");
                        System.out.print("from inside a text file input....only from the command line \n");
                        System.out.print("The PROBLEM LINE WAS: " + line);
                        System.exit(1);
                    }
                    while (st.hasMoreTokens()) {
                        paramlist[countval] = st.nextToken();
                        countval++;
                    }
                    //now validate your input
                    for (int i = 0; i < 10; i++) {
                        Boolean goodarg = true;
                        goodarg = Project4.validate_input_param(paramlist[i], i);
                        if (!goodarg) {
                            System.out.print("Problem with the formatting in the file " + filename + " \n");
                            System.exit(1);
                        }
                    }
                    if (linecount==0) {
                        airlinetest.setName(paramlist[0]);
                    } else {
                        if (!airlinetest.getName().contentEquals(paramlist[0])) {
                            System.out.print("There is an airline name mismatch in the file "+ filename + " between "+airlinetest.getName() + " and "+ paramlist[0] +"\n");
                            System.exit(1);
                        }
                    }
                    Flight thisflight = new Flight(Integer.parseInt(paramlist[1]), paramlist[2], paramlist[3] + " " + paramlist[4]+" "+paramlist[5], paramlist[6], paramlist[7] + " " + paramlist[8]+" "+paramlist[9]);
                    airlinetest.addFlight(thisflight);
                    linecount++;
                }
            } catch (IOException ex) {
                System.out.print("Problem with opening/reading from the file in parse" + filename );
                System.exit(1);
            }
        }
        return airlinetest;
    }

    /**filename is the String name of the file you are reading from */
    private String filename;
    /**airlinename is the String name of the airline that you are working with */
    private String airlinename;
}
