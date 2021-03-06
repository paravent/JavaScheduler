package solution;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;

import baseclasses.DataLoadingException;
import baseclasses.IRouteDAO;
import baseclasses.Route;

/**
 * The RouteDAO parses XML files of route information, each route specifying
 * where the airline flies from, to, and on which day of the week
 */
public class RouteDAO implements IRouteDAO {
	List<Route> routes = new ArrayList<>(); 
	/**
	 * Finds all flights that depart on the specified day of the week
	 * @param dayOfWeek A three letter day of the week, e.g. "Tue"
	 * @return A list of all routes that depart on this day
	 */
	@Override   
	public List<Route> findRoutesByDayOfWeek(String dayOfWeek) {
		// TODO Auto-generated method stub
		List<Route> byDayOfWeek = new ArrayList<>(); 
		for(int i=0; i<routes.size(); i++) {
			if(dayOfWeek.equals(routes.get(i).getDayOfWeek())) {
				byDayOfWeek.add(routes.get(i));
			}
			 
		}
		return byDayOfWeek; 
	}

	/**
	 * Finds all of the flights that depart from a specific airport on a specific day of the week
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @param dayOfWeek the three letter day of the week code to search for, e.g. "Tue"
	 * @return A list of all routes from that airport on that day
	 */
	@Override
	public List<Route> findRoutesByDepartureAirportAndDay(String airportCode, String dayOfWeek) {
		// TODO Auto-generated method stub
		List<Route> portAndDay = new ArrayList<>(); 
		for(int i=0; i<routes.size(); i++) {
			if(airportCode.equals(routes.get(i).getDepartureAirportCode()) && dayOfWeek.equals(routes.get(i).getDayOfWeek())) {
				portAndDay.add(routes.get(i)); 
			}
		}
		return portAndDay; 
	}

	/**
	 * Finds all of the flights that depart from a specific airport
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @return A list of all of the routes departing the specified airport
	 */
	@Override
	public List<Route> findRoutesDepartingAirport(String airportCode) {
		// TODO Auto-generated method stub
		List<Route> depAir = new ArrayList<>(); 
		for(int i=0; i<routes.size(); i++) {
			if(airportCode.equals(routes.get(i).getDepartureAirportCode())) {
				depAir.add(routes.get(i)); 
			}
		}
		return depAir; 
	}

	/**
	 * Finds all of the flights that depart on the specified date
	 * @param date the date to search for
	 * @return A list of all routes that depart on this date
	 */
	@Override
	public List<Route> findRoutesbyDate(LocalDate date) {
		List<Route> findByDate = new ArrayList<>(); 
		String myDateString = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); 
		
		for(int i=0; i<routes.size(); i++){
			
			if(myDateString.equals(routes.get(i).getDayOfWeek())) {
				
				findByDate.add(routes.get(i)); 
			}
		}
		
		return findByDate; 
	}

	/**
	 * Returns The full list of all currently loaded routes
	 * @return The full list of all currently loaded routes
	 */
	@Override
	public List<Route> getAllRoutes() {
		// TODO Auto-generated method stub
		List<Route> allRoutes = new ArrayList<>(); 
		
		for(int i=0; i<routes.size(); i++) {
			allRoutes.add(routes.get(i)); 
			
		}
		return allRoutes; 
	}

	/**
	 * Returns The number of routes currently loaded
	 * @return The number of routes currently loaded
	 */
	@Override
	public int getNumberOfRoutes() {
		// TODO Auto-generated method stub
		int size = routes.size(); 
		return size; 
	}

	/**
	 * Loads the route data from the specified file, adding them to the currently loaded routes
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadRouteData(Path p) throws DataLoadingException {
		// TODO Auto-generated method stub
		DocumentBuilder docBuild; 
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance(); 
		Document doc = null;
		
		try {
			
			docBuild = fac.newDocumentBuilder(); 
			doc = docBuild.parse(p.toFile()) ;
			Element root = doc.getDocumentElement(); //Gets the root element of the XML file
            NodeList Routes = root.getElementsByTagName("Route"); 
            

            for(int i=0; i<Routes.getLength(); i++) {
            	Route r = new Route();
            	
            	Node myNode = Routes.item(i); 
          	    
          	   
          	    Element myElement = (Element) myNode;
                

                try {
                	int flightNumber = Integer.parseInt(myElement.getElementsByTagName("FlightNumber").item(0).getTextContent());
                	r.setFlightNumber(flightNumber); 
                }
                
                catch(Exception e){
                	throw new DataLoadingException(e); 
                }
                
                try {
                	String dayOfWeek = myElement.getElementsByTagName("DayOfWeek").item(0).getTextContent(); 
                    r.setDayOfWeek(dayOfWeek);
 
                }
                
                catch(Exception e){
                	throw new DataLoadingException(e); 
                }

                LocalTime depTime = LocalTime.parse(myElement.getElementsByTagName("DepartureTime").item(0).getTextContent()); 
                LocalTime arrTime = LocalTime.parse(myElement.getElementsByTagName("ArrivalTime").item(0).getTextContent()); 
                Duration dur = Duration.parse(myElement.getElementsByTagName("Duration").item(0).getTextContent()); 
                
                r.setDepartureTime(depTime);
                r.setDepartureAirport(myElement.getElementsByTagName("DepartureAirport").item(0).getTextContent());
                r.setDepartureAirportCode(myElement.getElementsByTagName("DepartureAirportIATACode").item(0).getTextContent());
                r.setArrivalTime(arrTime);
                r.setArrivalAirport(myElement.getElementsByTagName("ArrivalAirport").item(0).getTextContent());
                r.setArrivalAirportCode(myElement.getElementsByTagName("ArrivalAirportIATACode").item(0).getTextContent());
                r.setDuration(dur);
                
                
                routes.add(r); 
                

            }

		
	}	catch(ParserConfigurationException | SAXException | IOException | NullPointerException  e) {
		throw new DataLoadingException(e); 
    	
    }
 
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		routes.removeAll(routes); 
	}

}
