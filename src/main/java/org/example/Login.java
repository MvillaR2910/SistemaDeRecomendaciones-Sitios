package org.example;
import java.io.Console;

public class Login {
    private static final String username = "admin";
    private static final String password = "admin";
    private static final int n_intentos = 3;

    public static boolean loginSuccessful() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Asegúrate de estar usando la consola");
            return false;
        }
        int intentos = 0;
        while (intentos < n_intentos) {
            String userInput = console.readLine("Usuario: ");
            char[] passwordInput = console.readPassword("Contraseña: ");

            if (username.equals(userInput) && password.equals(new String(passwordInput))) {
                System.out.println("Bienvenido al sistema");
                return true;
            } else {
                System.out.println("Usuario o contraseña incorrectos. Intentos restantes: " + (n_intentos - intentos - 1));
                intentos++;
                if (intentos < n_intentos) {
                    console.readLine("Presiona Enter para continuar...");
                }
            }
        }
        System.out.println("Número de intentos excedidos");
        return false;
    }
}
