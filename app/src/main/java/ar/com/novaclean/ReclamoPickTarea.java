package ar.com.novaclean;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ar.com.novaclean.Models.Complaint;
import ar.com.novaclean.Models.VisitEvent;
import ar.com.novaclean.Models.CleaningTask;

public class ReclamoPickTarea extends AppCompatActivity {
    Complaint complaint;
    VisitEvent visitEventCurrent;
    ArrayList<CheckBox> Checkboxes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo_pick_tarea);
        complaint = (Complaint) getIntent().getSerializableExtra("Complaint");
        visitEventCurrent = (VisitEvent) getIntent().getSerializableExtra("VisitEvent");
        Checkboxes= new ArrayList<>();
        for(CleaningTask T : visitEventCurrent.cleaningTasks){
            CheckBox CB = new CheckBox(this);
            CB.setText(T.descripcion);
            CB.setTextColor(Color.WHITE);
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
                        SelectedTareasIds.add(visitEventCurrent.cleaningTasks.get(i).id);
                    }
                    i++;
                }
                //return to other activity:
                complaint.comment =android.text.TextUtils.join(",",SelectedTareasIds);
                Intent ReturnIntent = new Intent();
                ReturnIntent.putExtra("Complaint", complaint);
                setResult(Activity.RESULT_OK, ReturnIntent);
                finish();
                break;

        }
    }
}
