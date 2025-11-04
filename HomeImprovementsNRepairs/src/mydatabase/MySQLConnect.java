package mydatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JOptionPane;

import main_screen.HomeData;




public class MySQLConnect {


	private String jdbcUrl = null; 
	private String userid = null; 
	private String password = null; 
	final private String CLASSNAME = "MYSQLConnect";
	//private final String lockerDatabase = "lockerassignment_roseville";//database that stores current locker information
	private final String HOMEIMPROVEMENT_DATABASE = "houseexpenses";//database that stores past locker information


	static List<HomeData> list = new ArrayList<>();

	public List<HomeData> getList(){
		return list;
	}

	public void clearList() {
		list.clear();
	}

	public HomeData lockerInfo  = null;
	public HomeData getLockerInfo() {
		return lockerInfo;
	}


	public MySQLConnect(String jdbcUrl, String userid, String password){
		//System.out.println("Credentials = " + jdbcUrl +", "+ userid + ", " + password);
		this.jdbcUrl = jdbcUrl;
		this.userid = userid;
		this.password = password;
		System.out.println(jdbcUrl);
		System.out.println(userid);
		System.out.println(password);

	}

	
	
	
	public Boolean isConnected()  {
		//Open a connection
		Connection conn = null;
		//System.out.println("Connecting to a selected database...");
		try {
			conn = DriverManager.getConnection(jdbcUrl, userid, password);
			//conn = DriverManager.getConnection(url,userid, password);
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, CLASSNAME + " 1: Unable to connect to database. Try Again, "); 
			//JOptionPane.showMessageDialog(null, "ERROR: Unable to connect to database. Try Again");
		} 
			

