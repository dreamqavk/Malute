package com.example.malute;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity {

    private LinearLayout navHome, navCatalog, navProjects, navProfile;
    private ImageView navHomeIcon, navCatalogIcon, navProjectsIcon, navProfileIcon;
    private TextView navHomeText, navCatalogText, navProjectsText, navProfileText;
    private LinearLayout projectsContainer;
    private Button addProjectButton;
    private List<Project> projects;
    private Dialog projectDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        initViews();
        initProjects();
        setupNavigation();
        setupAddProjectButton();
        displayProjects();
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
        projectsContainer = findViewById(R.id.projectsContainer);
        addProjectButton = findViewById(R.id.addProjectButton);
    }

    private void initProjects() {
        projects = new ArrayList<>();
        projects.add(new Project("Вязаный свитер", "Свитер ручной вязки из натуральной шерсти", 3500));
        projects.add(new Project("Шарф с узорами", "Теплый шарф с норвежскими узорами", 1500));
        projects.add(new Project("Плед для дома", "Уютный плед из акриловой пряжи", 2800));
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectsActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });

        navCatalog.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectsActivity.this, CatalogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });

        navProjects.setOnClickListener(v -> {
            Toast.makeText(this, "Вы уже в проектах", Toast.LENGTH_SHORT).show();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectsActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });
    }

    private void setupAddProjectButton() {
        addProjectButton.setOnClickListener(v -> showCreateProjectDialog());
    }

    private void showCreateProjectDialog() {
        projectDialog = new Dialog(this);
        projectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        projectDialog.setContentView(R.layout.create_project_dialog);
        if (projectDialog.getWindow() != null) {
            projectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            projectDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            projectDialog.getWindow().setGravity(Gravity.CENTER);
        }

        EditText titleInput = projectDialog.findViewById(R.id.projectTitleInput);
        EditText descriptionInput = projectDialog.findViewById(R.id.projectDescriptionInput);
        EditText priceInput = projectDialog.findViewById(R.id.projectPriceInput);
        Button saveButton = projectDialog.findViewById(R.id.saveProjectButton);
        Button cancelButton = projectDialog.findViewById(R.id.cancelProjectButton);

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String priceStr = priceInput.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }
            int price = Integer.parseInt(priceStr);
            projects.add(0, new Project(title, description, price));
            displayProjects();
            projectDialog.dismiss();
            Toast.makeText(this, "Проект создан", Toast.LENGTH_SHORT).show();
        });
        cancelButton.setOnClickListener(v -> projectDialog.dismiss());
        projectDialog.show();
    }

    private void displayProjects() {
        projectsContainer.removeAllViews();

        if (projects.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("У вас пока нет проектов\nНажмите + Создать проект");
            emptyText.setTextColor(Color.parseColor("#999999"));
            emptyText.setTextSize(14);
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setPadding(0, 100, 0, 0);
            projectsContainer.addView(emptyText);
            return;
        }

        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);

            View projectView = getLayoutInflater().inflate(R.layout.project_card_item, projectsContainer, false);

            TextView title = projectView.findViewById(R.id.projectTitle);
            TextView description = projectView.findViewById(R.id.projectDescription);
            TextView price = projectView.findViewById(R.id.projectPrice);
            Button editButton = projectView.findViewById(R.id.editProjectButton);
            Button deleteButton = projectView.findViewById(R.id.deleteProjectButton);

            if (title != null) title.setText(project.getTitle());
            if (description != null) description.setText(project.getDescription());
            if (price != null) price.setText(project.getPrice() + " ₽");

            final int position = i;
            if (editButton != null) {
                editButton.setOnClickListener(v -> showEditProjectDialog(position));
            }
            if (deleteButton != null) {
                deleteButton.setOnClickListener(v -> {
                    projects.remove(position);
                    displayProjects();
                    Toast.makeText(this, "Проект удален", Toast.LENGTH_SHORT).show();
                });
            }
            projectsContainer.addView(projectView);
        }
    }

    private void showEditProjectDialog(int position) {
        Project project = projects.get(position);
        projectDialog = new Dialog(this);
        projectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        projectDialog.setContentView(R.layout.create_project_dialog);
        if (projectDialog.getWindow() != null) {
            projectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            projectDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            projectDialog.getWindow().setGravity(Gravity.CENTER);
        }

        EditText titleInput = projectDialog.findViewById(R.id.projectTitleInput);
        EditText descriptionInput = projectDialog.findViewById(R.id.projectDescriptionInput);
        EditText priceInput = projectDialog.findViewById(R.id.projectPriceInput);
        Button saveButton = projectDialog.findViewById(R.id.saveProjectButton);
        Button cancelButton = projectDialog.findViewById(R.id.cancelProjectButton);

        if (titleInput != null) titleInput.setText(project.getTitle());
        if (descriptionInput != null) descriptionInput.setText(project.getDescription());
        if (priceInput != null) priceInput.setText(String.valueOf(project.getPrice()));

        if (saveButton != null) {
            saveButton.setText("Обновить");
            saveButton.setOnClickListener(v -> {
                String title = titleInput != null ? titleInput.getText().toString().trim() : "";
                String description = descriptionInput != null ? descriptionInput.getText().toString().trim() : "";
                String priceStr = priceInput != null ? priceInput.getText().toString().trim() : "";

                if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }
                int price = Integer.parseInt(priceStr);
                projects.set(position, new Project(title, description, price));
                displayProjects();
                projectDialog.dismiss();
                Toast.makeText(this, "Проект обновлен", Toast.LENGTH_SHORT).show();
            });
        }

        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> projectDialog.dismiss());
        }

        projectDialog.show();
    }

    private void updateNavigationState() {
        int activeColor = Color.parseColor("#1A6FEE");
        int inactiveColor = Color.parseColor("#999999");

        navHomeText.setTextColor(inactiveColor);
        navCatalogText.setTextColor(inactiveColor);
        navProjectsText.setTextColor(activeColor);
        navProfileText.setTextColor(inactiveColor);

        navHomeIcon.setImageResource(R.drawable.ic_home);
        navCatalogIcon.setImageResource(R.drawable.ic_catalog);
        navProjectsIcon.setImageResource(R.drawable.ic_projects_active);
        navProfileIcon.setImageResource(R.drawable.ic_profile);
    }

    private static class Project {
        private final String title;
        private final String description;
        private final int price;

        public Project(String title, String description, int price) {
            this.title = title;
            this.description = description;
            this.price = price;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getPrice() { return price; }
    }
}