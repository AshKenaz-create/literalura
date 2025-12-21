package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponseDTO(
        Integer count,
        String next,
        String previous,
        List<BookDTO> results
) {}
