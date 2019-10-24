package ar.com.novaclean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.VisitEvent;


public class Observacion1 extends AppCompatActivity {

    private VisitEvent visitEventCurrent;
    boolean isBefore;
    private float calificacion = 2.5f;
    ProgressBar progressBar;
    private String apiToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion1);

        Button BotonEnviar = findViewById(R.id.btEnviar);
        Button BotonReclamo = findViewById(R.id.btReclamo);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        progressBar = findViewById(R.id.progressBarObservacion);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                calificacion = rating;
            }
        });
        ratingBar.setRating(calificacion);

        visitEventCurrent = (VisitEvent) getIntent().getSerializableExtra("VisitEvent");
        apiToken = getIntent().getStringExtra("apiToken");
        Date today= new Date();
        isBefore =(visitEventCurrent.date.after(today));
        if(isBefore){
            BotonEnviar.setText("Solicitar reprogramacion");
            BotonReclamo.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
        }


    }
    public void buttonService(View v){
        switch (v.getId()){
            case R.id.btEnviar:
                if(isBefore){
                    //Solicitar reprogramacion
                    Toast.makeText(getApplicationContext(),"No implementado",Toast.LENGTH_LONG).show();
                }
                else{
                    //Calificar
                    enviarCalificacion();

                }

                break;
            case R.id.btReclamo:
                //complaint:
                Intent myIntent = new Intent(getApplicationContext(), Reclamo.class);
                myIntent.putExtra("VisitEvent", visitEventCurrent);
                myIntent.putExtra("apiToken", apiToken);
                startActivityForResult(myIntent, Constants.RQReclamo);

                break;
        }
    }
    private class CalificacionResponse{
        public int newReclamoId=-1;
        public String error="";
    }
    private void enviarCalificacion() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_POST_CALIFICACION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        progressBar.setVisibility(View.INVISIBLE);
                        try{
                            CalificacionResponse Response = gson.fromJson(response, CalificacionResponse.class);
                            if(Response.newReclamoId >= 0) {
                                if (Response.error.equals("NONE")) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                            else{
                                String error = Response.error;
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
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Settings", MODE_PRIVATE);
                String tok = sharedPreferences.getString("token", "");
                String clientId = sharedPreferences.getString("clientId", "0");


                params.put("calificacion",((Float)calificacion).toString());
                params.put("client_id",clientId);
                params.put("evento_id",String.valueOf(visitEventCurrent.id));
                params.put("tok",tok);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
            finish();

    }
    private class Calificacion{
        float valor;
        int usuario_id;
        int evento_id;
    }


}
