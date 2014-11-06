package com.example.pandapanic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import database.DbConnection;

import model.Account;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	MobileServiceClient mClient;
	MobileServiceTable<Account> mAccount;

	EditText username;
	EditText password;
	Button loginButton;
	Button registerButton;
	
	Account user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		username = (EditText) findViewById(R.id.txtUser);
		password = (EditText) findViewById(R.id.txtPassword);
		loginButton = (Button) findViewById(R.id.Login);
		loginButton.setOnClickListener(new loginListener());
		registerButton = (Button) findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new registerListener());
		mClient = DbConnection.connectToAzureService(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	class registerListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
			startActivity(i);
		}
	}
	
	class loginListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String uname = username.getText().toString();
			String pword = password.getText().toString();
			mAccount = mClient.getTable(Account.class);
			mAccount.where().field("username").eq(uname).and()
					.field("password").eq(pword)
					.execute(new TableQueryCallback<Account>() {

						@Override
						public void onCompleted(List<Account> account,
								int position, Exception exception,
								ServiceFilterResponse response) {
							// TODO Auto-generated method stub
							if (exception == null) {

								if (account.size() == 0) {
									Log.e("Login","Failure");
									
								} else {
									Log.e("Login","Success");
									user = account.get(0);
									Log.e("Login",user.getId());
									Intent i = new Intent(getApplicationContext(),MenuActivity.class);
									i.putExtra("user", user);
									startActivity(i);
								}

							} else {
								Log.e("Login Status", "Failure");
							}

						}

					});
		}
	}
}
