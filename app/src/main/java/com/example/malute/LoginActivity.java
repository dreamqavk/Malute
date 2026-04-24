package com.example.malute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.malute.api.ApiClient;
import com.example.malute.api.AuthManager;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegistration;
    private Button btnVk;
    private Button btnYandex;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        authManager = new AuthManager(this);

        if (authManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, EntranceActivity.class);
            startActivity(intent);
            finish();
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.login_button);
        textViewRegistration = findViewById(R.id.registration);
        btnVk = findViewById(R.id.btnVk);
        btnYandex = findViewById(R.id.btnYandex);
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Введите email", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Введите пароль", Toast.LENGTH_SHORT).show();
            } else {
                loginWithPassword(email, password);
            }
        });

        textViewRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
            startActivity(intent);
        });

        btnVk.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Вход через VK", Toast.LENGTH_SHORT).show();
        });

        btnYandex.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Вход через Яндекс", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginWithPassword(String email, String password) {
        JsonObject body = new JsonObject();
        body.addProperty("identity", email);
        body.addProperty("password", password);

        ApiClient.getApiService().authWithPassword(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject data = response.body();
                    String token = data.get("token").getAsString();
                    String userId = data.get("record").getAsJsonObject().get("id").getAsString();

                    authManager.saveToken(token);
                    authManager.saveUserId(userId);

                    Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, EntranceActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}