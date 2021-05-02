package com.example.campuseats.ui.s5;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuseats.R;
import com.example.campuseats.Room.MainAdapter;
import com.example.campuseats.Room.MainData;
import com.example.campuseats.Room.RoomDB;

import java.util.Arrays;
import java.util.List;

public class S5Fragment extends Fragment {

    private S5ViewModel mViewModel;

    List<MainData> dataList;
    RoomDB database;
    MainAdapter adapter;
    List product_name_list;

    View RootView;

    public static S5Fragment newInstance() {
        return new S5Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View S5View = inflater.inflate(R.layout.fragment_s5, container, false);
        RootView = S5View;

        database=RoomDB.getInstance(getActivity());
        dataList=database.mainDao().getAll();
        adapter = new MainAdapter(getActivity(),dataList);

        final List<String> namesList = Arrays.asList( "照燒雞腿排", "舒肥迷迭香雞胸肉", "台式滷雞腿","味噌鯛魚","日式烤鯖魚");
        final List<Integer> priceList = Arrays.asList(150,140,125,160,150);
        //final List<String> pastList=new ArrayList<>();

        //數量紀錄(不會跳出變0)
        for (int j=2;j<7;j++){
            final int quantity_id=getResources().getIdentifier("s5_"+j, "id", getActivity().getPackageName());
            TextView tt = S5View.findViewById(quantity_id);
            try {
                int q=database.mainDao().getquantity(namesList.get(j-2));
                System.out.println(q);
                tt.setText(String.format("%d",q));
            }catch (Exception e){
                System.out.println(quantity_id+"error");
                continue;
            }
        }

        //add
        for (int i = 2; i < 7; i++) {
            final int id = getResources().getIdentifier("s5_"+i+"_add", "id", getActivity().getPackageName());
            Button btnAdd = S5View.findViewById(id);

            final int finalI = i;
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int tid = getResources().getIdentifier("s5_"+finalI, "id", getActivity().getPackageName());
                    TextView TV = RootView.findViewById(tid);
                    int TV_NUM=Integer.parseInt(TV.getText().toString());
                    TV_NUM+=1;
                    TV.setText(String.format("%d",TV_NUM));


                    String sName = namesList.get(finalI -2);
                    int sPrice=priceList.get(finalI-2);
                    int sQuantity=TV_NUM;
                    String sMode="外送";
                    String sAddress="abc";

                    MainData data =new MainData();
                    data.setName(sName);
                    data.setPrice(sPrice);
                    data.setQuantity(sQuantity);
                    data.setMode(sMode);
                    data.setAddress(sAddress);


                    //更新數量or增加item
                    int item_count=adapter.getItemCount();
                    if (item_count!=0){
                        for (int i = 0; i <= item_count; i++){
                            MainData vv=dataList.get(i);
                            if (vv.getName()==sName){
                                //Toast.makeText(RootView.getContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
                                database.mainDao().update(sName,sQuantity);
                                dataList.clear();
                                adapter.notifyDataSetChanged();
                                break;
                            }else{
                                database.mainDao().update(sName,sQuantity);
                                dataList.clear();
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }else {
                        database.mainDao().insert(data);
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                    }

                }
            });
        }

        //minus
        for (int i = 2; i < 7; i++) {
            final int id = getResources().getIdentifier("s5_"+i+"_minus", "id", getActivity().getPackageName());
            Button btnAdd = S5View.findViewById(id);

            final int finalI = i;
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int tid = getResources().getIdentifier("s5_"+finalI, "id", getActivity().getPackageName());
                    TextView TV = RootView.findViewById(tid);
                    int TV_NUM=Integer.parseInt(TV.getText().toString());
                    if (TV_NUM>0)
                    {
                        TV_NUM-=1;

                        if(TV_NUM==0){
                            String ssName=namesList.get(finalI-2);
                            database.mainDao().delete(ssName);
                            dataList.clear();
                            adapter.notifyDataSetChanged();
                        }

                        TV.setText(String.format("%d",TV_NUM));
                        String sName = namesList.get(finalI -2);
                        int sQuantity=TV_NUM;
                        database.mainDao().update(sName,sQuantity);
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(RootView.getContext(), "目前數量為0，無法再減少", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return S5View;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(S5ViewModel.class);
        // TODO: Use the ViewModel
    }

}