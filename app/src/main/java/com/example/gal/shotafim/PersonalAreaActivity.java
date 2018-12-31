package com.example.gal.shotafim;

import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonalAreaActivity extends AppCompatActivity {
    //Regular User Views
    private TextView personalName;
    private TextView groupName;
    private TextView groupAdminName;
    private ListView listOfUsers;

    //list
    ArrayList<User> users = new ArrayList<>();
    ArrayList<String> usersNames = new ArrayList<>();

    //Admin Area Views
    private EditText invite_editTxt;
    private Button invite_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);

        personalName = findViewById(R.id.usr_name_dynamic);
        groupName = findViewById(R.id.group_name_dynamic);
        groupAdminName = findViewById(R.id.group_admin_dynamic);

        listOfUsers = findViewById(R.id.users_group_list_personal);

        invite_btn = findViewById(R.id.invite_btn_admin);
        invite_editTxt = findViewById(R.id.invite_edittxt_admin);
        invite_editTxt.setVisibility(View.INVISIBLE);
        invite_btn.setVisibility(View.INVISIBLE);

        //FIRST CHECK IF USER IS GROUP ADMIN
        visibilityForAdmin();

        personalName.setText(AuthenticatedUserHolder.instance.getAppUser().getName());
        groupName.setText(GroupHolder.instance.getAppGroup().getGroupName());
        groupAdminName.setText(GroupHolder.instance.getAppGroup().getAdminUserID());
        users = GroupHolder.getGroupMembers();
        usersNames = getUsersNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1, usersNames);
        listOfUsers.setAdapter(adapter);

        //Invite//
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInviteEmail();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        usersNames.clear();
        users.clear();
    }
    private ArrayList<String> getUsersNames(){
        ArrayList<String> tmp = new ArrayList<>();
        for(User user : users){
            //Only if it doesn't exist already.
            if(!tmp.contains(user.getEmail()))
                tmp.add(user.getEmail());
        }
        return tmp;
    }

    private void visibilityForAdmin(){
        if(isAdmin()){
            showForAdmin();
        }else {
            showForRegularUsr();
        }
    }
    private void showForRegularUsr() {
        invite_editTxt.setVisibility(View.INVISIBLE);
        invite_btn.setVisibility(View.INVISIBLE);
    }

    private void showForAdmin() {
        invite_editTxt.setVisibility(View.VISIBLE);
        invite_btn.setVisibility(View.VISIBLE);
    }
    private boolean isAdmin(){
        return AuthenticatedUserHolder.instance.getAppUser().getName().equals(GroupHolder.instance.getAppGroup().getAdminUserID());
    }
    private void sendInviteEmail(){
        if(invite_editTxt.getText().toString().isEmpty() || !invite_editTxt.getText().toString().contains("@")){
            invite_editTxt.setError("Must enter valid email");
        }else {
            String message =
                    "Hi,\n" + AuthenticatedUserHolder.instance.getAppUser().getName() + ""
                            + " invite you to Shotafim. \n Download Shotafim app in this Link: \n <LINK> \n " +
                            "Your group ID is : " + AuthenticatedUserHolder.instance.getAppUser().getmGroupName() + "\nWaiting for you!";

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", invite_editTxt.getText().toString(), null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Shotafim Invitation");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
    }

}

