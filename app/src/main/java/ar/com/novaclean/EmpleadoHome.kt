package ar.com.novaclean

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import ar.com.novaclean.Models.Evento
import ar.com.novaclean.Models.Usuario
import java.util.*
import kotlin.collections.ArrayList

class EmpleadoHome : AppCompatActivity() , evento_list_fragment.OnFragmentInteractionListener  {


    lateinit var usuario :Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleado_home)
        usuario = intent.getSerializableExtra("Usuario") as Usuario
        val EventosDelDia : ArrayList<Evento> = getEventosByUsuario(usuario)
        populateItinerario(EventosDelDia)
    }

    private fun getEventosByUsuario(usuario: Usuario): ArrayList<Evento> {
        val eventos = ArrayList<Evento>()
        val dummy = Evento()
        dummy.fecha = Date()
        dummy.lugar = "La casa de Miguel"

        //Todo: Fetch eventos from server
        eventos.add(dummy)
        return eventos

    }

    private fun populateItinerario(eventos: ArrayList<Evento>) {
        val eventosContainer = findViewById(R.id.eventosContainer) as ViewGroup
        eventosContainer.removeAllViews()
        val transaction = supportFragmentManager.beginTransaction()
        for(evento:Evento in eventos){
            transaction.add(R.id.eventosContainer, evento_list_fragment.newInstance(evento))
        }
        transaction.commit()
    }

    override fun onSelectedEvent(evento: Evento?) {
        val newIntent= Intent(applicationContext,DetallesEvento::class.java)
        newIntent.putExtra("Evento",evento)
        newIntent.putExtra("Usuario", usuario)

        startActivity(newIntent)
    }
}
