package ar.com.novaclean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;



import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Usuario;

public class MainActivity extends AppCompatActivity {

    private int ClienteID;
    private Button LogInButton;
    private Switch IniciarAutomatico;
    private Context ThisActivity;
    private SharedPreferences sharedPreferences;
    private ArrayList<Evento> Itinerario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisActivity=this;
        setContentView(R.layout.activity_main);
        LogInButton = findViewById(R.id.loginButton);
        IniciarAutomatico = findViewById(R.id.iniciarAutomatico);
        ClienteID=0;
        sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
        ((EditText)findViewById(R.id.userEmail)).setText(sharedPreferences.getString("userEmail",""));
        ((EditText)findViewById(R.id.userPass)).setText(sharedPreferences.getString("userPass",""));
        IniciarAutomatico.setChecked(sharedPreferences.getBoolean("autologin",false));
        if(IniciarAutomatico.isChecked())
            doLogin();
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();

            }
        });
    }
    void doLogin(){
        final String email=((EditText)findViewById(R.id.userEmail)).getText().toString();
        final String pass=((EditText)findViewById(R.id.userPass)).getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        Usuario usuario = g.fromJson(response, Usuario.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if(IniciarAutomatico.isChecked()){

                            editor.putBoolean("autologin",true);
                            editor.putString("userEmail",email);
                            editor.putString("userPass",pass);

                        }
                        else{
                            editor.putBoolean("autologin",false);
                            editor.putString("userEmail","");
                            editor.putString("userPass","");
                        }
                        editor.commit();
                        if(usuario.error.length() == 0){
                            if(usuario.isEmpleado) {
                                Intent myIntent;
                                myIntent = new Intent(ThisActivity, EmpleadoHome.class);
                                myIntent.putExtra("Usuario", usuario);
                                startActivity(myIntent);
                            }
                            else {
                                //myIntent = new Intent(ThisActivity, Calendario.class);
                                getEventos(usuario);
                            }

                        }
                        else{
                            Toast.makeText(MainActivity.this,usuario.error,Toast.LENGTH_LONG).show();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("pass",pass);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    @Override
    protected void onResume() {
        IniciarAutomatico.setChecked(sharedPreferences.getBoolean("iniciarAutomatico",false));
        super.onResume();
    }
    private void getEventos(final Usuario usuario) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_EVENTOS_FROM_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        try{
                            Evento[] eventos = g.fromJson(response, Evento[].class);
                            ArrayList<Evento> EventosTodos = new ArrayList<>(Arrays.asList(eventos));
                            ArrayList<Evento> EventosDelDia = new ArrayList<>();
                            Date today = new Date();
                            for (Evento evento : EventosTodos){
                                if(evento.isOnDate(today)){
                                    evento.fecha=today;
                                    EventosDelDia.add(evento);
                                }
                            }
                            Intent myIntent = new Intent(MainActivity.this, ListaDeEventos.class);
                            myIntent.putExtra("Eventos",EventosDelDia);
                            myIntent.putExtra("Usuario",usuario);
                            myIntent.putExtra("Date",today);

                            startActivity(myIntent);

                        }catch(IllegalStateException e){
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                return usuario.getLoginParams();
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
