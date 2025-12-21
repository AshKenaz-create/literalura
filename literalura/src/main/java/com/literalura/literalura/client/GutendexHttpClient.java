package com.literalura.literalura.client;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class GutendexHttpClient {

    private static final String BASE_URL = "https://gutendex.com/books/";
    private final HttpClient httpClient;

    public GutendexHttpClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String buscarPorTituloRawJson(String titulo) {
        try {
            String encoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String url = BASE_URL + "?search=" + encoded;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error HTTP " + response.statusCode() + " al consultar Gutendex.");
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error consultando Gutendex: " + e.getMessage(), e);
        }
    }
}
