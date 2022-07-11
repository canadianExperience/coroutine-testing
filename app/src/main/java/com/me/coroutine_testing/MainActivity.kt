package com.me.coroutine_testing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
        val cruncher = NumberCruncher(coroutineScope)
        var textState by rememberSaveable { mutableStateOf("Press button to start calculation") }

        Text(text = textState)

        Button(
            onClick = {
                textState = "Start single coroutine ..."
                coroutineScope.launch {
                    val calculationResult = cruncher.getResult()
                    textState = String.format("Got result %d", calculationResult)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Calculate result with single coroutine",
                color = Color.White
            )
        }

        Button(
            onClick = {
                textState = "Emitting result to shared flow ..."
                coroutineScope.launch {
                    cruncher.calculateShared()
                    cruncher.resultsShared.collect{
                        textState = String.format("Got result %d", it)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Calculate result with shared flow",
            )
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    cruncher.calculateState()
                    cruncher.resultsState.collect{
                        textState = if(it == -1) "Emitting result to state flow ..."
                        else String.format("Got result %d", it)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Calculate result with state flow",
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