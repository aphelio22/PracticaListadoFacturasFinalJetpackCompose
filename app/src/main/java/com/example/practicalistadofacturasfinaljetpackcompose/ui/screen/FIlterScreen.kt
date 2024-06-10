package com.example.practicalistadofacturasfinaljetpackcompose.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.practicalistadofacturasfinaljetpackcompose.R
import com.example.practicalistadofacturasfinaljetpackcompose.ui.viewmodel.InvoiceActivityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesFiltersScreen(
    onApplyFiltersClick: () -> Unit,
    onRestoreFiltersClick: () -> Unit,
    viewModel: InvoiceActivityViewModel,
    navigationCOntroller: NavHostController
) {
    val scrollState = rememberScrollState()
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtrar Facturas") },
                actions = {
                    IconButton(onClick = { navigationCOntroller.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close Filter")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            SectionTitle(text = "Por fecha")

            Row {
                Column {
                    SubTitle(text = stringResource(R.string.sinceTitle))
                    FilterButton(
                        label = stringResource(R.string.dayMonthYear),
                        selectedDate = startDate,
                        onDateSelected = { newDate ->
                            if (endDate.isNotEmpty() && dateFormat.parse(newDate)
                                    ?.after(dateFormat.parse(endDate)) == true
                            ) {
                                endDate = ""
                            }
                            startDate = newDate
                        },
                        dateConstraints = { dialog ->
                            if (endDate.isNotEmpty()) {
                                dialog.datePicker.maxDate = dateFormat.parse(endDate)?.time ?: 0L
                            }
                        }
                    )
                }
                Column {
                    SubTitle(text = stringResource(R.string.untilTitle))
                    FilterButton(label = stringResource(R.string.dayMonthYear),
                        selectedDate = endDate,
                        onDateSelected = { newDate ->
                            if (startDate.isNotEmpty() && dateFormat.parse(newDate)
                                    ?.before(dateFormat.parse(startDate)) == true
                            ) {
                                startDate = ""
                            }
                            endDate = newDate
                        },
                        dateConstraints = { dialog ->
                            if (startDate.isNotEmpty()) {
                                dialog.datePicker.minDate = dateFormat.parse(startDate)?.time ?: 0L
                            }
                        }
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            SectionTitle(text = "Por importe")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "", fontSize = 20.sp)
                Text(text = "", fontSize = 20.sp)
                Text(text = "", fontSize = 20.sp)
            }

            Slider(
                value = 0.5f,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            SectionTitle(text = "Por Estado")

            FilterCheckBox(text = "Pagadas")
            FilterCheckBox(text = "Anuladas")
            FilterCheckBox(text = "Pendientes de pago")
            FilterCheckBox(text = "Cuota fija")
            FilterCheckBox(text = "dd")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onApplyFiltersClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
                    .height(48.dp)
            ) {
                Text(text = "")
            }

            TextButton(
                onClick = onRestoreFiltersClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
            ) {
                Text(text = "", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
fun SubTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 16.dp, top = 35.dp)
    )
}

@Composable
fun FilterButton(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    dateConstraints: (DatePickerDialog) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
                showDialog = false
            },
            year,
            month,
            day
        )

        dateConstraints(datePickerDialog)
        datePickerDialog.show()
    }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(text = if (selectedDate.isEmpty()) label else selectedDate, fontSize = 20.sp)
    }
}

@Composable
fun FilterCheckBox(text: String) {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = false, onCheckedChange = { /* TODO: Handle checkbox change */ })
        Text(text = text, fontSize = 20.sp)
    }
}
