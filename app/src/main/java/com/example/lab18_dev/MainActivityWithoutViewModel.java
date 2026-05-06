package com.example.lab18_dev;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * VERSION SANS VIEWMODEL - DÉMONSTRATION DU PROBLÈME
 * Cette version montre pourquoi les variables classiques échouent lors des rotations d'écran
 * À UTILISER UNIQUEMENT POUR COMPARAISON
 */
public class MainActivityWithoutViewModel extends AppCompatActivity {

    // Problème: cette variable est une variable d'instance classique
    // Elle sera PERDUE à chaque rotation d'écran !
    private int counterValue = 0;
    
    private TextView counterDisplayText;
    private Button incrementButton;
    private Button decrementButton;
    private Button resetButton;
    
    // Clé pour sauvegarder dans Bundle
    private static final String COUNTER_SAVE_KEY = "saved_counter_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        counterDisplayText = findViewById(R.id.numberDisplayText);
        incrementButton = findViewById(R.id.incrementActionButton);
        decrementButton = findViewById(R.id.decrementActionButton);
        resetButton = findViewById(R.id.resetActionButton);
        
        // RESTAURATION MANUELLE (ancienne méthode limitée)
        // Fonctionne uniquement pour les types primitifs
        if (savedInstanceState != null) {
            counterValue = savedInstanceState.getInt(COUNTER_SAVE_KEY, 0);
            Toast.makeText(this, "Restauration depuis Bundle: " + counterValue, Toast.LENGTH_SHORT).show();
        }
        
        updateDisplay();
        
        incrementButton.setOnClickListener(v -> {
            counterValue++;
            updateDisplay();
        });
        
        decrementButton.setOnClickListener(v -> {
            counterValue--;
            updateDisplay();
        });
        
        resetButton.setOnClickListener(v -> {
            counterValue = 0;
            updateDisplay();
        });
    }
    
    private void updateDisplay() {
        counterDisplayText.setText(String.valueOf(counterValue));
    }
    
    /**
     * SAVE INSTANCE STATE - Méthode obligatoire pour sauvegarder l'état
     * LIMITATIONS:
     * - Ne fonctionne que pour les types primitifs ou Parcelable/Serializable
     * - Impossible de sauvegarder des LiveData, des threads, des objets complexes
     * - Code devient rapidement illisible avec beaucoup de données
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNTER_SAVE_KEY, counterValue);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("MainActivityWithoutVM", "onDestroy - La variable counterValue va être perdue si rotation !");
    }
}
