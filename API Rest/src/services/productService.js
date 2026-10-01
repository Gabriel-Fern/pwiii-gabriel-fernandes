/**
 * productService.js
 * Servico de gerenciamento de produtos da Loja Oficial da Tracen Academy.
 * Campos: id, name, description, price, quantity, category, createdAt, updatedAt
 *
 *   Uma musume Pretty Derby — Loja da Tracen Academy
 *   "Correr e uma arte, e cada produto conta uma historia!"
 */

let products = [
    {
        id: 1,
        name: "Figura Special Week — Season 1",
        description: "Figura de 20cm da protagonista Special Week com pose de vitoria. Edicao limitada da 1a temporada.",
        price: 289.90,
        quantity: 12,
        category: "Figuras",
        createdAt: new Date("2023-04-01").toISOString(),
        updatedAt: new Date("2023-04-01").toISOString()
    },
    {
        id: 2,
        name: "Plushie Silence Suzuka",
        description: "Plushie fofinhos da Silence Suzuka com seu uniforme de corrida. Altura: 25cm. Material: pelucia premium.",
        price: 149.90,
        quantity: 30,
        category: "Pelucias",
        createdAt: new Date("2023-04-15").toISOString(),
        updatedAt: new Date("2023-04-15").toISOString()
    },
    {
        id: 3,
        name: "Boneca Tokai Teio — Edição Campeã",
        description: "Boneca articulada 30cm da Tokai Teio com tiara e capa de campeã do Japan Cup. Acompanha suporte.",
        price: 319.90,
        quantity: 8,
        category: "Figuras",
        createdAt: new Date("2023-05-01").toISOString(),
        updatedAt: new Date("2023-05-01").toISOString()
    },
    {
        id: 4,
        name: "Boné de Corrida Gold Ship",
        description: "Boné oficial inspirado no estilo rebelde e energetico da Gold Ship. Ajuste snapback. Tamanho unico.",
        price: 89.90,
        quantity: 50,
        category: "Vestuario",
        createdAt: new Date("2023-05-10").toISOString(),
        updatedAt: new Date("2023-05-10").toISOString()
    },
    {
        id: 5,
        name: "CD Soundtrack — Umamusume Pretty Derby OST Vol.1",
        description: "Soundtrack oficial com 22 faixas, incluindo 'Make Debut!', 'Grow Up Shine!' e temas exclusivos das corridas.",
        price: 129.90,
        quantity: 25,
        category: "Musica",
        createdAt: new Date("2023-06-01").toISOString(),
        updatedAt: new Date("2023-06-01").toISOString()
    },
    {
        id: 6,
        name: "Camiseta Mejiro McQueen — Team Spica",
        description: "Camiseta 100% algodao com estampa artistica da Mejiro McQueen. Disponivel nos tamanhos P ao GG.",
        price: 79.90,
        quantity: 60,
        category: "Vestuario",
        createdAt: new Date("2023-06-15").toISOString(),
        updatedAt: new Date("2023-06-15").toISOString()
    },
    {
        id: 7,
        name: "Chaveiro Acrílico — Daiwa Scarlet",
        description: "Chaveiro de acrilico dupla face com arte oficial da Daiwa Scarlet em pose de desafio. Mede 6cm.",
        price: 29.90,
        quantity: 100,
        category: "Acessorios",
        createdAt: new Date("2023-07-01").toISOString(),
        updatedAt: new Date("2023-07-01").toISOString()
    },
    {
        id: 8,
        name: "Set de Pins — Time Spica Completo",
        description: "Conjunto com 7 pins esmaltados dos membros do Time Spica: Special Week, Silence Suzuka, Tokai Teio, Gold Ship, Mejiro McQueen, Vodka e Daiwa Scarlet.",
        price: 119.90,
        quantity: 40,
        category: "Acessorios",
        createdAt: new Date("2023-07-15").toISOString(),
        updatedAt: new Date("2023-07-15").toISOString()
    },
    {
        id: 9,
        name: "Caneca Tracen Academy",
        description: "Caneca de ceramica 350ml com o brasao oficial da Tracen Academy e a frase 'Win the Triple Crown!'. Vai ao microondas.",
        price: 64.90,
        quantity: 45,
        category: "Utilidades",
        createdAt: new Date("2023-08-01").toISOString(),
        updatedAt: new Date("2023-08-01").toISOString()
    },
    {
        id: 10,
        name: "Artbook — Umamusume Season 2",
        description: "Artbook oficial de 192 paginas com ilustracoes da 2a temporada, comentarios dos artistas e arte conceitual inedita.",
        price: 199.90,
        quantity: 15,
        category: "Livros",
        createdAt: new Date("2023-08-20").toISOString(),
        updatedAt: new Date("2023-08-20").toISOString()
    },
    {
        id: 11,
        name: "Figura El Condor Pasa — Pose de Largada",
        description: "Figura de resina 18cm da El Condor Pasa capturando o momento da largada com detalhes de pintura premium.",
        price: 349.90,
        quantity: 6,
        category: "Figuras",
        createdAt: new Date("2023-09-01").toISOString(),
        updatedAt: new Date("2023-09-01").toISOString()
    },
    {
        id: 12,
        name: "Mousepad Vodka & Daiwa Scarlet — Rival Eternas",
        description: "Mousepad XXL (80x40cm) com ilustracao das rivais Vodka e Daiwa Scarlet lado a lado. Base antiderrapante.",
        price: 94.90,
        quantity: 35,
        category: "Utilidades",
        createdAt: new Date("2023-09-15").toISOString(),
        updatedAt: new Date("2023-09-15").toISOString()
    }
];

