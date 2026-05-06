package com.example.lab18_dev;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel qui stocke et gère les données du compteur
 * Survie automatiquement aux rotations d'écran
 */
public class CounterStorageViewModel extends ViewModel {

    // MutableLiveData = modifiable depuis l'intérieur du ViewModel uniquement
    // Stocke la valeur actuelle du compteur
    private final MutableLiveData<Integer> counterValue = new MutableLiveData<>();

    /**
     * Constructeur - initialise le compteur à 0
     * Cette méthode n'est appelée qu'UNE SEULE fois durant toute la vie du ViewModel
     */
    public CounterStorageViewModel() {
        counterValue.setValue(0);  // Valeur initiale
    }

    /**
     * Incrémente le compteur de 1
     * setValue() doit être appelé depuis le thread principal
     */
    public void increaseCounter() {
        Integer currentValue = counterValue.getValue();
        if (currentValue != null) {
            counterValue.setValue(currentValue + 1);
        }
    }

    /**
     * Décrémente le compteur de 1
     */
    public void decreaseCounter() {
        Integer currentValue = counterValue.getValue();
        if (currentValue != null) {
            counterValue.setValue(currentValue - 1);
        }
    }

    /**
     * Remet le compteur à zéro
     */
    public void resetCounter() {
        counterValue.setValue(0);
    }

    /**
     * Getter public exposé à l'Activity
     * Retourne un LiveData (lecture seule) → bonne pratique MVVM
     * L'Activity ne peut que observer, pas modifier directement
     */
    public LiveData<Integer> getCurrentCounter() {
        return counterValue;
    }

    /**
     * BONUS: Méthode pour simuler un thread background
     * Utilise postValue() au lieu de setValue() pour les threads non-principaux
     */
    public void increaseFromBackgroundThread() {
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simule un chargement réseau
                Integer currentValue = counterValue.getValue();
                if (currentValue != null) {
                    // postValue() est thread-safe pour les backgrounds threads
                    counterValue.postValue(currentValue + 1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
