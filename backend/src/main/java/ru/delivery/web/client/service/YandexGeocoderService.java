package ru.delivery.web.client.service;

import io.netty.handler.logging.LogLevel;
import java.util.Optional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import ru.delivery.web.client.dto.YandexGeocoderResponse;

@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "clients.yandex.geocoder")
@Data
public class YandexGeocoderService {

  private final HttpClient httpClient = HttpClient.create()
      .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
  private final ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);

  private final WebClient webClient = WebClient.builder()
      .baseUrl("https://geocode-maps.yandex.ru")
      .clientConnector(conn)
      .build();

  private String referer;
  private String path;
  private String apikey;
  private String lang;
  private String results;
  private String ll;
  private String format;
  private String kind;

  public Mono<YandexGeocoderResponse> getLonLatByAddress(String geocode) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(path)
            .queryParam("apikey", apikey)
            .queryParam("geocode", geocode)
            .queryParamIfPresent("lang", Optional.ofNullable(lang))
            .queryParamIfPresent("results", Optional.ofNullable(results))
            .queryParamIfPresent("ll", Optional.ofNullable(ll))
            .queryParamIfPresent("format", Optional.ofNullable(format))
            .queryParamIfPresent("kind", Optional.ofNullable(kind))
            .build())
        .header("Referer", referer)
        .retrieve()
        .bodyToMono(YandexGeocoderResponse.class);
  }

}
