package ar.com.novaclean

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ar.com.novaclean.Models.Evento
import ar.com.novaclean.Models.ReclamoData


class reclamo_puntualidad : AppCompatActivity() {
    lateinit var Reclamo:ReclamoData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reclamo_puntualidad)
        Reclamo = intent.getSerializableExtra("ReclamoData") as ReclamoData


    }

    fun onClick(view: View) {
        when (view.id){
            R.id.button1->Reclamo.Detalles = "PUNTUAL"
            R.id.button2->Reclamo.Detalles = "IMPUNTUAL<10"
            R.id.button3->Reclamo.Detalles = "IMPUNTUAL>10"
            else -> return
        }
        var resultIntent = Intent()
        resultIntent.putExtra("ReclamoData",Reclamo);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }
}
