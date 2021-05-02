package com.example.campuseats.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.campuseats.LoginActivity;
import com.example.campuseats.MainActivity;
import com.example.campuseats.R;
import com.example.campuseats.Room.CoinData;
import com.example.campuseats.Room.MainData;
import com.example.campuseats.Room.RoomDB;

import java.util.List;

public class GalleryFragment extends Fragment {
    public EditText deposit;
    int test;
    private View RootView;
    List<CoinData> coinList;
    RoomDB database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View GalleryView = inflater.inflate(R.layout.fragment_gallery, container, false);
        RootView = GalleryView;
        final Button btn_deposit = (Button) GalleryView.findViewById(R.id.button_deposit);
        final TextView money = (TextView) GalleryView.findViewById(R.id.usermoney);
        deposit = (EditText) GalleryView.findViewById(R.id.editText_deposit);

        /*final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        database= RoomDB.getInstance(getActivity());


        //資料庫定義
        System.out.println("tt");
        System.out.println(database.coinDao().getcount());
        if(database.coinDao().getcount()==1){

        }else{
            CoinData coinData=new CoinData();
            coinData.setID(1);
            coinData.setValue(1000);
            database.coinDao().insert(coinData);
        }



        //確保離開頁面再進入能顯示正確存款金額
        money.setText(String.valueOf(database.coinDao().getcoinvalue(1)));

        //監聽存款button
        btn_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (!(deposit.getText().toString().equals("")) && Integer.parseInt(deposit.getText().toString())>0)
                {
                    int des = Integer.parseInt(deposit.getText().toString());
                    test=database.coinDao().getcoinvalue(1)+des;
                    database.coinDao().update(1,test);
                    money.setText(String.valueOf(database.coinDao().getcoinvalue(1)));
                    Toast.makeText(RootView.getContext(), "儲值成功", Toast.LENGTH_LONG).show();
                    TextView edit = GalleryView.findViewById(R.id.editText_deposit);
                    edit.setText("");
                }
                else
                {
                    ShowMsgDialog("請輸入大於0元的金額");
                }
            }
        });
        return GalleryView;
    }



    private void ShowMsgDialog(String Msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(RootView.getContext()).create();
        alertDialog.setTitle("金額錯誤");
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
}