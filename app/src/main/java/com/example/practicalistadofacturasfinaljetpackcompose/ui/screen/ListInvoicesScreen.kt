package com.example.practicalistadofacturasfinaljetpackcompose.ui.screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.practicalistadofacturasfinaljetpackcompose.R
import com.example.practicalistadofacturasfinaljetpackcompose.ui.viewmodel.InvoiceActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicesListScreen(modifier: Modifier, viewModel: InvoiceActivityViewModel) {
    val invoices by viewModel.filteredInvoicesLiveData.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atrás") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation icon press */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Add any additional actions here, e.g., menu icons
                }
            )
        }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (tvInvoiceTitle, rvInvoices, tvEmptyList, switchRetromock, toggleRetroKtor) = createRefs()

            Text(
                text = stringResource(id = R.string.invoiceTitle),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .constrainAs(tvInvoiceTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            // Placeholder for RecyclerView
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(rvInvoices) {
                        top.linkTo(tvInvoiceTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(switchRetromock.top)
                        height = Dimension.fillToConstraints
                    }
            ) {
                items(invoices) { item ->
                    // Render each item
                    InvoiceItem(
                        selectionText = item.fecha,
                        amount = item.importeOrdenacion.toString(),
                        stateText = item.descEstado
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.emptyListText),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .constrainAs(tvEmptyList) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(8.dp)
                    .visible(false) // Adjust visibility as needed
            )

            Switch(
                checked = false, // Bind this to a state variable
                onCheckedChange = { /* Handle switch change */ },
                modifier = Modifier
                    .constrainAs(switchRetromock) {
                        top.linkTo(rvInvoices.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(8.dp)
                    .visible(false) // Adjust visibility as needed
            )

            Column(
                modifier = Modifier
                    .constrainAs(toggleRetroKtor) {
                        top.linkTo(rvInvoices.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                TextButton(onClick = { /* Handle Retromock click */ }) {
                    Text("Retromock", fontSize = 10.sp)
                }
                TextButton(onClick = { /* Handle Retrofit click */ }) {
                    Text("Retrofit", fontSize = 10.sp)
                }
                TextButton(onClick = { /* Handle Ktor click */ }) {
                    Text("Ktor", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun InvoiceItem(
    selectionText: String,
    amount: String,
    stateText: String,
    euroSign: String = "€"
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (tvSelection, tvAmount, tvEuro, tvState, ivArrow) = createRefs()

        Text(
            text = selectionText,
            fontSize = 28.sp,
            modifier = Modifier.constrainAs(tvSelection) {
                top.linkTo(parent.top)
                bottom.linkTo(tvState.top)
                start.linkTo(parent.start)
                end.linkTo(tvAmount.start)
            }
        )

        Text(
            text = amount,
            fontSize = 34.sp,
            modifier = Modifier.constrainAs(tvAmount) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(tvSelection.end)
                end.linkTo(tvEuro.start)
            }
        )

        Text(
            text = euroSign,
            fontSize = 34.sp,
            modifier = Modifier.constrainAs(tvEuro) {
                baseline.linkTo(tvAmount.baseline)
                end.linkTo(ivArrow.start, margin = 8.dp)
            }
        )

        Text(
            text = stateText,
            fontSize = 24.sp,
            modifier = Modifier.constrainAs(tvState) {
                bottom.linkTo(parent.bottom, margin = 4.dp)
                start.linkTo(tvSelection.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun Modifier.visible(visible: Boolean): Modifier = this.then(
    if (visible) Modifier else Modifier.size(0.dp)
)