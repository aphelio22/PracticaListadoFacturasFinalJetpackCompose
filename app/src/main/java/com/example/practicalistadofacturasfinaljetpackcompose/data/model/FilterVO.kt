package com.example.practicalistadofacturasfinaljetpackcompose.data.model

data class FilterVO (
    var maxDate: String,
    var minDate: String,
    var maxValueSlider: Double,
    var status: HashMap<String, Boolean>
)