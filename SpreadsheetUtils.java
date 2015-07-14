package terry.example.spread;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;

// This class contains helper methods that are used for getting spreadsheet
// feeds and individual spreadsheets, especially if they are used by
// multiple different activities. In general these methods operate in the
// current thread, and may be long-running, so they should probably be called
// from a background thread.
public class SpreadsheetUtils {
	 // Given a context in which the service will be used and an account name
	// returns a SpreadsheetService authenticated for that user.
	// Will throw a UserRecoverableAuthException if the user has not yet
	// authorized spreadsheet access for this app, in which case the caller should
	// handle it and do something.
	static String token = null;
	
	public static SpreadsheetService setupSpreadsheetServiceInThisThread(
			Context context, String account_name) throws GoogleAuthException {
		String token = null;
		try {
			token = GoogleAuthUtil.getToken(
					context, 
					account_name, 
					"oauth2:https://spreadsheets.google.com/feeds https://docs.google.com/feeds");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SpreadsheetService s =
				new SpreadsheetService("NumberNDate");
		s.setAuthSubToken(token);
		return s;
	}
	
	public static String gettoken()
	{
			return token;
	}

	public static SpreadsheetFeed currentSpreadsheetFeedInThisThread(Context context, String account_name) 
			throws GoogleAuthException {
		SpreadsheetService service = 
				setupSpreadsheetServiceInThisThread(context, account_name);
		try {
			URL SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
			SpreadsheetFeed feed = null;
			feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
			return feed;
		} catch (MalformedURLException e) {
			// TODO When does this happen? Hopefully never if the above URL is correct.
			e.printStackTrace();
		} catch (IOException e) {
			// TODO When does this happen?
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO When does this happen?
			e.printStackTrace();
		}
		return null;
	}
}
