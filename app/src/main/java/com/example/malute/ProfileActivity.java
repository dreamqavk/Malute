package com.example.malute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.malute.api.AuthManager;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout navHome, navCatalog, navProjects, navProfile;
    private ImageView navHomeIcon, navCatalogIcon, navProjectsIcon, navProfileIcon;
    private TextView navHomeText, navCatalogText, navProjectsText, navProfileText;
    private TextView userName, userSurname, userBirthDate, userGender, profileName, profileEmail;
    private Button editProfileButton, logoutButton;
    private AuthManager authManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        authManager = new AuthManager(this);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        initViews();
        setupNavigation();
        loadUserData();
        setupButtons();
        updateNavigationState();
    }

    private void initViews() {
        navHome = findViewById(R.id.navHome);
        navCatalog = findViewById(R.id.navCatalog);
        navProjects = findViewById(R.id.navProjects);
        navProfile = findViewById(R.id.navProfile);
        navHomeIcon = findViewById(R.id.navHomeIcon);
        navCatalogIcon = findViewById(R.id.navCatalogIcon);
        navProjectsIcon = findViewById(R.id.navProjectsIcon);
        navProfileIcon = findViewById(R.id.navProfileIcon);
        navHomeText = findViewById(R.id.navHomeText);
        navCatalogText = findViewById(R.id.navCatalogText);
        navProjectsText = findViewById(R.id.navProjectsText);
        navProfileText = findViewById(R.id.navProfileText);
        userName = findViewById(R.id.userName);
        userSurname = findViewById(R.id.userSurname);
        userBirthDate = findViewById(R.id.userBirthDate);
        userGender = findViewById(R.id.userGender);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        editProfileButton = findViewById(R.id.editProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("name", "Анна");
        String surname = sharedPreferences.getString("surname", "Иванова");
        String patronymic = sharedPreferences.getString("patronymic", "");
        String birthDate = sharedPreferences.getString("birthDate", "15.05.1995");
        String gender = sharedPreferences.getString("gender", "Женский");
        String email = sharedPreferences.getString("email", "anna@example.com");

        String fullName = name + " " + surname;
        if (!patronymic.isEmpty()) {
            fullName = name + " " + patronymic + " " + surname;
        }

        userName.setText(name);
        userSurname.setText(surname);
        userBirthDate.setText(birthDate);
        userGender.setText(gender);
        profileName.setText(fullName);
        profileEmail.setText(email);
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomePageActivity.class));
            finish();
        });
        navCatalog.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, CatalogActivity.class));
            finish();
        });
        navProjects.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ProjectsActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {});
    }

    private void setupButtons() {
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CreateProfileActivity.class);
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            authManager.clear();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateNavigationState() {
        int activeColor = Color.parseColor("#1A6FEE");
        int inactiveColor = Color.parseColor("#999999");

        navHomeText.setTextColor(inactiveColor);
        navCatalogText.setTextColor(inactiveColor);
        navProjectsText.setTextColor(inactiveColor);
        navProfileText.setTextColor(activeColor);

        navHomeIcon.setImageResource(R.drawable.ic_home);
        navCatalogIcon.setImageResource(R.drawable.ic_catalog);
        navProjectsIcon.setImageResource(R.drawable.ic_projects);
        navProfileIcon.setImageResource(R.drawable.ic_profile_active);
    }
}