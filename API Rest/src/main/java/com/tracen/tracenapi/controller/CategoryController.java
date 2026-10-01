//Define as rodas GET
package com.tracen.tracenapi.controller;

import com.tracen.tracenapi.model.ApiResponse;
import com.tracen.tracenapi.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller responsável pelas rotas de /category.
// Expõe as categorias distintas dos produtos cadastrados na loja.
@RestController
@RequestMapping("/category")
public class CategoryController {

    // Injeção de dependência: reutiliza o mesmo ProductService do ProductController.
    // O Spring garante que é a mesma instância (Singleton por padrão).
    private final ProductService service;

    public CategoryController(ProductService service) {
        this.service = service;
    }

    // GET /category — lista todas as categorias únicas em ordem alfabética.
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        List<String> categories = service.getDistinctCategories();
        return ResponseEntity.ok(new ApiResponse(
                "Categorias disponiveis na Loja da Tracen Academy!", categories));
    }
}
