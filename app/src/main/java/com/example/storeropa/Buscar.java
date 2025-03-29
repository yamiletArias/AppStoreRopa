package com.example.storeropa;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Buscar extends AppCompatActivity {

    private final String URLWS = "http://192.168.18.87/WSTienda/app/service/service-producto.php";

    RequestQueue requestQueue;

    EditText edtID, edtTipoProducto, edtGeneroProducto, edtTallaProducto, edtPrecioProducto;

    Button btnBuscarProducto, btnActualizarProducto, btnEliminarProducto, btnCancelarProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUI();
        btnBuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto();
            }
        });

        btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarEliminacion();
            }
        });

        btnCancelarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarProducto();
            }
        });

        btnActualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto();
            }
        });

        adBotones(false);
    }

    //botones que se habilitan al buscar un id
    private void adBotones(boolean sw){
        btnEliminarProducto.setEnabled(sw);
        btnActualizarProducto.setEnabled(sw);
    }

    //CANCELAR
    private void cancelarProducto(){
        resetUI();

        adBotones(false);

        edtID.requestFocus();
    }

    //ACTUALIZAR
    private void actualizarProducto() {
        requestQueue = Volley.newRequestQueue(this);

        String idProducto = edtID.getText().toString().trim();
        String tipo = edtTipoProducto.getText().toString().trim();
        String genero = edtGeneroProducto.getText().toString().trim();
        String talla = edtTallaProducto.getText().toString().trim();
        String precio = edtPrecioProducto.getText().toString().trim();

        JSONObject producto = new JSONObject();
        try {
            producto.put("id", idProducto);
            producto.put("tipo", tipo);
            producto.put("genero", genero);
            producto.put("talla", talla);
            producto.put("precio", precio);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URLUPDATE = URLWS;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URLUPDATE,
                producto,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response", "Response: " + response.toString());
                            boolean status = response.getBoolean("status");
                            if (status) {
                                showToast("Producto actualizado con éxito");
                                resetUI();
                            } else {
                                showToast("Hubo un Error al actualizar el producto");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("Error en la respuesta");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorWService", error.toString());
                        showToast("Error en la conexión");
                    }
                }
        );


        requestQueue.add(jsonRequest);
    }

    // ELIMINAR
    private void buscarProducto() {
        requestQueue = Volley.newRequestQueue(this);

        String URLparams = Uri.parse(URLWS)
                .buildUpon()
                .appendQueryParameter("q","findById")
                .appendQueryParameter("id",edtID.getText().toString())
                .build()
                .toString();

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                URLparams,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        resetUI();
                        if (response.length() == 0){
                            showToast("no existe el Producto");
                            adBotones(false);
                            edtID.requestFocus();

                        }else{

                            try {
                                JSONObject jsonObject = response.getJSONObject(0);

                                edtTipoProducto.setText(jsonObject.getString("tipo"));
                                edtGeneroProducto.setText(jsonObject.getString("genero"));
                                edtTallaProducto.setText(jsonObject.getString("talla"));
                                edtPrecioProducto.setText(jsonObject.getString("precio"));

                                adBotones(true);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorWS: ", error.toString());
                    }
                }
        );
        requestQueue.add(jsonRequest);
    }

    private void eliminarProducto() {
        requestQueue = Volley.newRequestQueue(this);
        String URLDELETE = URLWS + "/" + edtID.getText().toString().trim();
        Log.d("URL", URLDELETE);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                URLDELETE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Recibido", response.toString());

                        showToast("Producto eliminado correctamente");
                        resetUI();
                        adBotones(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorWService", error.toString());
                        showToast("Error al eliminar el producto");
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }

    private void confirmarEliminacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle("STORE ROPA");
        dialogo.setMessage("¿Seguro de eliminar?");
        dialogo.setCancelable(false);

        dialogo.setNegativeButton("No", null);
        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarProducto();
            }
        });

        dialogo.create().show();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();

    }

    private void resetUI(){
        edtTipoProducto.setText(null);
        edtGeneroProducto.setText(null);
        edtTallaProducto.setText(null);
        edtPrecioProducto.setText(null);
    }

    private void loadUI(){
        edtID = findViewById(R.id.edtID);
        edtTipoProducto = findViewById(R.id.edtTipoProducto);
        edtGeneroProducto = findViewById(R.id.edtGeneroProducto);
        edtTallaProducto = findViewById(R.id.edtTallaProducto);
        edtPrecioProducto = findViewById(R.id.edtPrecioProducto);

        btnBuscarProducto = findViewById(R.id.btnBuscarProducto);
        btnActualizarProducto = findViewById(R.id.btnActualizarProducto);
        btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
        btnCancelarProducto = findViewById(R.id.btnCancelarProducto);
    }
}