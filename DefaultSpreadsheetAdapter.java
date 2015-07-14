package terry.example.spread;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import terry.example.spread.Create_db.Table_name;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

// Default spreadsheet adapter. Stores numbers in the database until they
// are posted to the spreadsheet, which occurs by adding a new row.
// TODO(nbeckman): Consider making this two classes; one that adds the expenses
// to the database, and the other that actually posts them when the network is
// ready.
public class DefaultSpreadsheetAdapter implements SpreadsheetAdapter {
	
	private static final String NUMBER_HEADER_STRING = "number";
	private static final String DATE_HEADER_STRING = "date";
	private static final String WHAT_BUY_STRING = "buy";
	private static final String WHAT_BUY_TYPE = "type";
	private final Create_db dbHelper;
	int a = 0; // counter
	boolean thetimeissame = false ;
	// The Url of the entire spreadsheet feed.
	private final String spreadsheetFeedUrl;
	
	private ArrayList<Columns> all;
	
	// Authenticated spreadsheet service used for making calls out to Google Docs.
	// This field can be changed as we expect it to be set only once a connection
	// to the network has been established.
	// MUST BE GUARDED BY this, AS IT CAN CHANGE.
	private SpreadsheetService spreadsheetService = null;
	
	public DefaultSpreadsheetAdapter(Context context, String spreadsheetFeedUrl) {
		this.dbHelper = new Create_db(context);
		this.spreadsheetFeedUrl = spreadsheetFeedUrl;
		
		// Uncomment to clear database for testing.
//		// TODO
//		final SQLiteDatabase db = this.dbHelper.getWritableDatabase();
//		db.delete(ExpenseEntry.TABLE_NAME, "1", null);
	}
	
	public synchronized void setSpreadsheetService(SpreadsheetService spreadsheetService) {
		this.spreadsheetService = spreadsheetService;
	}
	
	public void AddValue(double number , String remark) {
		final long date_added = System.currentTimeMillis();
		// Create a new database entry for this number. The DB handler will post it later.
		//  
		Create_db db = this.dbHelper;
		Columns newColumn = new Columns();
		ArrayList<Columns> all = new ArrayList<Columns>();
		String state = "i";
		all = db.getAllColumns();
		newColumn.setDATE(date_added);
		newColumn.setId(all.size()+1);
		newColumn.setNumber(number);
		newColumn.setRemark(remark);
		newColumn.setUrl(spreadsheetFeedUrl);
		newColumn.setState(state);
		db.addColumn(newColumn);
	}

