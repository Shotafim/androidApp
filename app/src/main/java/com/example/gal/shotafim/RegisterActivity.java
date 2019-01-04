package com.example.gal.shotafim;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    //Button
    private Button signUpBtn;
    //EditText
    private EditText nameTxt;
    private EditText passTxt;
    private EditText emailTxt;
    private EditText aptTxt;
    private EditText aptIDTxt;
    private EditText cityTxt;
    private EditText streetTxt;
    private EditText countryTxt;

    //RadioButtons
    private RadioButton hasAptRadio;
    private RadioButton createAptRadio;
    private RadioGroup radioGroup;

    private Group regUserGroup;
    private Address address;
    private User user;

    //Geolocation
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView geoText;
    private double location_lat;
    private double location_lng;

    //DEBUG
    private double DEBUG_LAT_TLV = 32.120397;
    private double DEBUG_LNG_TLV =32.120397;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        nameTxt = findViewById(R.id.nameTxt);
        passTxt = findViewById(R.id.passTxt);
        emailTxt = findViewById(R.id.emailTxt);
        aptTxt = findViewById(R.id.aptTxt);
        aptIDTxt = findViewById(R.id.aptIDTxt);
        cityTxt = findViewById(R.id.cityTxt);
        streetTxt = findViewById(R.id.streetTxt);
        countryTxt = findViewById(R.id.countryTxt);
        hasAptRadio = findViewById(R.id.radioButtonHas);
        createAptRadio = findViewById(R.id.radioButtonCreate);
        signUpBtn = findViewById(R.id.signUpBtn);

        geoText = findViewById(R.id.geoTextView);

        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioGroup.getCheckedRadioButtonId() == hasAptRadio.getId()) {
                    createAptInvisiable();
                } else {
                    //Geo setup
                    setup_geo_coordinates();

                    createAptVisiable();

                    int _id = new Random().nextInt(9000) + 1000;
                    aptIDTxt.setText("" + _id);
                    set_address_fields_by_geo(location_lat,location_lng);

                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickSignUp();
            }
        });

        //Geo
       setup_geo_coordinates();




    }

    private void set_address_fields_by_geo(double lat, double lng){

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            Log.d("Coordinates: ",""+addresses.get(0).getLocality() +" " +addresses.get(0).getCountryName()  +" " +addresses.get(0).getThoroughfare()); // Debug log
            cityTxt.setText(addresses.get(0).getLocality());
            countryTxt.setText(addresses.get(0).getCountryName());
            streetTxt.setText(addresses.get(0).getThoroughfare());

        }
        else {
            // do your stuff
        }

    }

    private void createAptVisiable() {
        aptTxt.setVisibility(View.VISIBLE);
        cityTxt.setVisibility(View.VISIBLE);
        streetTxt.setVisibility(View.VISIBLE);
        countryTxt.setVisibility(View.VISIBLE);


    }

    private void createAptInvisiable() {
        aptTxt.setVisibility(View.INVISIBLE);
        cityTxt.setVisibility(View.INVISIBLE);
        streetTxt.setVisibility(View.INVISIBLE);
        countryTxt.setVisibility(View.INVISIBLE);
    }

    public void OnClickSignUp() {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        // Sets the user data by the clicked RadioBtn
        final User user = new User(
                nameTxt.getText().toString(),
                emailTxt.getText().toString(),
                passTxt.getText().toString(),
                aptIDTxt.getText().toString());

        Log.v("USER DATA : ###", user.toString());


        address = new Address(countryTxt.getText().toString(),
                cityTxt.getText().toString(),
                aptTxt.getText().toString(),
                streetTxt.getText().toString());

        if (createAptRadio.isChecked()) {
            regUserGroup = new Group(Generator.nextSessionId(),
                    aptIDTxt.getText().toString(),
                    user.getName(),
                    4,
                    address);
        }


        if (HasEmptyFields() || (radioGroup.getCheckedRadioButtonId() == -1)) {
            Log.v("REGISTER:::", "RADIO ID IN IF: " + radioGroup.getCheckedRadioButtonId());
            Log.v("REGISTER:::", "RADIO hasAPT: " + hasAptRadio.getId());
            Log.v("REGISTER:::", "RADIO createAPT: " + createAptRadio.getId());
            Toast.makeText(RegisterActivity.this, "There is empty field,try again!", Toast.LENGTH_LONG).show();
        } else {
            db.child("Users").child(user.getEmail().replace(".", "|").toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(RegisterActivity.this, "Email already registered ", Toast.LENGTH_LONG).show();
                    } else { // User dont exist in the system, adding new user to db.
                        if (createAptRadio.isChecked()) {
                            //looking if the group is already exist.
                            db.child("Group").child(regUserGroup.getGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(RegisterActivity.this, "Group already exists", Toast.LENGTH_LONG).show();
                                    } else { // their is no such group , we can add the user and the group to the database.
                                        db.child("Users").child(user.getEmail().replace(",", "|")).setValue(user);
                                        db.child("Group").child(user.getmGroupName()).setValue(regUserGroup);
                                        Toast.makeText(RegisterActivity.this, "User Registration & Group Complete", Toast.LENGTH_LONG).show();
                                        Intent s = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(s);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else { // hasAptBtn checked
                            db.child("Group").child(user.getmGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                             @Override
                                                                                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                 if (dataSnapshot.exists()) {
                                                                                                                     db.child("Users").child(user.getEmail().replace(",", "|")).setValue(user);
                                                                                                                     Toast.makeText(RegisterActivity.this, "You added to group: " + user.getmGroupName(), Toast.LENGTH_LONG).show();
                                                                                                                 } else {
                                                                                                                     Toast.makeText(RegisterActivity.this, "Their is no such group: " + user.getmGroupName(), Toast.LENGTH_LONG).show();
                                                                                                                 }
                                                                                                             }

                                                                                                             @Override
                                                                                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                             }
                                                                                                         }
                            );
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    /**
     *  Check if all field is filled.
     * @return True / False , True if there is empty field ,False otherwise.
     */
    private boolean HasEmptyFields() {
        if (radioGroup.getCheckedRadioButtonId() == hasAptRadio.getId()) {
            return (aptIDTxt.getText().toString().isEmpty() ||
                    nameTxt.getText().toString().isEmpty() ||
                    emailTxt.getText().toString().isEmpty() ||
                    passTxt.getText().toString().isEmpty());
        } else {
            return (
                    aptTxt.getText().toString().isEmpty() ||
                            streetTxt.getText().toString().isEmpty() ||
                            countryTxt.getText().toString().isEmpty() ||
                            cityTxt.getText().toString().isEmpty() ||
                            nameTxt.getText().toString().isEmpty() ||
                            emailTxt.getText().toString().isEmpty() ||
                            passTxt.getText().toString().isEmpty());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure();
                break;
            default:
                break;
        }
    }

    private void configure(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    private void setup_geo_coordinates(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geoText.setText("\n" + location.getLongitude() + " " + location.getLatitude());
                location_lat = location.getLatitude();
                location_lng = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure();
    }



}
