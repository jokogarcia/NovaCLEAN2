package ar.com.novaclean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.VisitEvent;
import ar.com.novaclean.Models.User;
import ar.com.novaclean.Models.Usuario;
import ar.com.novaclean.Utils.LoginResultListener;
import ar.com.novaclean.Utils.RequestResult;
import ar.com.novaclean.Utils.RequestResultListener;

public class MainActivity extends AppCompatActivity {

    private int ClienteID;
    private Button LogInButton;
    private Switch IniciarAutomatico;
    private Context ThisActivity;
    private SharedPreferences sharedPreferences;
    private ArrayList<VisitEvent> Itinerario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisActivity=this;
        RequestQueueSingleton dummy = RequestQueueSingleton.getInstance(this);
        setContentView(R.layout.activity_main);
        LogInButton = findViewById(R.id.loginButton);
        IniciarAutomatico = findViewById(R.id.iniciarAutomatico);
        ClienteID=0;
        sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
        ((EditText)findViewById(R.id.userEmail)).setText(sharedPreferences.getString("userEmail",""));
        ((EditText)findViewById(R.id.userPass)).setText(sharedPreferences.getString("userPass",""));
        IniciarAutomatico.setChecked(sharedPreferences.getBoolean("autologin",false));
        if(IniciarAutomatico.isChecked())
            doLogin();
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();

            }
        });
        //for debug only:
        ((EditText)findViewById(R.id.userEmail)).setText("ihuel@example.com");
        ((EditText)findViewById(R.id.userPass)).setText("password");
        doLogin();
        User user = new User();
        user.Home("", new RequestResultListener() {
            @Override
            public void OnRequestResult(@NotNull RequestResult requestResult) {

            }
        });

    }
    void doLogin(){
        final String email=((EditText)findViewById(R.id.userEmail)).getText().toString();
        final String pass=((EditText)findViewById(R.id.userPass)).getText().toString();
        User user = new User();
        user.setEmail(email);
        user.RequestLogin(pass, new LoginResultListener() {
            @Override
            public void OnLoginResult(LoginResult loginResult) {
                if(loginResult.success){
                    //getVisitEvents(loginResult.user);
                    Intent intent = new Intent(MainActivity.this,ListaDeEventos.class);
                    intent.putExtra("apiToken", loginResult.user.getApi_token());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Email o Contraseña incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    protected void onResume() {
        IniciarAutomatico.setChecked(sharedPreferences.getBoolean("iniciarAutomatico",false));
        super.onResume();
    }
    private void getEventos(final Usuario usuario) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_EVENTOS_FROM_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        try{
                            VisitEvent[] _visitEvents = g.fromJson(response, VisitEvent[].class);
                            ArrayList<VisitEvent> visitEvents = new ArrayList<>(Arrays.asList(_visitEvents));
                            ArrayList<VisitEvent> thisDaysVisitEvents = new ArrayList<>();
                            Date today = new Date();
                            for (VisitEvent visitEvent : visitEvents){
                                if(visitEvent.isOnDate(today)){
                                    visitEvent.date=today;
                                    thisDaysVisitEvents.add(visitEvent);
                                }
                            }
                            Intent myIntent = new Intent(MainActivity.this, ListaDeEventos.class);
                            myIntent.putExtra("visitEvents", thisDaysVisitEvents);
                            myIntent.putExtra("Usuario",usuario);
                            myIntent.putExtra("Date",today);

                            startActivity(myIntent);

                        }catch(IllegalStateException e){
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                return usuario.getLoginParams();
            }

        };
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
