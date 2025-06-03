package ru.delivery.web.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexGeocoderResponse {

  @JsonProperty("response")
  private Response response;

  public Position getPosition() {
    if (response != null &&
        response.geoObjectCollection != null &&
        response.geoObjectCollection.featureMember != null &&
        response.geoObjectCollection.featureMember.length > 0) {

      return response.geoObjectCollection.featureMember[0].geoObject.point.pos;
    }
    return null;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Response {
    @JsonProperty("GeoObjectCollection")
    private GeoObjectCollection geoObjectCollection;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class GeoObjectCollection {
    @JsonProperty("featureMember")
    private FeatureMember[] featureMember;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class FeatureMember {
    @JsonProperty("GeoObject")
    private GeoObject geoObject;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class GeoObject {
    @JsonProperty("Point")
    private Point point;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Point {
    @JsonProperty("pos")
    private Position pos;
  }

  @Data
  public static class Position {
    private final BigDecimal lat;
    private final BigDecimal lon;

    public Position(String pos) {
      if (pos == null || pos.trim().isEmpty()) {
        throw new IllegalArgumentException("Position string cannot be null or empty");
      }

      String[] parts = pos.split(" ");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Position string must contain exactly two numbers separated by space");
      }

      try {
        this.lon = new BigDecimal(parts[0]);
        this.lat = new BigDecimal(parts[1]);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid position format: " + pos, e);
      }
    }
  }

}
