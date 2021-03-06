package com.cabot.calculator.activities;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cabot.calculator.data.Expression;
import com.cabot.calculator.data.ExprnDataSource;
import com.cabot.calculator.fragments.SearchDialogFragment;
import com.example.calculator.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LogDisplayActivity extends SherlockFragmentActivity implements
		SearchDialogFragment.inputDialogListener {
	Bundle send_exprn;
	ArrayList<Expression> arrayList, searchList;
	List<Expression> expList;
	ListView list;
	HistoryAdapter adp;
	ExprnDataSource eds;
	MenuItem mndelte;
	MenuItem searchItem;
	TextView heading;
	final int HISTORY = 1, SEARCH = 2;
	int displayStatus;
	logDisplayTask loadingProgess;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		
		arrayList = new ArrayList<Expression>();
		eds = new ExprnDataSource(LogDisplayActivity.this);
		loadingProgess = new logDisplayTask();
		loadingProgess.execute();
		list = (ListView) findViewById(R.id.listLog);
		heading = (TextView) findViewById(R.id.txtHeader);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.log_display_menu, menu);
		searchItem = menu.findItem(R.id.sbytext);
		SearchView searchView = (SearchView) searchItem.getActionView();
		mndelte = (MenuItem) menu.findItem((R.id.delete));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.icClear:
			arrayList.clear();
			eds.removeAll();
			Toast.makeText(this, R.string.history_clear_msg, Toast.LENGTH_SHORT).show();
			this.finish();
			break;
			
		case R.id.delete:
			expList = new ArrayList<Expression>();
			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i).isChecked()) {
					expList.add(arrayList.get(i));
					arrayList.remove(i--);
				}
			}
			adp.notifyDataSetChanged();
			list.setAdapter(adp);
			Toast.makeText(this, "" + expList.size() + " items deleted",
					Toast.LENGTH_SHORT).show();
			eds.removeSelected(expList);
			mndelte.setVisible(false);
			break;
			
		case R.id.icBack:
			if (displayStatus == HISTORY)
				this.finish();
			else if (displayStatus == SEARCH)
				setAdapter(arrayList, HISTORY);
			else
				this.finish();
			break;
			
		case R.id.sbytext:
			showEditDialog(R.layout.search_dialog_by_text,getResources().getString(R.string.search_text));
			break;
			
		case R.id.sbydate:
			showEditDialog(R.layout.search_dialog_search_by_date,
					getResources().getString(R.string.search_by_date));
			break;
			
		case R.id.sbtdate:
			showEditDialog(R.layout.search_dialog_bt_date,
					getResources().getString(R.string.search_bt_dates));
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Adapter class for loading Expression list in list view
	private class HistoryAdapter extends ArrayAdapter<Expression> {
		
		ArrayList<Expression> exprn;
		private LayoutInflater mInflater;

		public HistoryAdapter(Context context, int layoutResourceId,
				ArrayList<Expression> data) {
			super(context, layoutResourceId, data);
			mInflater = LayoutInflater.from(context);
			this.exprn = data;
			heading.setText(getResources().getString(R.string.History) + "                			"
					+ data.size() + " items found");
		}

		// Inflate different fields to the objects in list view items
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listview_row_layout,
						null);
				holder = new ViewHolder();
				holder.check = (CheckBox) convertView.findViewById(R.id.check);
				holder.textExprn = (TextView) convertView
						.findViewById(R.id.tvExpr);
				holder.textDate = (TextView) convertView
						.findViewById(R.id.tvTimeStamp);
				holder.delete = (ImageButton) convertView
						.findViewById(R.id.btdelete);
				convertView.setTag(holder);
			} 
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
				
			String date = "";
			SimpleDateFormat istFormat = new SimpleDateFormat(
					getResources().getString(R.string.date_format));
			holder.textExprn.setText(exprn.get(position).getExprn());
			date = istFormat.format(
					new Date(exprn.get(position).getTimeStamp())).toString();
			holder.textDate.setText(date);

			holder.check.setTag(position);
			holder.delete.setTag(position);
			// on click listener method of the expression
			/*
			 * on selecting the expression it will be displayed in the text
			 * field in main activity page
			 */
			holder.textExprn.setOnClickListener(new View.OnClickListener() {
				@SuppressLint("NewApi")
				@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
				@Override
				public void onClick(View v) {
					final TextView tv = (TextView) v;
					v.animate().setDuration(1000).alpha(0)
							.withEndAction(new Runnable() {
								@Override
								public void run() {
									Intent moveBack = new Intent(
											LogDisplayActivity.this,
											CalculatorActivity.class);
									send_exprn = new Bundle();
									send_exprn.putString("expr", tv.getText()
											.toString());
									moveBack.putExtras(send_exprn);
									setResult(2, moveBack);
									finish();
								}
							});
				}
			});
			
			/*
			 * On checking the list items a boolean value is set 
			 * 
			 */
			holder.check.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox c = (CheckBox) v;
					int row_id = (Integer) v.getTag();
					arrayList.get(row_id).setChecked(c.isChecked());
					for (int i = 0; i < arrayList.size(); i++) {
						if (arrayList.get(i).isChecked()) {
							mndelte.setVisible(true);
							break;
						}
						mndelte.setVisible(false);
					}
				}
			});
			
			/*
			 * on clicking the delete button in each item this code will be called
			 * 
			 */
			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int row_id = (Integer) v.getTag();
					eds.removeExprn(arrayList.get(row_id));
					Toast.makeText(LogDisplayActivity.this, "1 item deleted",
							Toast.LENGTH_SHORT).show();
					arrayList.remove(row_id);
					adp.notifyDataSetChanged();
				}
			});
			return convertView;
		}
	}

	// Holder class that holds the three objects in a list item
	class ViewHolder {
		CheckBox check;
		TextView textExprn, textDate;
		ImageButton delete;
	}
