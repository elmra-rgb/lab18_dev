package com.example.lab18_dev;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

public class MainActivity extends AppCompatActivity {

    // Composants UI
    private TextView counterDisplayText;
    private Button incrementButton;
    private Button decrementButton;
    private Button resetButton;
    
    // ViewModel - contient les données qui survivent à la rotation
    private CounterStorageViewModel counterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        counterDisplayText = findViewById(R.id.numberDisplayText);
        incrementButton = findViewById(R.id.incrementActionButton);
        decrementButton = findViewById(R.id.decrementActionButton);
        resetButton = findViewById(R.id.resetActionButton);

        // ÉTAPE 1: Récupération du ViewModel associé à cette Activity
        // ViewModelProvider s'occupe de créer ou retrouver l'instance existante
        // Le ViewModel survit même si l'Activity est détruite (rotation)
        counterViewModel = new ViewModelProvider(this).get(CounterStorageViewModel.class);

        // ÉTAPE 2: Configuration de l'observation du LiveData
        // L'Observer est automatiquement notifié quand la valeur change
        // Mais uniquement si l'Activity est en état STARTED ou RESUMED (lifecycle-aware)
        counterViewModel.getCurrentCounter().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer newCounterValue) {
                // Mise à jour automatique de l'UI quand la valeur change
                if (newCounterValue != null) {
                    counterDisplayText.setText(String.valueOf(newCounterValue));
                    
                    // Petit effet visuel pour montrer que l'UI se met à jour
                    counterDisplayText.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            counterDisplayText.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();
                        })
                        .start();
                }
            }
        });

        // ÉTAPE 3: Configuration des listeners des boutons
        // Le ViewModel gère la logique métier, l'Activity gère uniquement l'UI
        incrementButton.setOnClickListener(v -> {
            counterViewModel.increaseCounter();
            showToastMessage("Incrémentation +1");
        });
        
        decrementButton.setOnClickListener(v -> {
            counterViewModel.decreaseCounter();
            showToastMessage("Décrémentation -1");
        });
        
        resetButton.setOnClickListener(v -> {
            counterViewModel.resetCounter();
            showToastMessage("Compteur réinitialisé à 0");
        });

        // Affichage d'un message de debug dans le log
        android.util.Log.d("MainActivity", "onCreate exécuté - Version avec ViewModel");
    }

    /**
     * Affiche un message temporaire pour confirmer l'action
     */
    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Méthode appelée uniquement pour démonstration
     * Montre que l'Activity peut être détruite mais pas le ViewModel
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("MainActivity", "onDestroy appelé - L'Activity est détruite mais le ViewModel reste en mémoire");
    }
}
