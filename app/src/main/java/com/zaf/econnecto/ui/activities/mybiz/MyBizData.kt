package com.zaf.econnecto.ui.activities.mybiz

data class Amenities(val status: Int, val message:List<String>,val data: List<AmenityData>)

data class AmenityData(val amenity_id: String, val amenity_name: String)


data class OPHours(val status: Int, val message:List<String>, val data: OPHoursData)

data class OPHoursData(val CurrentStatus: String, val Mon: List<String>, val Tue: List<String>, val Wed: List<String>, val Thu: List<String>, val Fri: List<String>, val Sat: List<String>, val Sun: List<String>)