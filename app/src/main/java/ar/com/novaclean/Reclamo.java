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
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.android.volley.request.SimpleMultiPartRequest;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.ReclamoData;
import ar.com.novaclean.Models.Usuario;


public class Reclamo extends AppCompatActivity {
    String currentPhotoPath;
    TextView Pregunta;
    Button Button1;
    Button Button2;
    ReclamoData ReclamoData;
    Evento EventoActual;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        setContentView(R.layout.activity_reclamo);
        Pregunta = findViewById(R.id.PreguntaTV);
        Button1 = findViewById(R.id.button1);
        Button2 = findViewById(R.id.button2);
        ReclamoData = new ReclamoData();
        ReclamoData.EventoId=EventoActual.id;
        ReclamoData.FechaTimestamp=EventoActual.fecha.getTime()/1000;

    }


    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.button1:
                ReclamoData.Tipo=1;
                intent = new Intent(getApplicationContext(),reclamo_puntualidad.class);
                break;
            case R.id.button2:
                ReclamoData.Tipo=2;
                intent = new Intent(getApplicationContext(),ReclamoPickTarea.class);
                intent.putExtra("Evento",EventoActual);
                break;
            default:return;
        }
        intent.putExtra("ReclamoData",ReclamoData);
        startActivityForResult(intent,Constants.RQReclamoPuntualidad);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode)
        {
            case Constants.RQTareas:
            case Constants.RQReclamoPuntualidad:
                if(resultCode == Activity.RESULT_OK){
                    ReclamoData= (ReclamoData) data.getSerializableExtra("ReclamoData");
                    Intent ComentarioIntent = new Intent(getApplicationContext(),ReclamoComentarioYfoto.class);
                    ComentarioIntent.putExtra("ReclamoData",ReclamoData);
                    startActivityForResult(ComentarioIntent,Constants.RQComentario);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result

                }
                break;
            case Constants.RQComentario:
                if(resultCode == Activity.RESULT_OK){
                    ReclamoData= (ReclamoData) data.getSerializableExtra("ReclamoData");
                    String path = (String) data.getSerializableExtra("CurrentPhotoPath");
                    postReclamo(path,ReclamoData);
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
    private void postReclamo(final String imagePath, final ReclamoData reclamo) {

        boolean hasImage;
        /*
        TODO:
        if size(imagePath)> 1MB resize image;
        */
        try
        {
            File f = new File(imagePath);
            hasImage=f.isFile();
        }
        catch (NullPointerException e){
            hasImage=false;
        }
        ShowProgressBar(true);
        final boolean finalHasImage = hasImage;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_POST_RECLAMO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Gson gson = new Gson();
                            ShowProgressBar(false);
                            try{
                                ReclamoResponse Response = gson.fromJson(response,ReclamoResponse.class);
                                if(Response.newReclamoId >= 0) {
                                    if (Response.error.equals("NONE")) {
                                        setResult(RESULT_OK);
                                        finish();

                                    }
                                }
                                else{
                                    String error = Response.error + ". Imagen subida: '"
                                            +Response.newFileName+"' ID: "+Response.newReclamoId;
                                    Toast.makeText(getApplicationContext(),"ERROR: "
                                            +error,Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(java.lang.IllegalStateException e){
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG)
                                        .show();
                                return;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ShowProgressBar(false);
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = usuario.getLoginParams();

                Gson gson  = new Gson();

                String reclamoJSON = gson.toJson(reclamo);

                params.put("reclamo",reclamoJSON);
                params.put("hasImage",String.valueOf(finalHasImage));
                params.put("lugar_id",String.valueOf(EventoActual.lugar_id));
                if(finalHasImage){
                    File imageFile= new File(imagePath);
                    String image64 = encodeFileToBase64Binary(imageFile);
                    params.put("foto",image64);
                    String[] dot = imagePath.split("[.]");

                    params.put("ext",dot[dot.length-1]);
                }
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);



        //TODO: Verificar el tamaño máximo de archivo para evitar errores de subida (https://stackoverflow.com/questions/8053824/ssl-broken-pipe)

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
