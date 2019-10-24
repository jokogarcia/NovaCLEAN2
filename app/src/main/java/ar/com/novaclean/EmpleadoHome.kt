package ar.com.novaclean

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import ar.com.novaclean.Models.VisitEvent
import ar.com.novaclean.Models.Usuario
import java.util.*
import kotlin.collections.ArrayList

class EmpleadoHome : AppCompatActivity() , evento_list_fragment.OnFragmentInteractionListener  {


    lateinit var usuario :Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleado_home)
        usuario = intent.getSerializableExtra("Usuario") as Usuario
        val EventosDelDia : ArrayList<VisitEvent> = getEventosByUsuario(usuario)
        populateItinerario(EventosDelDia)
    }

    private fun getEventosByUsuario(usuario: Usuario): ArrayList<VisitEvent> {
        val eventos = ArrayList<VisitEvent>()
        val dummy = VisitEvent()
        dummy.date = Date()

        //Todo: Fetch eventos from server
        eventos.add(dummy)
        return eventos

    }

    private fun populateItinerario(visitEvents: ArrayList<VisitEvent>) {
        val eventosContainer = findViewById(R.id.eventosContainer) as ViewGroup
        eventosContainer.removeAllViews()
        val transaction = supportFragmentManager.beginTransaction()
        for(visitEvent: VisitEvent in visitEvents){
            transaction.add(R.id.eventosContainer, evento_list_fragment.newInstance(visitEvent))
        }
        transaction.commit()
    }

    override fun onSelectedEvent(visitEvent: VisitEvent?) {
        val newIntent= Intent(applicationContext,DetallesEvento::class.java)
        newIntent.putExtra("VisitEvent",visitEvent)
        newIntent.putExtra("Usuario", usuario)

        startActivity(newIntent)
    }
}
