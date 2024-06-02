package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipTimeTheme {
                CalculateTip(modifier = Modifier)
            }
        }
    }
}

@Composable
fun CalculateTip(modifier: Modifier) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        CalculateFields(modifier = Modifier)
    }
}

@Composable
fun CalculateFields(modifier: Modifier) {
    var billAmount by remember {
        mutableStateOf("")
    }

    var tipPercentage by remember {
        mutableStateOf("")
    }

    var tipAmount by remember {
        mutableStateOf("0.00")
    }

    var roundUpTip by remember {
        mutableStateOf(false)
    }

    fun updateTipAmount() {
        if (billAmount != "" && tipPercentage != "") {
            var amount = (billAmount.toDouble() * tipPercentage.toDouble()) / 100.0
            tipAmount = amount.toString()
        }
    }

    fun roundTip() {
        if (roundUpTip) {
            tipAmount = ceil(tipAmount.toDouble()).toString()
        } else {
            updateTipAmount()
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Text(text = "Calculate Tip", modifier.padding(bottom = 16.dp))
        EditField(
            billAmount, stringResource(R.string.bill_amount), KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ), onValueChange = {
                billAmount = it
                updateTipAmount()
            }, modifier = Modifier
        )
        Spacer(modifier.height(16.dp))
        EditField(
            tipPercentage, stringResource(R.string.tip_percentage), KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ), onValueChange = {
                tipPercentage = it
                updateTipAmount()
            }, modifier = Modifier
        )
        RoundTip(roundUpTip, onCheckedChange = {
            if (billAmount != "" && tipPercentage != "") {
                roundUpTip = it
                roundTip()
            }
        }, modifier = Modifier)
        Text(
            text = stringResource(R.string.tip_amount, tipAmount),
            modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EditField(
    value: String,
    fieldLabel: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier.fillMaxWidth(),
        label = { Text(text = fieldLabel) },
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}

@Composable
fun RoundTip(value: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier) {
    Row(
        modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.round_up_tip))
        Switch(checked = value, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipTimeTheme {
        CalculateTip(modifier = Modifier)
    }
}