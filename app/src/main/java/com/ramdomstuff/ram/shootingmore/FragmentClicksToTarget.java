package com.ramdomstuff.ram.shootingmore;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by Randall.Mutter on 7/25/2016.
 */
public class FragmentClicksToTarget extends Fragment implements View.OnClickListener {

    TextView tvAnswer;
    EditText etScopeIncrement;
    EditText etTargetYards;
    EditText etImpactInches;
    Button btnCal;
    Button btnReset;
    InputMethodManager imm;

    private double dScopeIncrement;
    private double dTargetYards;
    private double dImpactInches;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_formula_clickstotarget, container, false);

        //get the answer widget + clear
        tvAnswer = (TextView) myView.findViewById(R.id.tvAnswer);
        tvAnswer.setText("");

        //get the edittext boxes
        etScopeIncrement = (EditText) myView.findViewById(R.id.etScopeincrement);
        etTargetYards = (EditText) myView.findViewById(R.id.etTargetYards);
        etImpactInches = (EditText) myView.findViewById(R.id.etImpactInches);

        btnCal = (Button) myView.findViewById(R.id.btnCalc);
        btnCal.setOnClickListener(this);

        btnReset = (Button) myView.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        etScopeIncrement.requestFocus();
        //show keyboard
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return myView;
    }

    @Override
    public void onClick (View v) {

        try {
            switch (v.getId()) {
                case R.id.btnCalc:
                    if (etScopeIncrement.getText().length() == 0 || etScopeIncrement.getText().toString().equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(getActivity(), "You need to enter a value for scope increment (and that number must be greater than 0).", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                        toast.show();
                        etScopeIncrement.requestFocus();
                        return;
                    }
                    else if (etTargetYards.getText().length() == 0 || etTargetYards.getText().toString().equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(getActivity(), "You need to enter a value for target yardage (and that number must be greater than 0).", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                        toast.show();
                        etTargetYards.requestFocus();
                        return;
                    }
                    else if (etImpactInches.getText().length() == 0 || etImpactInches.getText().toString().equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(getActivity(), "You need to enter a value for adjust bullet impact (and that number must be greater than 0).", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                        toast.show();
                        etImpactInches.requestFocus();
                        return;
                    }

                    //Adjustment in Inches / (Range in Yards / 100) = MOA
                    //So for 23.17 MOA of adjustment with a 1/4 MOA scope, the numbers are:
                    double dAnswer = Double.parseDouble(etImpactInches.getText().toString()) / (Double.parseDouble(etTargetYards.getText().toString()) / 100) ;
                    dAnswer = dAnswer / Double.parseDouble(etScopeIncrement.getText().toString());
                    int iClicks = (int) Math.round(dAnswer);
                    String sAnswer = " Adjust <b>" + iClicks + " clicks</b>";
                    Spanned htmlText = Html.fromHtml(sAnswer);
                    tvAnswer.setText(htmlText);

                    // hide keyboard
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);

                    break;
                case R.id.btnReset:
                    tvAnswer.setText("");
                    etScopeIncrement.setText("");
                    etTargetYards.setText("");
                    etImpactInches.setText("");
                    etScopeIncrement.requestFocus();
                    //show keyboard
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage().toString());
        }

    }
}
