package com.nseif.bmi_calculator;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import static com.nseif.bmi_calculator.Utils.showInfoDialog;

public class MainActivity extends AppCompatActivity {

    private static final String sFORMAT_STRING = "%2.2f";
    private static final String mCALCULATIONS_DONE_KEY = "CALCULATIONS_DONE";

    private BMICalc mBMICalc;               // model
    private EditText mEditTextHeight, mEditTextWeight;
    private Snackbar mSnackBar;
    private int mCalculationsDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFAB();
        setupFields();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> handleFABClick());
    }

    private void handleFABClick() {
        Pair<String, String> heightAndWeight = new Pair<>(mEditTextHeight.getText().toString(),
                mEditTextWeight.getText().toString());
        if (isValidFormData(heightAndWeight)) {                    // check view/user input
            mCalculationsDone++;
            setModelFieldsHeightAndWeightTo(heightAndWeight);
            String msg = generateFormattedStringOfBMIAndGroupFromModel();
            showMessageWithLinkToResultsActivity(msg);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.error_msg_height_or_weight_not_valid,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidFormData(Pair<String, String> heightAndWeight) {
        String height = heightAndWeight.first;
        String weight = heightAndWeight.second;

        return height != null && weight != null &&
                height.length() > 0 && weight.length() > 0
                && Double.parseDouble(height) > 0
                && Double.parseDouble(weight) > 0;
    }


    private void setModelFieldsHeightAndWeightTo(
            Pair<String, String> heightAndWeight) {
        assert heightAndWeight.first != null;
        assert heightAndWeight.second != null;

        double height = Double.parseDouble(heightAndWeight.first);
        double weight = Double.parseDouble(heightAndWeight.second);

        if (mBMICalc == null)
            mBMICalc = new BMICalc(height, weight);
        else {
            mBMICalc.setHeight(height);
            mBMICalc.setWeight(weight);
        }
    }


    private String generateFormattedStringOfBMIAndGroupFromModel() {
        final double bmi;
        final String bmiGroup, bmiString;

        bmi = mBMICalc.getBMI();
        bmiString = String.format(Locale.US, sFORMAT_STRING, bmi);
        bmiGroup = mBMICalc.getBmiGroup();

        return String.format(Locale.getDefault(),
                "%s %s; %s %s\n%s %d",
                getString(R.string.bmi), bmiString,
                getString(R.string.bmi_group), bmiGroup,
                getResources().getQuantityString(R.plurals.calcs_done, mCalculationsDone), mCalculationsDone);
    }

    private void showMessageWithLinkToResultsActivity(String msg) {
        mSnackBar.setText(msg);
//        mSnackBar.setAction(getString(R.string.details), mResultsListener);
        mSnackBar.show();
    }

    private void setupFields() {
        mEditTextHeight = findViewById(R.id.et_height);
        mEditTextWeight = findViewById(R.id.et_weight);

        View layoutMain = findViewById(R.id.main_activity);
        mSnackBar = Snackbar.make(layoutMain, "", Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(mCALCULATIONS_DONE_KEY, mCalculationsDone);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCalculationsDone = savedInstanceState.getInt(mCALCULATIONS_DONE_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            showAbout();
            return true;
        } else if (id == R.id.action_reset) {
            resetCalculationCount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        showInfoDialog(MainActivity.this, R.string.about, R.string.about_text);
    }

    private void resetCalculationCount() {
        mCalculationsDone = 0;
    }
}