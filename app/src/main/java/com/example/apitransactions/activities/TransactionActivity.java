package com.example.apitransactions.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apitransactions.R;
import com.example.apitransactions.adapters.TransactionAdapter;
import com.example.apitransactions.model.Transaction;
import com.example.apitransactions.network.ApiClient;
import com.example.apitransactions.network.ApiService;
import com.example.apitransactions.utils.SharedPrefManager;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        // Redirect to login if not logged in
        if (SharedPrefManager.getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // API client
        apiService = ApiClient.getClient().create(ApiService.class);

        // Logout button
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            SharedPrefManager.clearToken();
            startActivity(new Intent(TransactionActivity.this, LoginActivity.class));
            finish();
        });

        // Start biometric prompt
        showBiometricPrompt();
    }


    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        fetchTransactions();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(TransactionActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(TransactionActivity.this, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
                        finish(); // exit activity
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Authenticate to view your transactions")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void fetchTransactions() {
        String token = SharedPrefManager.getToken();

        System.out.println("Navigating    "+ token);
        apiService.getTransactions( token).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    List<Transaction> transactions = response.body();
                    System.out.println("textTitle   "+ transactions);
                    if (transactions != null) {
                        adapter = new TransactionAdapter(transactions);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(TransactionActivity.this, "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TransactionActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Error body: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Toast.makeText(TransactionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}