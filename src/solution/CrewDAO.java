package solution;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import baseclasses.Aircraft;
import baseclasses.CabinCrew;
import baseclasses.Crew;
import baseclasses.DataLoadingException;
import baseclasses.ICrewDAO;
import baseclasses.Pilot;
import baseclasses.Pilot.Rank;

/**
 * The CrewDAO is responsible for loading data from JSON-based crew files 
 * It contains various methods to help the scheduler find the right pilots and cabin crew
 */
public class CrewDAO implements ICrewDAO {

	
	List<Pilot> pilots = new ArrayList<>();
	List<CabinCrew> cabinCrew = new ArrayList<>(); 
	
	/**
	 * Loads the crew data from the specified file, adding them to the currently loaded crew
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadCrewData(Path p) throws DataLoadingException {
		// TODO Auto-generated method stub
		try {
			
			BufferedReader br = Files.newBufferedReader((p));
			
			
			String json = ""; 
			String line = ""; 
			while((line = br.readLine()) != null) {
				json = json + line; 
			}
			
			//JSONarray used because root element is a '[' 
			String arrayUtil = ""; 
			String otherArrayUtil = ""; 
			JSONObject root = new JSONObject(json); 
			
			
			
			//create JSON array of pilots
			JSONArray pilotList =  root.getJSONArray("pilots"); 
			JSONArray cabinCrewList = root.getJSONArray("cabincrew");
			
			for(int i=0; i<pilotList.length(); i++) {
				Pilot myPilot = new Pilot();
				JSONObject pilot = pilotList.getJSONObject(i);
				if(pilot.length() != 5) {
					throw new DataLoadingException(); 
				}
				
				myPilot.setForename(pilot.getString("forename")); 
				myPilot.setSurname(pilot.getString("surname"));
				myPilot.setHomeBase(pilot.getString("home_airport"));
				
				if(pilot.getString("rank") != null){
					myPilot.setRank(Rank.valueOf(pilot.getString("rank"))); 
				}
				
				
				
				JSONArray typeRatings = pilot.getJSONArray("type_ratings"); 
				
				for(int k=0; k<typeRatings.length(); k++) {
					myPilot.setQualifiedFor(typeRatings.get(k).toString());
				}
				  
				pilots.add(myPilot);
				
				
			}
			
			for(int i=0; i<cabinCrewList.length(); i++) {
				
				CabinCrew myCabinCrew = new CabinCrew();
				JSONObject cabin = cabinCrewList.getJSONObject(i);
				if(cabin.length() != 4) {
					throw new DataLoadingException(); 
				}
				myCabinCrew.setForename(cabin.getString("forename")); 
				myCabinCrew.setSurname(cabin.getString("surname"));
				myCabinCrew.setHomeBase(cabin.getString("home_airport"));
				
				JSONArray typeRatings = cabin.getJSONArray("type_ratings"); 
				
				for(int k=0; k<typeRatings.length(); k++) {
					myCabinCrew.setQualifiedFor(typeRatings.get(k).toString());
				}
				  
				cabinCrew.add(myCabinCrew);
				 
				
			}

		} catch (IOException e) {
			throw new DataLoadingException(e); 
		}
		
		
	}
	
	/**
	 * Returns a list of all the cabin crew based at the airport with the specified airport code
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at the airport with the specified airport code
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBase(String airportCode) {
		// TODO Auto-generated method stub
		List<CabinCrew> homeBase = new ArrayList<>(); 
		
		for(int i=0; i<cabinCrew.size(); i++) {
			if(cabinCrew.get(i).getHomeBase().equals(airportCode)) {
				
				homeBase.add(cabinCrew.get(i)); 
			}
		}
		
		return homeBase; 
	}

	/**
	 * Returns a list of all the cabin crew based at a specific airport AND qualified to fly a specific aircraft type
	 * @param typeCode the type of plane to find cabin crew for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at a specific airport AND qualified to fly a specific aircraft type
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		// TODO Auto-generated method stub
		List<CabinCrew> homeBaseAndType = new ArrayList<>(); 
		
		for(int i=0; i<cabinCrew.size(); i++) {
			if(cabinCrew.get(i).getHomeBase().equals(airportCode) && cabinCrew.get(i).getTypeRatings().contains(typeCode)) {
				
				homeBaseAndType.add(cabinCrew.get(i)); 
			}
		}
		
		return homeBaseAndType;
	}

	/**
	 * Returns a list of all the cabin crew currently loaded who are qualified to fly the specified type of plane
	 * @param typeCode the type of plane to find cabin crew for
	 * @return a list of all the cabin crew currently loaded who are qualified to fly the specified type of plane
	 */
	@Override
	public List<CabinCrew> findCabinCrewByTypeRating(String typeCode) {
		// TODO Auto-generated method stub
		List<CabinCrew> typeRating = new ArrayList<>(); 
		
		for(int i=0; i<cabinCrew.size(); i++) {
			if(cabinCrew.get(i).getTypeRatings().contains(typeCode)) {
				
				typeRating.add(cabinCrew.get(i)); 
			}
		}
		
		return typeRating;
	}

	/**
	 * Returns a list of all the pilots based at the airport with the specified airport code
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at the airport with the specified airport code
	 */
	@Override
	public List<Pilot> findPilotsByHomeBase(String airportCode) {
		// TODO Auto-generated method stub
		List<Pilot> homeBase = new ArrayList<>(); 
		
		for(int i=0; i<pilots.size(); i++) {
			if(pilots.get(i).getHomeBase().equals(airportCode)) {
				
				homeBase.add(pilots.get(i)); 
			}
		}
		
		return homeBase; 
	}

	/**
	 * Returns a list of all the pilots based at a specific airport AND qualified to fly a specific aircraft type
	 * @param typeCode the type of plane to find pilots for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at a specific airport AND qualified to fly a specific aircraft type
	 */
	@Override
	public List<Pilot> findPilotsByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		// TODO Auto-generated method stub
		List<Pilot> homeBaseAndType = new ArrayList<>(); 
		
		for(int i=0; i<pilots.size(); i++) {
			if(pilots.get(i).getHomeBase().equals(airportCode) && pilots.get(i).getTypeRatings().contains(typeCode)) {
				
				homeBaseAndType.add(pilots.get(i)); 
			}
		}
		
		return homeBaseAndType;
	}

	/**
	 * Returns a list of all the pilots currently loaded who are qualified to fly the specified type of plane
	 * @param typeCode the type of plane to find pilots for
	 * @return a list of all the pilots currently loaded who are qualified to fly the specified type of plane
	 */
	@Override
	public List<Pilot> findPilotsByTypeRating(String typeCode) {
		// TODO Auto-generated method stub
		List<Pilot> typeRating = new ArrayList<>(); 
		
		for(int i=0; i<pilots.size(); i++) {
			if(pilots.get(i).getTypeRatings().contains(typeCode)) {
				
				typeRating.add(pilots.get(i)); 
			}
		}
		
		return typeRating; 
	}

	/**
	 * Returns a list of all the cabin crew currently loaded
	 * @return a list of all the cabin crew currently loaded
	 */
	@Override
	public List<CabinCrew> getAllCabinCrew() {
		// TODO Auto-generated method stub
		List<CabinCrew> myCabinCrew = new ArrayList<>(); 
		
		for(int i=0; i<cabinCrew.size(); i++) {
			myCabinCrew.add(cabinCrew.get(i)); 
			
		}
		return myCabinCrew;
	}

	/**
	 * Returns a list of all the crew, regardless of type
	 * @return a list of all the crew, regardless of type
	 */
	@Override
	public List<Crew> getAllCrew() {
		// TODO Auto-generated method stub
		List<Crew> myCrew = new ArrayList<>(); 
		
		for(int i=0; i<cabinCrew.size(); i++) {
			myCrew.add(cabinCrew.get(i)); 
			
			
		}
		for(int x=0; x<pilots.size(); x++) {
			myCrew.add(pilots.get(x)); 
		}
		return myCrew; 
	}

	/**
	 * Returns a list of all the pilots currently loaded
	 * @return a list of all the pilots currently loaded
	 */
	@Override
	public List<Pilot> getAllPilots() {
		// TODO Auto-generated method stub
		List<Pilot> myPilots = new ArrayList<>(); 
		
		for(int i=0; i<pilots.size(); i++) {
			myPilots.add(pilots.get(i)); 
			
		}
		return myPilots;
	}

	@Override 
	public int getNumberOfCabinCrew() {
		// TODO Auto-generated method stub
		return cabinCrew.size(); 
	}

	/**
	 * Returns the number of pilots currently loaded
	 * @return the number of pilots currently loaded
	 */
	@Override
	public int getNumberOfPilots() {
		// TODO Auto-generated method stub
		return pilots.size(); 
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		pilots.removeAll(pilots) ; 
		cabinCrew.removeAll(cabinCrew); 
		
	}

}
