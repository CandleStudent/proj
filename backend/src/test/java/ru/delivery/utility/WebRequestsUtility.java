package ru.delivery.utility;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@UtilityClass
public class WebRequestsUtility {

  public static MockHttpServletRequestBuilder preparePostRequest(
      String postUrl, Object context, ObjectMapper objectMapper) {

    try {
      return post(postUrl)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(context));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static MockHttpServletRequestBuilder prepareGetRequest(String getUrl) {
      return get(getUrl);
  }

  public static MockHttpServletRequestBuilder prepareDeleteRequest(String deleteUrl) {
    return delete(deleteUrl);
  }

}
