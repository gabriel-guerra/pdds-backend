package com.pdds.dto;

import com.pdds.domain.Product;
import com.pdds.domain.enums.SelectedProductOperation;

public record SelectedProductResponseDTO(
        long id,
        SelectedProductOperation operation,
        int quantity,
        double total,
        Product product
){
}
