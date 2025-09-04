package com.weighttrackerapp.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.animation.animateContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.weighttrackerapp.data.WeightEntry
import com.weighttrackerapp.viewmodel.WeightViewModel
import com.patrykandpatryk.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.core.entry.ChartEntryModel
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.entryOf
import com.patrykandpatryk.vico.core.entry.entryModelOf
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

// The main composable function for the entire app.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightTrackerApp(viewModel: WeightViewModel) {
    val weightEntries by viewModel.allWeightEntries.collectAsState(initial = emptyList())
    var weightInput by remember { mutableStateOf(TextFieldValue("")) }
    var heightInput by remember { mutableStateOf(TextFieldValue("")) }

    // Compute latest BMI from the latest entry when available
    val latestBmi: String? = remember(weightEntries) {
        weightEntries.lastOrNull()?.let { entry ->
            val hMeters = if (entry.heightCm > 0f) entry.heightCm / 100f else 0f
            if (hMeters > 0f) {
                val bmi = entry.weight / (hMeters * hMeters)
                String.format(Locale.getDefault(), "%.1f", bmi)
            } else null
        }
    }

    var showClearAllDialog by remember { mutableStateOf(false) }
    var pendingDeleteId by remember { mutableStateOf<Int?>(null) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Plain title (not an app bar)
            Text(
                text = "Weight Tracker",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Instruction card at the top (collapsible)
            var instructionsExpanded by remember { mutableStateOf(false) }
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .animateContentSize()
                    .clickable { instructionsExpanded = !instructionsExpanded },
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = if (instructionsExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (instructionsExpanded) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "1) Enter weight (kg) and height (cm), then tap Add.\n2) The latest BMI appears below.\n3) The chart shows your weight over time.\n4) Use Delete to remove a record or Clear All to reset.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Input field and button to add new weight
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Enter weight") },
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 72.dp),
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3
                )
                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 72.dp),
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show latest BMI if available
            latestBmi?.let { bmiStr ->
                Text(
                    text = "Latest BMI: $bmiStr",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            // Clear all button aligned to the end
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add button
                Button(
                    onClick = {
                        val weight = weightInput.text.toFloatOrNull()
                        val height = heightInput.text.toFloatOrNull()
                        if (weight != null && height != null && height > 0f) {
                            viewModel.addWeightEntry(weight, height)
                            weightInput = TextFieldValue("")
                            // keep height for convenience
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                ) {
                    Text("Add")
                }
                Spacer(Modifier.width(8.dp))
                // Clear All button
                OutlinedButton(
                    onClick = { showClearAllDialog = true },
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.DeleteSweep, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Clear All")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Display the graph if there is data
            if (weightEntries.isNotEmpty()) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Weight Over Time", 
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Chart area on a colored container for visual emphasis
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ) {
                            WeightGraph(weightEntries)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Entries list with per-item delete
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Entries",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        val dateFormat = remember { SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()) }
                        weightEntries.forEachIndexed { idx, entry ->
                            val subtitle = "Weight: ${entry.weight} kg  •  Height: ${if (entry.heightCm > 0f) entry.heightCm.toInt() else 0} cm"
                            ListItem(
                                headlineContent = {
                                    Text(dateFormat.format(Date(entry.timestamp)))
                                },
                                supportingContent = {
                                    Text(subtitle)
                                },
                                trailingContent = {
                                    IconButton(onClick = { pendingDeleteId = entry.id }) {
                                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete entry")
                                    }
                                }
                            )
                            if (idx < weightEntries.lastIndex) Divider()
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        "No weight data available. Add your first entry to see the graph.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    // Clear all confirmation dialog
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text("Clear all records?") },
            text = { Text("This will permanently delete all entries.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAll()
                    showClearAllDialog = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Per-item delete confirmation dialog
    pendingDeleteId?.let { id ->
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text("Delete this record?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteEntry(id)
                    pendingDeleteId = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) { Text("Cancel") }
            }
        )
    }
}

// Composable to display the graph using the Vico library.
@Composable
fun WeightGraph(weightEntries: List<WeightEntry>) {
    val dateFormat = remember { SimpleDateFormat("MMM d", Locale.getDefault()) }
    val labels = remember(weightEntries) {
        weightEntries.map { entry -> dateFormat.format(Date(entry.timestamp)) }
    }
    val chartEntryModel = remember(weightEntries) {
        entryModelOf(weightEntries.mapIndexed { index, entry ->
            entryOf(index.toFloat(), entry.weight)
        })
    }

    Chart(
        chart = lineChart(),
        model = chartEntryModel,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(valueFormatter = { x, _ ->
            val i = x.roundToInt()
            if (i in labels.indices) labels[i] else ""
        }),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
