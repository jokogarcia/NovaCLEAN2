package ar.com.novaclean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Cliente;
import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Evento> Eventos;
    private int ClienteID;
    private Button LogInButton;
    private Context ThisActivity;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisActivity=this;
        setContentView(R.layout.activity_main);
        LogInButton = findViewById(R.id.loginButton);
        ClienteID=0;
        sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
        ((EditText)findViewById(R.id.userEmail)).setText(sharedPreferences.getString("email","removethisnada"));
        ((EditText)findViewById(R.id.userPass)).setText(sharedPreferences.getString("pass",""));
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
//                Intent myIntent = new Intent(ThisActivity, Calendario.class);
//                myIntent.putExtra("ClienteID",ClienteID);
//                startActivity(myIntent);
            }
        });
    }
    void doLogin(){
        final String email=((EditText)findViewById(R.id.userEmail)).getText().toString();
        final String pass=((EditText)findViewById(R.id.userPass)).getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        LoginResponse lr = g.fromJson(response, LoginResponse.class);
                        if(lr.id > 0){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userEmail", email);
                            editor.putString("userPass", pass);
                            editor.putString("token", lr.token);
                            Log.d("token",lr.token);
                            if(!editor.commit())
                                Toast.makeText(getApplicationContext(),"Not commited!",Toast.LENGTH_LONG);
                            Intent myIntent = new Intent(ThisActivity, Calendario.class);
                            myIntent.putExtra("ClienteID",lr.id);
                            startActivity(myIntent);
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("pass",pass);
                return params;
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private class LoginResponse{
        public int id;
        public String nombre;
        public String apellido;
        public String cuit;
        public String telefono;
        public String domicilio;
        public String token;
    }
}
