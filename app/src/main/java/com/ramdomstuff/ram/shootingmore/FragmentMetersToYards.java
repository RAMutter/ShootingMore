package com.ramdomstuff.ram.shootingmore;

import android.app.Fragment;
import android.content.Context;
import android.hardware.input.InputManager;
import android.location.LocationManager;
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
 * Created by RAM on 7/21/2016.
 */
public class FragmentMetersToYards extends Fragment implements View.OnClickListener {

    TextView tvAnswer;
    Button btnCalc;
    Button btnReset;
    EditText etMeters;
    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_formula_meterstoyards, container, false);

        //get the answer widget + clear
        tvAnswer = (TextView) myView.findViewById(R.id.tvAnswer);
        tvAnswer.setText("");

        //get the button and set listener
        btnCalc = (Button) myView.findViewById(R.id.btnCalc);
        btnCalc.setOnClickListener(this);

        btnReset = (Button) myView.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        etMeters = (EditText) myView.findViewById(R.id.etMeters);
        etMeters.requestFocus();
        //show keyboard
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        return myView;
    }

    @Override
    public void onClick (View v) {

        switch (v.getId()) {
            case R.id.btnCalc:
                if (etMeters.getText().length() == 0) {
                    Toast toast = Toast.makeText(getActivity(),"You need to enter a number to convert.",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                    return;
                }
                else if (etMeters.getText().toString().equalsIgnoreCase("0")) {
                    Toast toast = Toast.makeText(getActivity(),"You need to enter a number greater than 0 to convert.",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();

                    //remove the 0 from entry field
                    etMeters.setText("");
                    return;
                }

                double dYards = Double.parseDouble(etMeters.getText().toString()) * 1.0936;
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                dYards = Double.valueOf(twoDForm.format(dYards));

                String sAnswer = etMeters.getText().toString() + " meters coverts to <b>" + dYards + "</b> yards";
                Spanned htmlText = Html.fromHtml(sAnswer);
                tvAnswer.setText(htmlText);

                // hide keyboard
                imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(),0);

                break;

            case R.id.btnReset:
                tvAnswer.setText("");
                etMeters.setText("");
                etMeters.requestFocus();

                //show keyboard
                imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                break;
        }
    }

}
