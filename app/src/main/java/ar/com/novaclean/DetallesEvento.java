package ar.com.novaclean;

import android.content.Intent;

import android.graphics.Color;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Empleado;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Tarea;
import ar.com.novaclean.Models.Usuario;

import static ar.com.novaclean.Utils.UtilsKt.getShortDate;

public class DetallesEvento extends AppCompatActivity
        implements fichaEmpleadoChica.OnEmpleadoFragmentInteractionListener {

    private Evento EventoActual;
    private TextView LabelDia,LabelHora;
    private ArrayList<TareaWidg> TareasWidgList;
    private Usuario usuario;
    //TODO: Quitar proteccion de directorio de imagenes, o poner imagenes de empleados en otro directrio.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Visita programada");
        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        setContentView(R.layout.activity_detalles_evento);
        LinearLayout TareasContainer = findViewById(R.id.tareasContainer);
        LinearLayout EmpleadosContainer = findViewById(R.id.empleadosContainer);
        TareasWidgList = new ArrayList<TareaWidg>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LabelDia = findViewById(R.id.label_dia);
        LabelHora = findViewById(R.id.label_hora);
        ImageButton ActionButton = findViewById(R.id.actionButton);
        ActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getApplicationContext(), Observacion1.class);
                myIntent.putExtra("Evento", EventoActual);
                myIntent.putExtra("Usuario", usuario);

                startActivity(myIntent);

            }
        });



        LabelDia.setText(EventoActual.getDias());
        LabelHora.setText(EventoActual.getHora());
        TextView TV = findViewById(R.id.tvFecha);
        if (EventoActual.repetible == 1) {
            TV.setText(getShortDate(EventoActual.fecha));
            TV.setVisibility(View.VISIBLE);
        } else
            TV.setVisibility(View.INVISIBLE);
        populateTareas();
        populateEmpleados();


    }

    private void populateTareas() {


        if (TareasWidgList == null)
            TareasWidgList = new ArrayList<>();
        TareasWidgList.clear();
        final ViewGroup TareasContainer = findViewById(R.id.tareasContainer);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       parseTareasJson(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JOKO", "Error getting tarea. " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = usuario.getLoginParams();
                params.put("field", "tareas");
                params.put("ids", EventoActual.tareas_ids);
                return params;
            }

        };
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void populateTareasWidgets(){
        final ViewGroup TareasContainer = findViewById(R.id.tareasContainer);
        String eventoAnterior="";
        for (Tarea t : EventoActual.Tareas) {

            TareaWidg T = new TareaWidg(t,getLayoutInflater(), TareasContainer);
            TareasWidgList.add(T);
        }
        TareasContainer.removeAllViews();
        for (TareaWidg T : TareasWidgList) {
            String eventoActual=T.Tarea.sector;
            if(!eventoAnterior.equals(eventoActual)){
                TextView TV = new TextView(getApplicationContext());
                TV.setText("Sector "+T.Tarea.sector);
                TV.setTextColor(Color.WHITE);
                TareasContainer.addView(TV);
                eventoAnterior=T.Tarea.sector;
            }
            TareasContainer.addView(T.getView());
        }
    }

    @Override
    public void onFragmentInteraction(@NotNull Empleado empleado) {
        Intent myIntent = new Intent(this, DetallesEmpleado.class);
        myIntent.putExtra("Empleado",empleado);
        myIntent.putExtra("Usuario",usuario);
        startActivity(myIntent);
    }

    private class shortSector{
        public int id;
        public String nombre;
    }
    private void parseTareasJson(String response) {

        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
        Gson g = new Gson();
        ArrayList<Tarea> _tareas;
        try {
            EventoActual.Tareas = new ArrayList<>(Arrays.asList(g.fromJson(response, Tarea[].class)));
            //Asignar el sector correspondiente a cada tarea.

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_SECTORES_BY_TAREA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gg = new Gson();
                            shortSector[] s = gg.fromJson(response,shortSector[].class);
                            int tmp=EventoActual.Tareas.size();
                            for (int i=0; i < EventoActual.Tareas.size() && i < s.length;i++){
                                EventoActual.Tareas.get(i).sector = s[i].nombre;
                            }
                            populateTareasWidgets();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("JOKO", "Error getting tarea. " + error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = usuario.getLoginParams();
                    params.put("tareas_ids", EventoActual.tareas_ids);
                    return params;
                }

            };
            RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);


        } catch (Exception e) {
            Log.d("JOKO", "Error getting tarea. Response: " + response);
        }
    }

    private void populateEmpleados() {

        final ViewGroup Container = findViewById(R.id.empleadosContainer);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        Empleado[] empleados;
                        View tmp = null;
                        try {

                            //Convert response into an array of EMPLEADOs
                            empleados = g.fromJson(response, Empleado[].class);

                            EventoActual.EmpleadosDesignados =
                                    new ArrayList<>(Arrays.asList(empleados));



                        } catch (Exception e) {
                            Log.d("JOKO", "Error getting empleado. Response: " + response +" Exception: "+e.toString());
                        }
                        Container.removeAllViews();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        for (Empleado empleado : EventoActual.EmpleadosDesignados){
                            fragmentTransaction.add(R.id.empleadosContainer,
                                    fichaEmpleadoChica.newInstance(empleado) );
                        }
                        fragmentTransaction.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JOKO", "Error getting empleado. " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params =usuario.getLoginParams();
                params.put("field", "empleados");
                params.put("ids", EventoActual.empleados_ids);
                return params;
            }

        };
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }





    private class TareaWidg {
        private Tarea Tarea;
        private View view;
        private String sector;
        public TareaWidg(Tarea T, LayoutInflater inflater, ViewGroup parent){
            this.Tarea=T;
            view = inflater.inflate(R.layout.tarea_fragment,parent,false);
            ((TextView)view.findViewById(R.id.TareaDescripcion)).setText(T.descripcion);
            ((TextView)view.findViewById(R.id.TareaMinutos)).setText(T.minutos+" min");
        }
        public View getView(){
            return view;
        }

    }



}
