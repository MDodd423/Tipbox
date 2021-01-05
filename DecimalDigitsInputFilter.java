package tech.dodd.tipbox;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    private final Pattern mPattern;

    DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        mPattern = Pattern.compile(String.format("[0-9]{0,%d}(\\.[0-9]{0,%d})?", digitsBeforeZero, digitsAfterZero));
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(createResultString(source, start, end, dest, dstart, dend));
        if (!matcher.matches())
            return "";
        return null;
    }

    private String createResultString(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceString = source.toString();
        String destString = dest.toString();
        return destString.substring(0, dstart) + sourceString.substring(start, end) + destString.substring(dend);
    }
}
