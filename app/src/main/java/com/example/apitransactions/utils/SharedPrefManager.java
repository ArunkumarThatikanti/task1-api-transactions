package com.example.apitransactions.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPrefManager {
    private static final String PREF_NAME = "secure_prefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";


    private static SharedPreferences encryptedPrefs;

    // Initialize the EncryptedSharedPreferences
    public static void init(Context context) {
        if (encryptedPrefs == null) {
            try {
                MasterKey masterKey = new MasterKey.Builder(context)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build();

                encryptedPrefs = EncryptedSharedPreferences.create(
                        context,
                        PREF_NAME,
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );

            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save token
    public static void saveToken(String token) {
        if (encryptedPrefs != null) {
            encryptedPrefs.edit().putString(KEY_AUTH_TOKEN, token).apply();
        }
    }

    // Get token
    public static String getToken() {
        if (encryptedPrefs != null) {
            return encryptedPrefs.getString(KEY_AUTH_TOKEN, null);
        }
        return null;
    }

    // Clear token (e.g., on logout)
    public static void clearToken() {
        if (encryptedPrefs != null) {
            encryptedPrefs.edit().remove(KEY_AUTH_TOKEN).apply();
        }
    }

    // Check if token exists
    public static boolean isLoggedIn() {
        return getToken() != null;
    }
}
