package com.example.practicalistadofacturasfinaljetpackcompose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.practicalistadofacturasfinaljetpackcompose.ui.screen.InvoicesListScreen
import com.example.practicalistadofacturasfinaljetpackcompose.ui.theme.PracticaListadoFacturasFinalJetpackComposeTheme
import com.example.practicalistadofacturasfinaljetpackcompose.ui.viewmodel.InvoiceActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: InvoiceActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaListadoFacturasFinalJetpackComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InvoicesListScreen(modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel)
//                    InvoicesFiltersScreen(
//                        modifier = Modifier.padding(innerPadding),
//                        onApplyFiltersClick = { /* TODO: Handle apply filters click */ },
//                        onRestoreFiltersClick = { /* TODO: Handle restore filters click */ }
//                    )
                }
            }
        }
    }
}

