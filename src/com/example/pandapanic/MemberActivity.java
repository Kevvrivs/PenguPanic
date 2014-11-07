package com.example.pandapanic;

import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import model.Account;
import model.Invite;
import model.ToDoListItem;
import database.DbConnection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class MemberActivity extends Activity{
	
	EditText txtUser;
	Button btnAdd;
	
	Account user;
	MobileServiceClient mClient;
	MobileServiceTable<Account> mAccount;
	MobileServiceTable<Invite> mInvite;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		Intent i = getIntent();
		mClient = DbConnection.connectToAzureService(this);
		mAccount = mClient.getTable(Account.class);
		user = (Account) i.getSerializableExtra("user");
		txtUser = (EditText) findViewById(R.id.txtUser);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new addMemberListener(user));
	}
	
	public AlertDialog createDialog(String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setTitle("Message");
	
		alertDialogBuilder.setNeutralButton(R.string.ok,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		
		});
		return alertDialogBuilder.create();
	}
	
	class addMemberListener implements OnClickListener {

		final Account temp;
		public addMemberListener(Account account){
			this.temp = account;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mInvite = mClient.getTable(Invite.class);
			String user = txtUser.getText().toString();
			mAccount.where().field("username").eq(user)
			.execute(new TableQueryCallback<Account>() {

				@Override
				public void onCompleted(List<Account> account,
						int position, Exception exception,
						ServiceFilterResponse response) {
					// TODO Auto-generated method stub
					if (exception == null) {

						if (account.size() == 0) {
							Log.e("Account","No such user");
							
						} else {
							final Account invited = account.get(0);
							Invite invite = new Invite();
							invite.setGroupId(temp.getGroupId());
							invite.setUserId(invited.getId());
							invite.setStatus(false);
							mClient.getTable(Invite.class).insert(invite,new TableOperationCallback<Invite>() {

								@Override
								public void onCompleted(Invite item,
										Exception exception, ServiceFilterResponse response) {
										// TODO Auto-generated method stub
										if (exception == null) {
											Log.e("Invite", "Invited user");
											createDialog("Invited " + invited.getUsername() + " to the group.");
										} else {
											Log.e("Add Item", "Failure");
										}
									}
								});
					
						}

					} else {
						Log.e("Login Status", "Failure");
					}

				}

			});;
			
			
		}
		
	}
}
