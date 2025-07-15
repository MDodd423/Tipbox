package tech.dodd.tipbox

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import tech.dodd.tipbox.theme.TipboxTheme
import kotlin.text.isNotBlank
import kotlin.text.removeSuffix

class SubActivity : ComponentActivity() {
    private val sharedPrefs = "mySavedTips"
    private val defaultTip1 = "10"
    private val defaultTip2 = "15"
    private val defaultTip3 = "17.25"
    private val defaultTip4 = "20"

    private var tip1 by mutableStateOf(defaultTip1)
    private var tip2 by mutableStateOf(defaultTip2)
    private var tip3 by mutableStateOf(defaultTip3)
    private var tip4 by mutableStateOf(defaultTip4)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        getSharedPreferences(sharedPrefs, 0).apply {
            tip1 = getString("savedTip1", defaultTip1).toString()
            tip2 = getString("savedTip2", defaultTip2).toString()
            tip3 = getString("savedTip3", defaultTip3).toString()
            tip4 = getString("savedTip4", defaultTip4).toString()
        }
        setContent {
            TipboxTheme {
                val focusManager = LocalFocusManager.current
                var inputTip1 by remember { mutableStateOf("") }
                var inputTip2 by remember { mutableStateOf("") }
                var inputTip3 by remember { mutableStateOf("") }
                var inputTip4 by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(
                        title = { Text("Settings") },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                        navigationIcon = {
                            IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    "Localized description",
                                    tint = Color.White
                                )
                            }
                        }, actions = {
                            IconButton(onClick = {
                                showAlert()
                                focusManager.clearFocus()
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_help),
                                    "contentDescription",
                                    tint = Color.White
                                )
                            }
                        })
                    TipInputRow(inputTip1, { inputTip1 = getValidatedNumber(it) }, tip1) {
                        tip1 = inputTip1.takeIf { it.isNotBlank() && it != "." }?.removeSuffix(".")
                            ?: tip1
                        save()
                        inputTip1 = ""
                        focusManager.clearFocus()
                    }
                    TipInputRow(inputTip2, { inputTip2 = getValidatedNumber(it) }, tip2) {
                        tip2 = inputTip2.takeIf { it.isNotBlank() && it != "." }?.removeSuffix(".")
                            ?: tip2
                        save()
                        inputTip2 = ""
                        focusManager.clearFocus()
                    }
                    TipInputRow(inputTip3, { inputTip3 = getValidatedNumber(it) }, tip3) {
                        tip3 = inputTip3.takeIf { it.isNotBlank() && it != "." }?.removeSuffix(".")
                            ?: tip3
                        save()
                        inputTip3 = ""
                        focusManager.clearFocus()
                    }
                    TipInputRow(inputTip4, { inputTip4 = getValidatedNumber(it) }, tip4) {
                        tip4 = inputTip4.takeIf { it.isNotBlank() && it != "." }?.removeSuffix(".")
                            ?: tip4
                        save()
                        inputTip4 = ""
                        focusManager.clearFocus()
                    }
                    Button(
                        onClick = {
                            tip1 = defaultTip1
                            tip2 = defaultTip2
                            tip3 = defaultTip3
                            tip4 = defaultTip4
                            inputTip1 = ""
                            inputTip2 = ""
                            inputTip3 = ""
                            inputTip4 = ""
                            save()
                            focusManager.clearFocus()
                        },
                        shape = RoundedCornerShape(10),
                        modifier = Modifier.padding(PaddingValues(top = 64.dp))
                    ) {
                        Text(text = "Reset")
                    }
                }
            }
        }
    }

    // Reusable Composable for Tip Input Rows
    @Composable
    private fun TipInputRow(
        inputText: String,
        onInputChange: (String) -> Unit,
        tipValue: String,
        onSaveClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 8.dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                value = inputText,
                modifier = Modifier
                    .background(Color.Transparent)
                    .weight(7f),
                onValueChange = onInputChange
            )
            Button(
                onClick = onSaveClick,
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .weight(3f)
                    .padding(PaddingValues(start = 8.dp))
            ) {
                Text(text = "$tipValue%")
            }
        }
    }


    private fun getValidatedNumber(text: String): String {
        // Start by filtering out unwanted characters like commas and multiple decimals
        val filteredChars = text.filterIndexed { index, c ->
            c in "0123456789" || (c == '.' && text.indexOf('.') == index)  // Take all digits OR Take only the first decimal
        }
        // Now we need to remove extra digits from the input
        return if (filteredChars.contains('.')) {
            val beforeDecimal = filteredChars.substringBefore('.')
            val afterDecimal = filteredChars.substringAfter('.')
            beforeDecimal.take(2) + "." + afterDecimal.take(2)    // If decimal is present, take first 2 digits before decimal and first 2 digits after decimal
        } else {
            filteredChars.take(2)
                .trimStart('0')   // If there is no decimal, just take the first 2 digits
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle(R.string.action_help)
            .setMessage(R.string.help_dialogue)
            .setPositiveButton(R.string.action_done, null)
            .setCancelable(true)
            .show()
            .apply {
                (findViewById<View>(android.R.id.message) as? TextView)?.movementMethod =
                    LinkMovementMethod.getInstance()
            }
    }

    private fun save() {
        getSharedPreferences(sharedPrefs, 0).edit().apply {
            putString("savedTip1", tip1)
            putString("savedTip2", tip2)
            putString("savedTip3", tip3)
            putString("savedTip4", tip4)
            apply()
        }
    }
}