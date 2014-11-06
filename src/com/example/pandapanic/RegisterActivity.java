package com.example.pandapanic;

import java.net.InetAddress;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import model.Account;
import model.ChecklistItem;
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

public class RegisterActivity extends Activity{
	
	EditText txtUsername;
	EditText txtPassword;
	EditText txtCPassword;
	EditText txtEmail;
	Button btnRegister;
	
	String validationMessage;
	MobileServiceClient mClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		txtUsername = (EditText) findViewById(R.id.reguserText);
		txtPassword = (EditText) findViewById(R.id.regpassText);
		txtCPassword = (EditText) findViewById(R.id.confpassText);
		txtEmail = (EditText) findViewById(R.id.emailText);
		btnRegister = (Button) findViewById(R.id.registerButton);
		btnRegister.setOnClickListener(new registerListener());
		mClient = DbConnection.connectToAzureService(this);
	}
	
	public Account validateInput(){
		Boolean flag = true;
		Account registerUser = null;
		validationMessage = "";
		String message = "";
		String password = txtPassword.getText().toString();
		String cpassword = txtCPassword.getText().toString();
		String email = txtEmail.getText().toString();
		String username = txtUsername.getText().toString();
		
		if(username.isEmpty()){
			message += "Username cannot be empty\n";
			flag = false;
		}
		
		if(password.isEmpty()){
			message += "Please provide a password";
			flag = false;
		}
		
		if(!password.equals(cpassword)){
			message += "Passwords did not match\n";
			flag = false;
		}
		
		if(email.isEmpty()){
			message += "Please provide an email address\n";
			flag = false;
		}
		if(flag) {
			registerUser = new Account();
			registerUser.setUsername(username);
			registerUser.setPassword(password);
			registerUser.setEmail(email);
			validationMessage = "Successfully created an account!";
		} else {
			validationMessage = message;
		}
		return registerUser;
	}
	
	public AlertDialog createDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(validationMessage);
		alertDialogBuilder.setTitle("Message");
	
		alertDialogBuilder.setNeutralButton(R.string.ok,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		
		});
		return alertDialogBuilder.create();
	}
	
	class registerListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Account registerUser = validateInput();
			if(registerUser != null){
				Log.e("Register Status: ", "Success");
				mClient.getTable(Account.class).insert(registerUser,new TableOperationCallback<Account>() {

					@Override
					public void onCompleted(Account item,
							Exception exception, ServiceFilterResponse response) {
							// TODO Auto-generated method stub
							if (exception == null) {
								Log.e("Add Item", "Success");
								Intent i = new Intent(getApplicationContext(),MenuActivity.class);
								i.putExtra("user", registerUser);
								startActivity(i);
							} else {
								Log.e("Add Item", "Failure");
							}
				
						}

					});
			} else {
				Log.e("Register Status: ", "Failed");
				createDialog().show();
			}
			
		}
		
	}
}

