package pl.edu.pg.tsp.activity;

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

import pl.edu.pg.tsp.ITSPService;
import pl.edu.pg.tsp.R;
import pl.edu.pg.tsp.service.TSPService;

public class MainActivity extends AppCompatActivity {

    private ITSPService service;
    private RemoteServiceConnection serviceConnection;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectService();

        Button startButton = (Button)findViewById(R.id.button);
        resultText = (TextView)findViewById(R.id.textView);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (service != null) {
                        int result = service.getResult();
                        resultText.setText(String.valueOf(result));
                    }
                } catch (RemoteException e) {
                    Log.e("MAIN", "Exception caught when calling service for result");
                }
            }
        });
    }


    private void connectService() {
        serviceConnection = new RemoteServiceConnection();
        Intent i = new Intent(this, TSPService.class);
        boolean ret = bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = ITSPService.Stub.asInterface(boundService);
            Log.d("MAIN", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d("MAIN", "Service disconnected");
        }
    }
}
