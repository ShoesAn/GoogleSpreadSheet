package terry.example.spread;

import java.util.ArrayList;
import java.util.List;

import com.example.test.Activity2Activity;
import com.example.test.Activity3Activity;
import com.example.test.Activity4Activity;
import com.example.test.Activity5Activity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;

import terry.example.spread.Columns;
import terry.example.spread.Create_db;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {

	// Intent ID; this tells us when the account picker is returning. 
	private static final int kAccountChoiceIntent = 1;
	// Intent ID; this tells us when the SpreadsheetFileManager is returning.
	private static final int kSpreadsheetChoiceIntent = 2;
	private Spinner spinner;
	private Button swittch;
	private Button swit;
	private Button pie;
	private Button lis;
	private Button send;
	private Button Synchronous;
	private EditText name;
	private EditText number;
	private DefaultSpreadsheetAdapter spreadsheet_adapter_= null;
	private OutstandingExpensesPoster expenses_poster_ = null;
	private SpreadsheetService spreadsheet_service_ = null;
	public Context main_account;
	ArrayList<Columns> list;
	private String posttype = null;
	final String[] type = {"l","e","t","c","p"};
	final String[] choose_type = {"請選擇消費類別","食", "衣", "住", "行", "娛樂"};
	public static  Create_db db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new Create_db(this);
		swittch = (Button) findViewById(R.id.button1);
		swit = (Button) findViewById(R.id.button2);
		pie = (Button) findViewById(R.id.button3);
		lis = (Button) findViewById(R.id.button4);
		spinner =(Spinner) findViewById(R.id.spinner1);
		Synchronous =(Button) findViewById(R.id.button6);
		send =(Button)findViewById(R.id.button5);
		name = (EditText) findViewById(R.id.expense_amount_textbox);
		number = (EditText) findViewById(R.id.editText1);
		SpinnerAdapter choose_type_list;
		//Columns co = new Columns();
		//db.addColumn(co);
		
		//										 0 		1		2	 3		4	 5		
		
		//                              0    1    2     3    4
		//                             住	食   行	   衣	娛樂
		choose_type_list= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, choose_type);
		
		spinner.setAdapter(choose_type_list);
		
		
		swittch.setEnabled(false);
		swit.setEnabled(false);
		pie.setEnabled(false);
		lis.setEnabled(false);
		Synchronous.setEnabled(false);
		send.setEnabled(false);
		
		if (establishAccountAndSpreadsheet()) { 
			//Columns updatecolumn = db.getAllColumns().get(1);
			//updatecolumn.setNumber(400);
			//updatecolumn.setState("u");
			//db.updateColumn(updatecolumn);
			//db.addColumn(new Columns(1000, Date , "house", SpreadsheetFileManager.getStoredSpreadsheetURL(MainActivity.this), state , type[2]));
			//db.addColumn(new Columns(23, Date, "shoes", SpreadsheetFileManager.getStoredSpreadsheetURL(MainActivity.this)));
			list = db.getAllColumns();
			swittch.setEnabled(true);
			swit.setEnabled(true);
			pie.setEnabled(true);
			lis.setEnabled(true);
			Synchronous.setEnabled(true);
			send.setEnabled(true);
			startDisplayLogic();
			
			
		}
		final SharedPreferences shared_pref = 
				PreferenceManager.getDefaultSharedPreferences(this);
    	shared_pref.registerOnSharedPreferenceChangeListener(this);
		
    	
    	swittch.setOnClickListener(new OnClickListener()
		{ 
          
			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				list = db.getAllColumns();
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, Activity2Activity.class);
				intent.putParcelableArrayListExtra("all", list);
				startActivity(intent);
			}
		}
	    );
		swit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				list = db.getAllColumns();
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, Activity3Activity.class);
				intent.putParcelableArrayListExtra("all", list);
				startActivity(intent);
			}
		}		
		);
		pie.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				list = db.getAllColumns();
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, Activity4Activity.class);
				intent.putParcelableArrayListExtra("all", list);
				startActivity(intent);
			}	
		}	
		);
		send.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				if(!number.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !posttype.toString().isEmpty())
				{
					final long Date = (System.currentTimeMillis()+(3600*12000));
					db.addColumn(new Columns(Integer.valueOf(number.getText().toString()), 
							Date , name.getText().toString(),
							SpreadsheetFileManager.getStoredSpreadsheetURL(MainActivity.this),
							"i" , posttype));
					name.setText("");;
					number.setText("");;
					list = db.getAllColumns();
				}
			}
			
		});
		Synchronous.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				startDisplayLogic();
				
			}
			
		});
		lis.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub\
				list = db.getAllColumns();
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, Activity5Activity.class);
				intent.putParcelableArrayListExtra("all", list);
				startActivity(intent);
			}	
		}	
	);
	}
    


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.filemenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Someone clicked on _something_ in the menu, and since
		// we have only one item, we assume it was the settings
		// menu. Launch it... 
		Intent intent = new Intent();
	    intent.setClass(this, settings.class);
	    startActivity(intent);
		return true;
	}
	
	private boolean establishAccountAndSpreadsheet() {
    	if(AccountManager.hasStoredAccount(this)) {
    		if (SpreadsheetFileManager.hasStoredSpreadsheet(this)) {
    			// Now we can actually do something.
    			return true;
    		} else {
    			// We need to call the ChooseFileActivity from an intent.
    			Intent choose_spreadsheet_intent = new Intent(this, SpreadsheetFileManager.class);
        		startActivityForResult(choose_spreadsheet_intent, kSpreadsheetChoiceIntent);
    		}
    	} else {
    		// We need to force the account picker intent.
    		Intent choose_account_intent = AccountManager.newReturnAuthorizedUserIntent(this);
    		startActivityForResult(choose_account_intent, kAccountChoiceIntent);
    	}
    	return false;
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == kAccountChoiceIntent && resultCode == RESULT_OK) {
			// Do it again, this time to get the spreadsheet.
			if (establishAccountAndSpreadsheet()) {
				swittch.setEnabled(true);
				swit.setEnabled(true);
				pie.setEnabled(true);
				lis.setEnabled(true);
				Synchronous.setEnabled(true);
				send.setEnabled(true);
				startDisplayLogic();
				
			}
			// In the else case, we are waiting for the next spreadsheet choice result.
		} else if (requestCode == kSpreadsheetChoiceIntent && resultCode == RESULT_OK) {
			// We are ready to begin.
			// TODO(nbeckman): This call could actually be redundant as the preferences
			// listener should automatically call startDisplayLogic().
			swittch.setEnabled(true);
			swit.setEnabled(true);
			pie.setEnabled(true);
			lis.setEnabled(true);
			Synchronous.setEnabled(true);
			send.setEnabled(true);
			startDisplayLogic();
		} else {
			Log.w("MainNumbersActivity", "Weird unhandled activity result. requestCode: " + requestCode
					+ " resultCode: " + resultCode);
		}
	}
    
	
	
	private void startDisplayLogic() {
		// By this point we should definitely have an account, so we can enable the controls
		// that allow the user to enter stuff.
		
        // Make sure we are logged in, and have a spreadsheet chosen.
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

		    @Override
		    public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
		       Toast.makeText(getBaseContext(), "你選的是"+choose_type[position], Toast.LENGTH_SHORT).show();
		        if(position == 1)
		          {
		        	posttype = type[1];
		          }
		        else if (position==2)
		        {
		        	posttype =type[3];	
		        }
		        else if (position==3)
		        {
		        	posttype =type[0];	
		        }
		        else if (position==4)
		        {
		        	posttype =type[2];	
		        }
		        else if (position==5)
		        {
		        	posttype =type[4];	
		        }
		    }
		    
		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
		       // TODO Auto-generated method stub
		    }
		 });
		 list = db.getAllColumns();
		 Log.d("getAllBooks()", list.toString());
        (new AsyncTask<String, String, InterfaceUpdateData>(){
        	@Override
        	protected InterfaceUpdateData doInBackground(String... arg0) {
        		final String account = AccountManager.getStoredAccount(MainActivity.this);
        		Log.i("MainNumbersActivity", "Account manager has stored account: " + account);
        		
        		final String spreadsheet_url = SpreadsheetFileManager.getStoredSpreadsheetURL(MainActivity.this);
        		final DefaultSpreadsheetAdapter next_spreadsheet_adapter = 
        				 new DefaultSpreadsheetAdapter(getBaseContext(), spreadsheet_url);
        		spreadsheet_adapter_ = next_spreadsheet_adapter;
        		
        		// Start callback thread that will periodically try to post outstanding expenses. If there had been
        		// one running before, stop the old one so it will no longer post to the old spreadsheet.
        		if (expenses_poster_ != null) {
        			expenses_poster_.stop();
        		}
        		final TextView outstanding_expenses = (TextView)findViewById(R.id.expensesToPostValue);
        		expenses_poster_ = 
        			new OutstandingExpensesPoster(outstanding_expenses, spreadsheet_adapter_);
        		expenses_poster_.start();
        		
        		final long num_outstanding_expenses =
        			spreadsheet_adapter_.NumOutstandingEntries();
        		final InterfaceUpdateData result = new InterfaceUpdateData(num_outstanding_expenses);
    			
    			try {
    				spreadsheet_service_ = 
    						SpreadsheetUtils.setupSpreadsheetServiceInThisThread(MainActivity.this, account);
        			next_spreadsheet_adapter.setSpreadsheetService(spreadsheet_service_);
				} catch (GoogleAuthException e) {
					// Not yet authenticated to use the spreadsheet.
					e.printStackTrace();
				} 
    			return result;
        	}
        	
        	@Override
        	protected void onPostExecute(InterfaceUpdateData update) {
        		if (update == null) {
        			return;
        		}
        		
        		if (update.getNumOutstandingExpenses() != null) {
        			final TextView outstanding_expenses_text_view =
        				(TextView)findViewById(R.id.expensesToPostValue);
        			outstanding_expenses_text_view.setText(
        				update.getNumOutstandingExpenses().toString());
        		}
        	}}).execute();
		// Since there's only one thing you want to do, type in a number,
		// start with the keyboard out and focused on the number box.
        // TODO(nbeckman): This does nothing and I can't get it to work.
		EditText editText = (EditText)findViewById(R.id.expense_amount_textbox);
		editText.requestFocus();
		editText.requestFocusFromTouch();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences shared_prefs, String key) {
		if (key.equals(SpreadsheetFileManager.kNumbersSpreadsheetPreferencesName)) {
			// See if  we've changed the settings TO a spreadsheet, or if we have somehow
			// cleared it.
			if (SpreadsheetFileManager.getStoredSpreadsheet(this) == null ||
					SpreadsheetFileManager.getStoredSpreadsheet(this).length() == 0) {
			} else {
				this.startDisplayLogic();
			}
		}
	}
}
