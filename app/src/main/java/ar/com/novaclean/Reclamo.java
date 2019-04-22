package ar.com.novaclean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.ReclamoData;


public class Reclamo extends AppCompatActivity {
    enum _State{
        INITIAL,
        PUNTUALIDAD,
        CALIDAD,
        CUMPLIMIENTO,
        COMENTARIO

    }
    _State State = _State.INITIAL;
    String currentPhotoPath;
    TextView Pregunta;
    EditText RespuestaET;
    Button Button1;
    Button Button2;
    Button Button3;
    ReclamoData ReclamoData;
    ImageView PhotoView;
    Evento EventoActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");
        setContentView(R.layout.activity_reclamo);
        Pregunta = findViewById(R.id.PreguntaTV);
        RespuestaET = findViewById(R.id.RespuestaET);
        Button1 = findViewById(R.id.button1);
        Button2 = findViewById(R.id.button2);
        Button3 = findViewById(R.id.button3);
        PhotoView = findViewById(R.id.PhotoView);
        PhotoView.setVisibility(View.INVISIBLE);
        ReclamoData = new ReclamoData();
        ReclamoData.EventoId=EventoActual.id;
        ReclamoData.FechaTimestamp=EventoActual.fecha.getTime()/1000;
        updateGUI();
    }
    void updateGUI(){
        switch (State){
            case INITIAL:
                Pregunta.setText("¿Qué tipo de reclamo desea realizar?");
                Button1.setText("Puntualidad");
                Button2.setText("Calidad de servicio");
                Button2.setText("Cumplimiento de objetivos");
                RespuestaET.setVisibility(View.GONE);
                RespuestaET.setVisibility(View.GONE);
                break;
            case PUNTUALIDAD:
                //Puntualidad A
                Pregunta.setText("En esta ocasión, nuestros representantes fueron");
                Button1.setText("Puntuales");
                Button2.setText("Impuntuales (hasta 10'')");
                Button3.setText("Muy impuntuales (más de 10 minutos)");
                RespuestaET.setVisibility(View.GONE);
                PhotoView.setVisibility(View.GONE);
                break;
            case CALIDAD://Calidad de servicio
                Pregunta.setText("Elija una opción");
                Button1.setText("Todas las tareas se realizaron adecuadamente");
                Button2.setText("Estoy insatisfecho con una o mas tareas realizadas");
                Button3.setVisibility(View.GONE);
                RespuestaET.setVisibility(View.GONE);
                PhotoView.setVisibility(View.GONE);
                break;
            case CUMPLIMIENTO://Cumplimiento de objetivos
                Pregunta.setText("Elija una opción");
                Button1.setText("Todas los objetivos fueorn cumplidos");
                Button2.setText("Quedaron tareas sin realizar o incompletas");
                Button3.setVisibility(View.GONE);
                RespuestaET.setVisibility(View.GONE);
                PhotoView.setVisibility(View.GONE);

                break;
            case COMENTARIO: //Comentario con foto
                Pregunta.setText("Puede escribir un comentario con foto");
                RespuestaET.setVisibility(View.VISIBLE);
                RespuestaET.setHint("Comentario");
                PhotoView.setVisibility(View.VISIBLE);
                Button1.setText("Tomar una foto");
                Button2.setText("Enviar");
                Button3.setVisibility(View.GONE);
                break;


        }
    }
    public void onClick(View v){
        switch (State){
            case INITIAL:
                switch (v.getId()){
                    case R.id.button1:
                        ReclamoData.Tipo=1;
                        State=State.PUNTUALIDAD;
                        break;
                    case R.id.button2:
                        ReclamoData.Tipo=2;
                        State=State.CALIDAD;
                        break;
                    case R.id.button3:
                        ReclamoData.Tipo=3;
                        State= State.CUMPLIMIENTO;
                        break;
                }
                break;
            case PUNTUALIDAD:
                switch (v.getId()){
                    case R.id.button1:
                        ReclamoData.Detalles="PUNTUAL";
                        State=State.COMENTARIO;
                    break;
                    case R.id.button2:
                        ReclamoData.Detalles="IMPUNTUAL<10";
                        State=State.COMENTARIO;
                    break;
                    case R.id.button3:
                        ReclamoData.Detalles="IMPUNTUAL>10";
                        State=State.COMENTARIO;
                    break;
                }
            break;
            case CALIDAD:
            case CUMPLIMIENTO:
                switch (v.getId()) {
                    case R.id.button1:
                        ReclamoData.Detalles = "OK";
                        State = State.COMENTARIO;
                        break;
                    case R.id.button2:
                        Intent getTareasIntent = new Intent(this, ReclamoPickTarea.class);
                        getTareasIntent.putExtra("ReclamoData",ReclamoData);
                        getTareasIntent.putExtra("EventoActual",EventoActual);
                        startActivityForResult(getTareasIntent, Constants.RQTareas);
                        break;
                }
            break;
            case COMENTARIO:
                switch (v.getId()){
                    case R.id.button1:
                        dispatchTakePictureIntent();
                        break;
                    case R.id.button2:
                        ReclamoData.Comentario=RespuestaET.getText().toString();
                        postReclamo(currentPhotoPath,ReclamoData);
                        break;
                }
            break;
        }
        updateGUI();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode)
        {
            case Constants.RQTareas:
                if(resultCode == Activity.RESULT_OK){
                    ReclamoData= (ReclamoData) data.getSerializableExtra("ReclamoData");
                    State=State.COMENTARIO;
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result

                }
                updateGUI();
                break;
            case Constants.RQTakePhoto:
                // Get the dimensions of the View
                int targetW = PhotoView.getWidth();
                int targetH = PhotoView.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                PhotoView.setImageBitmap(bitmap);

        }
    }//onActivityResult


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("JOKOGARCIA","Error occured while creating the file." +ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ar.com.novaclean.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constants.RQTakePhoto);
            }
        }
    }
    class ReclamoResponse{
        public String uploadedFile="";
        public String newFileName="";
        public int newReclamoId=-1;
        public String error="";
    }
    private void postReclamo(final String imagePath, ReclamoData reclamo) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                Constants.URL_POST_RECLAMO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Response", response);
                        //{"uploadedFile":"OK","newFileName":"..\/_privado\/imagenes\/reclamos\/6JPEG_20190419_122454_3760847181478474975.jpg","newReclamoId":6,"error":"NONE"}
                        Gson gson = new Gson();
                        ReclamoResponse Response = gson.fromJson(response,ReclamoResponse.class);
                        if(Response.newReclamoId >= 0) {
                            if (Response.error.equals("NONE")) {

                                /*
                                setResult(RESULT_OK);
                                finish();
                                */
                            }
                        }
                        else{
                            String error = Response.error + ". Imagen subida: '"+Response.newFileName+"' ID: "+Response.newReclamoId;
                            Toast.makeText(getApplicationContext(),"ERROR: "+error,Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Gson gson  = new Gson();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Settings", MODE_PRIVATE);
        String tok = sharedPreferences.getString("token", "");
        String clientId = sharedPreferences.getString("clientId", "0");
        String reclamoJSON = gson.toJson(reclamo);
        smr.addStringParam("reclamo", reclamoJSON);
        smr.addStringParam("client_id", clientId);
        smr.addStringParam("tok", tok);
        smr.addStringParam ("lugar_id",String.valueOf(EventoActual.lugar_id));
        smr.addFile("foto", imagePath);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smr);

    }
}
