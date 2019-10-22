package ar.com.novaclean.Utils

import java.text.SimpleDateFormat
import java.util.*

fun getShortDate(date:Date): String {
    val pattern = "dd/MM/yyyy"

    val df = SimpleDateFormat(pattern)

    return df.format(date)
}
fun getLongDate(date:Date): String {
    val pattern = "dd/MM/yyyy"
    val formateador = SimpleDateFormat("EEEE dd 'de' MMMM", Locale("es"))

    //val df = SimpleDateFormat(pattern)

    return formateador.format(date)
}