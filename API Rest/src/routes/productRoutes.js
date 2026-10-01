const express = require("express");
const router = express.Router();
const service = require("../services/productService");


function validateProduct(data, requireAll = true) {
    const errors = [];

    if (requireAll || data.name !== undefined) {
        if (!data.name || typeof data.name !== "string" || data.name.trim() === "") {
            errors.push("name e obrigatorio e deve ser uma string nao vazia");
        }
    }

    if (requireAll || data.price !== undefined) {
        if (data.price === undefined || typeof data.price !== "number" || data.price < 0) {
            errors.push("price e obrigatorio e deve ser um numero nao negativo");
        }
    }

    if (requireAll || data.quantity !== undefined) {
        if (data.quantity === undefined || !Number.isInteger(data.quantity) || data.quantity < 0) {
            errors.push("quantity e obrigatorio e deve ser um inteiro nao negativo");
        }
    }

    if (requireAll || data.category !== undefined) {
        if (!data.category || typeof data.category !== "string" || data.category.trim() === "") {
            errors.push("category e obrigatoria e deve ser uma string nao vazia");
        }
    }

    return errors;
}

// ─── ROTAS ─────────────────────────────────────────────────────────────────

// GET /product — lista ois produtos com filtros
router.get("/", (req, res) => {
    const result = service.getAll(req.query); //req.query contém os parâmetros
    res.status(200).json(result);
});

// GET /product/stats - retorna as estatiscas gerais da loja
router.get("/stats", (req, res) => {
    res.status(200).json(service.getStats());
});

// GET /product/:id - busca um produto pelo seu ID
router.get("/:id", (req, res) => {
    const id = parseInt(req.params.id);
    if (isNaN(id)) return res.status(400).json({ message: "ID invalido — nem o Trainer aceitaria esse numero!" });

    const product = service.getById(id);
    if (!product) return res.status(404).json({ message: "Produto nao encontrado — talvez esteja treinando na pista?" });

    res.status(200).json(product);
});

// POST /product — cria um produto novo
router.post("/", (req, res) => {
    const errors = validateProduct(req.body, true);
    if (errors.length) return res.status(400).json({ message: "Dados invalidos — o Trainer nao aprovaria isso!", errors });

    if (service.existsByName(req.body.name)) {
        return res.status(409).json({ message: "Ja existe um produto com esse nome na Loja da Tracen Academy!" });
    }

    const product = service.create(req.body);
    res.status(201).json({ message: "Novo item adicionado a Loja da Tracen Academy!", data: product });
});

// PUT /product/:id — atualização completa
router.put("/:id", (req, res) => {
    const id = parseInt(req.params.id);
    if (isNaN(id)) return res.status(400).json({ message: "ID invalido — nem o Trainer aceitaria esse numero!" });

    const errors = validateProduct(req.body, true);
    if (errors.length) return res.status(400).json({ message: "Dados invalidos — o Trainer nao aprovaria isso!", errors });

    if (service.existsByName(req.body.name, id)) {
        return res.status(409).json({ message: "Ja existe um produto com esse nome na Loja da Tracen Academy!" });
    }

    const product = service.update(id, req.body);
    if (!product) return res.status(404).json({ message: "Produto nao encontrado — talvez esteja treinando na pista?" });

    res.status(200).json({ message: "Produto atualizado com sucesso! Vamos vencer o Triple Crown!", data: product });
});

// PATCH /product/:id — atualiza apenas os campos enviados
router.patch("/:id", (req, res) => {
    const id = parseInt(req.params.id);
    if (isNaN(id)) return res.status(400).json({ message: "ID invalido — nem o Trainer aceitaria esse numero!" });

    const errors = validateProduct(req.body, false);
    if (errors.length) return res.status(400).json({ message: "Dados invalidos — o Trainer nao aprovaria isso!", errors });

    if (req.body.name && service.existsByName(req.body.name, id)) {
        return res.status(409).json({ message: "Ja existe um produto com esse nome na Loja da Tracen Academy!" });
    }

    const product = service.patch(id, req.body);
    if (!product) return res.status(404).json({ message: "Produto nao encontrado — talvez esteja treinando na pista?" });

    res.status(200).json({ message: "Produto atualizado parcialmente! Ganbatte!", data: product });
});

// DELETE /product/:id — remover
router.delete("/:id", (req, res) => {
    const id = parseInt(req.params.id);
    if (isNaN(id)) return res.status(400).json({ message: "ID invalido — nem o Trainer aceitaria esse numero!" });

    const removed = service.remove(id);
    if (!removed) return res.status(404).json({ message: "Produto nao encontrado — talvez esteja treinando na pista?" });

    res.status(200).json({ message: "Produto removido da Loja da Tracen Academy. Sayonara! 🐴" });
});

module.exports = router;
