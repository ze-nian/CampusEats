package com.example.campuseats;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuseats.Room.CoinData;
import com.example.campuseats.Room.MainAdapter;
import com.example.campuseats.Room.MainData;
import com.example.campuseats.Room.RoomDB;
import com.example.campuseats.ui.home.HomeFragment;
import com.example.campuseats.ui.s1.S1Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseats.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Menu menu = null;
        //MenuItem MI = menu.findItem(R.id.action_about);

        /*Button btn_about = (Button) menu;
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ShowMsgDialog("?????????????????????????????????????????????");
            }
        });*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("http://www.google.com.tw");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public List<MainData> getAllData() {
        return dataList;
    }

    //?????????????????????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // ?????????????????????id
        int id = item.getItemId();

        // ??????id??????????????????????????????????????????
        if (id == R.id.action_about) {
            // ??????????????????????????????
            ShowMsgDialog("\n???????????????????????????\n" +
                    "C107118130  ?????????\n" +
                    "C107118130@nkust.edu.tw\n" +
                    "\n" +
                    "??????????????????\n" +
                    "C107118120 ?????????\n" +
                    "C107118120@nkust.edu.tw\n" +
                    "\n" +
                    "????????????\n" +
                    "C107118126  ?????????\n" +
                    "C107118126@nkust.edu.tw\n");

        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowMsgDialog(String Msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("????????????");
        alertDialog.setMessage(Msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                //???????????????????????? ?????????????????? ????????????
            }
        };;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // ??????mConnReceiver?????????IntentFilter??????????????????????????????????????????
        this.registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // ????????????
        this.unregisterReceiver(mConnReceiver);
    }

    // ????????????BroadcastReceiver?????????mConnReceiver
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // ???????????????????????????????????????????????????

            // ????????????????????????
            if(isNetworkAvailable()) {
                // ?????????????????????????????????????????????
            }
            else {
                // ????????????
                Toast.makeText(MainActivity.this, "?????????!", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    };

    // ????????????????????????????????????
    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null &&
                networkInfo.isConnected();
    }

}