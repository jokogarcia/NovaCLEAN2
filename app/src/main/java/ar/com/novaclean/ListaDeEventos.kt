package ar.com.novaclean

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import ar.com.novaclean.Models.Location

import ar.com.novaclean.Models.VisitEvent
import ar.com.novaclean.Models.User
import ar.com.novaclean.Models._user
import ar.com.novaclean.Utils.RequestResult
import ar.com.novaclean.Utils.RequestResultListener
import ar.com.novaclean.Utils.getLongDate
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_lista_de_eventos.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ListaDeEventos : AppCompatActivity(),evento_list_fragment.OnFragmentInteractionListener{


    lateinit var visitEvents: ArrayList<VisitEvent>
    lateinit var eventosContainer: LinearLayout
    private val user:User = User()
    private var date:Date=Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_de_eventos)
        val apiToken = intent.getStringExtra("apiToken")
        user.Home(apiToken, object : RequestResultListener {
            override fun OnRequestResult(requestResult: RequestResult) {
                if(requestResult.success)
                    populateGUI()
                else{
                    Toast.makeText(applicationContext,"Error obteniendo datos",Toast.LENGTH_LONG).show()
                }
            }
        })
        if(intent.hasExtra("Date"))
            date = intent.getSerializableExtra("Date") as Date

        title = "Novaclean"
        tvTitle.text = "Eventos del día "+getLongDate(date)
        eventosContainer = findViewById(R.id.eventosContainer)



    }

    private fun populateGUI() {
        if(user.hasVisitEvents(date))
        {
            eventosContainer.removeAllViews()
            val transaction = supportFragmentManager.beginTransaction()

            for(location: Location in user.Locations){
                for(visitEvent:VisitEvent in location.visitEvents){
                    transaction.add(R.id.eventosContainer, evento_list_fragment.newInstance(visitEvent))
                }
            }
            transaction.commit()
        }
    }

    override fun onSelectedEvent(visitEvent: VisitEvent?) {
        val newIntent= Intent(applicationContext,DetallesEvento::class.java)
        val gson = Gson();
        val visitEventJson = gson.toJson(visitEvent)
        newIntent.putExtra("VisitEventJson",visitEventJson);
        //newIntent.putExtra("VisitEven",visitEvent)

        newIntent.putExtra("apiToken", user.api_token)

        startActivity(newIntent)
    }

    fun onButtonClick(view: View) {
        if(view.id == R.id.ibCalendar){
            val calendarioIntent = Intent(this,Calendario::class.java)
            val gson= Gson()
            var uu: _user = user
            try{
                val userJson=gson.toJson(uu)
                calendarioIntent.putExtra("user",userJson)
                startActivity(calendarioIntent)
            }catch (ex:Exception){
                Log.d("WTF", ex.message)
            }

        }
        else if(view.id == R.id.ibNew){
            Toast.makeText(this,"No implementado",Toast.LENGTH_LONG).show();
        }

    }
}
