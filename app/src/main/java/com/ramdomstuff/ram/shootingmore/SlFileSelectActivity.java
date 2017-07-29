package com.ramdomstuff.ram.shootingmore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlFileSelectActivity extends AppCompatActivity {

    public TextView filetext;

    //added for expandable list
    public android.widget.ExpandableListAdapter listAdapter;
    public ExpandableListView expListView;
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;

    //for folder chooser
    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;

    //for preferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "myPrefs";
    public static final String Folder = "folderKey";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sl_file_select);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.elvFiles);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //read preferences
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //Log.d("Folder path = ", sharedPreferences.getString("folderKey",null));
        GlobalVars.sTrajFilesPath = sharedPreferences.getString("folderKey",null);
        //Log.d("Folder path = ", sharedPreferences.getString("folderKey",null));

        Toast.makeText(getApplicationContext(), "Showing trajectory files in " + GlobalVars.sTrajFilesPath, Toast.LENGTH_SHORT).show();

        /**
         if (Build.VERSION.SDK_INT >= 6.0) {
         getPermissionToReadExternalStorage();
         }
         */

        // preparing list data
        prepareListData();
        CreateExpandList();



    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Trajectory Files");

        // Adding child data
        List<String> filesTraj = new ArrayList<String>();

        try
        {
            String path = GlobalVars.sTrajFilesPath + "/";
            String fileToOpen = "";
            Log.d("Files", "Path: " + path);

            File f = new File(path);
            File file[] = f.listFiles();
            Log.d("Files", "Size: " + file.length);

            for (int i = 0; i < file.length; i++) {
                Log.d("Files", "FileName:" + file[i].getName());
                filesTraj.add(file[i].getName());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        listDataChild.put(listDataHeader.get(0), filesTraj); // Header, Child data

        if (filesTraj.size() == 0) {
            //clears the list in case the new folder has no files
            //listDataChild.clear();
            AlertDialog ad = new AlertDialog.Builder(SlFileSelectActivity.this).create();
            ad.setTitle("No Files Alert");
            ad.setMessage("There are no files in the configured folder. To change folder go to 'Settings' above.");
            ad.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            ad.show();
        }
    }

    public void CreateExpandList() {
        //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        listAdapter = new com.ramdomstuff.ram.shootingmore.ExpandableListAdapter(this,listDataHeader,listDataChild);


        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                GlobalVars.sFileGroup = listDataHeader.get(groupPosition);
                GlobalVars.sFilename = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                // TODO Auto-generated method stub
                /*Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(
                    childPosition), Toast.LENGTH_SHORT).show();*/

                expListView.collapseGroup(groupPosition);

                //call the activity to show the file data
                //getTrajData();
                Intent intentSlShowFile = new Intent(SlFileSelectActivity.this,SlFileShowActivity.class);
                startActivity(intentSlShowFile);

                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ReadFile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ramdomstuff.ram.shootingmore/http/host/path")

        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ReadFile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ramdomstuff.ram.shootingmore/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
