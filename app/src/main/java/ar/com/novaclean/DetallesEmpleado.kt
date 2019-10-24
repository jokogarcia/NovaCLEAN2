package ar.com.novaclean

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import ar.com.novaclean.Models.Employee
import ar.com.novaclean.Models.User
import ar.com.novaclean.Utils.DownloadImageTask
import ar.com.novaclean.Utils.RequestResult
import ar.com.novaclean.Utils.RequestResultListener
import ar.com.novaclean.Utils.getShortDate
import java.text.SimpleDateFormat

class DetallesEmpleado : AppCompatActivity() {
  val currentEmployee: User = User()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detalles_empleado)
    this.setTitle("comment de Employee")
    val employeeId = intent.getIntExtra("employee_id",-1)
    val apiToken = intent.getStringExtra("apiToken")

    currentEmployee.fetchAsEmployee(employeeId,apiToken,object:RequestResultListener{
      override fun OnRequestResult(requestResult: RequestResult) {
        populateGUI()
      }

    }
    )

  }

  private fun populateGUI() {
    val nombreTV = findViewById<TextView>(R.id.nombretv)
    val puestoTV = findViewById<TextView>(R.id.puesto_areaTV)
    val fechaTV = findViewById<TextView>(R.id.tvFechaEmpleado)
    val ratingTV = findViewById<TextView>(R.id.tvRatingEmpleado)
    nombreTV.text = currentEmployee.name + ", " + currentEmployee.last_name
    //puestoTV.text = currentEmployee. + " en sector " + currentEmployee.area
    fechaTV.text = SimpleDateFormat("dd/MM/yyyy").format(currentEmployee.employee_start_date)
    //ratingTV.text = String.format("%.02f / 5",currentEmployee.rating)
    DownloadImageTask(findViewById<View>(R.id.empleadoPhotoIV) as ImageView)
            .execute( currentEmployee.photo_url)

  }
}