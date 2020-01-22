package com.projekty.ania.komiwojazer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.projekty.ania.komiwojazer.aidlService;
import com.projekty.ania.komiwojazer.R;
import com.projekty.ania.komiwojazer.service.KomiwojazerService;

// https://www.sitepoint.com/aidl-for-sharing-functionality-between-android-apps/?fbclid=IwAR0fYrVRmAz6Bp8FilmK-SJaCUV7o1YSX0TynsIs04ovT1MrPfQ_jx4KhQw

public class MainActivity extends AppCompatActivity {


    private TextView nodesTextView;
    private Button startButton;
    private aidlService service;
    private RemoteServiceConnection remote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectService();


        startButton = (Button)findViewById(R.id.startButton);
        nodesTextView = (TextView)findViewById(R.id.textView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (service != null) {
                        int nodesNumber = service.getProduct();
                        nodesTextView.setText(String.valueOf(nodesNumber));
                    }
                } catch (RemoteException e) {
                    Log.e("MAIN", "Exception: " + e) ;
                }
            }
        });
    }


    private void connectService() {
        remote = new RemoteServiceConnection();
        Intent i = new Intent(this, KomiwojazerService.class);
        boolean ret = bindService(i, remote, Context.BIND_AUTO_CREATE);
    }

    class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = aidlService.Stub.asInterface(boundService);
            Log.d("MAIN", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d("MAIN", "Service disconnected");
        }
    }
}
