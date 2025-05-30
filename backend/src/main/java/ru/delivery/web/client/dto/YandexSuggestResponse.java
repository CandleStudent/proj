package ru.delivery.web.client.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.delivery.web.client.dto.YandexSuggestResponse.Result.Address;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class YandexSuggestResponse {

  private String suggest_reqid;
  private List<Result> results;

  @Data
  public static class Result {
    private Title title;
    private Subtitle subtitle;
    private List<String> tags;
    private Distance distance;
    private Address address;

    @Data
    public static class Title {
      private String text;
      private List<Highlight> hl;

      @Data
      public static class Highlight {
        private int begin;
        private int end;
      }
    }

    @Data
    public static class Subtitle {
      private String text;
    }

    @Data
    public static class Distance {
      private double value;
      private String text;
    }

    @Data
    public static class Address {
      private String formatted_address;
      private List<Component> component;

      @Data
      public static class Component {
        private String name;
        private List<String> kind;
      }
    }
  }

  public static Map<String, String> createComponentMap(Address address) {
    var components = address.getComponent();

    Map<String, String> result = new HashMap<>();
    for (var component : components) {
      List<String> kinds = component.getKind();
      if (kinds != null && !kinds.isEmpty()) {
        result.put(kinds.get(0), component.getName());
      }
    }
    return result;
  }
}
