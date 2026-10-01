// Registro de método, rota, status e tempo
package com.tracen.tracenapi.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;

// @Component registra este filtro no contexto Spring automaticamente.
// OncePerRequestFilter garante que o filtro é executado exatamente uma vez por requisição.
// Equivale ao middleware "logger.js" do Express.
@Component
public class LoggingFilter extends OncePerRequestFilter {

    // doFilterInternal é chamado para cada requisição recebida pelo servidor.
    // chain.doFilter() passa a requisição para o próximo elemento da cadeia (controller).
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // Marca o instante de chegada da requisição para calcular o tempo de resposta
        long start = System.currentTimeMillis();
        String timestamp = Instant.now().toString();

        // Passa a requisição para o controller processar
        chain.doFilter(request, response);

        // Após a resposta ser enviada, calcula a duração e exibe o log
        long duration = System.currentTimeMillis() - start;
        System.out.printf("[%s] %s %s — %d (%dms)%n",
                timestamp,
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration);
    }
}
