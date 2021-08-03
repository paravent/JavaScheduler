package solution;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import baseclasses.Aircraft;
import baseclasses.DoubleBookedException;
import baseclasses.FlightInfo;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.InvalidAllocationException;
import baseclasses.Pilot.Rank;
import baseclasses.Schedule;

/**
 * The Scheduler class is responsible for deciding which aircraft and crew will be
 * used for each of an airline's flights in a specified period of time, referred to
 * as a "scheduling horizon". A schedule must have an aircraft, two pilots, and 
 * sufficient cabin crew for the aircraft allocated to every flight in the horizon 
 * to be valid.
 */
public class Scheduler implements IScheduler {

  /**
	 * Generates a schedule, providing you with ready-loaded DAO objects to get your data from
	 * @param aircraftDAO the DAO for the aircraft to be used when scheduling
	 * @param crewDAO the DAO for the crew to be used when scheduling                                                           
	 * @param routeDAO the DAO to use for routes when scheduling
	 * @param passengerNumbersDAO the DAO to use for passenger numbers when scheduling
	 * @param startDate the start of the scheduling horizon
	 * @param endDate the end of the scheduling horizon
	 * @return The generated schedule - which must happen inside 2 minutes
	 */
  @Override
  public Schedule generateSchedule(IAircraftDAO aircraftDAO, ICrewDAO crewDAO, IRouteDAO routeDAO, IPassengerNumbersDAO passengerNumbersDAO, LocalDate startDate, LocalDate endDate) {
	  

    Schedule newSchedule = new Schedule(routeDAO, startDate, endDate);
    List <FlightInfo> maxFlights = newSchedule.getRemainingAllocations();
    List <FlightInfo> flights = newSchedule.getRemainingAllocations();
    Boolean masterAllocation = true;
    int time = flights.size(); 
    
    String indent = "";
    List <Integer> routePos = new ArrayList <>();
    //Get list of all flights to be allocated. 
    int count = 0;
    int countCounter = 0;
    Boolean match;
	
    while (masterAllocation) {
    	
      match = false;
      List <FlightInfo> flightsUnallocated = new ArrayList < >(newSchedule.getRemainingAllocations());
      
      //go into loop of looking for matching flight && check its going back to original airport and flight number is within  1
      //if multiple same flights pick the earliest one
      //update routePos , when match is found. set match to true, break out of for loop. 
      //Check to see if we found a match
      //Plane and crew assignment 
      //else raise and error
      //loop 
      //if we have max amount, return schedule
      //read first unallocated flight
      
      routePos.add(count);
      
      for (int i = 0; i < flightsUnallocated.size(); i++) {
    	
        if (((flightsUnallocated.get(i).getFlight().getFlightNumber()) - (flightsUnallocated.get(count).getFlight().getFlightNumber()) == 1)  || 
        		(flightsUnallocated.get(i).getFlight().getFlightNumber()) - (flightsUnallocated.get(count).getFlight().getFlightNumber()) == -1) {

          indent = indent + "\t";
          routePos.add(i);
          countCounter = countCounter + 1;
          match = true;
          System.out.println("-----------------------------------------------------------------------------");
          System.out.println("Dep port : " + flightsUnallocated.get(count).getFlight().getDepartureAirportCode() );
          System.out.println("Departure time : " + flightsUnallocated.get(count).getDepartureDateTime());
          System.out.println("Arrival port : " + flightsUnallocated.get(count).getFlight().getArrivalAirportCode());
          System.out.println("Arrival time : " + flightsUnallocated.get(count).getLandingDateTime());
          System.out.println("-----------------------------------------------------------------------------");
          System.out.println("Dep port : " + flightsUnallocated.get(i).getFlight().getDepartureAirportCode() );
          System.out.println("Departure time : " + flightsUnallocated.get(i).getDepartureDateTime());
          System.out.println("Arrival port : " + flightsUnallocated.get(i).getFlight().getArrivalAirportCode());
          System.out.println("Arrival time : " + flightsUnallocated.get(i).getLandingDateTime());
          break;

        }
        
        else if((flightsUnallocated.get(i).getFlight().getFlightNumber()) != (flightsUnallocated.get(count).getFlight().getFlightNumber())) {
        	indent = indent + "\t";
            routePos.add(i);
            countCounter = countCounter + 1;
            match = true;
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Dep port : " + flightsUnallocated.get(count).getFlight().getDepartureAirportCode() );
            System.out.println("Departure time : " + flightsUnallocated.get(count).getDepartureDateTime());
            System.out.println("Arrival port : " + flightsUnallocated.get(count).getFlight().getArrivalAirportCode());
            System.out.println("Arrival time : " + flightsUnallocated.get(count).getLandingDateTime());
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Dep port : " + flightsUnallocated.get(i).getFlight().getDepartureAirportCode() );
            System.out.println("Departure time : " + flightsUnallocated.get(i).getDepartureDateTime());
            System.out.println("Arrival port : " + flightsUnallocated.get(i).getFlight().getArrivalAirportCode());
            System.out.println("Arrival time : " + flightsUnallocated.get(i).getLandingDateTime());
            break;
        }

      }

      //Check to see if we found a match
      //Plane and crew assignment 
      //else raise and error
      if (match) {
    	
        //end of list, no match found
        //check if arrival port is same as original airport

        indent = "";
        count = 0;
        //got to end of flights, lets allocate stuff
        int Max = 0;
        for (int j = 0; j < routePos.size(); j++) {

            
          //maximum number of required passengers per in bound / out bound
          //System.out.println("route pos is  = " + routePos.get(j));
        	//find biggest seat for the two 
         
          int reqPerHop = passengerNumbersDAO.getPassengerNumbersFor(flights.get(routePos.get(j)).getFlight().getFlightNumber(), 
        		  flights.get(routePos.get(j)).getDepartureDateTime().toLocalDate());
          if (reqPerHop > Max) {
            Max = reqPerHop;
          }
          
          
        }
          //how many passengers needed? plane to match! 
          //logic figure out most efficient size of plane for a trip
          //Get rid of any planes with conflicts
          
          //find aircraft by start pos
          List<Aircraft> useable = new ArrayList<>();

          List <Aircraft> planes = new ArrayList<>();  // = aircraftDAO.findAircraftBySeats(Max);

          if(useable.size() == 0) {
    		  planes = aircraftDAO.findAircraftBySeats(Max);
    		//  System.out.println("GETTING PLANES FROM MAX");
    		  
    		  for (int q = 0; q < planes.size(); q++) {  
              	int myCounter = 0; 
              	
  	                for (int i = 0; i < routePos.size(); i++) {
  	                  
  	                  if (newSchedule.hasConflict(planes.get(q), flightsUnallocated.get(routePos.get(i)))) {
  	                	  
  	                //	System.out.println("we caught a conflict! ------ " + planes.get(q).getTailCode());
  	                	myCounter = myCounter + 1;
  	                  }
  	                  
  	                  if(myCounter == 0) {
  	                  	useable.add(planes.get(q)); 
  	                  }
  	
  	                }
  	                
                }
    	  }
          
          System.out.println(planes.size());

          int n = useable.size(); 
          Aircraft temp;
          
          for(int w=0; w<n; w++) {
        	  for(int m=1; m<(n-w); m++) {
        		  if(useable.get(m-1).getSeats() < useable.get(m).getSeats()){
        			  temp = useable.get(m-1); 
        			  useable.get(m-1).equals(useable.get(m)); 
        			  useable.get(m).equals(temp); 
        			  
        		  }
        	  }
          }

          //Loop through our two inbound/outbound pair
          for (int o = 0; o < routePos.size(); o++) {

             
            //System.out.println("--- ASSIGNED TO : " + newSchedule.getCompletedAllocationsFor(plane));
            //if (!newSchedule.hasConflict(plane, flightsUnallocated.get(routePos.get(o)))) {
            for (int l = 0; l < useable.size(); l++) {
            	System.out.println("L POS IS" + l);
                try {
                  newSchedule.allocateAircraftTo(useable.get(l), flightsUnallocated.get(routePos.get(o)));
                  /*
                  System.out.println("PLANE ASSIGNED --  " + flightsUnallocated.get(routePos.get(o)).getDepartureDateTime()  + " " + flightsUnallocated.get(routePos.get(o))
                  .getLandingDateTime());
                  */
                  break; 
                }
                catch(DoubleBookedException dbe) {
               //   dbe.printStackTrace();
                }
              
              }

            //  && (crewDAO.getAllCabinCrew().get(k).
    //		isQualifiedFor(newSchedule.getAircraftFor(flightsUnallocated.get(routePos.get(o))).getTypeCode()))) {
            int counter = 0;
            int k = 0;
            
            //find available crew
           
        	while ((counter < newSchedule.getAircraftFor(flightsUnallocated.get(routePos.get(o))).getCabinCrewRequired()) && (k <= crewDAO.getAllCabinCrew().size())) {
        		//System.out.println("K IS " + k + " CABIN CREW SIZE IS " + crewDAO.getAllCabinCrew().size() + " Looking for " + 
        			//	newSchedule.getAircraftFor(flightsUnallocated.get(routePos.get(o))).getCabinCrewRequired() + "Crew found " + counter);
	                if (!newSchedule.hasConflict(crewDAO.getAllCabinCrew().get(k), flightsUnallocated.get(routePos.get(o)))){
	                	
	                  try {
	                	  
	                    newSchedule.allocateCabinCrewTo(crewDAO.getAllCabinCrew().get(k), flightsUnallocated.get(routePos.get(o)));
	                    crewDAO.getAllCabinCrew().remove(crewDAO.getAllCabinCrew().get(k));
	                    //System.out.println("CREW PERSON ---" + k + " HAS BEEN PUT ONTO A FLIGHT " + flightsUnallocated.get(routePos.get(o)).getFlight().getFlightNumber()); 
	                    counter = counter + 1;
	                  }
	                  catch(DoubleBookedException dbe) {
	                   // dbe.printStackTrace();
	                  //  System.out.println("\t DOUBLE BOOKED EXCEPTION");
	                  }
	                }
	
	                else {
	                  //System.out.println(" .. CABIN CREW ASSIGNMENT ERROR   .. --------------------------------------------------");
	                }
	                
	                k++;
	           
        	}
            
            
        	//find available and qualified pilots
            for (int x = 0; x < crewDAO.getAllPilots().size(); x++) {
              if (!newSchedule.hasConflict(crewDAO.getAllPilots().get(x), flightsUnallocated.get(routePos.get(o)))) {
                if (crewDAO.getAllPilots().get(x).getRank() == Rank.FIRST_OFFICER) {
                  try {
                    newSchedule.allocateFirstOfficerTo(crewDAO.getAllPilots().get(x), flightsUnallocated.get(routePos.get(o)));
                 //   System.out.println(" FIRST OFFICER ASSIGNED " + crewDAO.getAllPilots().get(x).getForename());
                    break;
                  }
                  catch(DoubleBookedException dbe) {
               //     System.out.println("DOUBLE BOOKED THIS BLOKE");
                  }

                }
                else {
             //     System.out.println(" FIRST OFFICER : " + crewDAO.getAllPilots().get(x).getForename() + "  IS NOT QUALIFIED!");
                }

              }
            }

            for (int x = 0; x < crewDAO.getAllPilots().size(); x++) {
            	
              if (!newSchedule.hasConflict(crewDAO.getAllPilots().get(x), flightsUnallocated.get(routePos.get(o)))) {
                if (crewDAO.getAllPilots().get(x).getRank() == Rank.CAPTAIN) {
                  try {
                    newSchedule.allocateCaptainTo(crewDAO.getAllPilots().get(x), flightsUnallocated.get(routePos.get(o)));
              //      System.out.println(" CAPTAIN ASSIGNED " + crewDAO.getAllPilots().get(x).getForename());
                    break;
                  }
                  catch(DoubleBookedException dbe) {
             //       System.out.println("DOUBLE BOOKED THIS BLOKE");
                  }

                }
                else {
           //       System.out.println(" CAPTAIN : " + crewDAO.getAllPilots().get(x).getForename() + "  IS NOT QUALIFIED!");
                }

              }
            }

            //complete flight path, should be a pair

            if (newSchedule.isValid(flightsUnallocated.get(routePos.get(o)))) {
              try {
               // System.out.println("Completing ...  " + newSchedule.getCompletedAllocations().size() + "... PLANE NUMBER >>> ");
                newSchedule.completeAllocationFor(flightsUnallocated.get(routePos.get(o)));
              //  System.out.println("SUCCESS! " + flightsUnallocated.get(routePos.get(o)).getFlight().getFlightNumber() + "SCHEDULED");
              }
              catch(InvalidAllocationException iae) {
               // iae.printStackTrace();

              }
            }
            else {
       //       System.out.println(" -- FALSE, IS NOT VALID --");
            }
        //    System.out.println("REMOVING 1");

          }
          //planes.remove(0); 
          System.out.println("end of if match ");
         
          
          System.out.println("flights completed  " + newSchedule.getCompletedAllocations().size());
          System.out.println("flights remaining " + newSchedule.getRemainingAllocations().size());
          System.out.println("end of master allocation 3");
          
          useable.clear();
          routePos.clear();
          //end of if match
          if (newSchedule.getCompletedAllocations().size() < time) {
               
            }
          
          else {
        	  System.out.println(" END OF WHILE LOOP ");
              masterAllocation = false;
              match = false;
          }

        }
      else {
    	  newSchedule.unAllocate(newSchedule.getCompletedAllocations().get(0));
    	  System.out.println("UNALLOCATING======================/////////////////////////////////////////////");
      }
        
      }

    //end of while loop
    
    return newSchedule;
  }

}