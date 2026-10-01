//Corpo do json recebido pelas requisições
package com.tracen.tracenapi.model;

// Record que representa o corpo da requisição para criar ou atualizar um produto.
// Usado pelo @RequestBody nos métodos POST, PUT e PATCH do controller.
public record ProductRequest(
        String name,
        String description,
        Double price,
        Integer quantity,
        String category
) {}
