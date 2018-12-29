package com.example.gal.shotafim;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupHolder {

    private Group appGroup = null;
    private static ArrayList<User> groupMembers = new ArrayList<>();

    private GroupHolder() { }

    public static final GroupHolder instance = new GroupHolder();

    public Group getAppGroup() { return this.appGroup; }

    public void setAppGroup(Group group) { this.appGroup = group; }

    public static ArrayList<User> getGroupMembers() {
        return groupMembers;
    }
    public static void instertNewMember(User user){
        groupMembers.add(user);
    }

    public static void initGroupMembers(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=AuthenticatedUserHolder.instance.getAppUser();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    User temp = dsp.getValue(User.class);
                    if(user.getmGroupName().equals(temp.getmGroupName()) && !user.getEmail().equals(temp.getEmail())){
                        groupMembers.add(dsp.getValue(User.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
