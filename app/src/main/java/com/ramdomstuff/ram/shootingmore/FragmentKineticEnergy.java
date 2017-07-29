package com.ramdomstuff.ram.shootingmore;

import android.app.Activity;
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
 * Created by RAM on 8/9/2016.
 */
public class FragmentKineticEnergy extends Fragment implements View.OnClickListener {

    TextView tvAnswer;
    TextView tvHeader;
    EditText etBulletWeight;
    EditText etBulletVelocity;
    Button btnCal;
    Button btnReset;
    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_formula_kineticenergy, container, false);

        //get the answer widget + clear
        tvAnswer = (TextView) myView.findViewById(R.id.tvKEanswer);
        tvAnswer.setText("");

        tvHeader = (TextView) myView.findViewById(R.id.tvKEHeader);
        tvHeader.setText(Html.fromHtml("KE = (w * velocity<sup><small>2</small></sup>) / 450,450"));

        //get the edittext boxes
        etBulletWeight = (EditText) myView.findViewById(R.id.etKEbulletWeight);
        etBulletVelocity = (EditText) myView.findViewById(R.id.etKEBulletVelocity);

        btnCal = (Button) myView.findViewById(R.id.btnKECalc);
        btnCal.setOnClickListener(this);

        btnReset = (Button) myView.findViewById(R.id.btnKEReset);
        btnReset.setOnClickListener(this);

        etBulletWeight.requestFocus();

        //show keyboard
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return myView;
    }

    @Override
    public void onClick (View v) {

        try {
            switch (v.getId()) {
                case R.id.btnKECalc:
                    if (etBulletWeight.getText().length() == 0 || etBulletWeight.getText().toString().equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(getActivity(), "You need to enter a value for bullet weight (and that number must be greater than 0).", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                        toast.show();
                        etBulletWeight.requestFocus();
                        return;
                    }
                    else if (etBulletVelocity.getText().length() == 0 || etBulletVelocity.getText().toString().equalsIgnoreCase("0")) {
                        Toast toast = Toast.makeText(getActivity(), "You need to enter a value for velocity (and that number must be greater than 0).", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                        toast.show();
                        etBulletVelocity.requestFocus();
                        return;
                    }

                    //KE = (w * velocity2) / 450,450  arrow is 450,420
                    //KE = 0.5 • m • v2
                    double dBulletVelocity = Double.parseDouble(etBulletVelocity.getText().toString());
                    double dBulletWeight = Double.parseDouble(etBulletWeight.getText().toString());
                    double dAnswer = ((dBulletVelocity * dBulletVelocity) * dBulletWeight) / 450450;
                    //double dAnswer = ((dBulletVelocity * dBulletVelocity) * dBulletWeight) * .5;
                    DecimalFormat twoDForm = new DecimalFormat("#.##");
                    dAnswer = Double.valueOf(twoDForm.format(dAnswer));
                    String sAnswer = " KE = <b>" + dAnswer + "</b> ft-lbs";
                    Spanned htmlText = Html.fromHtml(sAnswer);
                    tvAnswer.setText(htmlText);

                    // hide keyboard
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);

                    break;
                case R.id.btnKEReset:
                    tvAnswer.setText("");
                    etBulletWeight.setText("");
                    etBulletVelocity.setText("");
                    etBulletWeight.requestFocus();

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


