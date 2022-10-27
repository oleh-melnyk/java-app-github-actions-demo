package com.example.demo;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import com.example.demo.domain.request.VolunteerRequestDetails.RequestType;
import java.time.LocalDate;
import java.time.Month;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Testcontainers
@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = RequestControllerIT.MongoDbInitializer.class)
public class RequestControllerIT {

  @Autowired
  private WebApplicationContext applicationContext;
  private MockMvc mockMvc;
  @Autowired
  private MongoTemplate mongoTemplate;

  @Container
  static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  public static class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext configurableApplicationContext) {
      var values = TestPropertyValues.of(
          "spring.data.mongodb.host=" + mongoDBContainer.getHost(),
          "spring.data.mongodb.port=" + mongoDBContainer.getFirstMappedPort()
      );
      values.applyTo(configurableApplicationContext);
    }
  }

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
  }

  @BeforeAll
  public static void initAll() {
    mongoDBContainer.start();
  }

  @AfterAll
  public static void afterAll() {
    mongoDBContainer.stop();
  }

  @SneakyThrows
  @Test
  public void saveRequest() {
    var requestPayload = """
           {
              "deadline": "2022-10-29",
              "requestType": "MEDICINE",
              "comment": "ASAP"
           }
        """;

    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/requests?username=user1@test.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestPayload)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.details.deadline", is("2022-10-29")))
        .andExpect(jsonPath("$.details.requestType", is("MEDICINE")))
        .andExpect(jsonPath("$.details.comment", is("ASAP")));
  }
}
