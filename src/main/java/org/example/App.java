package org.example;

public class App {
    public static void main(String[] args) {
        if (Login.loginSuccessful()) {
            NearbyPlaces.main(null);
        } else {
            System.out.println("Inicio de sesión fallido. Saliendo del programa.");
        }
    }
}
