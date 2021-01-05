package tech.dodd.tipbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class SubActivity extends AppCompatActivity {
    private EditText tipAText, tipBText, tipCText, tipDText;
    private Button tipAButton, tipBButton, tipCButton, tipDButton;
    private Resources res;
    private String text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        res = getResources();
        this.tipAText = findViewById(R.id.tip1Text);
        this.tipBText = findViewById(R.id.tip2Text);
        this.tipCText = findViewById(R.id.tip3Text);
        this.tipDText = findViewById(R.id.tip4Text);
        tipAText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2,2)});
        tipBText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2,2)});
        tipCText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2,2)});
        tipDText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2,2)});
        this.tipAButton = findViewById(R.id.tip1Button);
        this.tipBButton = findViewById(R.id.tip2Button);
        this.tipCButton = findViewById(R.id.tip3Button);
        this.tipDButton = findViewById(R.id.tip4Button);

        text = res.getString(R.string.buttontext, TipActivity.tip1AmountDF);
        tipAButton.setText(text);
        text = res.getString(R.string.buttontext, TipActivity.tip2AmountDF);
        tipBButton.setText(text);
        text = res.getString(R.string.buttontext, TipActivity.tip3AmountDF);
        tipCButton.setText(text);
        text = res.getString(R.string.buttontext, TipActivity.tip4AmountDF);
        tipDButton.setText(text);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.tipAText.getWindowToken(), 0);
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (item.getItemId() == R.id.action_help) {
            showAlert();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
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
        ((TextView)helpalert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void buttontip1(View v) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.tipAText.getWindowToken(), 0);
        if (this.tipAText.getText().toString().trim().length() == 0) {
            return;
        }
        if (this.tipAText.getText().toString().endsWith(".")) {
            Toast.makeText(this, R.string.invalidAmount, Toast.LENGTH_SHORT).show();
            return;
        }
        TipActivity.tip1AmountDF = this.tipAText.getText().toString().replaceFirst("^0+(?!$)", "");
        text = res.getString(R.string.buttontext, TipActivity.tip1AmountDF);
        tipAButton.setText(text);
        TipActivity.tipButton1.setText(text);
        this.tipAText.setText(null);
        save();
    }

    public void buttontip2(View v) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.tipBText.getWindowToken(), 0);
        if (this.tipBText.getText().toString().trim().length() == 0) {
            return;
        }
        if (this.tipBText.getText().toString().endsWith(".")) {
            Toast.makeText(this, R.string.invalidAmount, Toast.LENGTH_SHORT).show();
            return;
        }
        TipActivity.tip2AmountDF = this.tipBText.getText().toString().replaceFirst("^0+(?!$)", "");
        text = res.getString(R.string.buttontext, TipActivity.tip2AmountDF);
        tipBButton.setText(text);
        TipActivity.tipButton2.setText(text);
        this.tipBText.setText(null);
        save();
    }

    public void buttontip3(View v) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.tipCText.getWindowToken(), 0);
        if (this.tipCText.getText().toString().trim().length() == 0) {
            return;
        }
        if (this.tipCText.getText().toString().endsWith(".")) {
            Toast.makeText(this, R.string.invalidAmount, Toast.LENGTH_SHORT).show();
            return;
        }
        TipActivity.tip3AmountDF = this.tipCText.getText().toString().replaceFirst("^0+(?!$)", "");
        text = res.getString(R.string.buttontext, TipActivity.tip3AmountDF);
        tipCButton.setText(text);
        TipActivity.tipButton3.setText(text);
        this.tipCText.setText(null);
        save();
    }

    public void buttontip4(View v) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.tipDText.getWindowToken(), 0);
        if (this.tipDText.getText().toString().trim().length() == 0) {
            return;
        }
        if (this.tipDText.getText().toString().endsWith(".")) {
            Toast.makeText(this, R.string.invalidAmount, Toast.LENGTH_SHORT).show();
            return;
        }
        TipActivity.tip4AmountDF = this.tipDText.getText().toString().replaceFirst("^0+(?!$)", "");
        text = res.getString(R.string.buttontext, TipActivity.tip4AmountDF);
        tipDButton.setText(text);
        TipActivity.tipButton4.setText(text);
        this.tipDText.setText(null);
        save();
    }

    public void resetTipAmounts(View v) {
        ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(this.tipAText.getWindowToken(), 0);
        TipActivity.tip1AmountDF = "10";
        text = res.getString(R.string.buttontext, TipActivity.tip1AmountDF);
        tipAButton.setText(text);
        TipActivity.tipButton1.setText(text);
        this.tipAText.setText(null);
        TipActivity.tip2AmountDF = "15";
        text = res.getString(R.string.buttontext, TipActivity.tip2AmountDF);
        tipBButton.setText(text);
        TipActivity.tipButton2.setText(text);
        this.tipBText.setText(null);
        TipActivity.tip3AmountDF = "17.25";
        text = res.getString(R.string.buttontext, TipActivity.tip3AmountDF);
        tipCButton.setText(text);
        TipActivity.tipButton3.setText(text);
        this.tipCText.setText(null);
        TipActivity.tip4AmountDF = "20";
        text = res.getString(R.string.buttontext, TipActivity.tip4AmountDF);
        tipDButton.setText(text);
        TipActivity.tipButton4.setText(text);
        this.tipDText.setText(null);
        save();
    }

    public void save() {
        SharedPreferences.Editor editor = getSharedPreferences(TipActivity.SAVE, 0).edit();
        editor.putString("savedTip1", TipActivity.tip1AmountDF).putString("savedTip2", TipActivity.tip2AmountDF).putString("savedTip3", TipActivity.tip3AmountDF).putString("savedTip4", TipActivity.tip4AmountDF);
        editor.apply();
    }
}
