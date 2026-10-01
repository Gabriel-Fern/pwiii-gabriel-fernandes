// Define as rotas de /product
package com.tracen.tracenapi.service;

import com.tracen.tracenapi.model.Product;
import com.tracen.tracenapi.model.ProductRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// @Service marca esta classe como um componente da camada de negócio do Spring.
// O Spring cria uma única instância dela e injeta onde for necessário (ex: no controller).
@Service
public class ProductService {

    // ConcurrentHashMap é um Map thread-safe — seguro para múltiplas requisições simultâneas.
    // Equivale ao array "products" do Node.js, mas indexado pelo ID para busca O(1).
    private final Map<Long, Product> store = new ConcurrentHashMap<>();

    // AtomicLong garante que o próximo ID seja gerado sem conflito mesmo em paralelo.
    // Equivale ao "nextId" do productService.js.
    private final AtomicLong nextId = new AtomicLong(13);

    // Bloco de inicialização: popula o estoque com os 12 produtos temáticos da Tracen Academy.
    // Executado uma única vez quando o Spring cria o bean ProductService.
    {
        List<Product> seed = List.of(
            new Product(1, "Figura Special Week — Season 1",
                    "Figura de 20cm da protagonista Special Week com pose de vitoria. Edicao limitada da 1a temporada.",
                    289.90, 12, "Figuras", "2023-04-01T00:00:00Z", "2023-04-01T00:00:00Z"),
            new Product(2, "Plushie Silence Suzuka",
                    "Plushie fofinho da Silence Suzuka com seu uniforme de corrida. Altura: 25cm. Material: pelucia premium.",
                    149.90, 30, "Pelucias", "2023-04-15T00:00:00Z", "2023-04-15T00:00:00Z"),
            new Product(3, "Boneca Tokai Teio — Edicao Campea",
                    "Boneca articulada 30cm da Tokai Teio com tiara e capa de campea do Japan Cup. Acompanha suporte.",
                    319.90, 8, "Figuras", "2023-05-01T00:00:00Z", "2023-05-01T00:00:00Z"),
            new Product(4, "Bone de Corrida Gold Ship",
                    "Bone oficial inspirado no estilo rebelde e energetico da Gold Ship. Ajuste snapback. Tamanho unico.",
                    89.90, 50, "Vestuario", "2023-05-10T00:00:00Z", "2023-05-10T00:00:00Z"),
            new Product(5, "CD Soundtrack — Umamusume Pretty Derby OST Vol.1",
                    "Soundtrack oficial com 22 faixas, incluindo 'Make Debut!', 'Grow Up Shine!' e temas exclusivos das corridas.",
                    129.90, 25, "Musica", "2023-06-01T00:00:00Z", "2023-06-01T00:00:00Z"),
            new Product(6, "Camiseta Mejiro McQueen — Team Spica",
                    "Camiseta 100% algodao com estampa artistica da Mejiro McQueen. Disponivel nos tamanhos P ao GG.",
                    79.90, 60, "Vestuario", "2023-06-15T00:00:00Z", "2023-06-15T00:00:00Z"),
            new Product(7, "Chaveiro Acrilico — Daiwa Scarlet",
                    "Chaveiro de acrilico dupla face com arte oficial da Daiwa Scarlet em pose de desafio. Mede 6cm.",
                    29.90, 100, "Acessorios", "2023-07-01T00:00:00Z", "2023-07-01T00:00:00Z"),
            new Product(8, "Set de Pins — Time Spica Completo",
                    "Conjunto com 7 pins esmaltados dos membros do Time Spica: Special Week, Silence Suzuka, Tokai Teio, Gold Ship, Mejiro McQueen, Vodka e Daiwa Scarlet.",
                    119.90, 40, "Acessorios", "2023-07-15T00:00:00Z", "2023-07-15T00:00:00Z"),
            new Product(9, "Caneca Tracen Academy",
                    "Caneca de ceramica 350ml com o brasao oficial da Tracen Academy e a frase 'Win the Triple Crown!'. Vai ao microondas.",
                    64.90, 45, "Utilidades", "2023-08-01T00:00:00Z", "2023-08-01T00:00:00Z"),
            new Product(10, "Artbook — Umamusume Season 2",
                    "Artbook oficial de 192 paginas com ilustracoes da 2a temporada, comentarios dos artistas e arte conceitual inedita.",
                    199.90, 15, "Livros", "2023-08-20T00:00:00Z", "2023-08-20T00:00:00Z"),
            new Product(11, "Figura El Condor Pasa — Pose de Largada",
                    "Figura de resina 18cm da El Condor Pasa capturando o momento da largada com detalhes de pintura premium.",
                    349.90, 6, "Figuras", "2023-09-01T00:00:00Z", "2023-09-01T00:00:00Z"),
            new Product(12, "Mousepad Vodka e Daiwa Scarlet — Rival Eternas",
                    "Mousepad XXL (80x40cm) com ilustracao das rivais Vodka e Daiwa Scarlet lado a lado. Base antiderrapante.",
                    94.90, 35, "Utilidades", "2023-09-15T00:00:00Z", "2023-09-15T00:00:00Z")
        );
        // Insere cada produto no ConcurrentHashMap usando o ID como chave.
        seed.forEach(p -> store.put(p.id(), p));
    }

