package com.bladesappfest.firebase.codelab.racketstars.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bladesappfest.firebase.codelab.racketstars.R;
import com.bladesappfest.firebase.codelab.racketstars.fragments.MessengerFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import android.graphics.drawable.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MainMainActivity extends AppCompatActivity  {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    FrameLayout mapLayout;
    private String mUsername;
    private String mPhotoUrl;

    ArrayList<String> ar=new ArrayList<>();
    ArrayList<String> getcurrentuser()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsername = "ANONYMOUS";
        mPhotoUrl="";

        ar=new ArrayList<>();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity

        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }

            ar.add(mUsername);
            ar.add(mPhotoUrl);
        }

        return  ar;
    }

    void savetodatabase(String courtname, boolean ownID)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();// .getReference("courts");

        ArrayList<String> username = getcurrentuser();
        if (username.isEmpty()) return;

        if (courtname == "test") myRef.child("courts").child("trst").child(username.get(0)).child(username.get(0)).setValue(username.get(1));
        else {

            if (ownID == true)
                myRef.child("courts").child(courtname).child(chosencourt + "booked").child(chosencourt + "booked"+username.get(0)).setValue(username.get(1));
            else
                myRef.child("courts").child(courtname).child(chosencourt + "other").child(username.get(0)).setValue(username.get(1));
        }

        //myRef.child("courts").child(courtname).child(username.get(0)).setValue(username.get(1));
    }

    String chosencourt="";

    void readfromdatabase(final String courtname)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String values = dataSnapshot.toString();
                int ix = values.indexOf(chosencourt+"other");
                String _partnername="";
                if (ix!=-1) {
                    String partnername = values.substring(ix + (chosencourt + "other").length());
                    int ix2 = partnername.indexOf(',');
                    int ix3 = partnername.indexOf('}');
                    partnername = partnername.substring(0, Math.min(ix2, ix3)).trim();
                    if (partnername.length() != 0) _partnername = partnername;

                    if (_partnername.length() != 0) {
                        Toast.makeText(getApplicationContext(), "This partner " + _partnername + " has confirmed game with you at " + courtname, Toast.LENGTH_LONG).show();
                    }
                }
                else {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();// .getReference("courts");

                    ArrayList<String> username = getcurrentuser();
                    if (!username.isEmpty()) {
                        int ix4 = values.indexOf(chosencourt + "booked");

                        if (ix4 != -1) {

                            int ix5 = values.indexOf(chosencourt + "booked"+username.get(0));

                            if (ix5==-1) savetodatabase(chosencourt, false);

                        } else {

                            savetodatabase(chosencourt, true);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng courtUniPutra = new LatLng(2.991707, 101.716258);
            LatLng courtPrecint11 = new LatLng(2.958782, 101.677670);
            LatLng courtKelabCommunity = new LatLng(2.938286, 101.644500);
            LatLng courtBankAcademy = new LatLng(2.964501, 101.733939);
            LatLng courtPark1 = new LatLng(2.961852, 101.666857);

            //savetodatabase("Playground Uni Putra");
            //readfromdatabase("Playground Uni Putra");

            mMap.addMarker(new MarkerOptions().position(courtUniPutra).title("Playground Uni Putra"));
            mMap.addMarker(new MarkerOptions().position(courtPrecint11).title("Playground Precint11"));
            mMap.addMarker(new MarkerOptions().position(courtKelabCommunity).title("Playground Kelab"));
            mMap.addMarker(new MarkerOptions().position(courtBankAcademy).title("Playground Bank Academy")
                    .icon(BitmapDescriptorFactory.fromBitmap(  BitmapFactory.decodeResource(getResources(),
                            R.drawable.coach_men))));
            mMap.addMarker(new MarkerOptions().position(courtPark1).title("Playground Park1")
                    .icon(BitmapDescriptorFactory.fromBitmap(  BitmapFactory.decodeResource(getResources(),
                            R.drawable.coach_women))));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(courtUniPutra));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(courtUniPutra,10));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    chosencourt=marker.getTitle();

                    savetodatabase("test", true);
                    readfromdatabase(chosencourt);

                    return true;
                }
            });


        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, new MessengerFragment());
                    fragmentTransaction.commit();
                    mapLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_people:
                    mapLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_fields:

                    mapLayout.setVisibility(View.VISIBLE);

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(onMapReadyCallback);


                    return true;
            }
            return false;
        }

    };

    private GoogleMap mMap;

    /*@Override
    public void onMapReady(GoogleMap googleMap) {

    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mapLayout = (FrameLayout) findViewById(R.id.maps);
        mapLayout.setVisibility(View.GONE);
    }

}
