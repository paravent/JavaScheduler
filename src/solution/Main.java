 package solution;

import org.sqlite.*; 
import java.sql.*; 

import java.nio.file.Paths;
import java.time.LocalDate;

import baseclasses.DataLoadingException;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;

/**
 * This class allows you to run the code in your classes yourself, for testing and development
 */
public class Main {

	public static void main(String[] args) {	
		IAircraftDAO aircraft = new AircraftDAO();
		ICrewDAO crew = new CrewDAO(); 
		IRouteDAO routes = new RouteDAO(); 
		IPassengerNumbersDAO passengers = new PassengerNumbersDAO(); 
		IScheduler schedule = new Scheduler(); 
		
		
		
		/*
		try {
			//Tells your Aircraft DAO to load this particular data file
			aircraft.loadAircraftData(Paths.get("./data/aircraft.csv"));
			System.out.println("---------------------------------------------------------");
			aircraft.loadAircraftData(Paths.get("./data/mini_aircraft.csv"));
			aircraft.loadAircraftData(Paths.get("./data/malformed_aircraft1.csv"));
		}
		catch (DataLoadingException dle) {
			System.err.println("Error loading aircraft data");
			dle.printStackTrace();
		}
		aircraft.getAllAircraft(); 
		aircraft.getNumberOfAircraft(); 
		aircraft.findAircraftBySeats(220); 
		aircraft.findAircraftByStartingPosition("MAN"); 
		aircraft.findAircraftByTailCode("G-AAAC"); 
		aircraft.reset(); 
		
		try {
			crew.loadCrewData(Paths.get("./data/crew.json"));
		} catch (DataLoadingException dle) {
			// TODO Auto-generated catch block
			dle.printStackTrace();
		} 
		  
		try {
			routes.loadRouteData(Paths.get("./data/mini_routes.xml"));
	//		routes.loadRouteData(Paths.get("./data/routes.xml"));
			
		} catch (DataLoadingException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		routes.findRoutesbyDate(LocalDate.parse("2018-12-06")); 
		
		
		
		try {
	//		passengers.loadPassengerNumbersData(Paths.get("./data./passengernumbers.db"));
			passengers.loadPassengerNumbersData(Paths.get("./data./mini_passengers.db"));
		} catch (DataLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		passengers.getPassengerNumbersFor(618, LocalDate.parse("2020-08-26")); 
		*/ 
		
		schedule.generateSchedule(aircraft, crew, routes, passengers, LocalDate.parse("2020-07-01"), LocalDate.parse("2020-07-08")); 
		
	}
	
	
}
