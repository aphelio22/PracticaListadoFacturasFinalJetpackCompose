package com.example.practicalistadofacturasfinaljetpackcompose.data.room

import androidx.room.Entity

@Entity(tableName = "energyData_table", primaryKeys = ["cau"])
class EnergyDataModelRoom (
    val cau: String,
    val requestStatus: String,
    val selfConsumptionType: String,
    val surplusCompensation: String,
    val installationPower: String
)