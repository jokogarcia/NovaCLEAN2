package ar.com.novaclean;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.VisitEvent;
import ar.com.novaclean.Models.Complaint;


public class Reclamo extends AppCompatActivity {
    String currentPhotoPath;
    TextView Pregunta;
    Button Button1;
    Button Button2;
    Complaint Complaint;
    VisitEvent visitEventCurrent;
    String apiToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitEventCurrent = (VisitEvent) getIntent().getSerializableExtra("VisitEvent");
        apiToken = getIntent().getStringExtra("apiToken");
        setContentView(R.layout.activity_reclamo);
        Pregunta = findViewById(R.id.PreguntaTV);
        Button1 = findViewById(R.id.button1);
        Button2 = findViewById(R.id.button2);
        Complaint = new Complaint();

        Complaint.visit_event_id= visitEventCurrent.id;
        Complaint.referenceDate = visitEventCurrent.date;

    }


    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.button1:
                Complaint.complaint_type =1;
                intent = new Intent(getApplicationContext(),reclamo_puntualidad.class);
                break;
            case R.id.button2:
                Complaint.complaint_type =2;
                intent = new Intent(getApplicationContext(),ReclamoPickTarea.class);
                intent.putExtra("VisitEvent", visitEventCurrent);
                break;
            default:return;
        }
        intent.putExtra("Complaint", Complaint);
        startActivityForResult(intent,Constants.RQReclamoPuntualidad);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode)
        {
            case Constants.RQTareas:
            case Constants.RQReclamoPuntualidad:
                if(resultCode == Activity.RESULT_OK){
                    Complaint = (Complaint) data.getSerializableExtra("Complaint");
                    Intent ComentarioIntent = new Intent(getApplicationContext(),ReclamoComentarioYfoto.class);
                    ComentarioIntent.putExtra("Complaint", Complaint);
                    startActivityForResult(ComentarioIntent,Constants.RQComentario);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result

                }
                break;
            case Constants.RQComentario:
                if(resultCode == Activity.RESULT_OK){
                    Complaint = (Complaint) data.getSerializableExtra("Complaint");
                    String path = (String) data.getSerializableExtra("CurrentPhotoPath");
                    postReclamo(path, Complaint);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result

                }
                break;

        }
    }//onActivityResult



    class ReclamoResponse{
        public String uploadedFile="";
        public String newFileName="";
        public int newReclamoId=-1;
        public String error="";
    }
    private void postReclamo(final String imagePath, final Complaint reclamo) {


        //TODO: Implementar

    }
    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        if (file.length() == 0L || file.length() > 1E6)
            return "ERROR 0";
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);

            encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
    private void ShowProgressBar(boolean show){
        ProgressBar PB = findViewById(R.id.progressBar2);
        TextView TV3 = findViewById(R.id.textView3);
        int normalState=View.INVISIBLE;
        int progresState=View.VISIBLE;
        if(!show) {
            normalState = View.VISIBLE;
            progresState = View.INVISIBLE;
        }
        Pregunta.setVisibility(normalState);
        Button1.setVisibility(normalState);
        Button2.setVisibility(normalState);


        PB.setVisibility(progresState);
        TV3.setVisibility(progresState);

    }
}
