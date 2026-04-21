package com.medilabo.gateway.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
//Ordre de priorité élevé pour outrepasser SpringBoot
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        //Renvoit du JSON
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        //Ne correspond pas a une exception standard
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String body = """
                {"message":"Gateway error","status":500}
                """;

        //Recupère statut de l'exception
        if (ex instanceof ResponseStatusException responseStatusException) {
            status = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
            body = """
                    {"message":"Gateway request failed","status":%d}
                    """.formatted(status.value());
        }

        exchange.getResponse().setStatusCode(status);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        //Renvoit le JSON dans la réponse HTTP
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }
}