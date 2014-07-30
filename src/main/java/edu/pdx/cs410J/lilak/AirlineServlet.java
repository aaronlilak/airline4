package edu.pdx.cs410J.lilak;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import edu.pdx.cs410J.lilak.Airline;
import java.net.HttpURLConnection;

public class AirlineServlet extends HttpServlet
{
    private final Map<String, String> data = new HashMap<String, String>();

    public static Airline thisairline;


    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        StringBuffer requestURL=request.getRequestURL();
        if (request.getQueryString()!=null) {
            requestURL.append("?").append(request.getQueryString());
        }
        String query=request.getQueryString();
        //tokenize on the & symbol
        String [] paramlist=query.split("&");

        PrintWriter pw = response.getWriter();
        //pw.println("the length of the paramlist is "+paramlist.length+"\n");
        pw.flush();
        if (paramlist.length==2 || paramlist.length>=4) {
            //you don't know what to do here....just indicate an error and get out
            pw.println("The format of the query string is odd. Query is designed to handle 1 or 3 value-pairs in current implementation and you are passing in "+paramlist.length+"\n");
            pw.flush();
            return;
        }
        if (paramlist.length==1) {
            //this should be the url access to spit out a list of flights on airline....should have query of form 'airline=myairline'
            String splitparams []=paramlist[0].split("=");
            pw.println("Your query for the flights on airline "+splitparams[1] + ":\n");
            pw.flush();
            if (!splitparams[0].matches("name")) {
                pw.println("Unknown query parameter "+splitparams[0] +" did you perhaps mean 'name' \n");
                pw.flush();
                return;
            }
            if (thisairline.getName().matches(splitparams[1])) {
                for (Iterator it = thisairline.flightlist.iterator(); it.hasNext(); ) {
                    Object thisone = it.next();
                    Flight temp = (Flight) thisone;
                    //System.out.print(temp.GetPrettyString()+"\n");
                    pw.println(temp.GetPrettyString());
                    pw.flush();
                }
            }
        }
        if (paramlist.length==3) {
            //this should be a query for flights originating at src airport and terminating at dest airport
            String airportparams []=paramlist[0].split("=");
            String srcparams []=paramlist[1].split("=");
            String destparams []=paramlist[2].split("=");
            if (!airportparams[0].matches("name")) {
                pw.println("The parameter in query "+airportparams[0]+" is not recognized....did you mean 'name'\n");
                pw.flush();
                return;
            }
            if (!srcparams[0].matches("src")) {
                pw.println("The parameter in query "+srcparams[0]+" is not recognized....did you mean 'src'\n");
                pw.flush();
                return;
            }
            if (!destparams[0].matches("dest")) {
                pw.println("The parameter in query "+destparams[0]+" is not recognized....did you mean 'dest'\n");
                pw.flush();
                return;
            }
            Boolean goodsrc=Project4.validate_input_param(srcparams[1],2);
            Boolean gooddest=Project4.validate_input_param(destparams[1],2);
            if (!goodsrc || !gooddest) {
                String whichone="src";
                if (!gooddest) whichone="dest";
                pw.println("The "+whichone+" parameter is not properly formatted or does not represent a valid airport. \n");
                pw.flush();
                return;
            }
            pw.println("Your query for the flights on airline "+airportparams[1]+" departing from "+ srcparams[1] + " and going to "+destparams[1]+":\n");
            pw.flush();
            for (Iterator it=thisairline.flightlist.iterator(); it.hasNext();) {
                Object thisone = it.next();
                Flight temp = (Flight) thisone;
                if (temp.getSource().contentEquals(srcparams[1]) && temp.getDestination().contentEquals(destparams[1])) {
                    pw.println(temp.GetPrettyString());
                    pw.flush();
                }
            }
        }
        //pw.println(" the url in doget is "+requestURL+"\n");
        //System.out.println("in the Doget \n");
        //System.out.println("the url in doget from system is "+requestURL+"\n");
        response.setContentType( "text/plain" );

        String key = getParameter( "key", request );
        //System.out.println("the key is "+key +"\n");
        //pw.println("in get with key of "+ key +"\n");
        pw.flush();

        if (key != null) {
            writeValue(key, response);

        } else {
            writeAllMappings(response);
        }
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        String key = getParameter( "key", request );
        String [] tokenskey=key.split("\\?");


        if (thisairline==null) {
            thisairline=new Airline();
            thisairline.setName(tokenskey[0]);
            //System.out.print("just created an airline");
        }

        PrintWriter pw = response.getWriter();

