package com.example.practicalistadofacturasfinaljetpackcompose.data.retrofit.network.response

import com.example.practicalistadofacturasfinal.data.retrofit.network.response.InvoiceResponse
import kotlinx.serialization.Serializable


@Serializable
data class InvoiceRepositoryListResponse(
    val numFacturas: Int,
    val facturas: List<InvoiceResponse>
)