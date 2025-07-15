package tech.dodd.tipbox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

import tech.dodd.tipbox.databinding.ActivityTipBinding;

public class TipActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "tech.dodd.tipbox.EXTRA_ID";
    public static final String EXTRA_DATE =
            "tech.dodd.tipbox.EXTRA_DATE";
    public static final String EXTRA_AMOUNT =
            "tech.dodd.tipbox.EXTRA_AMOUNT";
    public static final String EXTRA_LOCATION =
            "tech.dodd.tipbox.EXTRA_LOCATION";
    public static final String SAVE = "mySavedTips";

    public static String tip1AmountDF, tip2AmountDF, tip3AmountDF, tip4AmountDF;
    public Double taxA;
    private Integer add1edit2;
    public NoteViewModel noteViewModel;
    ActivityTipBinding activityTipBinding;

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
        EdgeToEdge.enable(this);
        // Get the window instance & set the status bar color
        Window currentWindow = getWindow(); // <--- Get the window object
        WindowCompat.getInsetsController(currentWindow, currentWindow.getDecorView())
                .setAppearanceLightStatusBars(false); //true for dark, false for light
        activityTipBinding = ActivityTipBinding.inflate(getLayoutInflater());
        View view = activityTipBinding.getRoot();
        setContentView(view);

        //Edge to Edge Display
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.setMargins(0, insets.top, 0, insets.bottom);
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });

        SharedPreferences loadTips = getSharedPreferences(SAVE, 0);
        tip1AmountDF = loadTips.getString("savedTip1", "10");
        tip2AmountDF = loadTips.getString("savedTip2", "15");
        tip3AmountDF = loadTips.getString("savedTip3", "17.25");
        tip4AmountDF = loadTips.getString("savedTip4", "20");
        activityTipBinding.tip1Button.setText(getResources().getString(R.string.buttontext, tip1AmountDF));
        activityTipBinding.tip2Button.setText(getResources().getString(R.string.buttontext, tip2AmountDF));
        activityTipBinding.tip3Button.setText(getResources().getString(R.string.buttontext, tip3AmountDF));
        activityTipBinding.tip4Button.setText(getResources().getString(R.string.buttontext, tip4AmountDF));

        activityTipBinding.subtotalEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        activityTipBinding.taxEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        activityTipBinding.tipQamountEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        activityTipBinding.totalAmountEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        activityTipBinding.seekBar.setOnSeekBarChangeListener(new SBC());
        activityTipBinding.dateEditText.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date()));

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            add1edit2 = 2;
            activityTipBinding.subtotalEditText.setEnabled(false);
            activityTipBinding.taxEditText.setEnabled(false);
            activityTipBinding.tipQamountEditText.setHint("");
            activityTipBinding.tipQamountEditText.setEnabled(false);
            activityTipBinding.tipQButton.setEnabled(false);
            activityTipBinding.tip1Button.setEnabled(false);
            activityTipBinding.tip2Button.setEnabled(false);
            activityTipBinding.tip3Button.setEnabled(false);
            activityTipBinding.tip4Button.setEnabled(false);
            activityTipBinding.seekBar.setEnabled(false);
            activityTipBinding.totalAmountTextView.setVisibility(View.GONE);
            activityTipBinding.totalAmountEditText.setVisibility(View.VISIBLE);
            activityTipBinding.dateEditText.setText(intent.getStringExtra(EXTRA_DATE));
            activityTipBinding.totalAmountEditText.setText(intent.getStringExtra(EXTRA_AMOUNT));
            activityTipBinding.locationEditText.setText(intent.getStringExtra(EXTRA_LOCATION));
        } else {
            add1edit2 = 1;
            activityTipBinding.tipQamountEditText.setHint(R.string.tipAText_text);
        }

        activityTipBinding.tip1Button.setOnClickListener(v -> tipFunction((Double.parseDouble(tip1AmountDF) * 0.01), (Double.parseDouble(tip1AmountDF) * 10.0)));
        activityTipBinding.tip2Button.setOnClickListener(v -> tipFunction((Double.parseDouble(tip2AmountDF) * 0.01), (Double.parseDouble(tip2AmountDF) * 10.0)));
        activityTipBinding.tip3Button.setOnClickListener(v -> tipFunction((Double.parseDouble(tip3AmountDF) * 0.01), (Double.parseDouble(tip3AmountDF) * 10.0)));
        activityTipBinding.tip4Button.setOnClickListener(v -> tipFunction((Double.parseDouble(tip4AmountDF) * 0.01), (Double.parseDouble(tip4AmountDF) * 10.0)));
        activityTipBinding.tipQButton.setOnClickListener(v -> buttonQ());
        activityTipBinding.dateEditText.setOnClickListener(v -> dateEditTextFunction());
        activityTipBinding.saveButton.setOnClickListener(v -> buttonSave());
        activityTipBinding.resetButton.setOnClickListener(v -> buttonReset());
        activityTipBinding.OptionHistoryImageView.setOnClickListener(v -> doHistory());
        activityTipBinding.OptionHelpImageView.setOnClickListener(v -> showAlert());
        activityTipBinding.OptionSettingsImageView.setOnClickListener(v -> doSettings());
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences loadTips = getSharedPreferences(SAVE, 0);
        tip1AmountDF = loadTips.getString("savedTip1", "10");
        tip2AmountDF = loadTips.getString("savedTip2", "15");
        tip3AmountDF = loadTips.getString("savedTip3", "17.25");
        tip4AmountDF = loadTips.getString("savedTip4", "20");
        activityTipBinding.tip1Button.setText(getResources().getString(R.string.buttontext, tip1AmountDF));
        activityTipBinding.tip2Button.setText(getResources().getString(R.string.buttontext, tip2AmountDF));
        activityTipBinding.tip3Button.setText(getResources().getString(R.string.buttontext, tip3AmountDF));
        activityTipBinding.tip4Button.setText(getResources().getString(R.string.buttontext, tip4AmountDF));
    }

    public void doSettings(){
        buttonReset();
        startActivity(new Intent().setComponent(new ComponentName("appinventor.ai_MikeDodd944.TipCalc",
                "tech.dodd.tipbox.SubActivity")));
    }

    public void doHistory(){
        buttonReset();
        startActivity(new Intent().setComponent(new ComponentName("appinventor.ai_MikeDodd944.TipCalc",
                "tech.dodd.tipbox.HistoryActivity")));
    }

    public void showAlert() {
        final AlertDialog helpAlert = new AlertDialog.Builder(this)
                .setTitle(R.string.action_help)
                .setPositiveButton(R.string.action_done, null)
                .setMessage(R.string.help_dialogue)
                .setCancelable(true)
                .create();
        helpAlert.show();
        // Make the textview clickable. Must be called after show()
        ((TextView) helpAlert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void doneFunction() {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(activityTipBinding.subtotalEditText.getWindowToken(), 0);
        activityTipBinding.subtotalEditText.clearFocus();
        activityTipBinding.taxEditText.clearFocus();
        activityTipBinding.tipQamountEditText.clearFocus();
        activityTipBinding.locationEditText.clearFocus();
        //activityTipBinding.seekBar.requestFocus();
    }

    public void dateEditTextFunction() {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(activityTipBinding.subtotalEditText.getWindowToken(), 0);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePicker = new DatePickerDialog(TipActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year1, monthOfYear, dayOfMonth);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    String output = formatter.format(calendar.getTime()); //eg: "Tue May"
                    activityTipBinding.dateEditText.setText(output);
                }, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
        doneFunction();
    }


    public void buttonQ() {
        DecimalFormat df = new DecimalFormat("#.00");

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(activityTipBinding.subtotalEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(activityTipBinding.taxEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(activityTipBinding.tipQamountEditText.getWindowToken(), 0);
        if (!activityTipBinding.subtotalEditText.getText().toString().trim().isEmpty() && !activityTipBinding.tipQamountEditText.getText().toString().trim().isEmpty()) {
            if (activityTipBinding.subtotalEditText.getText().toString().equals(".") || activityTipBinding.taxEditText.getText().toString().equals(".") || activityTipBinding.tipQamountEditText.getText().toString().equals(".")) {
                Toast.makeText(this, R.string.toast_invalid_text, Toast.LENGTH_SHORT).show();
                return;
            }
            if (activityTipBinding.taxEditText.getText().toString().trim().isEmpty()) {
                taxA = 0.00;
            } else {
                taxA = Double.valueOf(activityTipBinding.taxEditText.getText().toString());
            }
            String tipAmountDf = df.format(Double.valueOf(activityTipBinding.tipQamountEditText.getText().toString()));
            String totalDf = df.format((Double.parseDouble(activityTipBinding.subtotalEditText.getText().toString()) + Double.parseDouble(tipAmountDf)) + taxA);
            String tipPercentDf = df.format((Double.parseDouble(activityTipBinding.tipQamountEditText.getText().toString()) / Double.parseDouble(activityTipBinding.subtotalEditText.getText().toString())) * 100.0d);
            activityTipBinding.seekBar.setProgress(((int) Double.valueOf((Double.parseDouble(activityTipBinding.tipQamountEditText.getText().toString()) / Double.parseDouble(activityTipBinding.subtotalEditText.getText().toString())) * 100.0d).doubleValue()) * 10);
            activityTipBinding.tipPercentTextView.setText(tipPercentDf);
            activityTipBinding.tipAmountTextView.setText(tipAmountDf);
            activityTipBinding.totalAmountTextView.setText(totalDf);
        }
        doneFunction();
    }


    public void tipFunction(double n1, double n2) {
        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.DOWN);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(activityTipBinding.subtotalEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(activityTipBinding.taxEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(activityTipBinding.tipQamountEditText.getWindowToken(), 0);
        if (activityTipBinding.subtotalEditText.getText().toString().trim().isEmpty()) {
            return;
        }
        if (activityTipBinding.subtotalEditText.getText().toString().equals(".") || activityTipBinding.taxEditText.getText().toString().equals(".")) {
            Toast.makeText(this, R.string.toast_invalid_text, Toast.LENGTH_SHORT).show();
            return;
        }
        if (activityTipBinding.taxEditText.getText().toString().trim().isEmpty()) {
            taxA = 0.00;
        } else {
            taxA = Double.valueOf(activityTipBinding.taxEditText.getText().toString());
        }
        String tipAmountDf = df.format(Double.parseDouble(activityTipBinding.subtotalEditText.getText().toString()) * n1);
        String totalDf = df.format((Double.parseDouble(activityTipBinding.subtotalEditText.getText().toString()) + Double.parseDouble(tipAmountDf)) + taxA);
        String tipPercentDf = df.format(0.1d * n2);
        activityTipBinding.seekBar.setProgress((int) Double.valueOf(n2).doubleValue());
        activityTipBinding.tipPercentTextView.setText(tipPercentDf);
        activityTipBinding.tipAmountTextView.setText(tipAmountDf);
        activityTipBinding.totalAmountTextView.setText(totalDf);
        doneFunction();
    }

    public void buttonReset() {
        add1edit2 = 1;
        activityTipBinding.subtotalEditText.setEnabled(true);
        activityTipBinding.taxEditText.setEnabled(true);
        activityTipBinding.tipQamountEditText.setEnabled(true);
        activityTipBinding.tipQamountEditText.setHint(R.string.tipAText_text);
        activityTipBinding.tipQButton.setEnabled(true);
        activityTipBinding.tip1Button.setEnabled(true);
        activityTipBinding.tip2Button.setEnabled(true);
        activityTipBinding.tip3Button.setEnabled(true);
        activityTipBinding.tip4Button.setEnabled(true);
        activityTipBinding.totalAmountTextView.setVisibility(View.VISIBLE);
        activityTipBinding.totalAmountEditText.setVisibility(View.GONE);
        activityTipBinding.subtotalEditText.setText(null);
        activityTipBinding.taxEditText.setText(null);
        activityTipBinding.tipQamountEditText.setText(null);
        activityTipBinding.locationEditText.setText(null);
        activityTipBinding.totalAmountEditText.setText(null);
        activityTipBinding.tipPercentTextView.setText("");
        activityTipBinding.totalAmountTextView.setText("");
        activityTipBinding.tipAmountTextView.setText("");
        activityTipBinding.seekBar.setEnabled(true);
        activityTipBinding.seekBar.setProgress(150);

        Date getDate = new Date();//This creates a date representing this instance in time.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formatDate = sdf.format(getDate);
        activityTipBinding.dateEditText.setText(formatDate);

        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(activityTipBinding.subtotalEditText.getWindowToken(), 0);
        doneFunction();
    }

    public void buttonSave() {
        String date = activityTipBinding.dateEditText.getText().toString();
        String amount = "";
        String location = activityTipBinding.locationEditText.getText().toString();

        if (!activityTipBinding.tipQamountEditText.getText().toString().trim().isEmpty()) {
            DecimalFormat df = new DecimalFormat("#.00");
            String tipAmountDf = df.format(Double.valueOf(activityTipBinding.tipQamountEditText.getText().toString()));

            if (!tipAmountDf.equals(activityTipBinding.tipAmountTextView.getText().toString())) {
                Toast.makeText(this, R.string.toast_nomatch_text, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!activityTipBinding.totalAmountTextView.getText().toString().trim().isEmpty()) {
            if (activityTipBinding.totalAmountTextView.getText().toString().equals(".")) {
                Toast.makeText(this, R.string.toast_empty_text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                double convertTAV = Double.parseDouble(activityTipBinding.totalAmountTextView.getText().toString()) * 100;
                int convertTAVD = (int) Math.round(convertTAV);
                amount = String.valueOf(convertTAVD);
            }
        } else if (!activityTipBinding.totalAmountEditText.getText().toString().trim().isEmpty()) {
            if (activityTipBinding.totalAmountEditText.getText().toString().equals(".")) {
                Toast.makeText(this, R.string.toast_empty_text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                double convertTL = Double.parseDouble(activityTipBinding.totalAmountEditText.getText().toString()) * 100;
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
        doneFunction();
    }
}