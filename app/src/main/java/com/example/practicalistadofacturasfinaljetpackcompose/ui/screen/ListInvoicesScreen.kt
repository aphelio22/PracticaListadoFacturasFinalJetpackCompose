package com.example.practicalistadofacturasfinaljetpackcompose.ui.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.practicalistadofacturasfinaljetpackcompose.R
import com.example.practicalistadofacturasfinaljetpackcompose.enums.ApiType
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
                        bottom.linkTo(toggleRetroKtor.top)
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

            Row(
                modifier = Modifier
                    .constrainAs(toggleRetroKtor) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                TextButton(onClick = { viewModel.setSelectedApiType(ApiType.RETROMOCK) }) {
                    Text("Retromock", fontSize = 18.sp)
                }
                TextButton(onClick = { viewModel.setSelectedApiType(ApiType.RETROFIT) }) {
                    Text("Retrofit", fontSize = 18.sp)
                }
                TextButton(onClick = { viewModel.setSelectedApiType(ApiType.KTOR) }) {
                    Text("Ktor", fontSize = 18.sp)
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
   Box(modifier = Modifier
       .fillMaxWidth()
       .height(100.dp)
   ) {
       Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
           Column (
               modifier = Modifier.padding(8.dp)
           ) {
               Text(text = selectionText, fontSize = 24.sp)
               Spacer(modifier = Modifier.height(10.dp))
               Text(text = stateText, fontSize = 24.sp)
           }

           Column (modifier = Modifier
               .fillMaxWidth()
               .padding(end = 8.dp), horizontalAlignment = Alignment.End){
               Row(verticalAlignment = Alignment.CenterVertically) {
                   Text(text = amount, fontSize = 24.sp, modifier = Modifier.padding(end = 4.dp))
                   Text(text = euroSign, fontSize = 24.sp)
                   Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Information about invoice")
               }

           }
       }
       Divider(
           modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 5.dp)
       )
   }
}

@Composable
fun Modifier.visible(visible: Boolean): Modifier = this.then(
    if (visible) Modifier else Modifier.size(0.dp)
)