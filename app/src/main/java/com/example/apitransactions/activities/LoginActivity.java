package com.example.apitransactions.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apitransactions.R;
import com.example.apitransactions.model.LoginRequest;
import com.example.apitransactions.model.LoginResponse;
import com.example.apitransactions.network.ApiClient;
import com.example.apitransactions.network.ApiService;
import com.example.apitransactions.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ApiService apiService;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefManager.init(getApplicationContext());

        if (SharedPrefManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, TransactionActivity.class);
            startActivity(intent);
            finish(); // Prevent going back to login
            return;
        }
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        apiService = ApiClient.getClient().create(ApiService.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("LoginDebug", "Before moving to TransactionActivity");
//                Intent intent = new Intent(LoginActivity.this, TransactionActivity.class);
//                System.out.println("navigating....");
//                Toast.makeText(LoginActivity.this, "Navigating...", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//                Log.d("LoginDebug", "Started TransactionActivity");
//                finish();
                loginUser();
            }
        });
    }

    private void loginUser() {

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d("LoginDebug", "Username: " + username + ", Password: " + password);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        Call<LoginResponse> call = apiService.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    Log.d("LoginDebug", "Token: " + token );

                    SharedPrefManager.saveToken(token);
                    runOnUiThread(() -> {
                                Intent intent = new Intent(LoginActivity.this, TransactionActivity.class);
                                startActivity(intent);
                                finish();
                            });
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                    goToTransactionActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    private void goToTransactionActivity() {
        Intent intent = new Intent(LoginActivity.this, TransactionActivity.class);
        startActivity(intent);
        finish();
    }
}