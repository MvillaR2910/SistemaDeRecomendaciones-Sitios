package org.example;
import com.google.maps.*;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PlaceType;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Scanner;

public class NearbyPlaces {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa un lugar (barrio, dirección, etc.): ");
        String userInput = scanner.nextLine();

        PlaceType TipoDeLugar = ObtenerTipoDeLugarPorUsuario(scanner);

        if (TipoDeLugar != null) {
            // Inicializa el cliente de la API de Google Maps
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(dotenv.get("GOOGLE_MAPS_API_KEY"))
                    .build();

            // Geocodifica el lugar ingresado para obtener latitud y longitud
            LatLng location = LugarGeocodificado(userInput, context);

            if (location != null) {
                int radius = 5000; // Define un radio de búsqueda

                // Realiza la búsqueda de lugares cercanos del tipo especificado
                try {
                    PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, location)
                            .radius(radius)
                            .type(TipoDeLugar)
                            .await();

                    // Imprime los resultados
                    for (PlacesSearchResult result : response.results) {
                        System.out.println(result.name + " - " + result.vicinity);
                    }

                    // Busca las páginas si hay más resultados
                    while (response.nextPageToken != null) {
                        Thread.sleep(2000); // Espera un tiempo para evitar errores de velocidad de solicitud
                        response = PlacesApi.nearbySearchNextPage(context, response.nextPageToken).await();

                        // Imprime los resultados de la siguiente página
                        for (PlacesSearchResult result : response.results) {
                            System.out.println(result.name + " - " + result.vicinity);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se pudo encontrar el lugar especificado.");
            }
        } else {
            System.out.println("Tipo de lugar no válido.");
        }
    }

    public static PlaceType ObtenerTipoDeLugarPorUsuario(Scanner scanner) {
        System.out.println("Elige el tipo de lugar que deseas buscar:");
        System.out.println("1. Restaurante");
        System.out.println("2. Hotel");
        System.out.println("3. Centro Comercial");
        System.out.println("4. Museo");
        System.out.println("5. Panadería");
        System.out.println("6. Banco");
        System.out.println("7. Bar");
        System.out.println("8. Salón de belleza");
        System.out.println("9. Farmacia");
        System.out.println("10. Hospital");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                return PlaceType.RESTAURANT;
            case 2:
                return PlaceType.LODGING;
            case 3:
                return PlaceType.SHOPPING_MALL;
            case 4:
                return PlaceType.MUSEUM;
            case 5:
                return PlaceType.BAKERY;
            case 6:
                return PlaceType.BANK;
            case 7:
                return PlaceType.BAR;
            case 8:
                return PlaceType.BEAUTY_SALON;
            case 9:
                return PlaceType.DRUGSTORE;
            case 10:
                return PlaceType.HOSPITAL;
            default:
                return null;
        }
    }

    // Método para geocodificar una dirección o lugar en coordenadas
    public static LatLng LugarGeocodificado(String placeName, GeoApiContext context) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, placeName).await();
            if (results.length > 0) {
                return results[0].geometry.location;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Devuelve null si no se encuentra el lugar o si ocurre un error
    }
}
