package ar.com.novaclean;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import ar.com.novaclean.Models.Empleado;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Tarea;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");
        if(EventoActual.Repetible)
            LabelDia.setText(EventoActual.Dias);
        else
            LabelDia.setText(EventoActual.Fecha.toString());
        String h= String.format("%02d:%02d",(int)EventoActual.Hora/60,(int)EventoActual.Hora%60);
        LabelHora.setText(h);
        LayoutInflater inflater = getLayoutInflater();
        TareasWidgList.clear();
        for(Tarea t:EventoActual.Tareas){

            TareaWidg T = new TareaWidg(t, inflater,TareasContainer);
            TareasWidgList.add(T);
        }
        EmpleadosWidgList.clear();
        for (Empleado e: EventoActual.EmpleadosDesignados){
            EmpleadoWidg E = new EmpleadoWidg(e,inflater,EmpleadosContainer);
            EmpleadosWidgList.add(E);
        }
        TareasContainer.removeAllViews();
        for(TareaWidg T : TareasWidgList){
            TareasContainer.addView(T.getView());
        }
        EmpleadosContainer.removeAllViews();
        EmpleadoWidg tmp = null;
        for(int i=0; i < EmpleadosWidgList.size();i++){
            if (i%2 == 0){
                tmp=EmpleadosWidgList.get(i);

            }else{
                LinearLayout L=new LinearLayout(EmpleadosContainer.getContext());
                L.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                L.addView(tmp.getView());
                L.addView(EmpleadosWidgList.get(i).getView());
                tmp=null;
            }

        }
        if(tmp!=null){
            LinearLayout L=new LinearLayout(EmpleadosContainer.getContext());
            L.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            L.addView(tmp.getView());
        }

    }
    private class TareaWidg {
        private Tarea Tarea;
        private View view;
        public TareaWidg(Tarea T, LayoutInflater inflater, ViewGroup parent){
            this.Tarea=T;
            view = inflater.inflate(R.layout.tarea_fragment,parent,false);
            ((TextView)view.findViewById(R.id.TareaDescripcion)).setText(T.Descripcion);
            ((TextView)view.findViewById(R.id.TareaMinutos)).setText(T.Minutos+" min");
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
            ((TextView)view.findViewById(R.id.nombreTV)).setText(E.Apellido+", "+E.Nombre);
            ImageButton ib = view.findViewById(R.id.photoButton);
            Uri uri = Uri.parse("https://novaclean.com.ar/images/empleados/"+E.foto_url);
            ib.setImageURI(uri);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
        public View getView(){return view;}
    }
}
