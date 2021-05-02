package com.example.campuseats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuseats.ui.home.HomeFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //***點ok切換到主頁的code
        Button btn_to_B = (Button) findViewById(R.id.button_ok);

        btn_to_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText EditUsername =  (EditText) findViewById(R.id.EditUsername);
                final EditText EditPassword = (EditText) findViewById(R.id.EditPassword);

                if  (EditUsername.getText().toString().equals("test") && EditPassword.getText().toString().equals("123"))
                {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (EditUsername.getText().toString().equals("") || EditPassword.getText().toString().equals(""))
                {
                    EditUsername.setText("");
                    EditPassword.setText("");
                    ShowMsgDialog("您未輸入帳號或密碼，請重新登入");
                }
                else
                {
                    EditUsername.setText("");
                    EditPassword.setText("");
                    ShowMsgDialog("您輸入的帳號或密碼錯誤 或者，請重新登入");
                }

            }
        });

        Button btn_to_c = (Button) findViewById(R.id.button_cancel);
        btn_to_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText EditUsername = (EditText) findViewById(R.id.EditUsername);
                final EditText EditPassword = (EditText) findViewById(R.id.EditPassword);


                EditUsername.setText("");
                EditPassword.setText("");
            }
        });

        Button btn_to_e = (Button) findViewById(R.id.button_exit);
        btn_to_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void ShowMsgDialog(String Msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("登入錯誤");
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
                //如果不做任何事情 就會直接關閉 對話方塊
            }
        };;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // 註冊mConnReceiver，並用IntentFilter設置接收的事件類型為網路開關
        this.registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // 解除註冊
        this.unregisterReceiver(mConnReceiver);
    }

    // 建立一個BroadcastReceiver，名為mConnReceiver
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 當使用者開啟或關閉網路時會進入這邊

            // 判斷目前有無網路
            if(isNetworkAvailable()) {
                // 以連線至網路，做更新資料等事情
            }
            else {
                // 沒有網路
                Toast.makeText(LoginActivity.this, "沒網路!", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    };

    // 回傳目前是否已連線至網路
    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null &&
                networkInfo.isConnected();
    }


}