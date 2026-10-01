
const express = require("express");
const router = express.Router();
const service = require("../services/productService"); //importa a camada de serviço

//lista todas as categorias
router.get("/", (req, res) => {
    //chama o serviço para categórias únicas
    const categories = service.getDistinctCategories();
    //retorna as categorias em formato JSON
    res.status(200).json({
        message: "Categorias disponiveis na Loja da Tracen Academy!",
        data: categories,
        total: categories.length //retorna a quantidade de categorias
    });
});

module.exports = router;
