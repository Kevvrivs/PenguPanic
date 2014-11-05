package com.example.pandapanic;


import model.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity{

	Account user;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		Intent i = getIntent();
		user = (Account) i.getSerializableExtra("user");
		Button btnChecklist = (Button) findViewById(R.id.checklistButton);
		btnChecklist.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),ChecklistActivity.class);
				i.putExtra("user", user);
				startActivity(i);
				
			}
			
		});
		Button btnTodo = (Button) findViewById(R.id.disasterButton);
		btnTodo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),ToDoListActivity.class);
				i.putExtra("user", user);
				startActivity(i);
			}
			
		});
		
	}
	
}


	

