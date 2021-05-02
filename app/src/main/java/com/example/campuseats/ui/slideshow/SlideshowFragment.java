package com.example.campuseats.ui.slideshow;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseats.MainActivity;
import com.example.campuseats.R;
import com.example.campuseats.Room.MainAdapter;
import com.example.campuseats.Room.MainData;
import com.example.campuseats.Room.RoomDB;

import java.util.List;

public class SlideshowFragment extends Fragment {
    public Button btn_check;
    private View RootView;
    List<MainData> dataList;
    RoomDB database;
    MainAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    boolean check;

    //notify的宣告
    private static final String ACTION_UPDATE_NOTIFICATION = "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    //private NotificationReceiver mReceiver = new NotificationReceiver();
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;
    //notify的宣告

    //spinner
    private Spinner sp;
    private TextView t13;
    private boolean ch=true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View SlideshowView = inflater.inflate(R.layout.fragment_slideshow, container, false);
        RootView = SlideshowView;
        btn_check = (Button) SlideshowView.findViewById(R.id.checkout);

        //顯示item清單
        recyclerView=SlideshowView.findViewById(R.id.result);
        database= RoomDB.getInstance(getActivity());
        dataList=database.mainDao().getAll();
        adapter = new MainAdapter(getActivity(),dataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        //顯示總額
        final TextView total=SlideshowView.findViewById(R.id.checkout_money);


        //spinner
        t13 = RootView.findViewById(R.id.textView13);
        sp=(Spinner) RootView.findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((String) sp.getSelectedItem()=="外送"){
                    t13.setText("$30");
                    total.setText("$"+String.valueOf(database.mainDao().getTotal()+30));
                    ch=true;
                }else {
                    t13.setText("$0");
                    total.setText("$"+String.valueOf(database.mainDao().getTotal()));
                    ch = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        super.onCreate(savedInstanceState);

        createNotificationChannel();

        //結帳按鈕觸發:依據存款餘額決定是否能夠進行結帳，結帳成功同時清空資料
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deposit=database.coinDao().getcoinvalue(1);
                int total_d = 0;

                //spinner
                if (ch){
                    total_d = database.mainDao().getTotal()+30;
                }else {
                    total_d = database.mainDao().getTotal();
                }

                if (deposit<total_d){
                    Toast.makeText(RootView.getContext(), "結帳失敗，餘額不足!!", Toast.LENGTH_LONG).show();
                }else {
                    check=true;
                    database.coinDao().update(1,deposit-total_d);
                    database.mainDao().clear();
                    Toast.makeText(RootView.getContext(), "結帳成功，存款餘額為"+database.coinDao().getcoinvalue(1), Toast.LENGTH_LONG).show();
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    for(int i=0;i<dataList.size();i++){
                        adapter.notifyItemRemoved(i);
                    }
                    dataList.clear();
                    adapter.notifyItemRangeChanged(0,dataList.size());

                    //spinner
                    if (ch){
                        total.setText("$30");
                    }else
                        total.setText("$0");

                }

                btn_check.setVisibility(View.INVISIBLE);

                //notify
                if (check){
                    sendNotification();
                }

            }
        });




        String [] values = {"外送","自取"};
        Spinner spinner = (Spinner) SlideshowView.findViewById(R.id.spinner);
        ArrayAdapter<String> ssadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        ssadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(ssadapter);


        return SlideshowView;
    }

    //往下都是新增的
    public void createNotificationChannel()
    {

        mNotifyManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,"Mascot Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder()
    {
        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (getActivity(),NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getActivity(), PRIMARY_CHANNEL_ID)
                .setContentTitle("您的訂單已下達！")
                .setContentText("店家已收到您的訂單，請等候的餐點")
                .setSmallIcon(R.mipmap.app)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        return notifyBuilder;


    }

    public void sendNotification()
    {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (getActivity(), NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        // notifyBuilder.addAction(R.mipmap.app, "Update Notification", updatePendingIntent);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

}