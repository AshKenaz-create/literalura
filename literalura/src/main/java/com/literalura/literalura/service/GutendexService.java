package com.literalura.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.literalura.client.GutendexHttpClient;
import com.literalura.literalura.dto.GutendexResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class GutendexService {

    private final GutendexHttpClient client;
    private final ObjectMapper mapper;

    public GutendexService(ObjectMapper mapper) {
        this.client = new GutendexHttpClient();
        this.mapper = mapper;
    }

    public GutendexResponseDTO buscarLibrosPorTitulo(String titulo) {
        try {
            String json = client.buscarPorTituloRawJson(titulo);
            return mapper.readValue(json, GutendexResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo convertir JSON → DTO: " + e.getMessage(), e);
        }
    }
}
