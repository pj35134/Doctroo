package com.skincancer.doctro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Navdrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth firebaseAuth;
    Fragment currentFragment;
    ImageView imageView;
     FirebaseUser user;
     String uid;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    DatabaseReference databaseReference;

     SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        NavigationView navigationView = findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);
        final TextView title = headerView.findViewById(R.id.title);
        final TextView email = headerView.findViewById(R.id.textView);
        imageView = headerView.findViewById(R.id.imageView);

        user = firebaseAuth.getCurrentUser();
        uid =user.getUid();

        StorageReference storageReference = firebaseStorage.getReference();
      /*  progressDialog.setMessage("Loading");
        progressDialog.show();*/

        storageReference.child(firebaseAuth.getUid()).child("Images/Profilepic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imageView);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        // DatabaseReference databaseReference = firebaseDatabase.getReference().child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           /* UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
            username.setText("name"+userProfile.getUserName());
            email.setText("email"+userProfile.getUserEmail());*/


                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("userName").getValue().toString();
                    String userEmail = dataSnapshot.child("userEmail").getValue().toString();
                    title.setText(userName);
                    email.setText(userEmail);






                }
               // progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Toast.makeText(ProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });







        sharedPreferences = getSharedPreferences("Userinfo", MODE_PRIVATE);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        currentFragment = new Mainfragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            {
            drawer.closeDrawer(GravityCompat.START);
            }
        else
            {
            if(currentFragment instanceof Mainfragment) {

                super.onBackPressed();

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finishAffinity();
                System.exit(0);

            }
            else{
                currentFragment = new Mainfragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navdrawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
          /*  currentFragment = new Login_fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();
*/

           currentFragment = new Mainfragment();
           getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();

        } else if (id == R.id.gallery) {


           /* currentFragment = new Register_fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();
*/
        } else if (id == R.id.Profile) {
            currentFragment = new ProfileActivity();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();


        } else if (id == R.id.Change_password) {
            currentFragment = new changepassword_fragments();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,currentFragment).commit();

        } else if (id == R.id.logout) {
            //preferences.edit().putBoolean("rememberme", false).apply();
           /* Log.i("response","before"+this.user);
           if(this.user!=null) {
               this.user =null;
               this.uid=null;

               Log.i("response","after"+this.uid);
           }
            Log.i("response","againafter"+uid);*/
            firebaseAuth.signOut();
            sharedPreferences.edit().putBoolean("rememberme", false).apply();
            startActivity(new Intent(this, Tab_using_fragments.class));
            finish();



          /*  Intent intent = new Intent(getApplicationContext(), Tab_using_fragments.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            /*finishAffinity();
           System.exit(0);*/




        } else if (id == R.id.aboutus) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
   /* public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        return false;
    }*/
}
