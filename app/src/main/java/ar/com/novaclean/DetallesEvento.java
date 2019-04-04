package ar.com.novaclean;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Tarea;

public class DetallesEvento extends AppCompatActivity {

    private Evento EventoActual;
    private TextView LabelDia,LabelHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalles_evento);
        LinearLayout TareasContainer = findViewById(R.id.tareasContainer);

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
        for(Tarea t:EventoActual.Tareas){
            TareaWidg T = new TareaWidg(this,t);
            //TareasContainer.addView(T);
            TextView TV=new TextView(this);
            TV.setText(t.Descripcion);
            TV.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

            TareasContainer.addView(TV);
        }
    }
    class TareaWidg extends View {
        private TextView Text;
        private Tarea Tarea;
        public TareaWidg(Context context, Tarea T){
            super(context);
            Text=new TextView(context);
            this.Tarea=T;
            Text.setText(Tarea.Descripcion);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }
    }
}
