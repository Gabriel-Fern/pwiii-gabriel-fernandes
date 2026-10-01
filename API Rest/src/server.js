const express = require("express");
const productRoutes = require("./routes/productRoutes");
const categoryRoutes = require("./routes/categoryRoutes");
const logger = require("./middlewares/logger");

const app = express();
const PORT = process.env.PORT || 3000;

// Middlewares globais
app.use(express.json());
app.use(logger);

// Rota raiz
app.get("/", (req, res) => {
    res.status(200).json({
        message: "🐴 Bem-vindo a Loja Oficial da Tracen Academy! — Umamusume: Pretty Derby",
        tagline: "Correr e uma arte, e cada produto conta uma historia!",
        version: "2.0.0",
        endpoints: {
            products: "/product",
            categories: "/category"
        },
        dica: "Use GET /product?search=Special+Week para buscar sua Uma Musume favorita!"
    });
});

// Rotas
app.use("/product", productRoutes);
app.use("/category", categoryRoutes);

// 404 fallback
app.use((req, res) => {
    res.status(404).json({
        message: "Endpoint nao encontrado"
    });
});

// Tratamento de erros global
app.use((err, req, res, next) => {
    console.error("[ERROR]", err.message);
    res.status(500).json({
        message: "Erro interno do servidor"
    });
});

app.listen(PORT, () => {
    console.log(`Servidor rodando em http://localhost:${PORT}`);
    console.log(`Ambiente: ${process.env.NODE_ENV || "development"}`);
});
