package com.example.apprestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistroPersonas extends AppCompatActivity {

private EditText txtNombre, txtEdad, txtCiudad,txtCorreo,txtContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_personas);
        getSupportActionBar().hide();

        txtNombre = findViewById(R.id.txtNombre);
        txtEdad = findViewById(R.id.txtEdad);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasenia = findViewById(R.id.txtContrasenia);
    }

    private final String URL_REGISTRO = "http://192.168.0.13/restaurante/registroPersonas.php";
    public void registro(View view) {
        if (validarCampos()) {
            builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("politicas de privacidad");
            builder.setMessage("\n" +
                    "¿Por qué lo usamos?\n" +
                    "Es un hecho establecido hace demasiado tiempo que un lector se distraerá con el contenido del texto de un sitio mientras que mira su diseño. El punto de usar Lorem Ipsum es que tiene una distribución más o menos normal de las letras, al contrario de usar textos como por ejemplo \"Contenido aquí, contenido aquí\". Estos textos hacen parecerlo un español que se puede leer. Muchos paquetes de autoedición y editores de páginas web usan el Lorem Ipsum como su texto por defecto, y al hacer una búsqueda de \"Lorem Ipsum\" va a dar por resultado muchos sitios web que usan este texto si se encuentran en estado de desarrollo. Muchas versiones han evolucionado a través de los años, algunas veces por accidente, otras veces a propósito (por ejemplo insertándole humor y cosas por el estilo).\n" +
                    "\n");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    insertarPersona();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ingresarBuilder("El usuario no se ha creado");
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            ingresarBuilder("Ingrese los datos correctamente");
        }

    }

    private boolean validarCampos(){
        if (!txtNombre.getText().toString().equals("")
                && !txtEdad.getText().toString().equals("")
                && !txtCiudad.getText().toString().equals("")
                && !txtCorreo.getText().toString().equals("")
                && !txtContrasenia.getText().toString().equals("")){
            return true;
        }else{
            return false;
        }
    }

    private ProgressDialog progressDialog;

    private void insertarPersona(){
        progressDialog = ProgressDialog.show(this,"", "Verificando las credenciales", true);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String mensaje = jsonObject.getString("mensaje");
                    if (mensaje.equals("Usuario guardado correctamente")){
                        JSONObject persona = new JSONObject(jsonObject.getString("usario"));
                        String[] preferencias = {
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
                ingresarBuilder("Verifica la conexion de tu red");
                progressDialog.hide();
            }
        }){
            public Map<String,String> getParams(){
                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre",txtNombre.getText().toString().trim());
                parametros.put("edad",txtEdad.getText().toString().trim());
                parametros.put("ciudad",txtCiudad.getText().toString().trim());
                parametros.put("correo",txtCorreo.getText().toString().trim());
                parametros.put("contrasenia",txtContrasenia.getText().toString().trim());
                return parametros;
            }
        };
        queue.add(stringRequest);

    }

    private void redireccionar(String[] preferencias) {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("nombre",preferencias[0]);
        editor.putString("edad",preferencias[1]);
        editor.putString("ciudad",preferencias[2]);
        editor.putString("correo",preferencias[3]);
        editor.putString("login","true");
        editor.commit();

        Intent i = new Intent(RegistroPersonas.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }

    private AlertDialog.Builder builder;
    private void ingresarBuilder(String mensaje){
        builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegistroPersonas.this, Login.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }
}