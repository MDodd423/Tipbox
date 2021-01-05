package tech.dodd.tipbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TipActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "tech.dodd.quantifiedrecycler.EXTRA_ID";
    public static final String EXTRA_DATE =
            "tech.dodd.quantifiedrecycler.EXTRA_DATE";
    public static final String EXTRA_AMOUNT =
            "tech.dodd.quantifiedrecycler.EXTRA_AMOUNT";
    public static final String EXTRA_LOCATION =
            "tech.dodd.quantifiedrecycler.EXTRA_LOCATION";
    public static final String SAVE = "mySavedTips";

    public static String tip1AmountDF, tip2AmountDF, tip3AmountDF, tip4AmountDF;
    public static Button tipButtonQ, tipButton1, tipButton2, tipButton3, tipButton4;
    private EditText subTotal, Tax, tipQAmount, locationLabel, dateLabel, totalLabel;
    private TextView tipAmountView, tipPercentView, totalAmountView;
    private SeekBar mySeekBar;
    public Double taxA;
    private Integer add1edit2;
    private NoteViewModel noteViewModel;

    private class SBC implements SeekBar.OnSeekBarChangeListener {
        SBC() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TipActivity.this.tipFunction((progress * 0.001d), (progress * 1.0d));
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        subTotal = findViewById(R.id.subTotalText);
        Tax = findViewById(R.id.taxText);
        tipQAmount = findViewById(R.id.tipQAmountText);

        tipPercentView = findViewById(R.id.tipPercentView);
        tipAmountView = findViewById(R.id.tipAmountView);
        totalAmountView = findViewById(R.id.totalAmountView);
        locationLabel = findViewById(R.id.locationText);
        dateLabel = findViewById(R.id.dateText);
        totalLabel = findViewById(R.id.totalAmountText);
        subTotal.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        Tax.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        tipQAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        totalLabel.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        Date getdate = new Date();//This creates a date representing this instance in time.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formatdate = sdf.format(getdate);
        dateLabel.setText(formatdate);

        String text;
        Resources res = getResources();
        SharedPreferences loadTips = getSharedPreferences(SAVE, 0);
        tip1AmountDF = loadTips.getString("savedTip1", "10");
        tip2AmountDF = loadTips.getString("savedTip2", "15");
        tip3AmountDF = loadTips.getString("savedTip3", "17.25");
        tip4AmountDF = loadTips.getString("savedTip4", "20");
        tipButtonQ = findViewById(R.id.buttonQ);
        tipButton1 = findViewById(R.id.button15);
        tipButton2 = findViewById(R.id.button17);
        tipButton3 = findViewById(R.id.button18);
        tipButton4 = findViewById(R.id.button20);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        text = res.getString(R.string.buttontext, tip1AmountDF);
        tipButton1.setText(text);
        text = res.getString(R.string.buttontext, tip2AmountDF);
        tipButton2.setText(text);
        text = res.getString(R.string.buttontext, tip3AmountDF);
        tipButton3.setText(text);
        text = res.getString(R.string.buttontext, tip4AmountDF);
        tipButton4.setText(text);

        this.mySeekBar = findViewById(R.id.seekBar);
        this.mySeekBar.setOnSeekBarChangeListener(new SBC());

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            add1edit2 = 2;
            subTotal.setEnabled(false);
            Tax.setEnabled(false);
            tipQAmount.setEnabled(false);
            tipButtonQ.setEnabled(false);
            tipButton1.setEnabled(false);
            tipButton2.setEnabled(false);
            tipButton3.setEnabled(false);
            tipButton4.setEnabled(false);
            mySeekBar.setEnabled(false);
            totalAmountView.setVisibility(View.GONE);
            totalLabel.setVisibility(View.VISIBLE);
            dateLabel.setText(intent.getStringExtra(EXTRA_DATE));
            totalLabel.setText(intent.getStringExtra(EXTRA_AMOUNT));
            locationLabel.setText(intent.getStringExtra(EXTRA_LOCATION));
        } else {
            add1edit2 = 1;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.subTotal.getWindowToken(), 0);
        if (item.getItemId() == R.id.action_settings) {
            buttonResetFunction();
            startActivity(new Intent(this, SubActivity.class));
        }
        if (item.getItemId() == R.id.action_history) {
            buttonResetFunction();
            startActivity(new Intent(this, HistoryActivity.class));
        }
        if (item.getItemId() == R.id.action_help) {
            showAlert();
        }
        return true;
    }

    public void showAlert() {
        final AlertDialog helpalert = new AlertDialog.Builder(this)
                .setTitle(R.string.action_help)
                .setPositiveButton(R.string.action_done, null)
                .setMessage(R.string.help_dialogue)
                .setCancelable(true)
                .create();
        helpalert.show();
        // Make the textview clickable. Must be called after show()
        ((TextView) helpalert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void button15(View v) {
        tipFunction((Double.parseDouble(tip1AmountDF) * 0.01), (Double.parseDouble(tip1AmountDF) * 10.0));
    }

    public void button17(View v) {
        tipFunction((Double.parseDouble(tip2AmountDF) * 0.01), (Double.parseDouble(tip2AmountDF) * 10.0));
    }

    public void button18(View v) {
        tipFunction((Double.parseDouble(tip3AmountDF) * 0.01), (Double.parseDouble(tip3AmountDF) * 10.0));
    }

    public void button20(View v) {
        tipFunction((Double.parseDouble(tip4AmountDF) * 0.01), (Double.parseDouble(tip4AmountDF) * 10.0));
    }

    public void dateTextClick(View v) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.subTotal.getWindowToken(), 0);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datepicker = new DatePickerDialog(TipActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        String output = formatter.format(calendar.getTime()); //eg: "Tue May"
                        dateLabel.setText(output);
                    }
                }, year, month, day);
        datepicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datepicker.show();
    }


    public void buttonQ(View v) {
        DecimalFormat df = new DecimalFormat("#.00");

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(this.subTotal.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(this.Tax.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(this.tipQAmount.getWindowToken(), 0);
        if (this.subTotal.getText().toString().trim().length() != 0 && this.tipQAmount.getText().toString().trim().length() != 0) {
            if (this.subTotal.getText().toString().equals(".") || this.Tax.getText().toString().equals(".") || this.tipQAmount.getText().toString().equals(".")) {
                Toast.makeText(this, R.string.toast_invalid_text, Toast.LENGTH_SHORT).show();
                return;
            }
            if (this.Tax.getText().toString().trim().length() == 0) {
                this.taxA = 0.00;
            } else {
                this.taxA = Double.valueOf(this.Tax.getText().toString());
            }
            String tipAmountDf = df.format(Double.valueOf(this.tipQAmount.getText().toString()));
            String totalDf = df.format((Double.parseDouble(this.subTotal.getText().toString()) + Double.parseDouble(tipAmountDf)) + this.taxA);
            String tipPercentDf = df.format((Double.parseDouble(this.tipQAmount.getText().toString()) / Double.parseDouble(this.subTotal.getText().toString())) * 100.0d);
            this.mySeekBar.setProgress(((int) Double.valueOf((Double.parseDouble(this.tipQAmount.getText().toString()) / Double.parseDouble(this.subTotal.getText().toString())) * 100.0d).doubleValue()) * 10);
            this.tipPercentView.setText(tipPercentDf);
            this.tipAmountView.setText(tipAmountDf);
            this.totalAmountView.setText(totalDf);
        }
    }


    public void tipFunction(double n1, double n2) {
        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.DOWN);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(this.subTotal.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(this.Tax.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(this.tipQAmount.getWindowToken(), 0);
        if (this.subTotal.getText().toString().trim().length() == 0) {
            return;
        }
        if (this.subTotal.getText().toString().equals(".") || this.Tax.getText().toString().equals(".")) {
            Toast.makeText(this, R.string.toast_invalid_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.Tax.getText().toString().trim().length() == 0) {
            this.taxA = 0.00;
        } else {
            this.taxA = Double.valueOf(this.Tax.getText().toString());
        }
        String tipAmountDf = df.format(Double.parseDouble(this.subTotal.getText().toString()) * n1);
        String totalDf = df.format((Double.parseDouble(this.subTotal.getText().toString()) + Double.parseDouble(tipAmountDf)) + this.taxA);
        String tipPercentDf = df.format(0.1d * n2);
        this.mySeekBar.setProgress((int) Double.valueOf(n2).doubleValue());
        this.tipPercentView.setText(tipPercentDf);
        this.tipAmountView.setText(tipAmountDf);
        this.totalAmountView.setText(totalDf);

    }

    public void buttonResetFunction() {
        add1edit2 = 1;
        subTotal.setEnabled(true);
        Tax.setEnabled(true);
        tipQAmount.setEnabled(true);
        tipButtonQ.setEnabled(true);
        tipButton1.setEnabled(true);
        tipButton2.setEnabled(true);
        tipButton3.setEnabled(true);
        tipButton4.setEnabled(true);
        totalAmountView.setVisibility(View.VISIBLE);
        totalLabel.setVisibility(View.GONE);
        subTotal.setText(null);
        Tax.setText(null);
        tipQAmount.setText(null);
        locationLabel.setText(null);
        totalLabel.setText(null);
        tipPercentView.setText("");
        totalAmountView.setText("");
        tipAmountView.setText("");
        mySeekBar.setEnabled(true);
        this.mySeekBar.setProgress(150);

        Date getdate = new Date();//This creates a date representing this instance in time.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formatdate = sdf.format(getdate);
        dateLabel.setText(formatdate);

        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.subTotal.getWindowToken(), 0);
    }

    public void buttonReset(View v) {
        buttonResetFunction();
    }

    public void buttonSave(View v) {
        String date = dateLabel.getText().toString();
        String amount = "";
        String location = locationLabel.getText().toString();

        DecimalFormat df = new DecimalFormat("#.00");
        String tipAmountDf = df.format(Double.valueOf(this.tipQAmount.getText().toString()));

        if(!tipAmountDf.equals(tipAmountView.getText().toString())){
            Toast.makeText(this, R.string.toast_nomatch_text, Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalAmountView.getText().toString().trim().length() != 0) {
            if (totalAmountView.getText().toString().equals(".")) {
                Toast.makeText(this, R.string.toast_empty_text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                double convertTAV = Double.parseDouble(totalAmountView.getText().toString()) * 100;
                int convertTAVD = (int) Math.round(convertTAV);
                amount = String.valueOf(convertTAVD);
            }
        } else if (totalLabel.getText().toString().trim().length() != 0) {
            if (totalLabel.getText().toString().equals(".")) {
                Toast.makeText(this, R.string.toast_empty_text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                double convertTL = Double.parseDouble(totalLabel.getText().toString()) * 100;
                int convertTLD = (int) Math.round(convertTL);
                amount = String.valueOf(convertTLD);
            }
        }


        if (location.trim().isEmpty() || amount.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_empty_text, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_AMOUNT, amount);
        data.putExtra(EXTRA_LOCATION, location);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        if (add1edit2 == 2) {
            setResult(RESULT_OK, data);
            finish();
        } else if (add1edit2 == 1) {
            Note note = new Note(date, amount, location);
            noteViewModel.insert(note);

            Toast.makeText(this, R.string.toast_saved_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(add1edit2 == 1){
            this.finishAffinity();
        }else{
            super.onBackPressed();
        }
    }
}