        StringBuffer requestURL=request.getRequestURL();
        if (request.getQueryString()!=null) {
            requestURL.append("?").append(request.getQueryString());
        }
        String [] paramlist;
        String query=request.getQueryString();
        //tokenize on the & symbol
        if (query!=null) {
            paramlist = query.split("&");
            //pw.println("the length of the paramlist is "+paramlist.length+"\n");
            pw.flush();
            Boolean goodinput=true;
            if (paramlist.length!=6) {
                //you don't know how to deal with it if it isn't in right form
                String airlinestring=paramlist[0];
                String [] airlinetokens=airlinestring.split("=");
                if (!airlinetokens[0].matches("name")) {
                    pw.println("Expected a parameter named 'name' and got one named "+airlinetokens[0] + "in query string \n");
                    pw.flush();
                    return;
                }

                String flightstring=paramlist[1];
                String [] flighttokens=flightstring.split("=");
                if (!flighttokens[0].matches("flightNumber")) {
                    pw.println("Expected a parameter named 'flightNumber' and got one named "+flighttokens[0] + "in query string \n");
                    pw.flush();
                    return;
                }
                goodinput=Project4.validate_input_param(flighttokens[1],1);
                if (!goodinput) {
                    pw.println("There is a problem with the flightnumber "+flighttokens[1]+"\n");
                    pw.flush();
                    return;
                }

                String srcstring=paramlist[2];
                String [] srctokens=srcstring.split("=");
                if (!srctokens[0].matches("src")) {
                    pw.println("Expected a parameter named 'src' and got one named "+srctokens[0] + "in query string \n");
                    pw.flush();
                    return;
                }
                goodinput=Project4.validate_input_param(srctokens[1],2);
                if (!goodinput) {
                    pw.println("There is a problem with the src code "+srctokens[1]+"\n");
                    pw.flush();
                    return;
                }

                String departstring=paramlist[3];
                String [] departtokens=departstring.split("=");
                if (!departtokens[0].matches("departTime")) {
                    pw.println("Expected a parameter named 'departTime' and got one named "+departtokens[0] + "in query string \n");
                    pw.flush();
                    return;
                }

                String deststring=paramlist[4];
                String [] desttokens=deststring.split("=");
                if (!desttokens[0].matches("dest")) {
                    pw.println("Expected a parameter named 'dest' and got one named "+desttokens[0] + "in query string \n");
                    pw.flush();
                    return;
                }
                goodinput=Project4.validate_input_param(desttokens[1],2);
                if (!goodinput) {
                    pw.println("There is a problem with the dest code "+desttokens[1]+"\n");
                    pw.flush();
                    return;
                }

                String arrivestring=paramlist[5];
                String [] arrivetokens=arrivestring.split("=");
                if (!arrivetokens[0].matches("arriveTime")) {
                    pw.println("Expected a parameter named 'arriveTime' and got one named "+arrivetokens[0] + "in query string \n");
                    pw.flush();
                    return;
                }

                pw.println("will be adding a flight on " + airlinetokens[1]+ " with number of " + flighttokens[1]+" leaving from "+ srctokens[1] +" and leaving at "+departtokens[1]+" going to "+ desttokens[1] +" and arriving at "+arrivetokens[1]+"\n");
                pw.flush();

                Flight nextflight=new Flight(Integer.parseInt(flighttokens[1]),srctokens[1], departtokens[1], desttokens[1], arrivetokens[1]);
                thisairline.addFlight(nextflight);
                return;
            }
        }

        /*
        if (paramlist.length!=0 && paramlist.length!=6) {
            //you understand a 0 length paramlist and a 6 length paramlist....everything else puzzles you now
            pw.println("The format of the query string is puzzling with a length of "+paramlist.length+" parameters. This implementation of airline is configured to deal with 0 or 6 value pairs from URL post.\n");
            pw.flush();
            return;
        }
        if (paramlist.length==6) {
            //add in a flight this way
        }
        */

        response.setContentType( "text/plain" );


        if (key == null) {
            missingRequiredParameter( response, key );
            return;
        }

        String value = getParameter( "value", request );
        if ( value == null) {
            missingRequiredParameter( response, value );
            return;
        }

        if (value.contentEquals("add")) {
        //add a new flight
            Flight thisflight=new Flight(Integer.parseInt(tokenskey[1]), tokenskey[2], tokenskey[3]+" "+tokenskey[4]+" "+tokenskey[5], tokenskey[6], tokenskey[7]+" "+tokenskey[8]+" "+tokenskey[9]);
            thisairline.addFlight(thisflight);
            //System.out.println("NOW PRINTING THE AIRLINE \n");
            //thisairline.PrintAirline();
        }

        pw = response.getWriter();

        if (value.contentEquals("search")) {
            boolean printfirst=true;
            for (Iterator it=thisairline.flightlist.iterator(); it.hasNext();) {
                Object thisone = it.next();
                Flight temp = (Flight) thisone;
                if (temp.getSource().contentEquals(tokenskey[1]) && temp.getDestination().contentEquals(tokenskey[2])) {
                    if (printfirst==true) {
                        pw.println("Flights matching origin code of " + tokenskey[1] + " and destination code of " + tokenskey[2]+ "\n");
                        pw.flush();
                        //System.out.print("Flights matching origin code of " + tokenskey[1] + " and destination code of " + tokenskey[2]+ "\n");
                        printfirst=false;
                    }
                    //System.out.print(temp.GetPrettyString()+"\n");
                    pw.println(temp.GetPrettyString());
                    pw.flush();
                }
            }
        }

        //pw.println(Messages.mappedKeyValue(key, value));
        //pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    private void missingRequiredParameter( HttpServletResponse response, String key )
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(key));
        pw.flush();
        
        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    private void writeValue( String key, HttpServletResponse response ) throws IOException
    {
        String value = this.data.get(key);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( value != null ? 1 : 0 ));
        pw.println(Messages.formatKeyValuePair( key, value ));

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( data.size() ));

        for (Map.Entry<String, String> entry : this.data.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

}
