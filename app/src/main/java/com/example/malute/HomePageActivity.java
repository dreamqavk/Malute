package com.example.malute;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    private TextView searchText;
    private TextView categoryAll, categoryWomen, categoryMen;
    private View categoryUnderline;
    private LinearLayout navHome, navCatalog, navProjects, navProfile;
    private ImageView navHomeIcon, navCatalogIcon, navProjectsIcon, navProfileIcon;
    private TextView navHomeText, navCatalogText, navProjectsText, navProfileText;
    private ScrollView productsScroll;
    private HorizontalScrollView newsScroll;

    private Button addButton1, addButton2;
    private LinearLayout productCard1, productCard2;
    private LinearLayout newsCard1, newsCard2;

    private ArrayList<TextView> categoryList;
    private int currentCategory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        initViews();
        setupListeners();
        setupProductCards();
        setupNewsCards();
        updateUnderlinePosition();
    }

    private void initViews() {
        searchText = findViewById(R.id.searchText);
        categoryAll = findViewById(R.id.categoryAll);
        categoryWomen = findViewById(R.id.categoryWomen);
        categoryMen = findViewById(R.id.categoryMen);
        categoryUnderline = findViewById(R.id.categoryUnderline);

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

        productsScroll = findViewById(R.id.productsScroll);
        newsScroll = findViewById(R.id.newsScroll);

        addButton1 = findViewById(R.id.add_button);
        addButton2 = findViewById(R.id.add_button_2);

        productCard1 = findViewById(R.id.productCard1);
        productCard2 = findViewById(R.id.productCard2);

        newsCard1 = findViewById(R.id.newsCard1);
        newsCard2 = findViewById(R.id.newsCard2);

        categoryList = new ArrayList<>();
        categoryList.add(categoryAll);
        categoryList.add(categoryWomen);
        categoryList.add(categoryMen);
    }

    private void setupListeners() {
        searchText.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Поиск описаний", Toast.LENGTH_SHORT).show();
        });

        categoryAll.setOnClickListener(v -> {
            currentCategory = 0;
            selectCategory(categoryAll);
            filterProductsByCategory("all");
        });

        categoryWomen.setOnClickListener(v -> {
            currentCategory = 1;
            selectCategory(categoryWomen);
            filterProductsByCategory("women");
        });

        categoryMen.setOnClickListener(v -> {
            currentCategory = 2;
            selectCategory(categoryMen);
            filterProductsByCategory("men");
        });

        navHome.setOnClickListener(v -> {
            selectNavigationItem(0);
            productsScroll.smoothScrollTo(0, 0);
            newsScroll.smoothScrollTo(0, 0);
        });

        navCatalog.setOnClickListener(v -> {
            selectNavigationItem(1);
            Toast.makeText(HomePageActivity.this, "Открыть каталог", Toast.LENGTH_SHORT).show();
        });

        navProjects.setOnClickListener(v -> {
            selectNavigationItem(2);
            Toast.makeText(HomePageActivity.this, "Открыть проекты", Toast.LENGTH_SHORT).show();
        });

        navProfile.setOnClickListener(v -> {
            selectNavigationItem(3);
            Toast.makeText(HomePageActivity.this, "Открыть профиль", Toast.LENGTH_SHORT).show();
        });

        productCard1.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Рубашка Воскресенье", Toast.LENGTH_SHORT).show();
        });

        productCard2.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Рубашка Воскресенье", Toast.LENGTH_SHORT).show();
        });

        newsCard1.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Шорты Вторник - 4000 ₽", Toast.LENGTH_SHORT).show();
        });

        newsCard2.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Рубан Восклицкий - 8000 ₽", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupProductCards() {
        addButton1.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
            addButton1.setText("Добавлено");
            addButton1.setEnabled(false);
            addButton1.postDelayed(() -> {
                addButton1.setText("Добавить");
                addButton1.setEnabled(true);
            }, 2000);
        });

        addButton2.setOnClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
            addButton2.setText("Добавлено");
            addButton2.setEnabled(false);
            addButton2.postDelayed(() -> {
                addButton2.setText("Добавить");
                addButton2.setEnabled(true);
            }, 2000);
        });
    }

    private void setupNewsCards() {
        newsCard1.setOnLongClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Акция: Шорты Вторник", Toast.LENGTH_SHORT).show();
            return true;
        });

        newsCard2.setOnLongClickListener(v -> {
            Toast.makeText(HomePageActivity.this, "Акция: Рубан Восклицкий", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void selectCategory(TextView selected) {
        for (TextView category : categoryList) {
            if (category == selected) {
                category.setTextColor(Color.parseColor("#1A6FEE"));
            } else {
                category.setTextColor(Color.parseColor("#666666"));
            }
        }
        updateUnderlinePosition();
    }

    private void updateUnderlinePosition() {
        int underlineWidth = 60;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int categoryWidth = screenWidth / 3;

        int startPosition = currentCategory * categoryWidth + (categoryWidth / 2) - (underlineWidth / 2);

        categoryUnderline.animate()
                .translationX(startPosition)
                .setDuration(200)
                .start();
    }

    private void filterProductsByCategory(String category) {
        switch (category) {
            case "all":
                productCard1.setVisibility(View.VISIBLE);
                productCard2.setVisibility(View.VISIBLE);
                break;
            case "women":
                productCard1.setVisibility(View.GONE);
                productCard2.setVisibility(View.GONE);
                Toast.makeText(HomePageActivity.this, "Женские товары", Toast.LENGTH_SHORT).show();
                break;
            case "men":
                productCard1.setVisibility(View.VISIBLE);
                productCard2.setVisibility(View.VISIBLE);
                Toast.makeText(HomePageActivity.this, "Мужские товары", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void selectNavigationItem(int index) {
        int activeColor = Color.parseColor("#1A6FEE");
        int inactiveColor = Color.parseColor("#666666");

        TextView[] texts = {navHomeText, navCatalogText, navProjectsText, navProfileText};

        for (int i = 0; i < texts.length; i++) {
            if (texts[i] != null) {
                if (i == index) {
                    texts[i].setTextColor(activeColor);
                } else {
                    texts[i].setTextColor(inactiveColor);
                }
            }
        }

        if (index == 0) {
            navHomeIcon.setImageResource(R.drawable.ic_home_active);
        }
    }
}