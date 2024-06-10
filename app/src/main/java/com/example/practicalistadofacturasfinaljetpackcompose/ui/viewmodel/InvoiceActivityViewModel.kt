package com.example.practicalistadofacturasfinaljetpackcompose.ui.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalistadofacturasfinaljetpackcompose.R
import com.example.practicalistadofacturasfinaljetpackcompose.constants.Constants
import com.example.practicalistadofacturasfinaljetpackcompose.data.AppRepository
import com.example.practicalistadofacturasfinaljetpackcompose.data.room.InvoiceModelRoom
import com.example.practicalistadofacturasfinaljetpackcompose.data.model.FilterVO
import com.example.practicalistadofacturasfinaljetpackcompose.enums.ApiType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InvoiceActivityViewModel @Inject constructor(private val appRepository: AppRepository, private val appContext: Context) : ViewModel() {

    private var invoices: List<InvoiceModelRoom> = emptyList()

    private val _filteredInvoicesLiveData = MutableLiveData<List<InvoiceModelRoom>>()
    val filteredInvoicesLiveData: LiveData<List<InvoiceModelRoom>>
        get() = _filteredInvoicesLiveData

    private var _maxAmount = MutableLiveData<Float>()
    val maxAmount
        get() = _maxAmount

    private var _filterLiveData = MutableLiveData<FilterVO>()
    val filterLiveData: LiveData<FilterVO>
        get() = _filterLiveData

    private val _showRemoteConfig = MutableLiveData<Boolean>()
    val showRemoteConfig: LiveData<Boolean>
        get() = _showRemoteConfig

    private var selectedApiType = ApiType.RETROMOCK

    private val _filterCheckBoxSate = MutableLiveData<Map<String, Boolean>>()
    val filterCheckBoxState: LiveData<Map<String, Boolean>>
        get() = _filterCheckBoxSate

    init {
        fetchInvoices()
        fetchRemoteConfig()
        _filterCheckBoxSate.value = mapOf(
            "Pagada" to false,
            "Pendiente de pago" to false,
            "Anuladas" to false,
            "Cuota fija" to false,
            "planPago" to false
        )

    }

    private fun fetchRemoteConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(3000)
                appRepository.fetchAndActivateConfig()
                val showSwitch = appRepository.getBooleanValue("showSwitch")
                _showRemoteConfig.postValue(showSwitch)
            }
        }
    }

    fun fetchInvoices() {
        viewModelScope.launch(Dispatchers.IO) {
            _filteredInvoicesLiveData.postValue(appRepository.getAllInvoicesFromRoom())
            try {
                if (isInternetAvailable()) {
                    when (selectedApiType) {
                        ApiType.RETROFIT -> {
                            appRepository.deleteAllInvoicesFromRoom()
                            appRepository.fetchAndInsertInvoicesFromAPI()
                            }
                        ApiType.RETROMOCK -> {
                            appRepository.deleteAllInvoicesFromRoom()
                            appRepository.fetchAndInsertInvoicesFromMock()
                        }
                        ApiType.KTOR -> {
                            appRepository.deleteAllInvoicesFromRoom()
                            appRepository.fetchAndInsertInvoicesFromKtor()
                        }
                    }
                    invoices = appRepository.getAllInvoicesFromRoom()
                    _filteredInvoicesLiveData.postValue(invoices)
                    findMaxAmount()
                    //verifyFilters()
                }
            } catch (e: Exception) {
                //Log.d("Error", e.printStackTrace().toString())
            }
        }
    }

    fun setSelectedApiType(apiType: ApiType) {
        selectedApiType = apiType
        fetchInvoices()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }

    private fun findMaxAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            var max = 0.0

            for (invoice in invoices) {
                val actualInvoiceAmount = invoice.importeOrdenacion
                if (max < actualInvoiceAmount) {
                    max = actualInvoiceAmount
                }
            }
            _maxAmount.postValue(max.toFloat())
        }
    }

    fun applyFilters(
        maxDate: String,
        minDate: String,
        maxValueSlider: Double,
        status: HashMap<String, Boolean>,
        dateStringResource: String
    ) {
        if ((minDate == dateStringResource && maxDate == dateStringResource) ||
            (minDate != dateStringResource && maxDate != dateStringResource)
        ) {
            val filter = FilterVO(maxDate, minDate, maxValueSlider, status)
            _filterLiveData.postValue(filter)
        }
    }

    fun verifyFilters() {
        var filteredList = verifyDateFilter()
        filteredList = verifyCheckBox(filteredList)
        filteredList = verifyBalanceBar(filteredList)
        _filteredInvoicesLiveData.postValue(filteredList)
    }

    private fun verifyDateFilter(): List<InvoiceModelRoom> {
        val maxDate = filterLiveData.value?.maxDate
        val minDate = filterLiveData.value?.minDate
        val filteredList = ArrayList<InvoiceModelRoom>()
        if (minDate != getString(
                appContext,
                R.string.dayMonthYear
            ) && maxDate != getString(appContext, R.string.dayMonthYear)
        ) {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            //Variables locales del método en las que se almacenan la fecha mínima y la fecha máxima parseadas.
            var minDateLocal: Date? = null
            var maxDateLocal: Date? = null

            try {
                //Parseo de las fechas.
                minDateLocal = minDate?.let { simpleDateFormat.parse(it) }
                maxDateLocal = maxDate?.let { simpleDateFormat.parse(it) }
            } catch (e: ParseException) {
                Log.d("Error1: ", "comprobarFiltroFechas: ParseException")
            }
            for (invoice in invoices) {
                var invoiceDate = Date()
                try {
                    invoiceDate = simpleDateFormat.parse(invoice.fecha)!!
                } catch (e: ParseException) {
                    Log.d("Error2: ", "comprobarFiltroFechas: ParseException")
                }
                //Se verifica si la fecha de la factura está dentro del intervalo especificado.
                if (invoiceDate.after(minDateLocal) && invoiceDate.before(maxDateLocal)) {
                    filteredList.add(invoice)
                }
            }
        }
        return filteredList
    }

    private fun verifyCheckBox(
        filteredInvoices: List<InvoiceModelRoom>?
    ): List<InvoiceModelRoom> {
        val filteredInvoicesCheckBox = ArrayList<InvoiceModelRoom>()
        val status = filterLiveData.value?.status
        val checkBoxPaid = status?.get(Constants.PAID_STRING) ?: false
        val checkBoxCanceled = status?.get(Constants.CANCELED_STRING) ?: false
        val checkBoxFixedPayment = status?.get(Constants.FIXED_PAYMENT_STRING) ?: false
        val checkBoxPendingPayment = status?.get(Constants.PENDING_PAYMENT_STRING) ?: false
        val checkBoxPaymentPlan = status?.get(Constants.PAYMENT_PLAN_STRING) ?: false

        if (checkBoxPaid || checkBoxCanceled || checkBoxFixedPayment || checkBoxPendingPayment || checkBoxPaymentPlan) {
            //Verificación de los estados de las facturas y los CheckBoxes seleccionados.
            for (invoice in filteredInvoices ?: emptyList()) {
                val invoiceState = invoice.descEstado
                val isPaid = invoiceState == "Pagada"
                val isCanceled = invoiceState == "Anuladas"
                val isFixedPayment = invoiceState == "Cuota fija"
                val isPendingPayment = invoiceState == "Pendiente de pago"
                val isPaymentPlan = invoiceState == "planPago"

                //Se añade la factura a la lista filtrada si cumple con alguno de los estados seleccionados.
                if ((isPaid && checkBoxPaid) || (isCanceled && checkBoxCanceled) || (isFixedPayment && checkBoxFixedPayment) || (isPendingPayment && checkBoxPendingPayment) || (isPaymentPlan && checkBoxPaymentPlan)) {
                    filteredInvoicesCheckBox.add(invoice)
                }
            }
            return filteredInvoicesCheckBox
        } else {
            return filteredInvoices ?: emptyList()
        }
    }

    private fun verifyBalanceBar(filteredList: List<InvoiceModelRoom>): List<InvoiceModelRoom> {
    val filteredInvoicesBalanceBar = ArrayList<InvoiceModelRoom>()
        val maxValueSlider = filterLiveData.value?.maxValueSlider
        for (factura in filteredList) {
            //Se añade la factura a la lista filtrada si su importe es menor que el valor seleccionado.
            if (factura.importeOrdenacion < maxValueSlider!!) {
                filteredInvoicesBalanceBar.add(factura)
            }
        }
        return filteredInvoicesBalanceBar
    }

     fun formatDate(date: String): String {
        return try {
            val insert = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val format = insert.parse(date)
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))

            format?.let { outputFormat.format(it) } ?: date
        } catch (e: ParseException) {
            e.printStackTrace()
            date
        }
    }

    fun onFilterChange(filter: String, isChecked: Boolean) {
        val currentFilters = _filterCheckBoxSate.value?.toMutableMap() ?: mutableMapOf()
        currentFilters[filter] = isChecked
        _filterCheckBoxSate.value = currentFilters
    }
}