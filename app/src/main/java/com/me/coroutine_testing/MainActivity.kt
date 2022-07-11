package com.me.coroutine_testing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.coroutine_testing.data.NumberCruncher
import com.me.coroutine_testing.ui.theme.CoroutinetestingTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinetestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CruncherScreen()
                }
            }
        }
    }
}

@Composable
fun CruncherScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {

        val coroutineScope = rememberCoroutineScope()
        var textState by rememberSaveable { mutableStateOf("Press button to start calculation") }
        val cruncher = NumberCruncher()

        Text(text = textState)

        Button(
            onClick = {
                textState = "Calculating ..."
                coroutineScope.launch {
                    val calculationResult = cruncher.getResult()
                    textState = String.format("Got result %d", calculationResult)
                }
            },
        ) {
            Text(
                text = "Calculate",
                color = Color.White
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoroutinetestingTheme {
        CruncherScreen()
    }
}