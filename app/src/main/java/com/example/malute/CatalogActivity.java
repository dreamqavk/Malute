package com.example.malute;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.malute.api.ApiClient;
import com.example.malute.api.AuthManager;
import com.example.malute.api.ProductApiMapper;
import com.example.malute.entity.CatalogProduct;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity {

    private LinearLayout navHome, navCatalog, navProjects, navProfile;
    private ImageView navHomeIcon, navCatalogIcon, navProjectsIcon, navProfileIcon;
    private TextView navHomeText, navCatalogText, navProjectsText, navProfileText;
    private TextView categoryAll, categoryWomen, categoryMen;
    private LinearLayout productCard1, productCard2;
    private Button addButton1, addButton2;
    private TextView product1Title, product1Category, product1Price;
    private TextView product2Title, product2Category, product2Price;
    private ScrollView productsScroll;
    private List<CatalogProduct> allProducts;
    private List<CatalogProduct> filteredProducts;
    private String currentCategory = "all";
    private Dialog productDialog;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog);

        authManager = new AuthManager(this);

        initViews();
        initProducts();
        setupCategories();
        setupProductCards();
        setupNavigation();
        loadProductsFromApi();
        updateNavigationState(1);
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
        categoryAll = findViewById(R.id.categoryAll);
        categoryWomen = findViewById(R.id.categoryWomen);
        categoryMen = findViewById(R.id.categoryMen);
        productCard1 = findViewById(R.id.productCard1);
        productCard2 = findViewById(R.id.productCard2);
        addButton1 = findViewById(R.id.add_button);
        addButton2 = findViewById(R.id.add_button_2);
        product1Title = findViewById(R.id.product1Title);
        product1Category = findViewById(R.id.product1Category);
        product1Price = findViewById(R.id.product1Price);
        product2Title = findViewById(R.id.product2Title);
        product2Category = findViewById(R.id.product2Category);
        product2Price = findViewById(R.id.product2Price);
        productsScroll = findViewById(R.id.productsScroll);

        allProducts = new ArrayList<>();
        filteredProducts = new ArrayList<>();
    }

    private void initProducts() {
        allProducts.clear();
        filteredProducts.clear();
    }

    private void loadProductsFromApi() {
        String token = authManager.getToken();
        if (token == null) return;

        ApiClient.getApiService().getProducts("Bearer " + token, 50, "-created").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allProducts.clear();
                    allProducts.addAll(ProductApiMapper.parseProducts(response.body()));
                    filteredProducts.clear();
                    filteredProducts.addAll(allProducts);
                    filterProducts();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(CatalogActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategories() {
        categoryAll.setOnClickListener(v -> { currentCategory = "all"; updateCategoryColor(categoryAll); filterProducts(); });
        categoryWomen.setOnClickListener(v -> { currentCategory = "women"; updateCategoryColor(categoryWomen); filterProducts(); });
        categoryMen.setOnClickListener(v -> { currentCategory = "men"; updateCategoryColor(categoryMen); filterProducts(); });
    }

    private void updateCategoryColor(TextView selected) {
        categoryAll.setTextColor(Color.parseColor("#666666"));
        categoryWomen.setTextColor(Color.parseColor("#666666"));
        categoryMen.setTextColor(Color.parseColor("#666666"));
        selected.setTextColor(Color.parseColor("#1A6FEE"));
    }

    private void filterProducts() {
        filteredProducts.clear();
        for (CatalogProduct product : allProducts) {
            if (currentCategory.equals("all")) {
                filteredProducts.add(product);
            } else if (currentCategory.equals("women") && product.getCategory().toLowerCase().contains("жен")) {
                filteredProducts.add(product);
            } else if (currentCategory.equals("men") && product.getCategory().toLowerCase().contains("муж")) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.size() >= 1) {
            updateProductCard(productCard1, addButton1, product1Title, product1Category, product1Price, filteredProducts.get(0));
            productCard1.setVisibility(View.VISIBLE);
        } else {
            productCard1.setVisibility(View.GONE);
        }
        if (filteredProducts.size() >= 2) {
            updateProductCard(productCard2, addButton2, product2Title, product2Category, product2Price, filteredProducts.get(1));
            productCard2.setVisibility(View.VISIBLE);
        } else {
            productCard2.setVisibility(View.GONE);
        }
    }

    private void updateProductCard(LinearLayout card, Button button, TextView title, TextView category, TextView price, CatalogProduct product) {
        title.setText(product.getTitle());
        category.setText(product.getCategory());
        price.setText(product.getPriceText());

        card.setOnClickListener(v -> showProductDialog(product));

        button.setOnClickListener(v -> {
            Toast.makeText(CatalogActivity.this, product.getTitle() + " добавлен в корзину", Toast.LENGTH_SHORT).show();
            addToBasket(product);
        });
    }

    private void addToBasket(CatalogProduct product) {
        String token = authManager.getToken();
        if (token == null) return;

        JsonObject body = new JsonObject();
        body.addProperty("product_title", product.getTitle());
        body.addProperty("quantity", 1);
        body.addProperty("user_id", authManager.getUserId());

        ApiClient.getApiService().createBasket("Bearer " + token, body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }

    private void setupProductCards() {}

    private void showProductDialog(CatalogProduct product) {
        productDialog = new Dialog(this);
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDialog.setContentView(R.layout.product_dialog);
        if (productDialog.getWindow() != null) {
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            productDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            productDialog.getWindow().setGravity(Gravity.CENTER);
        }

        TextView title = productDialog.findViewById(R.id.dialogTitle);
        TextView category = productDialog.findViewById(R.id.dialogCategory);
        TextView description = productDialog.findViewById(R.id.dialogDescription);
        TextView characteristics = productDialog.findViewById(R.id.dialogCharacteristics);
        TextView price = productDialog.findViewById(R.id.dialogPrice);
        Button addButton = productDialog.findViewById(R.id.dialogAddButton);
        Button closeButton = productDialog.findViewById(R.id.dialogCloseButton);

        title.setText(product.getTitle());
        category.setText(product.getCategory());
        description.setText("Описание:\n" + product.getDescription());
        characteristics.setText("Характеристики:\nРасход: " + product.getConsumption());
        price.setText(product.getPriceText());

        addButton.setOnClickListener(v -> {
            Toast.makeText(CatalogActivity.this, product.getTitle() + " добавлен в корзину", Toast.LENGTH_SHORT).show();
            addToBasket(product);
            productDialog.dismiss();
        });
        closeButton.setOnClickListener(v -> productDialog.dismiss());
        productDialog.show();
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(CatalogActivity.this, HomePageActivity.class));
            finish();
        });
        navCatalog.setOnClickListener(v -> {});
        navProjects.setOnClickListener(v -> {
            startActivity(new Intent(CatalogActivity.this, ProjectsActivity.class));
            finish();
        });
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(CatalogActivity.this, ProfileActivity.class));
            finish();
        });
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