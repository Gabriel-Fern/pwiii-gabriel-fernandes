package com.tracen.tracenapi.model;

// Record simples para encapsular mensagens de resposta da API.
// Usado em respostas de sucesso, erros e confirmações de operações.
public record ApiResponse(String message, Object data) {

    // Construtor de conveniência para respostas que contêm apenas uma mensagem (sem dados).
    public ApiResponse(String message) {
        this(message, null);
    }
}
