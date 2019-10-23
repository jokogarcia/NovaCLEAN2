package ar.com.novaclean

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import ar.com.novaclean.Models.Empleado
import ar.com.novaclean.Utils.DownloadImageTask
import ar.com.novaclean.Utils.getShortDate

class DetallesEmpleado : AppCompatActivity() {
  lateinit var CurrentEmpleado: Empleado

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detalles_empleado)
    this.setTitle("Detalles de Empleado")
    CurrentEmpleado = intent.getSerializableExtra("Empleado") as Empleado
    val nombreTV = findViewById<TextView>(R.id.nombretv)
    val puestoTV = findViewById<TextView>(R.id.puesto_areaTV)
    val fechaTV = findViewById<TextView>(R.id.tvFechaEmpleado)
    val ratingTV = findViewById<TextView>(R.id.tvRatingEmpleado)
    nombreTV.text = CurrentEmpleado.apellido + ", " + CurrentEmpleado.nombre
    puestoTV.text = CurrentEmpleado.puesto + " en sector " + CurrentEmpleado.area
    fechaTV.text = getShortDate(CurrentEmpleado.fecha_inicio)
    ratingTV.text = String.format("%.02f / 5",CurrentEmpleado.rating)
    DownloadImageTask(findViewById<View>(R.id.empleadoPhotoIV) as ImageView)
            .execute("https://novaclean.com.ar/_privado/imagenes/empleados/" + CurrentEmpleado.foto_url)

  }
}