package ar.com.novaclean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Empleado;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Tarea;
import ar.com.novaclean.Utils.DownloadImageTask;

public class DetallesEvento extends AppCompatActivity {

    private Evento EventoActual;
    private TextView LabelDia,LabelHora;
    private ArrayList<TareaWidg> TareasWidgList;
    private ArrayList<EmpleadoWidg> EmpleadosWidgList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalles_evento);
        LinearLayout TareasContainer = findViewById(R.id.tareasContainer);
        LinearLayout EmpleadosContainer = findViewById(R.id.empleadosContainer);
        TareasWidgList=new ArrayList<TareaWidg>();
        EmpleadosWidgList = new ArrayList<EmpleadoWidg>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LabelDia=findViewById(R.id.label_dia);
        LabelHora=findViewById(R.id.label_hora);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent myIntent = new Intent(getApplicationContext(), Observacion1.class);
                    myIntent.putExtra("Evento",EventoActual);
                    startActivity(myIntent);

            }
        });
        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");


        if(EventoActual.repetible == 1) {
            String titulo="";
            for (int i=0;i<EventoActual.dias.length();i++){
                char c = EventoActual.dias.charAt(i);
                switch (c){
                    case 'L':titulo+="Lunes, ";break;
                    case 'M':titulo+="Martes, ";break;
                    case 'X':titulo+="Miércoles, ";break;
                    case 'J':titulo+="Jueves, ";break;
                    case 'V':titulo+="Viernes, ";break;
                    case 'S':titulo+="Sábado, ";break;
                    case 'D':titulo+="Domingo, ";break;
                }
            }
            titulo=titulo.substring(0,titulo.lastIndexOf(","));
            titulo= titulo.substring(0,titulo.lastIndexOf(",")) + " y" + titulo.substring(titulo.lastIndexOf(",")+1);

            LabelDia.setText(titulo);
        }
        else
            LabelDia.setText(EventoActual.fecha.toString());
        String h= String.format("%02d:%02d",(int)EventoActual.hora/60,(int)EventoActual.hora%60);
        LabelHora.setText(h);
        LayoutInflater inflater = getLayoutInflater();
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
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        ArrayList<Tarea> _tareas;
                        try {
                            EventoActual.Tareas = new ArrayList<>(Arrays.asList(g.fromJson(response, Tarea[].class)));
                            for (Tarea t : EventoActual.Tareas) {

                                TareaWidg T = new TareaWidg(t,getLayoutInflater(), TareasContainer);
                                TareasWidgList.add(T);
                            }
                            TareasContainer.removeAllViews();
                            for (TareaWidg T : TareasWidgList) {
                                TareasContainer.addView(T.getView());
                            }
                        } catch (Exception e) {
                            Log.d("JOKO", "Error getting tarea. Response: " + response);
                        }

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
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Settings", MODE_PRIVATE);
                String tok = sharedPreferences.getString("token", "");
                String clientId = sharedPreferences.getString("clientId", "0");
                Map<String, String> params = new HashMap<String, String>();
                params.put("client_id", "" + clientId);
                params.put("tok", tok);
                params.put("field", "tareas");
                params.put("ids", EventoActual.tareas_ids);
                return params;
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void populateEmpleados() {
        if (EmpleadosWidgList == null)
            EmpleadosWidgList = new ArrayList<>();
        EmpleadosWidgList.clear();
        final ViewGroup Container = findViewById(R.id.empleadosContainer);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        View tmp = null;
                        try {
                           ArrayList<Empleado> _1 = new ArrayList<>(Arrays.asList(g.fromJson(response, Empleado[].class)));
                            EventoActual.EmpleadosDesignados = _1;

                            EmpleadosWidgList.clear();
                            for (Empleado e: EventoActual.EmpleadosDesignados){
                                EmpleadoWidg E = new EmpleadoWidg(e,getLayoutInflater(),Container);
                                EmpleadosWidgList.add(E);
                            }

                            Container.removeAllViews();

                            for(int i=0; i < EmpleadosWidgList.size();i++){
                                View Widget= EmpleadosWidgList.get(i).getView();
                                if (i%2 == 0){
                                    tmp=Widget;
                                }else{
                                    LinearLayout L=new LinearLayout(Container.getContext());
                                    L.setLayoutParams(
                                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                                    L.addView(Widget);
                                    L.addView(tmp);
                                    Container.addView(L);
                                    tmp=null;
                                }

                            }
                            if(tmp!=null){
                                LinearLayout L=new LinearLayout(Container.getContext());
                                L.setLayoutParams(
                                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                L.addView(tmp);
                                View view = getLayoutInflater().inflate(R.layout.placeholder_photoview,L,false);
                                L.addView(view);
                                Container.addView(L);

                            }

                        } catch (Exception e) {
                            Log.d("JOKO", "Error getting empleado. Response: " + response +" Exception: "+e.toString());
                        }

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
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Settings", MODE_PRIVATE);
                String tok = sharedPreferences.getString("token", "");
                String clientId = sharedPreferences.getString("clientId", "0");
                Map<String, String> params = new HashMap<String, String>();
                params.put("client_id", "" + clientId);
                params.put("tok", tok);
                params.put("field", "empleados");
                params.put("ids", EventoActual.empleados_ids);
                return params;
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
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
    private class EmpleadoWidg{
        private Empleado empleado;
        private View view;
        public EmpleadoWidg(Empleado E, LayoutInflater inflater, ViewGroup parent){
            this.empleado=E;
            view = inflater.inflate(R.layout.empleado_photoview,parent,false);
            ImageButton ib = view.findViewById(R.id.photoButton);

            ((TextView)view.findViewById(R.id.nombreTV)).setText(E.apellido+", "+E.nombre);
            new DownloadImageTask(ib)
                    .execute("https://novaclean.com.ar/_privado/imagenes/empleados/"+E.foto_url);

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(EmpleadoWidg E : EmpleadosWidgList){
                        View ev = E.view.findViewById(R.id.photoButton);
                        if(v.equals(ev)){
                            goToDetallesEmpleado(E.empleado);
                        }

                    }
                }
            });

        }
        public View getView(){return view;}

    }

    private void goToDetallesEmpleado(Empleado empleado) {
        Intent myIntent = new Intent(this, DetallesEmpleado.class);
        myIntent.putExtra("Empleado",empleado);
        startActivity(myIntent);
    }

}
