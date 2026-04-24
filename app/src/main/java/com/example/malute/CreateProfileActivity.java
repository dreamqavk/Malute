package com.example.malute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.malute.api.ApiClient;
import com.example.malute.api.AuthManager;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPatronymic;
    private EditText editTextSurname;
    private EditText editTextBirthDate;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Spinner spinnerGender;
    private Button buttonSave;
    private String selectedGender = "";
    private AuthManager authManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        authManager = new AuthManager(this);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        initViews();
        setupGenderSpinner();
        setupSaveButton();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPatronymic = findViewById(R.id.editTextPatronymic);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerGender = findViewById(R.id.spinnerGender);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void setupGenderSpinner() {
        String[] genders = {"Выберите пол", "Мужской", "Женский"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedGender = genders[position];
                } else {
                    selectedGender = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = "";
            }
        });
    }

    private void setupSaveButton() {
        buttonSave.setOnClickListener(v -> registerUser());
    }

    private void saveProfileLocally(String name, String patronymic, String surname, String birthDate, String email, String gender) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("patronymic", patronymic);
        editor.putString("surname", surname);
        editor.putString("birthDate", birthDate);
        editor.putString("email", email);
        editor.putString("gender", gender);
        editor.apply();
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String patronymic = editTextPatronymic.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String birthDate = editTextBirthDate.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (surname.isEmpty()) {
            Toast.makeText(this, "Введите фамилию", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Введите почту", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Введите пароль (минимум 8 символов)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "Пароль должен содержать минимум 8 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedGender.isEmpty()) {
            Toast.makeText(this, "Выберите пол", Toast.LENGTH_SHORT).show();
            return;
        }

        saveProfileLocally(name, patronymic, surname, birthDate, email, selectedGender);

        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        body.addProperty("passwordConfirm", password);
        body.addProperty("name", name);
        body.addProperty("surname", surname);

        if (!patronymic.isEmpty()) {
            body.addProperty("patronymic", patronymic);
        }
        if (!birthDate.isEmpty()) {
            body.addProperty("birthDate", birthDate);
        }
        body.addProperty("gender", selectedGender);

        Toast.makeText(this, "Регистрация...", Toast.LENGTH_SHORT).show();

        ApiClient.getApiService().registerUser(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateProfileActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                    loginAfterRegistration(email, password);
                } else {
                    String errorMsg = "Ошибка регистрации";
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            if (errorBody.contains("password")) {
                                errorMsg = "Пароль должен быть не менее 8 символов";
                            } else if (errorBody.contains("email")) {
                                errorMsg = "Email уже существует или неверный формат";
                            } else {
                                errorMsg = errorBody;
                            }
                        }
                    } catch (Exception e) {}
                    Toast.makeText(CreateProfileActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CreateProfileActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginAfterRegistration(String email, String password) {
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

                    Intent intent = new Intent(CreateProfileActivity.this, EntranceActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CreateProfileActivity.this, "Ошибка входа после регистрации", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CreateProfileActivity.this, "Ошибка сети при входе", Toast.LENGTH_SHORT).show();
            }
        });
    }
}