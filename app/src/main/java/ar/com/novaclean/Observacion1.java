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

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.VisitEvent;
import ar.com.novaclean.Utils.RequestCallbackInterface;
import ar.com.novaclean.Utils.Requester;


public class Observacion1 extends AppCompatActivity {

    private VisitEvent visitEventCurrent;
    boolean isBefore;
    private float calificacion = 2.5f;
    ProgressBar progressBar;
    private String apiToken;
    private int userId;

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
        HashMap<String,String> params = new HashMap<String, String>();

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        float raiting = ratingBar.getRating();
        params.put("visit_event_id",String.valueOf(visitEventCurrent.id));
        //params.put("reference_date",String.valueOf(visitEventCurrent.date));
        params.put("reference_date","2017-05-06");
        params.put("raiting",String.valueOf(raiting));

        JSONObject paramsJsonObject = new JSONObject(params);


        progressBar.setVisibility(View.VISIBLE);
        Requester requester = new Requester(Request.Method.POST, new RequestCallbackInterface() {
            @Override
            public void Callback(int requestCode, JSONObject JSONObject) {
                Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void Callback(int requestCode, String string) {
                Toast.makeText(getApplicationContext(),"OK String",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void ErrorCallback(int requestCode, VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Error "+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        },paramsJsonObject,Constants.URL_POST_RAITING,0,apiToken);
        requester.queueMe();


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
