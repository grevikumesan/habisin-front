package com.example.habisin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.habisin.ui.router.AppRouter
import com.example.habisin.ui.theme.HabisInTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabisInTheme {
                // AppRouter owns the NavHost for the whole app.
                // TODO (teammates): if you need to pass app-level state down
                // (e.g. a shared ViewModel), do it here before AppRouter.
                AppRouter()
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabisInTheme {
        Greeting("Android")
    }
}