package com.example.pandapanic;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import model.Account;
import model.ChecklistItem;
import model.ToDoListItem;
import database.DbConnection;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_list);
		
		Intent i = getIntent();
		Account currentUser = (Account) i.getSerializableExtra("user");
		MobileServiceClient mClient = DbConnection.connectToAzureService(this);
		TwoDouLissAdapter adapter = new TwoDouLissAdapter(this, R.layout.layout_rowtodo);
		
		ListView toDoList = (ListView) findViewById(R.id.toDoListItem);
		toDoList.setAdapter(adapter);
		
		EditText text =(EditText) findViewById(R.id.text);
		
		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new AddListListener(currentUser, adapter,mClient,text));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

class AddListListener implements OnClickListener{

	private Account currentUser;
	private TwoDouLissAdapter adapter;
	private MobileServiceClient mClient;
	private EditText text;
	
	
	
	public AddListListener(Account currentUser, TwoDouLissAdapter adapter, MobileServiceClient mClient, EditText text){
		this.currentUser = currentUser;
		this.adapter = adapter;
		this.mClient = mClient;
		this.text = text;
	}
	
	@Override
	public void onClick(View v) {
		String desc = text.getText().toString();
		
		ToDoListItem item = new ToDoListItem();
		item.setItemName(desc);
		item.setCompleted(false);
		item.setUserId(currentUser.getId());
		
		mClient.getTable(ToDoListItem.class).insert(item,new TableOperationCallback<ToDoListItem>() {

			@Override
			public void onCompleted(ToDoListItem item,
					Exception exception, ServiceFilterResponse response) {
					// TODO Auto-generated method stub
					if (exception == null) {
						Log.e("Add Item", "Success");
						adapter.add(item);
					} else {
						Log.e("Add Item", "Failure");
					}
				}
			});
		
	}
	
}
