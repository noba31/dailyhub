package com.example.dailyhub.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val quotes = listOf(
    "The secret of getting ahead is getting started. — Mark Twain",
    "Well begun is half done. — Aristotle",
    "It always seems impossible until it's done. — Nelson Mandela",
    "Do what you can, with what you have, where you are. — Theodore Roosevelt",
    "Small steps every day lead to big results. — Unknown",
    "You don't have to be great to start, but you have to start to be great. — Zig Ziglar",
    "Action is the foundational key to all success. — Pablo Picasso",
    "The best way to predict the future is to create it. — Peter Drucker",
    "Quality is not an act, it is a habit. — Aristotle",
    "Start where you are. Use what you have. Do what you can. — Arthur Ashe",
    "Focus on being productive instead of busy. — Tim Ferriss",
    "Simplicity is the ultimate sophistication. — Leonardo da Vinci",
    "What we do every day matters more than what we do once in a while. — Gretchen Rubin",
    "Energy and persistence conquer all things. — Benjamin Franklin",
    "Don't watch the clock; do what it does. Keep going. — Sam Levenson"
)

@Composable
fun HomeScreen() {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }

    val cal = Calendar.getInstance().apply { timeInMillis = now }
    val greeting = when (cal.get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
    val dateText = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date(now))
    val timeText = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(now))

    var quoteIndex by rememberSaveable {
        mutableIntStateOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % quotes.size)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(greeting, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(dateText, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Current time", style = MaterialTheme.typography.labelLarge)
                Text(timeText, style = MaterialTheme.typography.displayMedium)
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Quote of the day", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    quotes[quoteIndex],
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { quoteIndex = (quoteIndex + 1) % quotes.size }) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Filled.Refresh, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Next quote")
                    }
                }
            }
        }
    }
}
