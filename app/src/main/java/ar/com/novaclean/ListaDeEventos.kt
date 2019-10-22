package ar.com.novaclean

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import java.lang.reflect.Array

import ar.com.novaclean.Models.Evento
import ar.com.novaclean.Models.Tarea
import ar.com.novaclean.Models.Usuario
import ar.com.novaclean.Utils.getLongDate
import ar.com.novaclean.Utils.getShortDate
import kotlinx.android.synthetic.main.activity_lista_de_eventos.*
import java.util.*

class ListaDeEventos : AppCompatActivity(),evento_list_fragment.OnFragmentInteractionListener{


    lateinit var Eventos: ArrayList<Evento>
    lateinit var eventosContainer: LinearLayout
    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_de_eventos)
        Eventos = intent.getSerializableExtra("Eventos") as ArrayList<Evento>
        usuario = intent.getSerializableExtra("Usuario") as Usuario
        val date = intent.getSerializableExtra("Date") as Date
        title = "Novaclean"
        tvTitle.text = "Eventos del día "+getLongDate(date)
        eventosContainer = findViewById(R.id.eventosContainer)
        if(Eventos.size>0){
            eventosContainer.removeAllViews()
            val transaction = supportFragmentManager.beginTransaction()
            for(evento:Evento in Eventos){
                transaction.add(R.id.eventosContainer, evento_list_fragment.newInstance(evento))
            }
            transaction.commit()
        }


    }

    override fun onSelectedEvent(evento: Evento?) {
        val newIntent= Intent(applicationContext,DetallesEvento::class.java)
        newIntent.putExtra("Evento",evento)
        newIntent.putExtra("Usuario", usuario)

        startActivity(newIntent)
    }

    fun onButtonClick(view: View) {
        if(view.id == R.id.ibCalendar){
            val calendarioIntent = Intent(this,Calendario::class.java)
            calendarioIntent.putExtra("Usuario",usuario);
            startActivity(calendarioIntent)
        }
        else if(view.id == R.id.ibNew){
            Toast.makeText(this,"No implementado",Toast.LENGTH_LONG).show();
        }

    }
}
