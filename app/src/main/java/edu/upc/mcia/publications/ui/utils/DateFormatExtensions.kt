package edu.upc.mcia.publications.ui.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toMonthAndYear() = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this)