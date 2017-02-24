package com.kamsung.testfirebasechatting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener{
    ListView listView;
    EditText content;
    Button send;

    MyAdapter adapter;

    ArrayList<Chatting> items;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        content = (EditText) findViewById(R.id.content);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);

        items = new ArrayList<>();
        adapter = new MyAdapter(this, items);

        listView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("room").addChildEventListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText nickname = new EditText(this);
        nickname.setLines(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nickname.setLayoutParams(params);

        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(10, 5, 10, 5);
        layout.addView(nickname);
        builder.setTitle("닉네임을 입력해주세요.").setView(layout).setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userName = nickname.getText().toString();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        getChattingData();

    }

    private void getChattingData(){
        databaseReference.child("room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("data", dataSnapshot.toString());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                for(Map.Entry<String, Object> entry : map.entrySet()){


                    Map data = (Map)entry.getValue();
                    Chatting chatting = new Chatting((String)data.get("user"), (String)data.get("content"));
                    chatting.setDate((String)data.get("date"));
                    items.add(chatting);

                }
                adapter.notifyDataSetChanged();

                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        Chatting chatting = new Chatting(userName, content.getText().toString());
        databaseReference.child("room").push().setValue(chatting);
        content.setText("");
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Chatting chatting = dataSnapshot.getValue(Chatting.class);
        items.add(chatting);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
