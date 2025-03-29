package com.example.storeropa;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarUsuario extends AppCompatActivity {

    private EditText edtUsernameRegistro, edtPasswordRegistro;
    private Button btnRegistro;
    private final String URL_REGISTER = "http://192.168.18.87/WSTienda/app/service/service-register.php"; // Asegúrate de que la URL es correcta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        btnRegistro.setOnClickListener(view -> {
            String username = edtUsernameRegistro.getText().toString();
            String password = edtPasswordRegistro.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegistrarUsuario.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                registroUser(username, password);
            }
        });
    }

    private void registroUser(String username, String password) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        boolean status = jsonResponse.getBoolean("status");

                        if (status) {
                            Toast.makeText(RegistrarUsuario.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegistrarUsuario.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegistrarUsuario.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(RegistrarUsuario.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }){

            @Override
            public byte[] getBody() {
                // Convierte el objeto JSON en bytes para enviarlo en el cuerpo de la solicitud POST.
                return jsonBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                // Indica que el contenido es JSON con codificación UTF-8.
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadUI() {
        edtUsernameRegistro = findViewById(R.id.edtUsernameRegistro);
        edtPasswordRegistro = findViewById(R.id.edtPasswordRegistro);
        btnRegistro = findViewById(R.id.btnRegistro);
    }
}
