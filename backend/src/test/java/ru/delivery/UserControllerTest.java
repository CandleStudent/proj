package ru.delivery;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.delivery.utility.WebRequestsUtility.preparePostRequest;

import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.delivery.dto.auth.RegisterRequest;
import ru.delivery.service.UserService;

@AutoConfigureMockMvc
public class UserControllerTest extends PostgresContainerTest {

  @MockitoBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

//  @Autowired
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @WithAnonymousUser
  @SneakyThrows
  public void registerTest() {
    var request = Instancio.create(RegisterRequest.class);
    doReturn("jwt").when(userService).register(any(), any());

    var content = mockMvc.perform(
            preparePostRequest("/api/auth/register", request, objectMapper))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertTrue(content.contains("jwt"));
  }

}
