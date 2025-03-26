package com.example.storeropa;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeropa.adaptadores.ProductoAdapter;
import com.example.storeropa.entidades.Producto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    private final String URLWS = "http://192.168.18.85/WSTienda/app/service/service-producto.php";

    RequestQueue requestQueue;
    ListView lstProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUI();

        obtenerRegistrosProduc();
    }

    private void obtenerRegistrosProduc(){
        requestQueue = Volley.newRequestQueue(this);

        final String URL_LISTAR = URLWS + "?q=showAll";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_LISTAR,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        procesarDatosProduc(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorWS", error.toString());
                    }
                }
        );

        //PASO 3: Envío
        requestQueue.add(jsonRequest);
    }

    private void procesarDatosProduc(JSONArray response) {
        try {
            Producto producto;
            ArrayList<Producto> listaProducto = new ArrayList<>();
            ArrayList<String> listaSimple = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                String genero = jsonObject.getString("genero");

                if (genero.equals("F")) {
                    genero = "Dama";
                } else if (genero.equals("M")) {
                    genero = "Varón";
                }

                listaSimple.add(jsonObject.getString("tipo") + " " + genero);

                producto = new Producto();
                producto.setId(jsonObject.getInt("id"));
                producto.setTipo(jsonObject.getString("tipo"));
                producto.setGenero(genero);
                producto.setTalla(jsonObject.getString("talla"));
                producto.setPrecio(jsonObject.getString("precio"));

                listaProducto.add(producto);
            }

            ProductoAdapter adaptador = new ProductoAdapter(this, listaProducto);
            lstProductos.setAdapter(adaptador);
        } catch (Exception e) {
            Log.e("ErrorResponse", e.toString());
        }
    }


    private void loadUI(){
        lstProductos = findViewById(R.id.lstProductos);
    }
}