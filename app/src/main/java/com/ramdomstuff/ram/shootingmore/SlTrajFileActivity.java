package com.ramdomstuff.ram.shootingmore;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by Randall.Mutter on 7/24/2016.
 */


public class SlTrajFileActivity extends AppCompatActivity{

    //public Button btnOpenRead;
    public TextView filetext;
    static final int READ_BLOCK_SIZE = 100;
    public TableLayout tblData;
    public TextView txtCalInfo;
    public  TextView txtCalInfo2;
    public  TextView txtCalInfo3;
    public  TextView txtCalInfo4;

    //added for expandable list
    public ExpandableListAdapter listAdapter;
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


    //private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;

    /**
     * expand list view sample      http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
     *
     */

    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature((Window.FEATURE_NO_TITLE));
        setContentView(R.layout.activity_sl_traj_file);

        //get controls
        tblData = (TableLayout) findViewById(R.id.tableData);
        txtCalInfo = (TextView) findViewById(R.id.textCaliberInfo);
        txtCalInfo2 = (TextView) findViewById(R.id.textCaliberInfo2);
        txtCalInfo3 = (TextView) findViewById(R.id.textCaliberInfo3);
        txtCalInfo4 = (TextView) findViewById(R.id.textCaliberInfo4);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        txtCalInfo.setVisibility(View.INVISIBLE);
        txtCalInfo2.setVisibility(View.INVISIBLE);
        txtCalInfo3.setVisibility(View.INVISIBLE);
        txtCalInfo4.setVisibility(View.INVISIBLE);
        tblData.setVisibility(View.INVISIBLE);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.elvFiles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.readfile_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
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
            AlertDialog ad = new AlertDialog.Builder(SlTrajFileActivity.this).create();
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
                getTrajData();