//this method will display the dialog fragment for searching
	private void showEditDialog(int layout_id, String title) {
		FragmentManager fm = getSupportFragmentManager();
		SearchDialogFragment searchDialog = new SearchDialogFragment();
		searchDialog.layoutID = layout_id;
		searchDialog.heading = title;
		searchDialog.show(fm, getResources().getString(R.string.search_text));
	}

	//implementation of the methods in the listener in SearchDialog fragment
	
	/* On clicking the done button in the dialog fragment for searching by date
	* this method will be called
	*/	
	@Override
	public void onFinishEditDialog(Date searchDate) {

		searchList = new ArrayList<Expression>();
		for (int i = 0; i < arrayList.size(); i++) {
			Date d = new Date(arrayList.get(i).getTimeStamp());
			String dateinList, sDate;
			dateinList = formatDate(d);
			sDate = formatDate(searchDate);
			if (dateinList.equals(sDate)) {
				searchList.add(arrayList.get(i));
			}
		}
		setAdapter(searchList, SEARCH);
	}
	/*
	 * 
	 * @On clicking the done button in the dialog fragment for searching between date
	 * This method will be called
	 */
	@Override
	public void onFinishEditDialog(Date searchStartDate, Date searchEndDate) {
		searchList = new ArrayList<Expression>();

		for (int i = 0; i < arrayList.size(); i++) {
			Date d = new Date(arrayList.get(i).getTimeStamp());
			if (d.compareTo(searchStartDate) > 0
					&& d.compareTo(searchEndDate) <= 0) {
				searchList.add(arrayList.get(i));
			}
		}
		setAdapter(searchList, SEARCH);
	}

	/*
	 * 
	 * @On clicking the done button in the dialog fragment for searching by text date
	 * This method will be called
	 */
	@Override
	public void onFinishEditDialog(String searchText) {
		searchList = new ArrayList<Expression>();

		for (int i = 0; i < arrayList.size(); i++) {
			int posIndex = arrayList.get(i).getExprn().indexOf(searchText);
			if (posIndex == -1) {
				continue;
			} else {
				searchList.add(arrayList.get(i));
			}
		}
		setAdapter(searchList, SEARCH);
	}
	
	//Method for formatting date in required format
	public String formatDate(Date d) {
		SimpleDateFormat istFormat = new SimpleDateFormat(getResources().getString(R.string.simple_date));
		return istFormat.format(d);
	}

	//Method to set the items in the Listview once the list is ready 
	public void setAdapter(ArrayList<Expression> listitems, int status) {
		displayStatus = status;
		adp = new HistoryAdapter(this, R.layout.listview_row_layout, listitems);
		list.setAdapter(adp);
	}

	//Asynchronous task to be called before loading the items in the list view
	public class logDisplayTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Showing the progress bar
			progressDialog = ProgressDialog.show(LogDisplayActivity.this, "",
					getResources().getString(R.string.loading_msg), true);

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			//method to get all the saved expressions in database
			arrayList = eds.getAllExpressions();
			return null;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			
			progressDialog.dismiss();
			setAdapter(arrayList, HISTORY);
		}
	}

}