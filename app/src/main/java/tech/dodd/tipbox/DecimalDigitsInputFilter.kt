package tech.dodd.tipbox

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter internal constructor(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
    private val mPattern: Pattern = Pattern.compile(String.format("[0-9]{0,%d}(\\.[0-9]{0,%d})?", digitsBeforeZero, digitsAfterZero))

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val matcher = mPattern.matcher(createResultString(source, start, end, dest, dstart, dend))
        return if (!matcher.matches()) "" else null
    }

    private fun createResultString(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): String {
        val sourceString = source.toString()
        val destString = dest.toString()
        return destString.substring(0, dStart) + sourceString.substring(start, end) + destString.substring(dEnd)
    }
}