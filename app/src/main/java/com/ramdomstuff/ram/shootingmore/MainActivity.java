package com.ramdomstuff.ram.shootingmore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //declare variables
    Button btnFormulas;
    Button btnSlTrajFile;
    Button btnSlSelect;

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get controls for use
        btnFormulas = (Button) findViewById(R.id.btnFormulas);
        btnSlTrajFile = (Button) findViewById(R.id.btnTrajFiles);
        btnSlSelect = (Button) findViewById(R.id.btnTrajFiles2);

        btnFormulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFormulas = new Intent(MainActivity.this,FormulasActivity.class);
                startActivity(intentFormulas);
            }
        });

        btnSlTrajFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSlTrajFile = new Intent(MainActivity.this,SlTrajFileActivity.class);
                startActivity(intentSlTrajFile);
            }
        });

        btnSlSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSlSelect = new Intent(MainActivity.this,SlFileSelectActivity.class);
                startActivity(intentSlSelect);
            }
        });

        if (Build.VERSION.SDK_INT >= 6.0) {
            getPermissionToReadExternalStorage();
        }

    }

    public void getPermissionToReadExternalStorage() {
        //sample code here https://guides.codepath.com/android/Understanding-App-Permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }
                //requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
            }
        }
    }

    //@Override
    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read External Storage permission granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Read External Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
