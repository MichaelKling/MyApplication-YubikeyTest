package com.example.michael.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {
    private TextView txtLog;
    private CheckBox cbxGetTag;
    private CheckBox cbxStartIsoDep;
    private NfcAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onStart() {
        super.onStart();

        txtLog = (TextView)findViewById(R.id.log);
        cbxGetTag = (CheckBox)findViewById(R.id.cbxGetTag);
        cbxStartIsoDep = (CheckBox)findViewById(R.id.cbxStartIsoDep);

        adapter = NfcAdapter.getDefaultAdapter(this);

        txtLog.append("View started\n");
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        onNewIntent(intent);
    }

    private void analyseNFCIntent(Intent intent) {
        if (cbxGetTag.isChecked()) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            txtLog.append("TAG:\n");
            txtLog.append(tag.toString());
            txtLog.append("\n");
            Log.i("MyApplication", "TAG:\n");
            Log.i("MyApplication", tag.toString());
            if (cbxStartIsoDep.isChecked()) {
                IsoDep isoDep = IsoDep.get(tag);
                txtLog.append("IsoDep Initialized:\n");
                txtLog.append("Is Connected: "+ Boolean.toString(isoDep.isConnected())+"\n");
                txtLog.append("Historical Bytes: "+isoDep.getHistoricalBytes().toString()+"\n");
                Log.i("MyApplication", "IsoDep Initialized:\n");
                Log.i("MyApplication", "Is Connected: " + Boolean.toString(isoDep.isConnected()) + "\n");
                Log.i("MyApplication", "Historical Bytes: " + isoDep.getHistoricalBytes().toString() + "\n");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        txtLog.append("Action \n");
        txtLog.append(intent.getAction().toString());
        txtLog.append(" received\n");
        Log.i("MyApplication", "Action \n");
        Log.i("MyApplication", intent.getAction().toString());
        Log.i("MyApplication", " received\n");

        if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            analyseNFCIntent(intent);

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    txtLog.append(msgs[i].toString()+"\n");
                    Log.i("MyApplication", msgs[i].toString() + "\n");
                }
            }

        } else if (intent.getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)){
            analyseNFCIntent(intent);
        } else if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            analyseNFCIntent(intent);
        } else {
            txtLog.append("Nothing to do for that action.\n");
            Log.i("MyApplication", "Nothing to do for that action.\n");
        }

    }
}
