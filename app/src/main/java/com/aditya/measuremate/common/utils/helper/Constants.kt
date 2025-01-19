package com.example.udemycourseshoppingapp.common.utils.helper

object Constants {

    const val ADD_EVENT_DATE_FORMAT = "EEE MMM dd yyyy"

}

enum class AppScreenName(name : String) {
    HomeScreen("Home"),
    DashboardScreen("Dashboard") ,
    AddItemScreen("Add Item") ,
    DetailsScreen("Details")
}

enum class MeasuringUnit(
    val code: String,
    val label: String
) {
    CM(code = "cm", label = "Centimeters"),
    IN(code = "in", label = "Inches"),
    FT(code = "ft", label = "Feet"),
    PERCENT(code = "%", label = "Percentage"),
    KG(code = "kg", label = "Kilograms"),
    PD(code = "pd", label = "Pounds"),
    M(code = "m", label = "Meters"),
    MM(code = "mm", label = "Millimeters")
}

enum class TimeRange(
    label : String
){
    LAST7DAYS("Last 7 days"),
    LAST30DAYS("Last 30 days") ,
    ALL_TIME ("All Time")
}