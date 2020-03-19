package de.vcc.volumecontrolcenter;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Socket so = null;
    private final int PORT = 12000;
    private final long DELAY = 10;
    private int intensity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    }

    public void volumeUp(View view) {
        for (int i = 0; i < intensity; i++) {
            try {
                OutputStream out = so.getOutputStream();
                out.write(1);
            } catch (IOException e) {

            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void volumeDown(View view) {
        for (int i = 0; i < intensity; i++){
            try {
                OutputStream out = so.getOutputStream();
                out.write(0);
            } catch (IOException e) {

            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            OutputStream out = so.getOutputStream();
            out.write(2);
        } catch (IOException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        connect();
        final TextView txtIntensity = findViewById(R.id.txtIntensity);
        txtIntensity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtIntensity.getText().toString() != null && !txtIntensity.getText().toString().equals("")) {
                    intensity = Integer.parseInt(txtIntensity.getText().toString());
                    if (intensity > 50) {
                        intensity = 50;
                        txtIntensity.setText("50");
                    } else if (intensity < 1) {
                        intensity = 1;
                        txtIntensity.setText("1");
                    }
                } else {
                    intensity = 1;
                }

            }
        });
    }

    private void connect() {
        try {
            so = new Socket("192.168.178.39", PORT);
        } catch (UnknownHostException e) {
            Log.d("Catch1", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Catch2", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Catch3", e.getMessage());
        }
    }
}