package com.ramdomstuff.ram.shootingmore;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SlFileShowActivity extends AppCompatActivity {

    public TextView filetext;
    static final int READ_BLOCK_SIZE = 100;
    public TextView txtFilename;
    public TableLayout tblData;
    public TextView txtCalInfo;
    public  TextView txtCalInfo2;
    public  TextView txtCalInfo3;
    public  TextView txtCalInfo4;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sl_file_show);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //get controls
        tblData = (TableLayout) findViewById(R.id.tableData);
        txtFilename = (TextView) findViewById(R.id.tvFile);
        txtCalInfo = (TextView) findViewById(R.id.textCaliberInfo);
        txtCalInfo2 = (TextView) findViewById(R.id.textCaliberInfo2);
        txtCalInfo3 = (TextView) findViewById(R.id.textCaliberInfo3);
        txtCalInfo4 = (TextView) findViewById(R.id.textCaliberInfo4);

        txtCalInfo.setVisibility(View.INVISIBLE);
        txtCalInfo2.setVisibility(View.INVISIBLE);
        txtCalInfo3.setVisibility(View.INVISIBLE);
        txtCalInfo4.setVisibility(View.INVISIBLE);
        tblData.setVisibility(View.INVISIBLE);

        getTrajData();
    }

    public void getTrajData()
    {
        try {

            txtFilename.setText("File-" + GlobalVars.sTrajFilesPath + "/" + GlobalVars.sFilename);
            FileInputStream fileIn = new FileInputStream(new File(GlobalVars.sTrajFilesPath + "/" + GlobalVars.sFilename));
            //FileInputStream fileIn = new FileInputStream(new File(path + "/" + fileToOpen));
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

                    TableRow tableRow = new TableRow(SlFileShowActivity.this);

                    for (int x = 1; x < iNumberOfColumns; x++)
                    {
                        TextView tv = new TextView(SlFileShowActivity.this);
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
