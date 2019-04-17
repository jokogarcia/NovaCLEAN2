package ar.com.novaclean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.ReclamoData;
import ar.com.novaclean.Models.Tarea;

public class ReclamoPickTarea extends AppCompatActivity {
    ReclamoData RD;
    Evento EventoActual;
    ArrayList<CheckBox> Checkboxes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo_pick_tarea);
        RD = (ReclamoData) getIntent().getSerializableExtra("ReclamoData");
        EventoActual = (Evento) getIntent().getSerializableExtra("EventoActual");
        Checkboxes= new ArrayList<>();
        for(Tarea T : EventoActual.Tareas){
            CheckBox CB = new CheckBox(this);
            CB.setText(T.descripcion);
            CB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
           Checkboxes.add(CB);
        }
        for(CheckBox CB : Checkboxes){
            ((LinearLayout)findViewById(R.id.TareasLL)).addView(CB);
        }

    }
    public void onClick(View v){
        ArrayList<Integer> SelectedTareasIds= new ArrayList<>();
        switch(v.getId()){
            case R.id.TareasListo:
                int i=0;
                for(CheckBox CB : Checkboxes){
                    if (CB.isChecked()){
                        SelectedTareasIds.add(EventoActual.Tareas.get(i).id);
                    }
                }
                //return to other activity:
                RD.Detalles=android.text.TextUtils.join(",",SelectedTareasIds);
                Intent ReturnIntent = new Intent();
                ReturnIntent.putExtra("ReclamoData", RD);
                setResult(Activity.RESULT_OK,ReturnIntent);
                finish();
                break;

        }
    }
}
