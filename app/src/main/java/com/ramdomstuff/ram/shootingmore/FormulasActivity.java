package com.ramdomstuff.ram.shootingmore;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by RAM on 7/20/2016.
 */
public class FormulasActivity extends Activity {

    RadioButton rbYardsToMeters;
    RadioButton rbMetersToYards;
    RadioButton rbClicksToTarget;
    RadioGroup rg;
    Fragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set above to extends Activity and add line below to hide title bar + note has to be added before setContent
        this.requestWindowFeature((Window.FEATURE_NO_TITLE));
        setContentView(R.layout.activity_formulas);

        rbYardsToMeters = (RadioButton) findViewById(R.id.rbFormula1);
        rbMetersToYards = (RadioButton) findViewById(R.id.rbFormula2);
        rbClicksToTarget = (RadioButton) findViewById(R.id.rbFormula3);

        rg = (RadioGroup) findViewById(R.id.rbGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbFormula1) {
                    fr = new FragmentYardsToMeters();
                }
                else if (checkedId == R.id.rbFormula2) {
                    fr = new FragmentMetersToYards();
                }
                else if (checkedId == R.id.rbFormula3) {
                    fr = new FragmentClicksToTarget();
                }
                else if (checkedId == R.id.rbFormula4) {
                    fr = new FragmentPowerFactor();
                }
                else if (checkedId == R.id.rbFormula5) {
                    fr = new FragmentKineticEnergy();
                }

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr, "frag");
                fragmentTransaction.commit();
            }
        });

    }

}
