package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
        Integer id,
        String title,
        List<AuthorDTO> authors,
        List<String> languages,
        @JsonProperty("download_count") Integer downloadCount
) {}
