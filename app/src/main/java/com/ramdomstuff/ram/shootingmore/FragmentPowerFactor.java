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
 * Created by RAM on 8/3/2016.
 */
public class FragmentPowerFactor extends Fragment implements View.OnClickListener {

    TextView tvAnswer;
    EditText etBulletWeight;
    EditText etBulletVelocity;
    Button btnCal;
    Button btnReset;
    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_formula_powerfactor, container, false);

        //get the answer widget + clear
        tvAnswer = (TextView) myView.findViewById(R.id.tvPFanswer);
        tvAnswer.setText("");

        //get the edittext boxes
        etBulletWeight = (EditText) myView.findViewById(R.id.etPFbulletWeight);
        etBulletVelocity = (EditText) myView.findViewById(R.id.etPFBulletVelocity);

        btnCal = (Button) myView.findViewById(R.id.btnCalc);
        btnCal.setOnClickListener(this);

        btnReset = (Button) myView.findViewById(R.id.btnReset);
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
                case R.id.btnCalc:
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

                    //pf = (weight * velocity) / 1000
                    double dAnswer = (Double.parseDouble(etBulletWeight.getText().toString()) * Double.parseDouble(etBulletVelocity.getText().toString()) / 1000) ;
                    DecimalFormat twoDForm = new DecimalFormat("#.##");
                    dAnswer = Double.valueOf(twoDForm.format(dAnswer));

                    String sAnswer = " Power Factor = <b>" + dAnswer + "</b>";
                    Spanned htmlText = Html.fromHtml(sAnswer);
                    tvAnswer.setText(htmlText);

                    // hide keyboard
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);

                    break;
                case R.id.btnReset:
                    tvAnswer.setText("");
                    etBulletWeight.setText("");
                    etBulletVelocity.setText("");

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



