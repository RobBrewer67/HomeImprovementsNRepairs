/*      */ package main_screen;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.time.LocalDate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;

/*      */ import javax.sound.sampled.AudioInputStream;
/*      */ import javax.sound.sampled.AudioSystem;
/*      */ import javax.sound.sampled.Clip;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JOptionPane;

import org.jfree.data.json.impl.JSONArray;
import org.jfree.data.json.impl.JSONObject;
import org.json.simple.parser.JSONParser;

import mydatabase.MySQLConnect;

/*      */ public class HomeMainGui
{
	/*      */   private static final long serialVersionUID = -8044048618173986424L;
	/*   74 */   private final String CLASSNAME = "HomeMainGui";
	/*      */   private static String username;
	/*      */   private JFrame frame;
	/*      */   private JMenuBar menuBar;
	/*      */   private JMenu myMenu;
	/*      */   private JMenuItem queryWork;
	/*      */   private JMenuItem logout;
	/*      */   private JMenuItem download;
	/*   82 */   private final String HOMEIMPROVEMENT_DATABASE = "houseexpenses";
	/*      */   
	/*      */   private JMenuItem upload;
	/*      */   
	/*      */   private JMenuItem about;
	/*      */   
	/*      */   private JMenuItem filePath;
	/*      */   
	/*      */   private JMenuItem reformat;
	/*      */   
	/*      */   private JMenuItem jsonToDatabase;
	/*      */   
	/*      */   private JMenuItem reconnectToDatabase;
	/*      */   
	/*      */   private JMenuItem currentMonth;
	/*      */   
	/*      */   public static Boolean getDatabaseStatus() {
		/*   99 */     return databaseStatus;
	/*      */   }
	/*      */   private MySQLConnect mySQLDatabase; 
	/*      */   private static final String inputFile = "cost.csv"; public static final int ROWS = 6;
	/*      */   public static final int COLS = 12;
	/*      */   private static boolean maxLimitFlag = false;
	/*      */   private static Boolean databaseStatus;
	/*      */   private JLabel lblDatabaseStatus;
	/*      */   private MyBackgroundPanel myBackgroundPanel;
	/*      */   
	/*      */   public HomeMainGui(String username, char[] password) {
		/*  109 */     HomeMainGui.username = username;
		/*  110 */     isSQLSignonCredentCorrect();
		/*  111 */     initialize();
		/*  112 */     if (databaseStatus) {
			/*  113 */       this.lblDatabaseStatus.setText("Database Connected");
			/*  114 */       this.lblDatabaseStatus.setForeground(Color.green);
		/*      */     } else {
			/*  116 */       this.lblDatabaseStatus.setText("Database Is Not Connected");
			/*  117 */       this.lblDatabaseStatus.setForeground(Color.red);
		/*      */     } 
	/*      */   }
	/*      */ 
	/*      */   
	/*      */   class MyMouseListener
	/*      */     extends MouseAdapter
	/*      */   {
		/*      */     public void mouseClicked(MouseEvent evt) {
			/*  126 */       if (evt.getClickCount() == 3) {
				/*  127 */         HomeMainGui.this.reformat.setVisible(false);
			/*  128 */       } else if (evt.getClickCount() == 2) {
				/*  129 */         HomeMainGui.this.reformat.setVisible(true);
				/*  130 */         System.out.println("double-click");
			/*      */       } 
		/*      */     }
	/*      */   }
	/*      */ 
	/*      */   
	/*      */   private void initialize() {
		/*  137 */     this.frame = new JFrame();
		/*  138 */     this.myBackgroundPanel = new MyBackgroundPanel(this.mySQLDatabase);
		/*  139 */     this.frame.setContentPane(this.myBackgroundPanel);
		/*      */ 
		/*      */     
		/*  142 */     this.menuBar = new JMenuBar();
		/*  143 */     this.menuBar.setLayout(new BorderLayout());
		/*  144 */     this.menuBar.setFont(new Font("Segoe UI", 1, 12));
		/*  145 */     this.menuBar.setBackground(Color.WHITE);
		/*      */ 
		/*      */ 
		/*      */     
		/*  149 */     this.myMenu = new JMenu("MENU");
		/*  150 */     this.myMenu.setMnemonic(0);
		/*  151 */     this.myMenu.getAccessibleContext().setAccessibleDescription("Get My Shit");
		/*  152 */     this.myMenu.setBackground(new Color(50, 205, 50));
		/*  153 */     this.menuBar.add(this.myMenu, "West");
		/*      */     
		/*  155 */     this.lblDatabaseStatus = new JLabel("NOT CONNECTED");
		/*  156 */     this.lblDatabaseStatus.setBounds(156, 16, 159, 15);
		/*  157 */     this.lblDatabaseStatus.setFont(new Font("Verdana", 1, 14));
		/*      */     
		/*  159 */     this.lblDatabaseStatus.setDisplayedMnemonic(65);
		/*      */     
		/*  161 */     this.menuBar.add(this.lblDatabaseStatus, "East");
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */     
		/*  167 */     this.download = new JMenuItem("Download From Server");
		/*  168 */     this.queryWork = new JMenuItem("Perform Queries");
		/*  169 */     this.currentMonth = new JMenuItem("Current Month's Expenses");
		/*  170 */     this.upload = new JMenuItem("Copy File Data To Database");
		/*  171 */     this.logout = new JMenuItem("Logout");
		/*  172 */     this.about = new JMenuItem("About");
		/*  173 */     this.reformat = new JMenuItem("Reformat CSV File");
		/*  174 */     this.reformat.setVisible(false);
		/*      */ 
		/*      */     
		/*  177 */     this.jsonToDatabase = new JMenuItem("Copy JSON Data To Database");
		/*  178 */     this.reconnectToDatabase = new JMenuItem("Reconnect To Database");
		/*      */ 
		/*      */     
		/*  181 */     this.myMenu.add(this.reformat);
		/*  182 */     this.myMenu.add(this.currentMonth);
		/*      */ 
		/*      */     
		/*  185 */     this.myMenu.add(this.jsonToDatabase);
		/*  186 */     this.myMenu.add(this.queryWork);
		/*  187 */     this.myMenu.add(this.upload);
		/*  188 */     this.myMenu.add(this.reconnectToDatabase);
		/*  189 */     this.myMenu.add(this.about);
		/*  190 */     this.myMenu.add(this.logout);
		/*  191 */     this.jsonToDatabase.setVisible(false);
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */     
		/*  197 */     this.myMenu.addMouseListener(new MouseAdapter()
				/*      */         {
			/*      */           public void mouseClicked(MouseEvent e) {
				/*  200 */             if (HomeMainGui.this.jsonToDatabase.isVisible()) {
					/*  201 */               HomeMainGui.this.jsonToDatabase.setVisible(false);
				/*      */             } else {
					/*  203 */               HomeMainGui.this.jsonToDatabase.setVisible(true);
				/*      */             } 
			/*      */           }
		/*      */         });
		/*      */     
		/*  208 */     this.jsonToDatabase.addActionListener(new ActionListener()
				/*      */         {
			/*      */ 
			/*      */           
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  214 */             if (!HomeMainGui.getDatabaseStatus().booleanValue()) {
					/*  215 */               String message = "Database Not Connected. JSON To Database Operation Not Performed";
					/*  216 */               JOptionPane.showMessageDialog(null, message, "Input Error", 0);
					/*      */               
					/*      */               return;
				/*      */             } 
				/*      */             
				/*  221 */             JFrame frame1 = new JFrame();
				/*  222 */             String theMessage = " Do You Want To UpDate The Database with JSON info? If so, all previous info in the database will be refreshed";
				/*  223 */             int result = JOptionPane.showConfirmDialog(frame1, theMessage, "alert", 0);
				/*  224 */             if (result == 0) {
					/*  225 */               if (HomeMainGui.this.populateDatabaseUsingJSONData("homeData.json").booleanValue()) {
						/*  226 */                 JOptionPane.showMessageDialog(null, "SUCCESS: JSON Data Downloaded to Database ");
					/*      */               } else {
						/*  228 */                 JOptionPane.showMessageDialog(null, "ERROR: JSON Data Did Not Downloaded to File ");
					/*      */               } 
				/*      */             }
			/*      */           }
		/*      */         });
		/*      */ 
		/*      */ 
		/*      */     
		/*  236 */     this.about.addActionListener(new ActionListener()
				/*      */         {
			/*      */           public void actionPerformed(ActionEvent e) {}
		/*      */         });
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */     
		/*  245 */     this.currentMonth.addActionListener(new ActionListener()
				/*      */         {
			/*      */ 
			/*      */           
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  251 */             if (HomeMainGui.databaseStatus.booleanValue()) {
					/*  252 */               HomeMainGui.this.getMonthlyExpensesFromDatabase(Boolean.valueOf(true));
				/*      */             } else {
					/*  254 */               HomeMainGui.this.getMonthlyExpensesFromFile(Boolean.valueOf(true));
				/*      */             } 
			/*      */           }
		/*      */         });
		/*      */     
		/*  259 */     this.download.addActionListener(new ActionListener()
				/*      */         {
			/*      */           
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  264 */             if (HomeMainGui.databaseStatus.booleanValue()) {
					/*  265 */               HomeMainGui.this.downloadFromServerToFile();
				/*      */             }
			/*      */           }
		/*      */         });

		/*  280 */     this.logout.addActionListener(new ActionListener()
				/*      */         {
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  284 */             if (!HomeMainGui.databaseStatus.booleanValue()) {
					/*  285 */               JOptionPane.showMessageDialog(null, "Database Not Connected. No Data Downlaoded to Google Drive. GoodBye");
					/*  286 */               System.exit(0);
				/*      */             } 
				/*      */             
				/*  289 */             JFrame frame1 = new JFrame();
				/*  290 */             String theMessage = " Do You Want To Quit The Application?";
				/*      */             
				/*  292 */             int result = JOptionPane.showConfirmDialog(frame1, theMessage, "alert", 0);
				/*  293 */             if (result == 0) {
					/*  294 */               HomeMainGui.this.downloadFromServerToFile();
					/*  295 */               HomeMainGui.this.copyFileToGoogleDrive();
					/*  296 */               System.exit(0);
				/*      */             } 
			/*      */           }
		/*      */         });
		/*      */     
		/*  301 */     this.queryWork.addActionListener(new ActionListener()
				/*      */         {
			/*      */           public void actionPerformed(ActionEvent e) {}
		/*      */         });
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */     
		/*  311 */     this.upload.addActionListener(new ActionListener()
				/*      */         {
			/*      */ 
			/*      */           
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  317 */             if (!HomeMainGui.getDatabaseStatus()) {
					/*  318 */               String message = "No Database Connected. File is not Uploaded to Database";
					/*  319 */               JOptionPane.showMessageDialog(null, message, "Input Error", 0);
					/*      */               
					/*      */               return;
				/*      */             } 
				/*      */             
				/*  324 */             Thread databaseThread = new Thread(new Runnable()
						/*      */                 {
					/*      */                   
					/*      */                   public void run()
					/*      */                   {
						/*  329 */                     if (refreshDatabase()) {
							/*  330 */                       JOptionPane.showMessageDialog(null, "MYSQL database updated");
						/*      */                     } else {
							/*  332 */                       JOptionPane.showMessageDialog(null, "ERROR:::MYSQL database was not updated");
						/*      */                     } 
					/*      */                   }
				/*  335 */                 }, "Database Thread");
				/*      */             
				/*  337 */             System.out.println(String.valueOf(databaseThread.getName()) + " has statrted");
				/*  338 */             databaseThread.start();
			/*      */           }
		/*      */         });
		/*      */ 
		/*      */ 
		/*      */     
		/*  344 */     this.reformat.addActionListener(new ActionListener()
				/*      */         {
			/*      */ 
			/*      */           
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  350 */             List<HomeData> list = HomeMainGui.getDataFromFile();
				/*  351 */             HomeMainGui.replaceDataInFile(list, "cost.csv");
				/*  352 */             JOptionPane.showMessageDialog(null, "Data reformatted in  cost.csv file");
			/*      */           }
		/*      */         });
		/*      */ 
		/*      */     
		/*  357 */     this.reconnectToDatabase.addActionListener(new ActionListener()
				/*      */         {
			/*      */ 
			/*      */           
			/*      */           public void actionPerformed(ActionEvent e)
			/*      */           {
				/*  363 */             HomeMainGui.this.mySQLDatabase = null;
				/*  364 */             HomeMainGui.this.isSQLSignonCredentCorrect();
				/*  365 */             if (HomeMainGui.databaseStatus.booleanValue()) {
					/*  366 */               HomeMainGui.this.lblDatabaseStatus.setText("Database Connected");
					/*  367 */               HomeMainGui.this.lblDatabaseStatus.setForeground(Color.green);
				/*      */             } else {
					/*  369 */               HomeMainGui.this.lblDatabaseStatus.setText("Database Is Not Connected");
					/*  370 */               HomeMainGui.this.lblDatabaseStatus.setForeground(Color.red);
				/*      */             } 
			/*      */           }
		/*      */         });
		/*      */ 
		/*      */ 
		/*      */ 
		/*      */     
		/*  378 */     this.frame.setJMenuBar(this.menuBar);
		/*  379 */     this.frame.pack();
		/*  380 */     this.frame.setVisible(true);
		/*  381 */     this.frame.setResizable(false);
		/*      */     
		/*  383 */     getMonthlyExpensesFromDatabase(getDatabaseStatus());
		/*  384 */     displayMarlinExpensesBarChart();
	/*      */   }
	/*      */ 
	/*      */   
	/*      */   private void copyFileToGoogleDrive() {
		/*  389 */     InputStream in = null;
		/*  390 */     OutputStream out = null;
		/*  391 */     File source = new File("cost.csv");
		/*  392 */     File dest = new File("M:\\My Drive\\Marlin Info\\cost.csv");
		/*      */     
		/*      */     try {
			/*  395 */       in = new FileInputStream(source);
			/*  396 */       out = new FileOutputStream(dest);
			/*  397 */       byte[] buffer = new byte[1024];
			/*      */       int length;
			/*  399 */       while ((length = in.read(buffer)) > 0) {
				/*  400 */         out.write(buffer, 0, length);
			/*      */       }
		/*  402 */     } catch (Exception e1) {
			/*      */       
			/*  404 */       JOptionPane.showMessageDialog(null, "ERROR: File Copy Not Successful To Google Drive ");
		/*      */     } finally {
			/*      */       
			/*      */       try {
				/*  408 */         in.close();
				/*  409 */         if (out == null) {
					/*  410 */           JOptionPane.showMessageDialog(null, "ERROR: Data Not copied to outfile");
				/*      */         } else {
					/*  412 */           out.close();
				/*      */         }
				/*      */       
			/*  415 */       } catch (IOException e2) {
				/*      */ 
				/*      */         
				/*  418 */         JOptionPane.showMessageDialog(null, "ERROR: File Copy Not Closed ");
			/*      */       } 
		/*      */     } 
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   protected boolean refreshDatabase() {
		/*  428 */     if (this.mySQLDatabase.refreshDatabase(getDataFromFile())) {
			/*  429 */       return true;
		/*      */     }
		/*  431 */     return false;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public void downloadFromServerToFile() {
		/*  438 */     JFrame frame = new JFrame();
		/*  439 */     String theMessage = " Download From Server? File Data Will Be Overwritten. Continue?";
		/*  440 */     int result = JOptionPane.showConfirmDialog(frame, theMessage, "alert", 0);
		/*  441 */     if (result == 0) {
			/*      */       
			/*  443 */       String query = "SELECT * FROM houseexpenses";
			/*  444 */       this.mySQLDatabase.getQuery(query);
			/*  445 */       List<HomeData> myList = this.mySQLDatabase.getList();
			/*  446 */       Collections.sort(myList, new SortHomeDataInDescendingOrderByDate());
			/*  447 */       if (replaceDataInFile(myList, "cost.csv").booleanValue()) {
				/*  448 */         JOptionPane.showMessageDialog(null, "SUCCESS: Data Downloaded to File ");
				/*      */ 
				/*      */         
				/*  451 */         if (createJSONFile(myList)) {
					/*  452 */           JOptionPane.showMessageDialog(null, "SUCCESS: JSON File created ");
				/*      */         } else {
					/*  454 */           JOptionPane.showMessageDialog(null, "ERROR: Downloading JSON Data ");
				/*      */         }
				/*      */       
			/*      */       } else {
				/*      */         
				/*  459 */         JOptionPane.showMessageDialog(null, "ERROR: Downloading Data ");
			/*      */       } 
			/*  461 */       this.mySQLDatabase.clearList();
		/*      */     } 
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   


	public boolean createJSONFile(List<HomeData> data) {
		if(data.size() ==0){
			return false;
		}
		//Creates a json file with HomeData data
		try (FileWriter file = new FileWriter("homeData.json")) {
			file.write("{ \n" );
			String str = " \"homeData\" ";
			file.write(str + ":[ \n") ;
			for(int i = 0; i < data.size(); i++) {
				ObjectMapper Obj = new ObjectMapper();
				String jsonStr = Obj.writeValueAsString(data.get(i));
				file.write(jsonStr + "\n");
			}
			file.write("] \n"  + " }");
		} catch (IOException e) {
			// TODO Auto-generated catch bljock
			e.printStackTrace();
		}	
		return true;

	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public Boolean populateDatabaseUsingJSONData(String filename) {
		/*  494 */     Boolean isDatabaseUpdated = Boolean.valueOf(false);
		/*  495 */     JSONParser parser = new JSONParser();
		/*      */     
		/*  497 */     ArrayList<HomeData> databaseEntries = new ArrayList<>();
		/*      */     try {
			/*  499 */       JSONObject jsonObject = (JSONObject)parser.parse(new FileReader(filename));
			/*  500 */       JSONArray homeDataArray = (JSONArray)jsonObject.get("homeData");
			/*  501 */       for (int i = 0; i < homeDataArray.size(); i++) {
				/*  502 */         JSONObject jsonObjectRow = (JSONObject)homeDataArray.get(i);
				/*      */         
				/*  504 */         databaseEntries.add(new HomeData(
						/*  505 */               (String)jsonObjectRow.get("date"), 
						/*  506 */               (String)jsonObjectRow.get("area"), 
						/*  507 */               (String)jsonObjectRow.get("item"), 
						/*  508 */               (Double)jsonObjectRow.get("cost"), 
						/*  509 */               (String)jsonObjectRow.get("receiptFilename"), 
						/*  510 */               (String)jsonObjectRow.get("info"), 
						/*  511 */               (Boolean)jsonObjectRow.get("isValue")));
			/*      */       } 
			/*  513 */       Collections.sort(databaseEntries, new SortHomeDataInDescendingOrderByDate());
			/*  514 */       if (this.mySQLDatabase.refreshDatabase(databaseEntries)) {
				/*  515 */         isDatabaseUpdated = Boolean.valueOf(true);
			/*      */       }
		/*  517 */     } catch (Exception e) {
			/*  518 */       JOptionPane.showMessageDialog(null, e.toString());
		/*      */     } 
		/*  520 */     return isDatabaseUpdated;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public void getMonthlyExpensesFromDatabase(Boolean showTable) {
		/*  536 */     Date date = new Date();
		/*      */     try {
			/*  538 */       double MONTHLY_MAX = 250.0D;
			/*      */ 
			/*      */       
			/*  541 */       String result = "SELECT * FROM houseexpenses WHERE  DATE >= '" + getFirstDayOfMonth(date) + 
					/*  542 */         "' AND DATE <= '" + getLastDayOfMonth(date) + "'";
			/*  543 */       this.mySQLDatabase.getDateRangeResults(result);
			/*      */       
			/*  545 */       List<HomeData> dateRangeList = this.mySQLDatabase.getList();
			/*  546 */       Collections.sort(dateRangeList, new SortHomeDataInDescendingOrderByDate());
			/*  547 */       double currentBalance = computeTotalCost(dateRangeList);
			/*      */       
			/*  549 */       if (showTable.booleanValue()) {
				/*  550 */         String stitle = "Monthly Query for " + getMonthOfDate() + ". Total Cost = $" + 
						/*  551 */           Double.toString(currentBalance);
			/*      */       }
			/*      */       
			/*  554 */       if (currentBalance > 250.0D && !maxLimitFlag)
			/*      */       {
				/*  556 */         maxLimitFlag = true;
				/*  557 */         playMaxLimitSound(currentBalance, 250.0D);
			/*      */       }
			/*      */     
		/*  560 */     } catch (Exception e) {
			/*      */       
			/*  562 */       JOptionPane.showMessageDialog(null, "ERROR: Unable To obtain current months total expense. Check database connection ");
		/*      */     } 
		/*      */ 
		/*      */     
		/*  566 */     this.mySQLDatabase.clearList();
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public void getMonthlyExpensesFromFile(Boolean showTable) {
		/*  579 */     Date date = new Date();
		/*  580 */     List<HomeData> dateRangeList = getDataFromFile(getFirstDayOfMonth(date), getLastDayOfMonth(date));
		/*  581 */     Collections.sort(dateRangeList, new SortHomeDataInDescendingOrderByDate());
		/*  582 */     double currentBalance = computeTotalCost(dateRangeList);
		/*  583 */     double MONTHLY_MAX = 250.0D;
		/*      */     
		/*  585 */     if (showTable.booleanValue()) {
			/*  586 */       String stitle = "Monthly Query for " + getMonthOfDate() + ". Total Cost = $" + 
					/*  587 */         Double.toString(currentBalance);
		/*      */     }
		/*      */     
		/*  590 */     if (currentBalance > 250.0D && !maxLimitFlag) {
			/*      */       
			/*  592 */       maxLimitFlag = true;
			/*  593 */       playMaxLimitSound(currentBalance, 250.0D);
		/*      */     } 
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static void playMaxLimitSound(double currentBalance, double maxLimit) {
		/*      */     try {
			/*  614 */       File musicpath = new File("cash_register.wav");
			/*  615 */       if (musicpath.exists()) {
				/*  616 */         AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicpath);
				/*  617 */         Clip clip = AudioSystem.getClip();
				/*  618 */         clip.open(audioInput);
				/*  619 */         clip.start();
				/*      */         
				/*  621 */         String theMessage = "Your monthly expenditures have exceed your set $" + String.format("%.2f", new Object[] { Double.valueOf(maxLimit)
				/*  622 */             }) + "\n by $" + String.format("%.2f", new Object[] { Double.valueOf(currentBalance - maxLimit) });
				/*  623 */         JOptionPane.showMessageDialog(null, theMessage, "ALERT", 0);
			/*      */       } else {
				/*      */         
				/*  626 */         String theMessage1 = "Audio File Not Found";
				/*  627 */         JOptionPane.showMessageDialog(null, theMessage1, "alert", 0);
			/*      */       }
			/*      */     
		/*  630 */     } catch (Exception exception) {}
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static String getFirstDayOfMonth(Date d) {
		/*  645 */     Calendar calendar = Calendar.getInstance();
		/*  646 */     calendar.setTime(d);
		/*  647 */     calendar.set(5, 1);
		/*  648 */     Date dddd = calendar.getTime();
		/*  649 */     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		/*  650 */     return sdf1.format(dddd);
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static String getLastDayOfMonth(Date d) {
		/*  663 */     Calendar calendar = Calendar.getInstance();
		/*  664 */     calendar.setTime(d);
		/*  665 */     calendar.set(5, calendar.getActualMaximum(5));
		/*  666 */     Date dddd = calendar.getTime();
		/*  667 */     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		/*  668 */     return sdf1.format(dddd);
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static String getMonthOfDate() {
		/*  681 */     LocalDate date = LocalDate.now();
		/*  682 */     return date.getMonth().toString();
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   private void displayMarlinExpensesBarChart() {
		/*  701 */     String query = "SELECT * FROM  houseexpenses";
		/*  702 */     this.mySQLDatabase.getQuery(query);
		/*  703 */     List<HomeData> myList = this.mySQLDatabase.getList();
		/*      */ 
		/*      */     
		/*  706 */     double[][] monthlyTotals = new double[6][12];
		/*  707 */     for (int row = 0; row < 6; row++) {
			/*  708 */       for (int col = 0; col < 12; col++) {
				/*  709 */         monthlyTotals[row][col] = 0.0D;
			/*      */       }
		/*      */     } 
		/*      */     
		/*  713 */     List<HomeData> list2020 = getListForYear(myList, 2020);
		/*  714 */     getMonthlyTotalForTheYear(monthlyTotals, list2020, 0);
		/*      */     
		/*  716 */     List<HomeData> list2021 = getListForYear(myList, 2021);
		/*  717 */     getMonthlyTotalForTheYear(monthlyTotals, list2021, 1);
		/*      */     
		/*  719 */     List<HomeData> list2022 = getListForYear(myList, 2022);
		/*  720 */     getMonthlyTotalForTheYear(monthlyTotals, list2022, 2);
		/*      */     
		/*  722 */     List<HomeData> list2023 = getListForYear(myList, 2023);
		/*  723 */     getMonthlyTotalForTheYear(monthlyTotals, list2023, 3);
		/*      */     
		/*  725 */     List<HomeData> list2024 = getListForYear(myList, 2024);
		/*  726 */     getMonthlyTotalForTheYear(monthlyTotals, list2024, 4);
		/*      */     
		/*  728 */     List<HomeData> list2025 = getListForYear(myList, 2025);
		/*  729 */     getMonthlyTotalForTheYear(monthlyTotals, list2025, 5);
		/*      */ 
		/*      */     
		/*  732 */     this.mySQLDatabase.clearList();
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   private List<HomeData> getListForYear(List<HomeData> myList, int year) {
		/*  745 */     int dateSelect = 3;
		/*  746 */     List<HomeData> list = new ArrayList<>();
		/*  747 */     Iterator<HomeData> it = myList.iterator();
		/*  748 */     while (it.hasNext()) {
			/*  749 */       HomeData data = it.next();
			/*  750 */       int listYear = convertDateStringToInt(data.getDate(), dateSelect);
			/*  751 */       if (listYear == year) {
				/*  752 */         list.add(data);
			/*      */       }
		/*      */     } 
		/*  755 */     return list;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static void getMonthlyTotalForTheYear(double[][] monthlyTotals, List<HomeData> list, int row) {
		/*  772 */     int dateSelect = 2;
		/*      */     
		/*  774 */     for (int i = 0; i < list.size(); i++) {
			/*  775 */       switch (convertDateStringToInt(((HomeData)list.get(i)).getDate(), dateSelect)) {
			/*      */         case 1:
				/*  777 */           monthlyTotals[row][0] = monthlyTotals[row][0] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 2:
				/*  780 */           monthlyTotals[row][1] = monthlyTotals[row][1] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 3:
				/*  783 */           monthlyTotals[row][2] = monthlyTotals[row][2] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 4:
				/*  786 */           monthlyTotals[row][3] = monthlyTotals[row][3] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 5:
				/*  789 */           monthlyTotals[row][4] = monthlyTotals[row][4] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 6:
				/*  792 */           monthlyTotals[row][5] = monthlyTotals[row][5] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 7:
				/*  795 */           monthlyTotals[row][6] = monthlyTotals[row][6] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 8:
				/*  798 */           monthlyTotals[row][7] = monthlyTotals[row][7] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 9:
				/*  801 */           monthlyTotals[row][8] = monthlyTotals[row][8] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 10:
				/*  804 */           monthlyTotals[row][9] = monthlyTotals[row][9] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 11:
				/*  807 */           monthlyTotals[row][10] = monthlyTotals[row][10] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */         case 12:
				/*  810 */           monthlyTotals[row][11] = monthlyTotals[row][11] + ((HomeData)list.get(i)).getCost().doubleValue();
				/*      */           break;
			/*      */       } 
		/*      */     } 
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static int convertDateStringToInt(String date, int dateSelect) {
		/*  832 */     String delimStr = "-";
		/*      */     
		/*  834 */     String[] words = date.split(delimStr);
		/*  835 */     int intDate = 0;
		/*  836 */     switch (dateSelect) {
		/*      */       
		/*      */       case 1:
			/*  839 */         intDate = Integer.parseInt(words[1]) * 100 + Integer.parseInt(words[2]) + 
			/*  840 */           Integer.parseInt(words[0]) * 10000;
			/*      */         break;
		/*      */       case 2:
			/*  843 */         intDate = Integer.parseInt(words[1]);
			/*      */         break;
		/*      */       case 3:
			/*  846 */         intDate = Integer.parseInt(words[0]);
			/*      */         break;
		/*      */     } 
		/*      */ 
		/*      */     
		/*  851 */     return intDate;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   private void isSQLSignonCredentCorrect() {
		/*  864 */     String credentialsFilename = "mysqlsignonstuff.txt";
		/*  865 */     String DELIMITER = "%";
		/*  866 */     String[] myDatastuff = getCredentialsFromFile(credentialsFilename).split(DELIMITER);
		
		/*  869 */     this.mySQLDatabase = new MySQLConnect(myDatastuff[0], myDatastuff[1], myDatastuff[2]);
		/*  870 */     if (this.mySQLDatabase.isConnected()) {
			/*      */       
			/*  872 */       databaseStatus = true;
		/*      */     } else {
			/*      */       
			/*  875 */       databaseStatus = false;
		/*      */     } 
	/*      */   }

	/*      */   private String getCredentialsFromFile(String inputFile) {
		/*  892 */     int myMagicNumber = 36;
		/*  893 */     String allData = null;
		/*  894 */     int count = 1;
		/*  895 */     BufferedReader bufferedReader = null;
		/*      */     
		/*      */     try {
			/*  898 */       bufferedReader = new BufferedReader(new FileReader(inputFile));
			/*      */       try {
				/*      */         String data;
				/*  901 */         while ((data = bufferedReader.readLine()) != null) {
					/*  902 */           if (count == 36) {
						/*  903 */             allData = data;
											return allData;
					/*      */           }
					/*  905 */           count++;
				/*      */         } 
			
			/*  910 */       } catch (IOException e) {
				/*      */         
				/*  912 */         e.printStackTrace();
				/*  913 */         JOptionPane.showMessageDialog(null, "HomeMainGui: HomeMainGUI: Can not read  from file ");
			/*      */       } 
		/*  915 */     } catch (FileNotFoundException e) {
			/*      */       String data;
			/*  917 */       JOptionPane.showMessageDialog(null, "HomeMainGui : Can not find MYSQL database \ncredential file. Program terminated ");
			/*  918 */       e.printStackTrace();
		/*      */     } finally {
			/*      */       try {
				/*  921 */         bufferedReader.close();
			/*      */       }
			/*  923 */       catch (IOException e) {
				/*      */         
				/*  925 */         JOptionPane.showMessageDialog(null, "HomeMainGui :Error Closing The File" + e);
				/*  926 */         e.printStackTrace();
			/*      */       } 
		/*      */     } 
		/*      */     
		/*  930 */     return null;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static class SortHomeDataInAscendingOrderByDate
	/*      */     implements Comparator<HomeData>
	/*      */   {
		/*      */     public int compare(HomeData a, HomeData b) {
			/*  969 */       int dateSelect = 1;
			/*      */       
			/*  971 */       return HomeMainGui.convertDateStringToInt(a.getDate(), dateSelect) - HomeMainGui.convertDateStringToInt(b.getDate(), dateSelect);
		/*      */     }
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static class SortHomeDataInDescendingOrderByDate
	/*      */     implements Comparator<HomeData>
	/*      */   {
		/*      */     public int compare(HomeData a, HomeData b) {
			/*  987 */       int dateSelect = 1;
			/*  988 */       return HomeMainGui.convertDateStringToInt(b.getDate(), dateSelect) - HomeMainGui.convertDateStringToInt(a.getDate(), dateSelect);
		/*      */     }
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static List<HomeData> getDataFromFile() {
		/* 1007 */     BufferedReader fileReader = null;
		/* 1008 */     String str = "";
		/* 1009 */     List<HomeData> myList = new ArrayList<>();
		/*      */ 
		/*      */     
		/*      */     try {
			/* 1013 */       fileReader = new BufferedReader(new FileReader("cost.csv"));
			/*      */ 
			/*      */       
			/* 1016 */       str = fileReader.readLine();
			/* 1017 */       while ((str = fileReader.readLine()) != null) {
				/* 1018 */         HomeData data = new HomeData(str);
				/* 1019 */         myList.add(data);
			/*      */       } 
		/* 1021 */     } catch (Exception e) {
			/* 1022 */       JOptionPane.showMessageDialog(null, "Error: Cannot find cost.csv file");
			/* 1023 */       e.printStackTrace();
		/*      */     } finally {
			/*      */       try {
				/* 1026 */         fileReader.close();
			/* 1027 */       } catch (IOException e) {
				/* 1028 */         JOptionPane.showMessageDialog(null, "Error Closing The File" + e);
			/*      */       } 
		/*      */     } 
		/*      */     
		/* 1032 */     if (myList.size() > 0) {
			/*      */       
			/* 1034 */       Collections.sort(myList, new SortHomeDataInDescendingOrderByDate());
			/*      */ 
			/*      */       
			/* 1037 */       return myList;
		/*      */     } 
		/* 1039 */     return null;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static List<HomeData> getDataFromFile(String firstDay, String lastDay) {
		/* 1056 */     System.out.println();
		/*      */     
		/* 1058 */     BufferedReader fileReader = null;
		/* 1059 */     String str = "";
		/* 1060 */     List<HomeData> myList = new ArrayList<>();
		/*      */ 
		/*      */     
		/*      */     try {
			/* 1064 */       fileReader = new BufferedReader(new FileReader("cost.csv"));
			/*      */ 
			/*      */       
			/* 1067 */       str = fileReader.readLine();
			/* 1068 */       while ((str = fileReader.readLine()) != null) {
				/* 1069 */         HomeData data = new HomeData(str);
				/*      */ 
				/*      */         
				/* 1072 */         if (convertDateStringToInt(data.getDate(), 1) >= convertDateStringToInt(firstDay, 1) && convertDateStringToInt(data.getDate(), 1) <= convertDateStringToInt(lastDay, 1)) {
					/* 1073 */           myList.add(data);
				/*      */         }
			/*      */       } 
		/* 1076 */     } catch (Exception e) {
			/* 1077 */       JOptionPane.showMessageDialog(null, "Error: Cannot find cost.csv file");
			/* 1078 */       e.printStackTrace();
		/*      */     } finally {
			/*      */       try {
				/* 1081 */         fileReader.close();
			/* 1082 */       } catch (IOException e) {
				/* 1083 */         JOptionPane.showMessageDialog(null, "Error Closing The File" + e);
			/*      */       } 
		/*      */     } 
		/*      */     
		/* 1087 */     if (myList.size() > 0) {
			/*      */       
			/* 1089 */       Collections.sort(myList, new SortHomeDataInDescendingOrderByDate());
			/*      */ 
			/*      */       
			/* 1092 */       return myList;
		/*      */     } 
		/* 1094 */     return null;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static Boolean placeInFile(HomeData item) {
		/* 1110 */     Boolean isWrittenToFile = Boolean.valueOf(true);
		/* 1111 */     BufferedWriter bw = null;
		/* 1112 */     Boolean createFileHeaders = Boolean.valueOf(true);
		/* 1113 */     String COMMA_DELIMITER = ",";
		/* 1114 */     String NEW_LINE_SEPARATOR = "\n";
		/*      */     try {
			/* 1116 */       File file = new File("cost.csv");
			/*      */ 
			/*      */ 
			/*      */ 
			/*      */ 
			/*      */       
			/* 1122 */       if (!file.exists()) {
				/* 1123 */         file.createNewFile();
				/* 1124 */         createFileHeaders = Boolean.valueOf(false);
			/*      */       } 
			/* 1126 */       FileWriter fw = new FileWriter(file, true);
			/* 1127 */       bw = new BufferedWriter(fw);
			/*      */       
			/* 1129 */       if (!createFileHeaders.booleanValue()) {
				/* 1130 */         bw.write("DATE");
				/* 1131 */         bw.write(",");
				/* 1132 */         bw.write("AREA");
				/* 1133 */         bw.write(",");
				/* 1134 */         bw.write("ITEMS");
				/* 1135 */         bw.write(",");
				/* 1136 */         bw.write("COST");
				/* 1137 */         bw.write(",");
				/* 1138 */         bw.write("RECEIPT FILE NAME");
				/* 1139 */         bw.write(",");
				/* 1140 */         bw.write("INFO");
				/* 1141 */         bw.write(",");
				/* 1142 */         bw.write("VALUE ADDED");
				/* 1143 */         bw.write("\n");
			/*      */       } 
			/*      */       
			/* 1146 */       bw.write(item.getDate());
			/* 1147 */       bw.write(",");
			/* 1148 */       bw.write(item.getArea());
			/* 1149 */       bw.write(",");
			/* 1150 */       bw.write(item.getItem());
			/* 1151 */       bw.write(",");
			/* 1152 */       bw.write(item.getCost().toString());
			/* 1153 */       bw.write(",");
			/* 1154 */       bw.write(item.getReceiptFilename());
			/* 1155 */       bw.write(",");
			/* 1156 */       bw.write(item.getInfo());
			/* 1157 */       bw.write(",");
			/* 1158 */       bw.write(item.getIsValue().toString());
			/* 1159 */       bw.write("\n");
			/*      */     
		/*      */     }
		/* 1162 */     catch (IOException ioe) {
			/* 1163 */       isWrittenToFile = Boolean.valueOf(false);
			/* 1164 */       JOptionPane.showMessageDialog(null, "Error Opening The File ");
		/*      */     } finally {
			/*      */       try {
				/* 1167 */         if (bw != null) {
					/* 1168 */           bw.close();
				/*      */         }
			/* 1170 */       } catch (Exception e) {
				/* 1171 */         JOptionPane.showMessageDialog(null, "Error Closing The File ");
			/*      */       } 
		/*      */     } 
		/* 1174 */     return isWrittenToFile;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static Boolean replaceDataInFile(List<HomeData> item, String filename) {
		/* 1186 */     Boolean isWriteSuccess = Boolean.valueOf(false);
		/* 1187 */     BufferedWriter bw = null;
		/*      */     
		/* 1189 */     String COMMA_DELIMITER = ",";
		/* 1190 */     String NEW_LINE_SEPARATOR = "\n";
		/*      */     try {
			/* 1192 */       File file = new File(filename);
			/*      */ 
			/*      */ 
			/*      */ 
			/*      */ 
			/*      */       
			/* 1198 */       if (!file.exists()) {
				/* 1199 */         file.createNewFile();
			/*      */       }
			/*      */       
			/* 1202 */       if (filename.equalsIgnoreCase("cost.csv")) {
				/* 1203 */         FileWriter fw = new FileWriter(file, false);
				/* 1204 */         bw = new BufferedWriter(fw);
				/* 1205 */         bw.write("DATE");
				/* 1206 */         bw.write(",");
				/* 1207 */         bw.write("AREA");
				/* 1208 */         bw.write(",");
				/* 1209 */         bw.write("ITEMS");
				/* 1210 */         bw.write(",");
				/* 1211 */         bw.write("COST");
				/* 1212 */         bw.write(",");
				/* 1213 */         bw.write("RECEIPT FILE NAME");
				/* 1214 */         bw.write(",");
				/* 1215 */         bw.write("INFO");
				/* 1216 */         bw.write(",");
				/* 1217 */         bw.write("VALUE ADDED");
				/* 1218 */         bw.write("\n");
			/*      */       }
			/*      */       else {
				/*      */         
				/* 1222 */         FileWriter fw = new FileWriter(file, true);
				/* 1223 */         bw = new BufferedWriter(fw);
			/*      */       } 
			/*      */       
			/* 1226 */       for (int i = 0; i < item.size(); i++) {
				/* 1227 */         bw.write(((HomeData)item.get(i)).getDate());
				/* 1228 */         bw.write(",");
				/* 1229 */         bw.write(((HomeData)item.get(i)).getArea());
				/* 1230 */         bw.write(",");
				/* 1231 */         bw.write(((HomeData)item.get(i)).getItem());
				/* 1232 */         bw.write(",");
				/* 1233 */         bw.write(((HomeData)item.get(i)).getCost().toString());
				/* 1234 */         bw.write(",");
				/* 1235 */         bw.write(((HomeData)item.get(i)).getReceiptFilename());
				/* 1236 */         bw.write(",");
				/* 1237 */         bw.write(((HomeData)item.get(i)).getInfo());
				/* 1238 */         bw.write(",");
				/* 1239 */         bw.write(((HomeData)item.get(i)).getIsValue().toString());
				/* 1240 */         bw.write("\n");
			/*      */       } 
			/* 1242 */       isWriteSuccess = Boolean.valueOf(true);
		/*      */     }
		/* 1244 */     catch (IOException ioe) {
			/* 1245 */       JOptionPane.showMessageDialog(null, "Data was not written to file. \nVerify the cost.csv file is closed");
		/*      */     } finally {
			/*      */       try {
				/* 1248 */         if (bw != null) {
					/* 1249 */           bw.close();
				/*      */         }
			/* 1251 */       } catch (Exception e) {
				/* 1252 */         JOptionPane.showMessageDialog(null, "Error Closing The File " + e);
			/*      */       } 
		/*      */     } 
		/* 1255 */     return isWriteSuccess;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static String reformatDateString(String date) {
		/* 1270 */     String newDate = null, DELIMITER = "/";
		/* 1271 */     String[] oldDate = date.split(DELIMITER);
		/* 1272 */     String month = oldDate[0];
		/* 1273 */     String day = oldDate[1];
		/*      */ 
		/*      */     
		/* 1276 */     if (Integer.parseInt(oldDate[0]) < 10) {
			/* 1277 */       month = "0" + oldDate[0];
		/*      */     } else {
			/* 1279 */       month = oldDate[0];
		/*      */     } 
		/* 1281 */     if (Integer.parseInt(oldDate[1]) < 10) {
			/* 1282 */       day = "0" + oldDate[1];
		/*      */     } else {
			/* 1284 */       day = oldDate[1];
		/*      */     } 
		/*      */ 
		/*      */     
		/* 1288 */     newDate = String.valueOf(oldDate[2]) + "-" + oldDate[0] + "-" + oldDate[1];
		/* 1289 */     return newDate;
	/*      */   }
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */ 
	/*      */   
	/*      */   public static double computeTotalCost(List<HomeData> list) {
		/* 1302 */     double count = 0.0D;
		/* 1303 */     for (HomeData myList : list) {
			/* 1304 */       count += myList.getCost().doubleValue();
		/*      */     }
		/*      */     
		/* 1307 */     return Math.round(count * 100.0D) / 100.0D;
	/*      */   }

	public static void main(String[] args) {


		char[] p = { 'l', 'i', 'i', 's', 't', '9', 'm', '1', '6', '5', '5' };

		new HomeMainGui("getMoney", p);
	}
}


/* Location:              C:\Users\rober\OneDrive\Documents\MyApplications\HomeImprovementsNRepairs\homeImprovementsNRepairs.jar!\main_screen\HomeMainGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */