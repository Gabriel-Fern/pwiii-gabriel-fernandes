package com.tracen.tracenapi.controller;

import com.tracen.tracenapi.model.ApiResponse;
import com.tracen.tracenapi.model.Product;
import com.tracen.tracenapi.model.ProductRequest;
import com.tracen.tracenapi.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// @RestController combina @Controller + @ResponseBody:
// todos os métodos retornam JSON automaticamente, sem precisar de @ResponseBody em cada um.
// Equivale ao "router" do Express no productRoutes.js.
@RestController
// @RequestMapping define o prefixo de todas as rotas deste controller: /product
@RequestMapping("/product")
public class ProductController {

    // Injeção de dependência via construtor — forma recomendada no Spring.
    // O Spring injeta automaticamente o ProductService criado como @Service.
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // ─── GET /product ──────────────────────────────────────────────────────────

    // Lista todos os produtos com filtros opcionais de categoria e busca por nome.
    // @RequestParam(required = false) aceita parâmetros de query opcionais: ?category=Figuras&search=Tokai
    @GetMapping
    public ResponseEntity<List<Product>> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(service.getAll(category, search));
    }

    // ─── GET /product/stats ────────────────────────────────────────────────────

    // Retorna estatísticas gerais do estoque da loja (total, valor, mais caro, etc.).
    // IMPORTANTE: este método deve ficar ANTES de /:id para o Spring não interpretar
    // "stats" como um ID numérico.
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(service.getStats());
    }

    // ─── GET /product/{id} ─────────────────────────────────────────────────────

    // Busca um produto pelo ID.
    // @PathVariable captura o {id} da URL e o converte para long.
    // ResponseEntity permite controlar o status HTTP da resposta (200, 404, etc.).
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        // map() executa a função se o Optional tiver valor; orElseGet() trata o caso vazio.
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Produto nao encontrado — talvez esteja treinando na pista?")));
    }

    // ─── POST /product ─────────────────────────────────────────────────────────

    // Cria um novo produto a partir do corpo JSON da requisição.
    // @RequestBody desserializa o JSON recebido para um ProductRequest.
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest req) {
        // Validação dos campos obrigatórios
        String err = validateFull(req);
        if (err != null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Dados invalidos — o Trainer nao aprovaria isso! " + err));
        }
        // Verifica duplicidade de nome antes de criar
        if (service.existsByName(req.name(), -1)) {
            return ResponseEntity.status(409)
                    .body(new ApiResponse("Ja existe um produto com esse nome na Loja da Tracen Academy!"));
        }
        Product created = service.create(req);
        // HTTP 201 Created: indica que um novo recurso foi criado com sucesso
        return ResponseEntity.status(201)
                .body(new ApiResponse("Novo item adicionado a Loja da Tracen Academy!", created));
    }

    // ─── PUT /product/{id} ─────────────────────────────────────────────────────

    // Substitui completamente um produto existente (atualização total).
    // Todos os campos são obrigatórios no corpo da requisição.
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody ProductRequest req) {
        String err = validateFull(req);
        if (err != null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Dados invalidos — o Trainer nao aprovaria isso! " + err));
        }
        if (service.existsByName(req.name(), id)) {
            return ResponseEntity.status(409)
                    .body(new ApiResponse("Ja existe um produto com esse nome na Loja da Tracen Academy!"));
        }
        return service.update(id, req)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(
                        new ApiResponse("Produto atualizado com sucesso! Vamos vencer o Triple Crown!", p)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Produto nao encontrado — talvez esteja treinando na pista?")));
    }

    // ─── PATCH /product/{id} ───────────────────────────────────────────────────

    // Atualiza apenas os campos enviados no corpo (atualização parcial).
    // Campos ausentes mantêm o valor atual do produto.
    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable long id, @RequestBody ProductRequest req) {
        // Valida apenas os campos que foram enviados (não são nulos)
        String err = validatePartial(req);
        if (err != null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Dados invalidos — o Trainer nao aprovaria isso! " + err));
        }
        if (req.name() != null && service.existsByName(req.name(), id)) {
            return ResponseEntity.status(409)
                    .body(new ApiResponse("Ja existe um produto com esse nome na Loja da Tracen Academy!"));
        }
        return service.patch(id, req)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(
                        new ApiResponse("Produto atualizado parcialmente! Ganbatte!", p)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Produto nao encontrado — talvez esteja treinando na pista?")));
    }

    // ─── DELETE /product/{id} ──────────────────────────────────────────────────

    // Remove um produto pelo ID.
    // Retorna 200 com mensagem temática em caso de sucesso ou 404 se não encontrado.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (service.remove(id)) {
            return ResponseEntity.ok(new ApiResponse("Produto removido da Loja da Tracen Academy. Sayonara!"));
        }
        return ResponseEntity.status(404)
                .body(new ApiResponse("Produto nao encontrado — talvez esteja treinando na pista?"));
    }

    // ─── HELPERS DE VALIDAÇÃO ──────────────────────────────────────────────────

    // Valida todos os campos obrigatórios — usado no POST e PUT.
    // Retorna uma mensagem de erro descritiva ou null se tudo estiver correto.
    private String validateFull(ProductRequest req) {
        if (req.name() == null || req.name().isBlank()) return "(campo: name)";
        if (req.price() == null || req.price() < 0) return "(campo: price)";
        if (req.quantity() == null || req.quantity() < 0) return "(campo: quantity)";
        if (req.category() == null || req.category().isBlank()) return "(campo: category)";
        return null;
    }

    // Valida apenas os campos presentes no PATCH — campos nulos são ignorados.
    private String validatePartial(ProductRequest req) {
        if (req.name() != null && req.name().isBlank()) return "(campo: name nao pode ser vazio)";
        if (req.price() != null && req.price() < 0) return "(campo: price nao pode ser negativo)";
        if (req.quantity() != null && req.quantity() < 0) return "(campo: quantity nao pode ser negativo)";
        if (req.category() != null && req.category().isBlank()) return "(campo: category nao pode ser vazio)";
        return null;
    }
}
