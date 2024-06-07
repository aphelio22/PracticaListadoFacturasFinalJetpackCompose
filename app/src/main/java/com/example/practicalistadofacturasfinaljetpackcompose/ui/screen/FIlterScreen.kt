package com.example.practicalistadofacturasfinaljetpackcompose.ui.screen

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
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.practicalistadofacturasfinaljetpackcompose.ui.theme.PracticaListadoFacturasFinalJetpackComposeTheme
import com.example.practicalistadofacturasfinaljetpackcompose.ui.viewmodel.InvoiceActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesFiltersScreen(
    onApplyFiltersClick: () -> Unit,
    onRestoreFiltersClick: () -> Unit,
    viewModel: InvoiceActivityViewModel,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtrar Facturas") },
                actions = {
                    IconButton(onClick = { /* TODO: Handle menu actions */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
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

            // Issue Date Section
            SectionTitle(text = "Por fecha")

            Row{
                Column {
                    SubTitle(text = "Desde")
                    FilterButton(text = "")
                }
                Column {
                    SubTitle(text = "Hasta")
                    FilterButton(text = "")
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            // Amount Section
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

            // State Section
            SectionTitle(text = "Por Estado")

            FilterCheckBox(text = "Pagadas")
            FilterCheckBox(text = "Anuladas")
            FilterCheckBox(text = "Pendientes de pago")
            FilterCheckBox(text = "Cuota fija")
            FilterCheckBox(text = "dd")

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons
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
fun FilterButton(text: String) {
    Button(
        onClick = { /* TODO: Handle button click */ },
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(text = text, fontSize = 20.sp)
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
