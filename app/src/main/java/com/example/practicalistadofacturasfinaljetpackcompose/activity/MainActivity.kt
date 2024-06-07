package com.example.practicalistadofacturasfinaljetpackcompose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicalistadofacturasfinaljetpackcompose.ui.screen.InvoicesFiltersScreen
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
                val navigationController = rememberNavController()

                NavHost(
                    navController = navigationController,
                    startDestination = "ListInvoicesScreen"
                ) {
                    composable("ListInvoicesScreen") {
                        InvoicesListScreen(
                            viewModel,
                            navigationController
                        )
                    }
                    composable("FilersScreen") {
                        InvoicesFiltersScreen(
                            onApplyFiltersClick = { /* TODO: Handle apply filters click */ },
                            onRestoreFiltersClick = { /* TODO: Handle restore filters click */ },
                            viewModel,
                            navigationController
                        )
                    }
                }
            }
//                    InvoicesFiltersScreen(
//                        modifier = Modifier.padding(innerPadding),
//                        onApplyFiltersClick = { /* TODO: Handle apply filters click */ },
//                        onRestoreFiltersClick = { /* TODO: Handle restore filters click */ }
//                    )
        }
    }
}

