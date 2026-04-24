package com.example.malute;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.malute.api.AuthManager;
import com.example.malute.entity.CatalogProduct;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private EditText searchText;
    private TextView categoryAll, categoryWomen, categoryMen;
    private View underlineAll, underlineWomen, underlineMen;
    private LinearLayout navHome, navCatalog, navProjects, navProfile;
    private ImageView navHomeIcon, navCatalogIcon, navProjectsIcon, navProfileIcon;
    private TextView navHomeText, navCatalogText, navProjectsText, navProfileText;
    private ScrollView productsScroll;
    private LinearLayout productsContainer;
    private LinearLayout newsCard1, newsCard2;

    private int currentCategory = 0;
    private List<CatalogProduct> allProducts;
    private List<CatalogProduct> filteredProducts;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        authManager = new AuthManager(this);

        if (!authManager.isLoggedIn()) {
            Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        initViews();
        initMockProducts();
        setupListeners();
        setupSearch();
        filterProductsByCategory();
        updateNavigationState(0);
        setupNewsCards();
    }

    private void initViews() {
        searchText = findViewById(R.id.searchText);
        categoryAll = findViewById(R.id.categoryAll);
        categoryWomen = findViewById(R.id.categoryWomen);
        categoryMen = findViewById(R.id.categoryMen);
        underlineAll = findViewById(R.id.underlineAll);
        underlineWomen = findViewById(R.id.underlineWomen);
        underlineMen = findViewById(R.id.underlineMen);

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
        productsContainer = findViewById(R.id.productsContainer);

        newsCard1 = findViewById(R.id.newsCard1);
        newsCard2 = findViewById(R.id.newsCard2);

        allProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();
    }

    private void initMockProducts() {
        allProducts.add(new CatalogProduct("1", "Рубашка Воскресенье", "Мужская одежда", "", "300 ₽", 300, "Рубашка для машинного вязания", "Хлопок 100%"));
        allProducts.add(new CatalogProduct("2", "Платье Летнее", "Женская одежда", "", "850 ₽", 850, "Легкое летнее платье", "Вискоза 95%"));
        allProducts.add(new CatalogProduct("3", "Брюки Классические", "Мужская одежда", "", "1200 ₽", 1200, "Классические брюки", "Полиэстер 65%"));
        allProducts.add(new CatalogProduct("4", "Юбка Миди", "Женская одежда", "", "650 ₽", 650, "Элегантная юбка", "Хлопок 100%"));
        allProducts.add(new CatalogProduct("5", "Свитер Уютный", "Мужская одежда", "", "950 ₽", 950, "Теплый свитер", "Шерсть 70%"));
        allProducts.add(new CatalogProduct("6", "Блуза Шелковая", "Женская одежда", "", "1100 ₽", 1100, "Изысканная блуза", "Полиэстер 100%"));
        allProducts.add(new CatalogProduct("7", "Джинсы Слим", "Мужская одежда", "", "1800 ₽", 1800, "Современные джинсы", "Хлопок 98%"));
        allProducts.add(new CatalogProduct("8", "Пальто Осеннее", "Женская одежда", "", "2500 ₽", 2500, "Стильное пальто", "Шерсть 60%"));
        allProducts.add(new CatalogProduct("9", "Шорты Спортивные", "Мужская одежда", "", "700 ₽", 700, "Спортивные шорты", "Полиэстер 100%"));
        allProducts.add(new CatalogProduct("10", "Топ Летний", "Женская одежда", "", "450 ₽", 450, "Легкий топ", "Хлопок 100%"));

        filteredProducts.addAll(allProducts);
    }

    private void setupSearch() {
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBySearch(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterBySearch(String query) {
        filteredProducts.clear();
        if (query.isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            for (CatalogProduct product : allProducts) {
                if (product.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        product.getCategory().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }
        filterProductsByCategory();
        if (filteredProducts.isEmpty()) {
            Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        categoryAll.setOnClickListener(v -> {
            currentCategory = 0;
            selectCategory(categoryAll, underlineAll, underlineWomen, underlineMen);
        });
        categoryWomen.setOnClickListener(v -> {
            currentCategory = 1;
            selectCategory(categoryWomen, underlineWomen, underlineAll, underlineMen);
        });
        categoryMen.setOnClickListener(v -> {
            currentCategory = 2;
            selectCategory(categoryMen, underlineMen, underlineAll, underlineWomen);
        });

        navHome.setOnClickListener(v -> updateNavigationState(0));
        navCatalog.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, CatalogActivity.class));
            finish();
        });
        navProjects.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, ProjectsActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, ProfileActivity.class));
            finish();
        });
    }

    private void setupNewsCards() {
        newsCard1.setOnClickListener(v -> {
            Toast.makeText(this, "Шорты Вторник - 4000 ₽", Toast.LENGTH_SHORT).show();
        });
        newsCard2.setOnClickListener(v -> {
            Toast.makeText(this, "Рубан Восклицкий - 8000 ₽", Toast.LENGTH_SHORT).show();
        });
    }

    private void selectCategory(TextView selectedCategory, View activeUnderline, View... inactiveUnderlines) {
        categoryAll.setTextColor(Color.parseColor("#666666"));
        categoryWomen.setTextColor(Color.parseColor("#666666"));
        categoryMen.setTextColor(Color.parseColor("#666666"));
        selectedCategory.setTextColor(Color.parseColor("#1A6FEE"));

        activeUnderline.setVisibility(View.VISIBLE);
        for (View underline : inactiveUnderlines) {
            underline.setVisibility(View.GONE);
        }
        filterProductsByCategory();
    }

    private void filterProductsByCategory() {
        List<CatalogProduct> displayProducts = new ArrayList<>();
        for (CatalogProduct product : filteredProducts) {
            if (currentCategory == 0) {
                displayProducts.add(product);
            } else if (currentCategory == 1 && product.getCategory().toLowerCase().contains("жен")) {
                displayProducts.add(product);
            } else if (currentCategory == 2 && product.getCategory().toLowerCase().contains("муж")) {
                displayProducts.add(product);
            }
        }
        updateProductsContainer(displayProducts);
    }

    private void updateProductsContainer(List<CatalogProduct> products) {
        productsContainer.removeAllViews();

        if (products.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("Нет товаров в этой категории");
            emptyText.setTextColor(Color.parseColor("#999999"));
            emptyText.setTextSize(14);
            emptyText.setPadding(0, 40, 0, 0);
            emptyText.setGravity(android.view.Gravity.CENTER);
            productsContainer.addView(emptyText);
            return;
        }

        for (CatalogProduct product : products) {
            View productView = getLayoutInflater().inflate(R.layout.product_card_item, productsContainer, false);

            TextView title = productView.findViewById(R.id.productTitle);
            TextView category = productView.findViewById(R.id.productCategory);
            TextView price = productView.findViewById(R.id.productPrice);
            Button addButton = productView.findViewById(R.id.addButton);

            title.setText(product.getTitle());
            category.setText(product.getCategory());
            price.setText(product.getPriceText());

            addButton.setOnClickListener(v -> {
                Toast.makeText(HomePageActivity.this, product.getTitle() + " добавлен в корзину", Toast.LENGTH_SHORT).show();
            });

            productView.setOnClickListener(v -> {
                Toast.makeText(HomePageActivity.this, product.getTitle(), Toast.LENGTH_SHORT).show();
            });

            productsContainer.addView(productView);
        }
    }

    private void updateNavigationState(int selectedIndex) {
        int activeColor = Color.parseColor("#1A6FEE");
        int inactiveColor = Color.parseColor("#999999");

        navHomeText.setTextColor(selectedIndex == 0 ? activeColor : inactiveColor);
        navCatalogText.setTextColor(selectedIndex == 1 ? activeColor : inactiveColor);
        navProjectsText.setTextColor(selectedIndex == 2 ? activeColor : inactiveColor);
        navProfileText.setTextColor(selectedIndex == 3 ? activeColor : inactiveColor);

        navHomeIcon.setImageResource(selectedIndex == 0 ? R.drawable.ic_home_active : R.drawable.ic_home);
        navCatalogIcon.setImageResource(selectedIndex == 1 ? R.drawable.ic_catalog_active : R.drawable.ic_catalog);
        navProjectsIcon.setImageResource(selectedIndex == 2 ? R.drawable.ic_projects_active : R.drawable.ic_projects);
        navProfileIcon.setImageResource(selectedIndex == 3 ? R.drawable.ic_profile_active : R.drawable.ic_profile);
    }
}