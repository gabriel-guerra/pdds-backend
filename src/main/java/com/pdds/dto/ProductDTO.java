package com.pdds.dto;

import java.util.List;

public record ProductDTO(
        String name,
        double price,
        int stock,
        List<String> images
) {
}