                return false;
            }
        });

    }

    public void getTrajData()
    {
        try {

            FileInputStream fileIn = new FileInputStream(new File(GlobalVars.sTrajFilesPath + "/" + GlobalVars.sFilename));
            //FileInputStream fileIn = new FileInputStream(new File(path + "/" + fileToOpen));
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String sInputFromFile = "";
            int charRead;

            Toast.makeText(getApplicationContext(), "File = " + GlobalVars.sTrajFilesPath + "/" + GlobalVars.sFilename, Toast.LENGTH_SHORT).show();

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);

                if (readstring.length() > 0) {
                    sInputFromFile += readstring;

                    // 1st column holds load description info so get column text and use as the header
                    //String[] aLoadData = sInputFromFile.split("\t");
                    //Log.d("Load Data array", aLoadData[0]);

                    //Integer arrayLength = (Integer) aLoadData.length;
                    //Log.d("Array Count:",arrayLength.toString());
                }

            }
            InputRead.close();

            // 1st split on "\n" then 2nd split on "\t"
            // sample for adding table  http://justsimpleinfo.blogspot.com/2014/05/android-tablelayout-example.html

            String[] aRowData = sInputFromFile.split("\n"); //this is array of rows
            Integer intRowArrayCount = (Integer) aRowData.length;  //gets me the # of rows for the table

            String sCaliberInfo = "";
            String sCaliberInfo2 = "";
            String sCaliberInfo3 = "";
            String sCaliberInfo4 = "";
            int iNumberOfColumns = 0;

            //clear the tableview in case > then 1st run
            tblData.removeAllViews();

            String[] aColumnData;

            //split each row into fields
            for (int i = 0; i < intRowArrayCount; i++) {

                //I don't need the 1st 3 rows
                if (i > 2) {

                    aColumnData = aRowData[i].split("\t");
                    iNumberOfColumns = (Integer) aColumnData.length;

                    //1st column is data about the caliber load
                    switch (i)
                    {
                        case 3: sCaliberInfo = aColumnData[0];
                            break;
                        case 4: sCaliberInfo2 = aColumnData[0]; // sCaliberInfo = sCaliberInfo + " " + aColumnData[0];
                            break;
                        case 5: sCaliberInfo3 = aColumnData[0]; // sCaliberInfo2 = aColumnData[0];
                            break;
                        case 6: sCaliberInfo4 = aColumnData[0]; // sCaliberInfo2 = sCaliberInfo2 + " " + aColumnData[0];
                            break;
                    }

                    TableRow tableRow = new TableRow(SlTrajFileActivity.this);

                    for (int x = 1; x < iNumberOfColumns; x++)
                    {
                        TextView tv = new TextView(SlTrajFileActivity.this);
                        tv.setPadding(12, 6, 12, 6);
                        ShapeDrawable sd = new ShapeDrawable();
                        sd.setShape(new RectShape());
                        sd.getPaint().setColor(Color.BLACK);
                        sd.getPaint().setStrokeWidth(5f);
                        sd.getPaint().setStyle(Paint.Style.STROKE);
                        tv.setBackground(sd);
                        tv.setText(aColumnData[x]);

                        //set everything to right justify here and below certin items will be reset
                        tv.setGravity(Gravity.RIGHT);

                        //if row 4 of file (3 in array) set font to bold for top row of table and center justify
                        if (i == 3)
                        {
                            tv.setTypeface(null, Typeface.BOLD);
                            tv.setGravity(Gravity.CENTER);
                        }

                        //if column is 1 set to bold and left justify
                        if (x == 1)
                        {
                            tv.setTypeface(null, Typeface.BOLD);
                            tv.setGravity(Gravity.LEFT);
                        }

                        try
                        {
                            Log.d("Column data = ","'" +  aColumnData[x].trim() + "'");
                            double z = Double.parseDouble(aColumnData[x].trim());
                            if (z < 0)
                            {
                                tv.setTextColor(Color.RED);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            //nothing to do here just proceed
                            Log.d("Convert to int error=", e.toString());
                        }

                        tableRow.addView(tv);
                    }

                    //add row to table
                    tblData.addView(tableRow,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }

            txtCalInfo.setText(sCaliberInfo);
            txtCalInfo.setVisibility(View.VISIBLE);

            if (sCaliberInfo2.length() > 0){
                txtCalInfo2.setText(sCaliberInfo2);
                txtCalInfo2.setVisibility(View.VISIBLE);
            }
            else {
                txtCalInfo2.setVisibility(View.GONE);
            }

            if (sCaliberInfo3.length() > 0) {
                txtCalInfo3.setText(sCaliberInfo3);
                txtCalInfo3.setVisibility(View.VISIBLE);
            }
            else {
                txtCalInfo3.setVisibility(View.GONE);
            }

            if (sCaliberInfo4.length() > 0 ) {
                txtCalInfo4.setText(sCaliberInfo4);
                txtCalInfo4.setVisibility(View.VISIBLE);
            }
            else {
                txtCalInfo4.setVisibility(View.GONE);
            }
            tblData.setVisibility(View.VISIBLE);

            //filetext.setText(sInputFromFile);
            //Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public View.OnClickListener listenbtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
            try {
                String path = Environment.getExternalStorageDirectory().toString() + "/RAMFiles";
                String fileToOpen = "";
                Log.d("Files", "Path: " + path);

                File f = new File(path);
                File file[] = f.listFiles();
                Log.d("Files", "Size: " + file.length);

                for (int i = 0; i < file.length; i++) {
                    Log.d("Files", "FileName:" + file[i].getName());
                    fileToOpen = file[i].getName();
                }

                FileInputStream fileIn = new FileInputStream(new File(path + "/" + fileToOpen));
                InputStreamReader InputRead = new InputStreamReader(fileIn);

                char[] inputBuffer = new char[READ_BLOCK_SIZE];
                String sInputFromFile = "";
                int charRead;

                while ((charRead = InputRead.read(inputBuffer)) > 0) {
                    // char to string conversion
                    String readstring = String.copyValueOf(inputBuffer, 0, charRead);

                    if (readstring.length() > 0) {
                        sInputFromFile += readstring;

                        // 1st column holds load description info so get column text and use as the header
                        //String[] aLoadData = sInputFromFile.split("\t");
                        //Log.d("Load Data array", aLoadData[0]);

                        //Integer arrayLength = (Integer) aLoadData.length;
                        //Log.d("Array Count:",arrayLength.toString());
                    }

                }
                InputRead.close();

                // 1st split on "\n" then 2nd split on "\t"
                // sample for adding table  http://justsimpleinfo.blogspot.com/2014/05/android-tablelayout-example.html

                String[] aRowData = sInputFromFile.split("\n"); //this is array of rows
                Integer intRowArrayCount = (Integer) aRowData.length;  //gets me the # of rows for the table

                String sCaliberInfo = "";
                int iNumberOfColumns = 0;

                String[] aColumnData;

                //split each row into fields
                for (int i = 0; i < intRowArrayCount; i++) {

                    //I don't need the 1st row
                    if (i > 0) {

                        aColumnData = aRowData[i].split("\t");
                        iNumberOfColumns = (Integer) aColumnData.length;

                        //1st column is data about the caliber load
                        sCaliberInfo = sCaliberInfo + aColumnData[0];
                        TableRow tableRow = new TableRow(ReadFileActivity.this);

                        for (int x = 1; x < iNumberOfColumns; x++)
                        {
                            TextView tv = new TextView(ReadFileActivity.this);
                            tv.setText(aColumnData[x]);
                            tableRow.addView(tv);
                        }

                        //add row to table
                        tblData.addView(tableRow,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }

                txtCalInfo.setText(sCaliberInfo);

                //filetext.setText(sInputFromFile);
                //Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_settings:
                //Intent intent = new Intent(ReadFileActivity.this, DeviceFiles.class);
                //startActivity(intent);
                Toast.makeText(this,"Settings Clicked",Toast.LENGTH_SHORT).show();

                //create DirectoryChooserDialog and register a callback
                DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(SlTrajFileActivity.this, new DirectoryChooserDialog.ChosenDirectoryListener() {
                    @Override
                    public void onChosenDir(String chosenDir)
                    {
                        m_chosenDir = chosenDir;
                        Toast.makeText(SlTrajFileActivity.this, "Choosen Dir: " + chosenDir, Toast.LENGTH_LONG).show();

                        //save the folder
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Folder,m_chosenDir);
                        editor.commit();

                        GlobalVars.sTrajFilesPath = m_chosenDir;

                        prepareListData();
                        CreateExpandList();
                    }

                });

                // Toggle new folder button enabling
                directoryChooserDialog.setNewFolderEnabled(true);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.
                directoryChooserDialog.chooseDirectory(m_chosenDir);
                m_newFolderEnabled = ! m_newFolderEnabled;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