let nextId = 13;

// ─── READ ──────────────────────────────────────────────────────────────────

function getAll({ category, search, sort, order, page, limit } = {}) {
    let result = [...products];

    // Filtrar por categoria
    if (category) {
        const cat = category.toLowerCase();
        result = result.filter(p => p.category.toLowerCase() === cat);
    }

    // Busca por nome (parcial, case-insensitive)
    if (search) {
        const term = search.toLowerCase();
        result = result.filter(p => p.name.toLowerCase().includes(term));
    }

    // Ordenacao
    const validSortFields = ["name", "price", "quantity", "category", "createdAt"];
    if (sort && validSortFields.includes(sort)) {
        const dir = order === "desc" ? -1 : 1;
        result.sort((a, b) => {
            if (a[sort] < b[sort]) return -1 * dir;
            if (a[sort] > b[sort]) return 1 * dir;
            return 0;
        });
    }

    // Paginacao
    const pageNum = parseInt(page) || 1;
    const limitNum = parseInt(limit) || result.length;
    const total = result.length;
    const totalPages = Math.ceil(total / limitNum);
    const start = (pageNum - 1) * limitNum;
    const paginated = result.slice(start, start + limitNum);

    return {
        data: paginated,
        pagination: {
            total,
            page: pageNum,
            limit: limitNum,
            totalPages
        }
    };
}

function getById(id) {
    return products.find(p => p.id === id) || null;
}

function existsByName(name, excludeId = null) {
    const lower = name.toLowerCase();
    return products.some(p => p.name.toLowerCase() === lower && p.id !== excludeId);
}

// ─── WRITE ─────────────────────────────────────────────────────────────────

function create(data) {
    const now = new Date().toISOString();
    const product = {
        id: nextId++,
        name: data.name,
        description: data.description || "",
        price: data.price,
        quantity: data.quantity,
        category: data.category,
        createdAt: now,
        updatedAt: now
    };
    products.push(product);
    return product;
}

function update(id, data) {
    const index = products.findIndex(p => p.id === id);
    if (index === -1) return null;

    products[index] = {
        id,
        name: data.name,
        description: data.description || "",
        price: data.price,
        quantity: data.quantity,
        category: data.category,
        createdAt: products[index].createdAt,
        updatedAt: new Date().toISOString()
    };
    return products[index];
}

function patch(id, data) {
    const product = getById(id);
    if (!product) return null;

    if (data.name !== undefined) product.name = data.name;
    if (data.description !== undefined) product.description = data.description;
    if (data.price !== undefined) product.price = data.price;
    if (data.quantity !== undefined) product.quantity = data.quantity;
    if (data.category !== undefined) product.category = data.category;
    product.updatedAt = new Date().toISOString();

    return product;
}

function remove(id) {
    const index = products.findIndex(p => p.id === id);
    if (index === -1) return false;
    products.splice(index, 1);
    return true;
}

// ─── STATS ─────────────────────────────────────────────────────────────────

function getStats() {
    if (products.length === 0) {
        return { total: 0, totalValue: 0, mostExpensive: null, cheapest: null, outOfStock: 0 };
    }

    const totalValue = products.reduce((sum, p) => sum + p.price * p.quantity, 0);
    const sorted = [...products].sort((a, b) => b.price - a.price);
    const outOfStock = products.filter(p => p.quantity === 0).length;
    const byCategory = {};
    products.forEach(p => {
        byCategory[p.category] = (byCategory[p.category] || 0) + 1;
    });

    return {
        total: products.length,
        totalValue: parseFloat(totalValue.toFixed(2)),
        mostExpensive: sorted[0],
        cheapest: sorted[sorted.length - 1],
        outOfStock,
        byCategory
    };
}

// ─── CATEGORIES ────────────────────────────────────────────────────────────

function getDistinctCategories() {
    const set = new Set(products.map(p => p.category));
    return [...set].sort();
}

module.exports = {
    getAll,
    getById,
    existsByName,
    create,
    update,
    patch,
    remove,
    getStats,
    getDistinctCategories
};