    // ─── READ ──────────────────────────────────────────────────────────────────

    // Retorna todos os produtos, opcionalmente filtrados por categoria e/ou busca por nome.
    // stream() transforma o Map em um fluxo de elementos para encadeamento de operações.
    public List<Product> getAll(String category, String search) {
        return store.values().stream()
                // Filtro de categoria (case-insensitive)
                .filter(p -> category == null || p.category().equalsIgnoreCase(category))
                // Filtro de busca parcial no nome
                .filter(p -> search == null || p.name().toLowerCase().contains(search.toLowerCase()))
                // Ordena por ID para manter a listagem consistente
                .sorted(Comparator.comparingLong(Product::id))
                .collect(Collectors.toList());
    }

    // Busca um produto pelo ID. Retorna Optional vazio se não encontrado.
    // Optional evita NullPointerException — o chamador decide o que fazer quando não existe.
    public Optional<Product> getById(long id) {
        return Optional.ofNullable(store.get(id));
    }

    // Verifica se já existe um produto com o mesmo nome (ignorando o próprio produto no caso de update).
    // excludeId é usado para não conflitar o produto consigo mesmo no PUT/PATCH.
    public boolean existsByName(String name, long excludeId) {
        return store.values().stream()
                .anyMatch(p -> p.name().equalsIgnoreCase(name) && p.id() != excludeId);
    }

    // ─── WRITE ─────────────────────────────────────────────────────────────────

    // Cria um novo produto e o adiciona ao store.
    // nextId.getAndIncrement() garante IDs únicos e crescentes sem condições de corrida.
    public Product create(ProductRequest req) {
        long id = nextId.getAndIncrement();
        Product product = Product.create(id, req.name(), req.description() != null ? req.description() : "",
                req.price(), req.quantity(), req.category());
        store.put(id, product);
        return product;
    }

    // Substitui completamente um produto existente (PUT — atualização total).
    // Retorna Optional vazio se o produto não existir.
    public Optional<Product> update(long id, ProductRequest req) {
        if (!store.containsKey(id)) return Optional.empty();
        Product updated = store.get(id).withUpdates(
                req.name(),
                req.description() != null ? req.description() : "",
                req.price(), req.quantity(), req.category()
        );
        store.put(id, updated);
        return Optional.of(updated);
    }

    // Atualiza apenas os campos enviados (PATCH — atualização parcial).
    // Campos nulos no request mantêm o valor atual do produto.
    public Optional<Product> patch(long id, ProductRequest req) {
        return getById(id).map(existing -> {
            String name = req.name() != null ? req.name() : existing.name();
            String desc = req.description() != null ? req.description() : existing.description();
            double price = req.price() != null ? req.price() : existing.price();
            int qty = req.quantity() != null ? req.quantity() : existing.quantity();
            String cat = req.category() != null ? req.category() : existing.category();

            Product patched = new Product(existing.id(), name, desc, price, qty, cat,
                    existing.createdAt(), Instant.now().toString());
            store.put(id, patched);
            return patched;
        });
    }

    // Remove um produto do store pelo ID. Retorna true se removido, false se não encontrado.
    public boolean remove(long id) {
        return store.remove(id) != null;
    }

    // ─── STATS ─────────────────────────────────────────────────────────────────

    // Calcula estatísticas gerais do estoque da loja.
    // Retorna um Map com as métricas para serialização automática em JSON pelo Spring.
    public Map<String, Object> getStats() {
        Collection<Product> all = store.values();
        if (all.isEmpty()) {
            return Map.of("total", 0, "totalValue", 0.0, "outOfStock", 0);
        }

        double totalValue = all.stream().mapToDouble(p -> p.price() * p.quantity()).sum();
        int outOfStock = (int) all.stream().filter(p -> p.quantity() == 0).count();

        // Agrupa produtos por categoria e conta quantos há em cada uma
        Map<String, Long> byCategory = all.stream()
                .collect(Collectors.groupingBy(Product::category, Collectors.counting()));

        // Ordena para encontrar o mais caro e o mais barato
        List<Product> sorted = all.stream()
                .sorted(Comparator.comparingDouble(Product::price).reversed())
                .toList();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", all.size());
        stats.put("totalValue", Math.round(totalValue * 100.0) / 100.0);
        stats.put("mostExpensive", sorted.get(0));
        stats.put("cheapest", sorted.get(sorted.size() - 1));
        stats.put("outOfStock", outOfStock);
        stats.put("byCategory", byCategory);
        return stats;
    }

    // ─── CATEGORIES ────────────────────────────────────────────────────────────

    // Retorna uma lista ordenada de categorias únicas dos produtos no estoque.
    public List<String> getDistinctCategories() {
        return store.values().stream()
                .map(Product::category)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
