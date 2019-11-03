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


import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


import ar.com.novaclean.Models.CleaningTask;

import ar.com.novaclean.Models.Sector;
import ar.com.novaclean.Models.VisitEvent;
import ar.com.novaclean.Models._user;

import static ar.com.novaclean.Utils.UtilsKt.getShortDate;

public class DetallesEvento extends AppCompatActivity
        implements fichaEmpleadoChica.OnEmpleadoFragmentInteractionListener {

    private VisitEvent visitEventCurrent;
    private TextView LabelDia,LabelHora;
    private ArrayList<TareaWidg> TareasWidgList;
    private String apiToken;
    //TODO: Quitar proteccion de directorio de imagenes, o poner imagenes de empleados en otro directrio.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Visita programada");
        Gson gson = new Gson();
        String visitEventJson = getIntent().getStringExtra("VisitEventJson");
        visitEventCurrent = gson.fromJson(visitEventJson,VisitEvent.class);
        //visitEventCurrent = (VisitEvent) getIntent().getSerializableExtra("visitEvent");
        apiToken = getIntent().getStringExtra("apiToken");
        setContentView(R.layout.activity_detalles_evento);

        TareasWidgList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LabelDia = findViewById(R.id.label_dia);
        LabelHora = findViewById(R.id.label_hora);
        ImageButton ActionButton = findViewById(R.id.actionBt);
        ActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getApplicationContext(), Observacion1.class);
                myIntent.putExtra("VisitEvent", visitEventCurrent);
                myIntent.putExtra("apiToken", apiToken);

                startActivity(myIntent);

            }
        });



        LabelDia.setText(visitEventCurrent.getDias());
        LabelHora.setText(visitEventCurrent.getHora());
        TextView TV = findViewById(R.id.tvFecha);
        if (visitEventCurrent.repeats) {
            TV.setText(getShortDate(visitEventCurrent.date));
            TV.setVisibility(View.VISIBLE);
        } else
            TV.setVisibility(View.INVISIBLE);
        populateCleaningTasks();
        populateEmployees();


    }
    private void populateCleaningTasks(){
        if (TareasWidgList == null)
            TareasWidgList = new ArrayList<>();
        TareasWidgList.clear();
        final ViewGroup TareasContainer = findViewById(R.id.tareasContainer);
        int previousSector=0;
        for (CleaningTask t : visitEventCurrent.cleaningTasks) {

            TareaWidg T = new TareaWidg(t,getLayoutInflater(), TareasContainer);

            if(t.sector_id != previousSector){
                TextView TV = new TextView(getApplicationContext());
                TV.setText("Sector "+t.sector_id);
                TV.setTextColor(Color.WHITE);
                TareasContainer.addView(TV);
                previousSector=t.sector_id;
            }
            TareasContainer.addView(T.getView());
        }

    }


    @Override
    public void onFragmentInteraction(@NotNull _user employee) {
        Intent myIntent = new Intent(this, DetallesEmpleado.class);
        myIntent.putExtra("employee_id", employee.getId());
        myIntent.putExtra("apiToken", apiToken);
        startActivity(myIntent);
    }

    private class shortSector{
        public int id;
        public String nombre;
    }

    private void populateEmployees(){
        ViewGroup Container = findViewById(R.id.empleadosContainer);
        Container.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        try{
            for (_user employee : visitEventCurrent.asignedEmployees){
                fragmentTransaction.add(R.id.empleadosContainer,
                        fichaEmpleadoChica.newInstance(employee) );
            }
        }catch(NullPointerException ex){
            Log.d("JOKO","Null pointer here");
        }
        fragmentTransaction.commit();
    }






    private class TareaWidg {
        private CleaningTask CleaningTask;
        private View view;
        private String sector;
        public TareaWidg(CleaningTask T, LayoutInflater inflater, ViewGroup parent){
            this.CleaningTask =T;
            view = inflater.inflate(R.layout.tarea_fragment,parent,false);
            ((TextView)view.findViewById(R.id.TareaDescripcion)).setText(T.descripcion);
            ((TextView)view.findViewById(R.id.TareaMinutos)).setText(T.duration.toString());
        }
        public View getView(){
            return view;
        }

    }



}
