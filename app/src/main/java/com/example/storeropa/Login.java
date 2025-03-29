package com.example.storeropa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private final String URL_WS = "http://192.168.18.87/WSTienda/app/service/service-login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Por favor llene los campos", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, RegistrarUsuario.class);
        startActivity(intent);
    }

    private void loginUser(String username, String password) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_WS,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean status = jsonResponse.getBoolean("status");

                        if (status) {
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Nombre de usuario o contraseña no válidos", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

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

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadUI() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }
}
