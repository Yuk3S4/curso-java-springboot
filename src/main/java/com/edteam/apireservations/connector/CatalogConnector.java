package com.edteam.apireservations.connector;

import com.edteam.apireservations.connector.configuration.EndpointConfiguration;
import com.edteam.apireservations.connector.configuration.HostConfiguration;
import com.edteam.apireservations.connector.configuration.HttpConnectorConfiguration;
import com.edteam.apireservations.connector.response.CityDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Component
public class CatalogConnector {

    private final String HOST = "api-catalog";
    private final String ENDPOINT = "get-city";

    private HttpConnectorConfiguration configuration;

    @Autowired // para que se inyecte
    public CatalogConnector(HttpConnectorConfiguration configuration) {
        this.configuration = configuration;
    }

    @CircuitBreaker(name = "api-catalog")
    public CityDTO getCity(String code) {

         HostConfiguration hostConfiguration = configuration.getHosts().get(HOST);
         EndpointConfiguration endpointConfiguration = hostConfiguration.getEndpoints().get(ENDPOINT);

         HttpClient httpClient = HttpClient.create()
                 // definición de los timeouts
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(endpointConfiguration.getConnectionTimeout()))
                 .doOnConnected(conn -> conn
                         .addHandler(new ReadTimeoutHandler(endpointConfiguration.getReadTimeout(), TimeUnit.MILLISECONDS))
                         .addHandler(new WriteTimeoutHandler(endpointConfiguration.getWriteTimeout(), TimeUnit.MILLISECONDS)));

        WebClient client = WebClient.builder() // encargado de la comunicación con otros microservicios
                .baseUrl("http://" + hostConfiguration.getHost() + ":" + hostConfiguration.getPort() + endpointConfiguration.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // indicar que se enviará json
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // indicar que se aceptará json
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return client.get()
                .uri(uriBuilder -> uriBuilder.build(code)) // para reemplazar el parámetro {code} por la variable
                .retrieve() // obtener la info
                .bodyToMono(CityDTO.class) // para obtener un sólo elemento y convertirlo a la clase CityDTO
                //.bodyToFlux(CityDTO.class) // para obtener una lista de elementos
                .share()
                .block();
    }
}
