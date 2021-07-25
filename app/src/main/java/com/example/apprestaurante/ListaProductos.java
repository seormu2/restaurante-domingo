package com.example.apprestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ListaProductos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        Bundle bundle = getIntent().getExtras();
        String categoria = bundle.getString("categoria");
        Toast.makeText(getApplicationContext(),categoria,Toast.LENGTH_LONG).show();
    }
}