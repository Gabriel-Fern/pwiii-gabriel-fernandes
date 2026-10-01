//Rota raiz GET
package com.tracen.tracenapi.controller;

import com.tracen.tracenapi.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Controller da rota raiz "/" — funciona como página inicial da API.
// Retorna informações básicas sobre a Loja da Tracen Academy.
@RestController
@RequestMapping("/")
public class HomeController {

    // GET / — retorna a mensagem de boas-vindas e a lista de endpoints disponíveis.
    @GetMapping
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
                "message", "Projeto API de umamusume funcionando do jeito que deveria",
                "tagline", "Correr e uma arte, e cada produto conta uma historia!",
                "version", "2.0.0",
                "endpoints", Map.of(
                        "products", "/product",
                        "categories", "/category"
                ),
                "dica", "Use GET /product?search=Special+Week para buscar sua Uma Musume favorita!"
        ));
    }
}
