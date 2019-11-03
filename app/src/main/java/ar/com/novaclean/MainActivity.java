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
        ((EditText)findViewById(R.id.userEmail)).setText("jackeline12@example.net");
        ((EditText)findViewById(R.id.userPass)).setText("password");
        doLogin();

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

}
