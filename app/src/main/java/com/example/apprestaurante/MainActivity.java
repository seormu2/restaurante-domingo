package com.example.apprestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void cerrarSesion(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }

    public void bebidas(View view) {
        redireccionar("bebidas");
    }

    public void redireccionar(String categoria){
        Intent i = new Intent(MainActivity.this,ListaProductos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    public void comidas(View view) {
        redireccionar("comidas");
    }
}