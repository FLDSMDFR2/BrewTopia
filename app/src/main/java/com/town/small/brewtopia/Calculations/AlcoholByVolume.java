package com.town.small.brewtopia.Calculations;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;

import com.town.small.brewtopia.R;

import java.math.BigDecimal;

public class AlcoholByVolume extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AlcoholByVolume";


    private Button CalculateButton;
    private EditText OG;
    private EditText FG;
    private TextView ABV;
    private TextView CalcABV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_by_volume);
        Log.e(LOG, "Entering: onCreate");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Alcohol By Volume");

        OG = (EditText)findViewById(R.id.OGeditText);
        FG = (EditText)findViewById(R.id.FGeditText);
        ABV = (TextView)findViewById(R.id.ABVTextView);
        CalcABV = (TextView)findViewById(R.id.ABVCalculationsTextView);
        CalculateButton = (Button)findViewById(R.id.ABVCalculateButton);
    }

    public void onCalculateClick(View aView)
    {
        double og = Double.parseDouble(OG.getText().toString());
        double fg = Double.parseDouble(FG.getText().toString());
        double abv = (og - fg)/.75;

        //Calculation
        Double truncatedDouble = new BigDecimal(Double.toString(abv))
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        CalcABV.setText(truncatedDouble.toString());

        //Percent
        truncatedDouble = new BigDecimal(Double.toString(abv*100))
                .setScale(1, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        ABV.setText(truncatedDouble.toString() + " %");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
