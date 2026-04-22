package com.example.malute;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EntranceActivity extends AppCompatActivity {

    private View dot1, dot2, dot3, dot4;
    private String pinCode = "";
    private final String CORRECT_PIN = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        setupKeyboard();
    }

    private void setupKeyboard() {
        FrameLayout btn1 = findViewById(R.id.btn1);
        FrameLayout btn2 = findViewById(R.id.btn2);
        FrameLayout btn3 = findViewById(R.id.btn3);
        FrameLayout btn4 = findViewById(R.id.btn4);
        FrameLayout btn5 = findViewById(R.id.btn5);
        FrameLayout btn6 = findViewById(R.id.btn6);
        FrameLayout btn7 = findViewById(R.id.btn7);
        FrameLayout btn8 = findViewById(R.id.btn8);
        FrameLayout btn9 = findViewById(R.id.btn9);
        FrameLayout btn0 = findViewById(R.id.btn0);
        FrameLayout btnBack = findViewById(R.id.btnBack);

        btn1.setOnClickListener(v -> addDigit("1"));
        btn2.setOnClickListener(v -> addDigit("2"));
        btn3.setOnClickListener(v -> addDigit("3"));
        btn4.setOnClickListener(v -> addDigit("4"));
        btn5.setOnClickListener(v -> addDigit("5"));
        btn6.setOnClickListener(v -> addDigit("6"));
        btn7.setOnClickListener(v -> addDigit("7"));
        btn8.setOnClickListener(v -> addDigit("8"));
        btn9.setOnClickListener(v -> addDigit("9"));
        btn0.setOnClickListener(v -> addDigit("0"));
        btnBack.setOnClickListener(v -> deleteLastDigit());
    }

    private void addDigit(String digit) {
        if (pinCode.length() < 4) {
            pinCode += digit;
            updateDots();

            if (pinCode.length() == 4) {
                checkPin();
            }
        }
    }

    private void deleteLastDigit() {
        if (!pinCode.isEmpty()) {
            pinCode = pinCode.substring(0, pinCode.length() - 1);
            updateDots();
        }
    }

    private void updateDots() {
        View[] dots = {dot1, dot2, dot3, dot4};

        for (int i = 0; i < dots.length; i++) {
            if (i < pinCode.length()) {
                dots[i].setBackgroundResource(R.drawable.circle_filled);
            } else {
                dots[i].setBackgroundResource(R.drawable.circle_empty);
            }
        }
    }

    private void checkPin() {
        if (pinCode.equals(CORRECT_PIN)) {
            Toast.makeText(this, "PIN верный!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Неверный PIN-код", Toast.LENGTH_SHORT).show();
            pinCode = "";
            updateDots();
        }
    }
}