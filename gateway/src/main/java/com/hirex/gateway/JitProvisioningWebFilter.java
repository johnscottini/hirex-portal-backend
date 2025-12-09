package com.hirex.gateway;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JitProvisioningWebFilter implements WebFilter {

    private final WebClient webClient;

    private final Map<String, Long> ensureCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MILLIS = Duration.ofSeconds(90).toMillis();

    public JitProvisioningWebFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://users-service").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
                        return Mono.empty();
                    }
                    Jwt jwt = jwtAuth.getToken();
                    String sub = jwt.getSubject();
                    if (sub == null || sub.isBlank()) {
                        return Mono.empty();
                    }

                    Long last = ensureCache.get(sub);
                    long now = Instant.now().toEpochMilli();
                    if (last != null && (now - last) < CACHE_TTL_MILLIS) {
                        return Mono.empty();
                    }

                    String bearer = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (bearer == null || bearer.isBlank()) {
                        return Mono.empty();
                    }

                    return webClient.post()
                            .uri("/internal/users/ensure")
                            .header(HttpHeaders.AUTHORIZATION, bearer)
                            .retrieve()
                            .toBodilessEntity()
                            .timeout(Duration.ofMillis(300))
                            .doOnSuccess(res -> ensureCache.put(sub, now))
                            .onErrorResume(ex -> Mono.empty())
                            .then();
                })
                .onErrorResume(ex -> Mono.empty())
                .then(chain.filter(exchange));
    }
}