	@Override
	public long NumOutstandingEntries() {
		final SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, Create_db.TABLE_name);
	}
	
	@SuppressWarnings("null")
	@Override
	public synchronized boolean PostOneEntry() {
		
		if (this.spreadsheetService == null) {
			return false;
		}
			final Create_db db = this.dbHelper;
			all = db.getAllColumns();
			Columns update = new Columns();
			double number = all.get(a).getNumber();
			String thing = all.get(a).getRemark();
			long date_added_millis = all.get(a).getDATE();
			String spreadsheet_url = all.get(a).getUrl();
			String state = all.get(a).getState();
			String type = all.get(a).getType();
			final boolean [] islive  = new boolean [all.size()];
			for(int i = 0 ; i < islive.length ; i++)
			{
				islive[i] = false;
			}
			try {
				//get the worksheetfeed from user account 
				WorksheetFeed worksheed_feed = spreadsheetService.getFeed(new URL(spreadsheet_url), WorksheetFeed.class);
				//get the worksheet from the feed
				List<WorksheetEntry> worksheets = worksheed_feed.getEntries();
				//get the first worksheet
				WorksheetEntry worksheet = worksheets.get(0);
				URL listFeedUrl = worksheet.getListFeedUrl();
				ListFeed listFeed = spreadsheetService.getFeed(listFeedUrl, ListFeed.class);
				
				SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat date_format_google = new SimpleDateFormat("yyyy/M/d a h:mm:ss", Locale.TAIWAN);
				Date date_added = new Date(date_added_millis);
				
				// Create a local representation of the new row. This is truly horrible;
				// you cannot add a new row the the spreadsheet except by naming the column
				// header that it should go in.
				// TODO(nbeckman): Make sure the columns have these names.
				ListEntry row = new ListEntry();
		    	List<ListEntry> comlist ;
		    	ListEntry com;
		    	String googledatatime = null , now = "1";
		    	Date date_g = new Date(101929000) , date_sql = new Date(101929000);
		    	
		    	DateTime updatetime = worksheed_feed.getUpdated();
		    	long lasttime = all.get(all.size()-1).getDATE();
		    	
		    	if(thetimeissame)
		    	{
		    		lasttime = updatetime.getValue()+(3600*12000);
		    	}
		    	
		    	/*Log.i("DefaultSpreadsheetAdapter", "Attempting to post number " + number + 
		    		",what you buy"+ thing + " and date " + 
		    		date_format.format(date_added) + " to url " + listFeedUrl);*/
		    	comlist = listFeed.getEntries();
		    	
				number = all.get(a).getNumber();
 			   thing = all.get(a).getRemark();
 			   date_added_millis = all.get(a).getDATE();
 			   spreadsheet_url = all.get(a).getUrl();
 			   state = all.get(a).getState();
 			   type = all.get(a).getType();
 			   row.getCustomElements().setValueLocal(DATE_HEADER_STRING, date_format.format(date_added));
 			   row.getCustomElements().setValueLocal(NUMBER_HEADER_STRING, Double.toString(number));
 				row.getCustomElements().setValueLocal(WHAT_BUY_STRING, thing);
 				row.getCustomElements().setValueLocal(WHAT_BUY_TYPE, type);
 				now = row.getCustomElements().getValue(DATE_HEADER_STRING);
 				//Log.d("getAllBooks()", all.toString());
		    	if(!comlist.isEmpty())
		    	{
		    		for(int j = 0 ; j<comlist.size() ; j++)
		    		{
		    			for( int k = 0 ; k <= a; k++ )
		    			{
		    				number = all.get(k).getNumber();
		    			   thing = all.get(k).getRemark();
		    			   date_added_millis = all.get(k).getDATE();
		    			   spreadsheet_url = all.get(k).getUrl();
		    			   state = all.get(k).getState();
		    			   type = all.get(k).getType();
		    			   row.getCustomElements().setValueLocal(DATE_HEADER_STRING, date_format.format(date_added_millis));
		    			   row.getCustomElements().setValueLocal(NUMBER_HEADER_STRING, Double.toString(number));
		    				row.getCustomElements().setValueLocal(WHAT_BUY_STRING, thing);
		    				row.getCustomElements().setValueLocal(WHAT_BUY_TYPE, type);
		    				now = row.getCustomElements().getValue(DATE_HEADER_STRING);
		    				//Log.d("getAllBooks()", state);
		    				//Log.i("knumber", String.valueOf(k)); 
		    				com = comlist.get(j);
		    				googledatatime = com.getCustomElements().getValue(DATE_HEADER_STRING);
		    				try {
		    					date_sql = date_format.parse(now);
		    				} catch (ParseException e1) {
		    					e1.printStackTrace();
		    				}	
		    				try { 
		    					date_g = date_format_google.parse(googledatatime);
		    				} catch (ParseException e) {
		    					e.printStackTrace();	
		    				}
		    				if(date_g.getTime() == date_sql.getTime() && state.equals("i"))
		    				{
		    					//Log.i("i", state); 
					    		islive[k] = true;
					    		//Log.i("numberofa", String.valueOf(islive[j]));
					    	}
		    				else if(date_g.getTime() == date_sql.getTime() && state.equals("u"))
		    				{
		    					Log.i("update!", "go!!");
		    					ListEntry googlerow = listFeed.getEntries().get(j); 
		    					googlerow.getCustomElements().setValueLocal(DATE_HEADER_STRING, date_format.format(date_added_millis));
		    					googlerow.getCustomElements().setValueLocal(NUMBER_HEADER_STRING, Double.toString(number));
		    					googlerow.getCustomElements().setValueLocal(WHAT_BUY_STRING, thing);
		    					googlerow.getCustomElements().setValueLocal(WHAT_BUY_TYPE, type);
		    					googlerow.update();
		    					islive[k] = true;
		    					Columns upcolumn = all.get(k);
		    					upcolumn.setState("i");
		    					db.updateColumn(upcolumn);
		    				}
		    				else if(date_g.getTime() == date_sql.getTime() && state.equals("d"))
		    				{
		    					ListEntry googlerow = listFeed.getEntries().get(j); 
		    					googlerow.delete();
		    					Columns decolumn = all.get(k);
		    					db.deleteColumn(decolumn);
		    					Log.i("delete", "yes!");
		    				}
					    	if(date_sql.getTime() > lasttime)
					    	{
					    		lasttime = date_sql.getTime();
					    	}
		    			}
		    		} 
		    		if(updatetime.getValue()+(3600*12000) <= lasttime)
		    		{
		    			for(int i = 0  ; i <= a; i++)
		    			{
		    				thetimeissame = true;
		    				if(a == all.size()-1) //If insert all row 
		    				{
		    	 				thetimeissame = false;
		    				}
		    				if(!islive[i])
		    				{
		    					Log.i("inumber", String.valueOf(i)); 
		    					try {
		    						row = listFeed.insert(row);
		    						islive[i] = true;
		    						thetimeissame = true;
		    					}
		    					catch (com.google.gdata.util.InvalidEntryException e) {
		    						// This means in all likelihood, you don't have 'date' and 'number'
		    						// headers. Try to create them if we can, and insert again.
		    						maybeAddSpreadsheetHeaders(worksheet);
		    						// Just try again so at least the exception will be thrown...
			    					row = listFeed.insert(row);
			    					islive[i] = true;
			    					thetimeissame = true;
			    				}
			    			}
		    			}
		    		}
		    		else
		    		{
		    			for(int i = 0 , j= 0 ; i < a; i++)
		    			{
		    				if(!islive[i])
		    				{
		    					
		    					Columns decolumn = all.get(i - j);
		    					db.deleteColumn(decolumn);
		    					islive[i] = true;
		    					j++;
		    					
		    				}
		    			}
		    			
		    			lasttime = date_sql.getTime();
		    			
		    		}
		    	// Send the new row to the API for insertion.
		    	}
		    	else
		    	{
		    		try {
	    				
	    				row = listFeed.insert(row);
	    				thetimeissame = true;
	    				
	    			}
	    			catch (com.google.gdata.util.InvalidEntryException e) {
	    				// This means in all likelihood, you don't have 'date' and 'number'
	    				// headers. Try to create them if we can, and insert again.
	    				maybeAddSpreadsheetHeaders(worksheet);
	    				// Just try again so at least the exception will be thrown...
	    				row = listFeed.insert(row);
	    				thetimeissame = true;
	    			}
		    	}
		    	// Now delete that row so we don't process it again.
		    	// Technically we've got no atomicity here... Hopefully
		    	// the network operation is much more likely to fail than
		    	// this.
		    
		    
		    	/*final long row_id = cursor.getLong(
		    		cursor.getColumnIndexOrThrow(Table_name._ID));
				// Define 'where' part of query.
				String selection = Table_name._ID + " LIKE ?";
				// Specify arguments in placeholder order.
				String[] selectionArgs = { String.valueOf(row_id) };
				// Issue SQL statement.
				db.delete(Create_db.TABLE_name, selection, selectionArgs);*/
		    	//Up is delete table 
		    	//all.remove(a);
		    	a++;
		    	return true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		//}
		return false;
	}
	
	// If the header row is empty, add 'number' and 'date' headers, otherwise
	// we are in danger of messing up someone's data.
	private void maybeAddSpreadsheetHeaders(WorksheetEntry worksheet) throws URISyntaxException, IOException, ServiceException {
		// Fetch entire top row to see if it's empty.
		URL cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString()
				+ "?min-row=1&max-row=4").toURL();
		CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrl, CellFeed.class);
		if (cellFeed.getEntries().isEmpty()) {
			
			// Now request two rows, and force them to be queried even if they are empty.
			cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString()
					+ "?min-row=1&max-row=4&min-col=1&max-col=4&return-empty=true").toURL();
			cellFeed = spreadsheetService.getFeed(cellFeedUrl, CellFeed.class);
			// Set their values to number and date.
			CellEntry first_cell = cellFeed.getEntries().get(0);
			first_cell.changeInputValueLocal(DATE_HEADER_STRING);
			first_cell.update();
			
			CellEntry second_cell = cellFeed.getEntries().get(1);
			second_cell.changeInputValueLocal(NUMBER_HEADER_STRING);
			second_cell.update();
			
			CellEntry three_cell = cellFeed.getEntries().get(2);
			three_cell.changeInputValueLocal(WHAT_BUY_STRING);
			three_cell.update();
			
			CellEntry four_cell = cellFeed.getEntries().get(3);
			four_cell.changeInputValueLocal(WHAT_BUY_TYPE);
			four_cell.update();
			
		} else {
			Log.w("DefaultSpreadsheetAdapter", "Can't add headers because spreadsheet is not empty.");
		}
	}

	private List<Loader<?>> month_oberservers_ = new ArrayList<Loader<?>>();
	@Override
	public void AddMonthsObserver(Loader<?> observer) {
		month_oberservers_.add(observer);
	}
	@Override
	public void RemoveMonthsObserver(Loader<?> observer) {
		month_oberservers_.remove(observer);
	}
	// TODO: Lots
	// have a start() method that will start a periodic (?) thread
	// it will load from the network
	// when network load is done, call 'detect changes & update'
	// after updates, call notifyMonthObservers()
	
}
