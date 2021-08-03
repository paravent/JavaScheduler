package solution;
import java.awt.List;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import baseclasses.DataLoadingException;
import baseclasses.IPassengerNumbersDAO;

/**
 * The PassengerNumbersDAO is responsible for loading an SQLite database
 * containing forecasts of passenger numbers for flights on dates
 */
public class PassengerNumbersDAO implements IPassengerNumbersDAO {

	 
	HashMap<HashMap<LocalDate, Integer>, Integer > mapSibling = new HashMap<>();
	HashMap<Integer, Integer> testFN = new HashMap<>(); 
	HashMap<Integer, Integer> testLoadEst = new HashMap<>();
	HashMap<Integer, LocalDate> testDate = new HashMap<>();
	
	/**
	 * Returns the number of passenger number entries in the cache
	 * @return the number of passenger number entries in the cache
	 */ 
	@Override
	public int getNumberOfEntries() { 
		// TODO Auto-generated method stub
		return mapSibling.size(); 
	}

	/**
	 * Returns the predicted number of passengers for a given flight on a given date, or -1 if no data available
	 * @param flightNumber The flight number of the flight to check for
	 * @param date the date of the flight to check for
	 * @return the predicted number of passengers, or -1 if no data available
	 */
	@Override
	public int getPassengerNumbersFor(int flightNumber, LocalDate date) {
	
		for(Entry<Integer, Integer> entry : testFN.entrySet()){
			if(entry.getValue().equals(flightNumber)) {
				
			}
		}
		
		for(Entry<Integer, LocalDate> entry2 : testDate.entrySet()) {
			if(entry2.getValue().equals(date)) {
				
				int key = entry2.getKey(); 
				int loadEstimation = testLoadEst.get(key);
				
				return loadEstimation; 
			}
		}	
		
		return -1; 

	}

	/**
	 * Loads the passenger numbers data from the specified SQLite database into a cache for future calls to getPassengerNumbersFor()
	 * Multiple calls to this method are additive, but flight numbers/dates previously cached will be overwritten
	 * The cache can be reset by calling reset() 
	 * @param p The path of the SQLite database to load data from
	 * @throws DataLoadingException If there is a problem loading from the database
	 */
	@Override
	public void loadPassengerNumbersData(Path p) throws DataLoadingException {
		// TODO Auto-generated method stub
		Connection c = null; 
		reset(); 
		try {
			//connect to database
			c = DriverManager.getConnection("jdbc:sqlite:" + p.toString()); 
			 
			//run query
			Statement s = c.createStatement(); 
			ResultSet rs = s.executeQuery("SELECT * FROM PassengerNumbers ORDER BY Date ASC"); 
			
			//get results
			while(rs.next()) {
				int size = rs.getRow(); 
				LocalDate Dates = LocalDate.parse(rs.getString("Date")); 
				 
				int loadEstimate = rs.getInt("LoadEstimate");
				
				int FN = rs.getInt("FlightNumber"); 
				HashMap<LocalDate,Integer> mapParent = new HashMap<LocalDate, Integer>();
				 
				testFN.put(size, FN); 
				testLoadEst.put(size, loadEstimate); 
				testDate.put(size, Dates); 
				
				mapParent.put(Dates, FN );
				mapSibling.put(mapParent, loadEstimate);
				
				
				
			}
			//System.out.println(mapSibling.toString());
		}
		catch(SQLException se){
			throw new DataLoadingException(se);
			
	}
		
	}

	/**
	 * Removes all data from the DAO, ready to start again if needed
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		mapSibling.clear();
		testFN.clear();
		testLoadEst.clear();
		testDate.clear();
	}

}
