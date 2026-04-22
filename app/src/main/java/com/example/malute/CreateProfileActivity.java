package com.example.malute;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPatronymic;
    private EditText editTextSurname;
    private EditText editTextBirthDate;
    private EditText editTextEmail;
    private Spinner spinnerGender;
    private Button buttonSave;

    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
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
        spinnerGender = findViewById(R.id.spinnerGender);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void setupGenderSpinner() {
        String[] genders = {"Выберите пол", "Мужской", "Женский"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genders
        );
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
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String patronymic = editTextPatronymic.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String birthDate = editTextBirthDate.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();


        if (name.isEmpty()) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
            return;
        }

        if (surname.isEmpty()) {
            Toast.makeText(this, "Введите фамилию", Toast.LENGTH_SHORT).show();
            return;
        }

        if (birthDate.isEmpty()) {
            Toast.makeText(this, "Введите дату рождения", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Введите почту", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedGender.isEmpty()) {
            Toast.makeText(this, "Выберите пол", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = "Профиль создан!\n" +
                "Имя: " + name + "\n" +
                "Фамилия: " + surname + "\n" +
                "Пол: " + selectedGender;

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }
}