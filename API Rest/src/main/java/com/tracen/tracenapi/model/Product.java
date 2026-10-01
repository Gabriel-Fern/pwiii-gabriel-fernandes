//Modelo do produto(campos e etc) 
package com.tracen.tracenapi.model;

import java.time.Instant;

// Record é uma classe imutável do Java 16+.
// Equivale ao nosso "produto" do Node.js — guarda todos os campos de um item da loja.
// O compilador gera automaticamente: construtor, getters, equals, hashCode e toString.
public record Product(
        long id,
        String name,
        String description,
        double price,
        int quantity,
        String category,
        String createdAt,
        String updatedAt
) {

    // Construtor auxiliar para criar um novo produto sem informar as datas manualmente.
    // As datas createdAt e updatedAt são preenchidas automaticamente com o momento atual.
    public static Product create(long id, String name, String description,
                                  double price, int quantity, String category) {
        String now = Instant.now().toString();
        return new Product(id, name, description, price, quantity, category, now, now);
    }

    // Retorna uma cópia do produto com os campos fornecidos atualizados (atualização completa - PUT).
    // updatedAt é sempre renovado para refletir a hora da modificação.
    public Product withUpdates(String name, String description,
                                double price, int quantity, String category) {
        return new Product(this.id, name, description, price, quantity, category,
                this.createdAt, Instant.now().toString());
    }
}
