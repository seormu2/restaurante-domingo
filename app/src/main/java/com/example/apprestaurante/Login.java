package com.example.apprestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText txtCorreo,txtContrasenia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        txtCorreo      = findViewById(R.id.txtCorreo);
        txtContrasenia = findViewById(R.id.txtContrasenia);

        verificarPreferencias();

    }

    private ProgressDialog progressDialog;
    private String URL_LOGIN = "http://192.168.0.13/restaurante/login.php";
    public void iniciarSesion(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        progressDialog = ProgressDialog.show(this,"", "Verificando las credenciales", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String mensaje = jsonObject.getString("mensaje");
                    if (mensaje.equals("Usuario o contraseñas incorrectas")){
                        ingresarBuilder(mensaje);
                        progressDialog.hide();
                    }else if(mensaje.equals("Datos correctos")){
                        JSONObject persona = new JSONObject(jsonObject.getString("persona"));
                        String[] preferencias = {
                                persona.getString("id"),
                                persona.getString("nombre"),
                                persona.getString("edad"),
                                persona.getString("ciudad"),
                                persona.getString("correo"),
                        };
                        redireccionar(preferencias);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ingresarBuilder("Verifica la conexión de tu internet."+error);
                progressDialog.hide();

            }
        }){
            public Map<String, String> getParams(){
                Map<String, String> parametros = new HashMap<>();
                parametros.put("correo",txtCorreo.getText().toString());
                parametros.put("contrasenia",txtContrasenia.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void redireccionar(String[] preferencias) {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id",preferencias[0]);
        editor.putString("nombre",preferencias[1]);
        editor.putString("edad",preferencias[2]);
        editor.putString("ciudad",preferencias[3]);
        editor.putString("correo",preferencias[4]);
        editor.putString("login","true");
        editor.commit();

        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }

    private void verificarPreferencias(){
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        String validacionLogin = sharedPreferences.getString("login","false");
        if (validacionLogin.equals("true")){
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
            finish();
        }
    }

    private AlertDialog.Builder builder;
    private void ingresarBuilder(String mensaje){
        builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void registro(View view) {
        Intent i = new Intent(getApplicationContext(),RegistroPersonas.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }
}