		return false;

	} 


	public void placeItemInPastDatabase(HomeData item) {
		Connection conn = null;
		try {
			if(isConnected()) {

				String query = "INSERT INTO " + HOMEIMPROVEMENT_DATABASE + " (DATE, AREA, ITEMS, COST FILENAME, INFO, VALUE"  +
						" )VALUES(" +  item.getDate() +"," + item.getArea() + ", '" + item.getItem() + "'," + item.getCost()+",'" +
						item.getReceiptFilename()+ "', " + "'" +item.getInfo() + "', '" + item.getIsValue()  +"' )";				

				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				myStatement = conn.createStatement();
				myStatement.executeUpdate(query);
				//conn.close();	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "ERROR: Unable to close connection to database. Try Again");
			}
		}
	}


	public void removeEmployeeFromLockerDatabase1(String query) {
		Connection conn = null;
		try {
			if(isConnected()) {

				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				myStatement = conn.createStatement();
				myStatement.executeUpdate(query);
				//conn.close();	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, "ERROR: Unable to close connection to database. Try Again");
			}
		}
	}

	public void deleteDatabaseDate() {
		
		
	}
	
	
	public boolean removeEmployeeFromLockerDatabase(String query) {
		Connection conn = null;
		Boolean isEmployeeRemoved = false;
		try {
			if(isConnected()) {
				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				myStatement = conn.createStatement();
				myStatement.executeUpdate(query);
				isEmployeeRemoved = true;
				//conn.close();	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ERROR: Employee Not Removed from Database. Try Again");

		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "ERROR: Unable to close connection to database. Try Again");
			}
		}
		return isEmployeeRemoved;
	}

	public Boolean insertData(String query){
		//boolean isAddSuccess = false;
		
	    Boolean isInsertSuccessful = false;
		Connection conn = null;
		try {
			if(isConnected()) {
				System.out.println(query);	
				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				myStatement.executeUpdate(query);
				isInsertSuccessful = true;
				//conn.close();
				//isAddSuccess = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isInsertSuccessful;
	}


	public void updateDatabase(String result) {

		System.out.println(result);
		Connection conn = null;
		try {
			if(isConnected()) {
				String query = result;
				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				myStatement = conn.createStatement();
				myStatement.executeUpdate(query);
				//conn.close();	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void UpdateComboAndSerialNumber(String query) {
		Connection conn = null;
		try {
			if(isConnected()) {
				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				myStatement = conn.createStatement();
				myStatement.executeUpdate(query);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean refreshDatabase(List<HomeData> item) {
		Connection conn = null;
		boolean isDatabaseUpdated = false;
		try {
			if(isConnected()) {
				conn = DriverManager.getConnection(jdbcUrl, userid, password);
				Statement myStatement = conn.createStatement();
				String truncateQuery = "TRUNCATE TABLE " + HOMEIMPROVEMENT_DATABASE;
				myStatement.executeUpdate(truncateQuery);

				for( int i = 0; i < item.size(); i++) {
					String  query = "INSERT INTO " + HOMEIMPROVEMENT_DATABASE +
							" (DATE, AREA, ITEMS, COST, FILENAME, INFO, VALUE" + " )VALUES('" +
							item.get(i).getDate() +"','" + item.get(i).getArea() + "', '" +
							item.get(i).getItem() + "', " + item.get(i).getCost()+",'" +
							item.get(i).getReceiptFilename()+ "', '" +item.get(i).getInfo() + "', '" +
							item.get(i).getIsValue() +"' );";
					myStatement.execute(query);
				}

			}
			isDatabaseUpdated = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return isDatabaseUpdated;


	}


	public void getQueryResultsFromDatabase( String columnName,String columnValueString, int columnValueInt ) {
		//System.out.println("-------- Crunchify's tutorial on Oracle JDBC Connectivity  ------");
		final String COMMA = ", "; 
		list.clear();
		//LockerInfo data = null;
		ExecutorService executor =  Executors.newCachedThreadPool();
		Future<HomeData>  myFuture= executor.submit(new Callable<HomeData>(){

			@Override
			public HomeData call() throws Exception {
				// TODO Auto-generated method stub
				HomeData data = null;
				if(isConnected()) {
					//System.out.println("-------- Connectivity to Oracle Database ------");
					// Executes the given SQL statement, which returns a single ResultSet object
					Connection conn = null;
					try {
						//LockerInfo data= null;
						conn = DriverManager.getConnection(jdbcUrl, userid, password);
						Statement myStatement = conn.createStatement();
						ResultSet rset;

						if(columnValueString == null) {

							rset = myStatement.executeQuery("SELECT * FROM " + HOMEIMPROVEMENT_DATABASE +  " WHERE " + columnName + " = "  + columnValueInt );
						}
						else {
							rset = myStatement.executeQuery("SELECT * FROM " +  HOMEIMPROVEMENT_DATABASE+ " WHERE " + columnName + " = "  + "'" +  columnValueString + "'");;
						}

						while (rset.next()) {
							String stringOutput = "";
							stringOutput = rset.getString("DATE") + COMMA +
									rset.getString("AREA") + COMMA +
									rset.getString("ITEM") + COMMA +
									Double.toString(rset.getDouble("COST")) + COMMA +
									rset.getString("FILENAME") + COMMA +
									rset.getString("INFO") + COMMA +
									rset.getBoolean("VALUE") + COMMA ;
							//System.out.println(stringOutput);
							data = new HomeData(stringOutput);
						}//end while
						rset.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}finally {
						conn.close();
					}
				}
				return data;
			}
		});
		executor.shutdown();
		try {
			lockerInfo = myFuture.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void readTableRows() {
		final String COMMA = ", ";

		ExecutorService executor =  Executors.newCachedThreadPool();
		Future<List<HomeData>> myFuture = executor.submit(new Callable<List<HomeData>>() {

			@Override
			public List<HomeData> call() throws Exception {
				// TODO Auto-generated method stub
				list.clear();
				List<HomeData> dataArray = new ArrayList<>();

				if(isConnected()) {

					Connection conn = DriverManager.getConnection(jdbcUrl, userid, password);
					Statement stmt = conn.createStatement();
					String  query = "SELECT * FROM " + HOMEIMPROVEMENT_DATABASE;
					ResultSet rset = stmt.executeQuery(query); 
					while(rset.next()) {
						String stringOutput = "";
						stringOutput = rset.getString("DATE") + COMMA +
								rset.getString("AREA") + COMMA +
								rset.getString("ITEM") + COMMA +
								Double.toString(rset.getDouble("COST")) + COMMA +
								rset.getString("FILENAME") + COMMA +
								rset.getString("INFO") + COMMA +
								rset.getBoolean("VALUE") + COMMA ;

						dataArray.add(new HomeData(stringOutput));	  
						//System.out.println(stringOutput);
					}//end while
					rset.close();
					conn.close();
				}//end isConnected			 	 
				return dataArray;
			}	
		});

		executor.shutdown();
		try {
			list = myFuture.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void getDateRangeResults(final String query) {

		final String COMMA = ", ";
		ExecutorService executor =  Executors.newCachedThreadPool();
		Future<List<HomeData>>  myFuture= executor.submit(new Callable<List<HomeData>>() {

			@Override
			public List<HomeData> call() throws Exception {
				// TODO Auto-generated method stub
				list.clear();
				List<HomeData> dataArray = new ArrayList<>();
				if(isConnected()) {

					Connection conn = DriverManager.getConnection(jdbcUrl, userid, password);
					Statement stmt = conn.createStatement();	
					//String query = "SELECT * FROM " + lockerDatabase + " WHERE LOCKER_NUMBER >= " + minLocker + " AND LOCKER_NUMBER <= " + maxLocker; 
					//System.out.println(query);
					ResultSet rset = stmt.executeQuery(query);
					while(rset.next()) {
						String stringOutput = "";

						stringOutput = rset.getString("DATE") + COMMA +
								rset.getString("AREA") + COMMA +
								rset.getString("ITEMS") + COMMA +
								Double.toString(rset.getDouble("COST")) + COMMA +
								rset.getString("FILENAME") + COMMA +
								rset.getString("INFO") + COMMA +
								rset.getBoolean("VALUE") + COMMA ;

						dataArray.add(new HomeData(stringOutput));	  
						// System.out.println(stringOutput);
					}//end while
					rset.close();
					conn.close();
				} //end if connected   
				//PayrollPanel.sortListByDate(dataArray);
				return dataArray;
			}});	
		executor.shutdown();
		try {
			list = myFuture.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void getSingleQuery(String query ) {


		final String COMMA = ",";

		ExecutorService executor =  Executors.newCachedThreadPool();
		Future<HomeData> myFuture = executor.submit(new Callable<HomeData>() {

			@Override
			public HomeData call() throws Exception {
				// TODO Auto-generated method stub
				list.clear();
				HomeData dataArray = null;

				if(isConnected()) {

					Connection conn = DriverManager.getConnection(jdbcUrl, userid, password);
					Statement stmt = conn.createStatement();
					//String  query = "SELECT * FROM " + lockerDatabase;
					ResultSet rset = stmt.executeQuery(query); 
					while(rset.next()) {
						String stringOutput = "";

						stringOutput = rset.getString("DATE") + COMMA +
								rset.getString("AREA") + COMMA +
								rset.getString("ITEM") + COMMA +
								Double.toString(rset.getDouble("COST")) + COMMA +
								rset.getString("FILENAME") + COMMA +
								rset.getString("INFO") + COMMA +
								rset.getBoolean("VALUE") + COMMA ;
						dataArray = new HomeData(stringOutput);	  
						//System.out.println(stringOutput);
					}
					rset.close();
					conn.close();
				}//end isConnected			 	 
				return dataArray;
			}	
		});

		executor.shutdown();
		try {
			lockerInfo = myFuture.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	///////////////////////////////////////////////////////////Query Panel Queries/////////////////////////////////////////////////////////


	public void getQuery(final String query ) {


		final String COMMA = ", ";

		ExecutorService executor =  Executors.newCachedThreadPool();
		Future<List<HomeData>> myFuture = executor.submit(new Callable<List<HomeData>>() {

			@Override
			public List<HomeData> call() throws Exception {
				// TODO Auto-generated method stub
				list.clear();
				List<HomeData> dataArray = new ArrayList<>();

				if(isConnected()) {

					Connection conn = DriverManager.getConnection(jdbcUrl, userid, password);
					Statement stmt = conn.createStatement();
					ResultSet rset = stmt.executeQuery(query); 
					while(rset.next()) {
						String stringOutput = "";
						stringOutput = rset.getString("DATE") + COMMA +
								rset.getString("AREA") + COMMA +
								rset.getString("ITEMS") + COMMA +
								Double.toString(rset.getDouble("COST")) + COMMA +
								rset.getString("FILENAME") + COMMA +
								rset.getString("INFO") + COMMA +
								rset.getBoolean("VALUE") + COMMA ;
						dataArray.add(new HomeData(stringOutput));	  
						//System.out.println(stringOutput);
					}//end while
					rset.close();
					conn.close();
				}//end isConnected			 	 
				return dataArray;
			}	
		});

		executor.shutdown();
		try {
			list = myFuture.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public void getQueryName(String column, String name) {



	}

	public static void main(String[] args) {

		/*
		// TODO Auto-generated method stub
		 String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/penlocker_database";
		  String userid = "rbrewer"; 
		  String password = "Great2BeAliveN2022#"; 
		 final String DATABASE_NAME = "lockerassignment_roseville";



		  MySQLConnect myDatabase = new MySQLConnect(jdbcUrl, userid, password);

		  if(myDatabase.isConnected()) {
			 List<HomeData> list = null;
			 String query = "SELECT * FROM " + DATABASE_NAME + " WHERE LOCKER_NUMBER >= 100 AND LOCKER_NUMBER <= 200 ";
			 myDatabase.getLockerRangeResults(query);
			 list = myDatabase.getList();
			 System.out.println(	list.size());
			 for(int i = 0; i < list.size(); i++) {
				 System.out.println(list.get(i).toString());
			 }
			 list.clear();

			System.out.println("-------------------------readTableRows()------------------------------------");
			myDatabase.readTableRows();
			list = myDatabase.getList();
			System.out.println(	list.size());
			list.clear();


			System.out.println("-------------------------getQueryResultsFromDatabase()------------------------------------");
			myDatabase.getQueryResultsFromDatabase("LAST_NAME","Tam", 0);
			HomeData myObject = myDatabase.getLockerInfo();
			if(myObject== null) {
				System.out.println("NO MATCH FOUND");
			}else {
				System.out.println(myObject.toString());
			}

		 }
		 */
	}


	private boolean isSignonCredentCorrect(String name, char[] pword) {

		final String credentialsFilename = "mysqlsignonstuff.txt" ;
		final String DELIMITER= "#";
		String myDatastuff[] = getCredentialsFromFile(credentialsFilename).split(DELIMITER);

		if(name.equals(myDatastuff[1]) && String.valueOf(pword).equals(myDatastuff[4])) {	
			return  true;
		}else {
			return false;
		}
	}



	/*********************************************************************************
	 * getCredentialFromFile() retrieves the sign-on credentials from a file
	 * 
	 * @pre String inputFile: filename storing credentials
	 * @parameter 
	 * @post String: sign-on credential
	 **********************************************************************************/
	private String getCredentialsFromFile(String inputFile) {

		final int myMagicNumber = 36;
		String allData = null;
		int count = 0;
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(inputFile));
			String data;
			try {
				while ((data = bufferedReader.readLine()) != null) {	
					if(count == myMagicNumber) {
						allData = data;
					}
					count++;
				}
				if(count < myMagicNumber) { //file does not contain sign-on credential info
					JOptionPane.showMessageDialog(null, "Credentials can not be found."); 
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Can not read  from file ");
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Can not find credential file ");
			e.printStackTrace();

		}finally {
			try {
				bufferedReader.close();
				//return allData;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,"Error Closing The File"+e );
				e.printStackTrace();
			}
		}

		return allData;
	}	



}
