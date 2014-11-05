package com.example.pandapanic;

import model.Account;
import model.ChecklistItem;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import database.DbConnection;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChecklistActivity extends Activity {

	ItemAdapter adapter;
	MobileServiceClient mClient;

	ListView listItems;
	Account currentUser;

	EditText txtItemName;
	EditText txtQuantity;

	Button btnAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklist);
		Intent i = getIntent();
		currentUser = (Account) i.getSerializableExtra("user");
		mClient = DbConnection.connectToAzureService(this);

		adapter = new ItemAdapter(this, R.layout.layout_rowitem);

		
		listItems = (ListView) findViewById(R.id.listItem);
		listItems.setAdapter(adapter);
		
		listItems.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("Delete",adapter.getItem(position).getItemName());
				return false;
			}
			
		});

		txtItemName = (EditText) findViewById(R.id.txtItemName);
		txtQuantity = (EditText) findViewById(R.id.txtQuantity);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new AddItemListener(currentUser, adapter,
				mClient, txtItemName, txtQuantity));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checklist, menu);
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

class AddItemListener implements OnClickListener {

	private EditText txtItemName;
	private EditText txtQuantity;
	private Account currentUser;
	private ItemAdapter adapter;
	private MobileServiceClient mClient;

	public AddItemListener(Account currentUser, ItemAdapter adapter,
			MobileServiceClient mClient, EditText txtItemName,
			EditText txtQuantity) {
		this.currentUser = currentUser;
		this.adapter = adapter;
		this.currentUser = currentUser;
		this.txtItemName = txtItemName;
		this.txtQuantity = txtQuantity;
		this.mClient = mClient;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String itemName = txtItemName.getText().toString();
		String quantity = txtQuantity.getText().toString();

		ChecklistItem item = new ChecklistItem();
		item.setItemName(itemName);
		item.setQuantity(Float.valueOf(quantity));
		item.setCompleted(false);
		item.setUserId(currentUser.getId());

		mClient.getTable(ChecklistItem.class).insert(item,new TableOperationCallback<ChecklistItem>() {

		@Override
		public void onCompleted(ChecklistItem item,
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
