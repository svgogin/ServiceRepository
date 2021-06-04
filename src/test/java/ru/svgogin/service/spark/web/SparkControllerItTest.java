package ru.svgogin.service.spark.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.repository.SparkRepositoryDb;

@SpringBootTest
@AutoConfigureMockMvc
public class SparkControllerItTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JdbcAggregateTemplate aggregateTemplate;
  @Autowired
  private SparkRepositoryDb sparkRepositoryDb;

  @AfterEach
  void tearDown() {
    aggregateTemplate.deleteAll(Company.class);
  }

  private final Company bank = new Company(
      null,
      "07725038124",
      "01037739527077",
      "0770401001",
      "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
      "АО\"БАНК ДОМ.РФ\"",
      "Действующая",
      LocalDate.of(2020, 11, 30));

  private final Company test7tec = new Company(
      null,
      "9705113553",
      "5177746290288",
      "772501001",
      "ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"СЕВЕНТЕК\"",
      "ООО \"7ТЕК\"",
      "Actual",
      LocalDate.of(2021, 1, 30));

  @Test
  void getCompaniesShouldReturnAllCompanies() throws Exception {
    // given
    aggregateTemplate.insert(bank);
    aggregateTemplate.insert(test7tec);
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/spark/companies"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "[\n"
            + "  {\n"
            + "    \"inn\": \"07725038124\",\n"
            + "    \"ogrn\": \"01037739527077\",\n"
            + "    \"kpp\": \"0770401001\",\n"
            + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"statusName\": \"Действующая\",\n"
            + "    \"statusDate\": \"2020-11-30\"\n"
            + "  },\n"
            +
            "    "
            + "{\n"
            + "        \"inn\": \"9705113553\",\n"
            + "        \"ogrn\": \"5177746290288\",\n"
            + "        \"kpp\": \"772501001\",\n"
            + "        \"fullNameRus\": \"ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\\"СЕВЕНТЕК\\\"\",\n"
            + "        \"shortNameRus\": \"ООО \\\"7ТЕК\\\"\",\n"
            + "        \"statusName\": \"Actual\",\n"
            + "        \"statusDate\": \"2021-01-30\"\n"
            + "    }\n"
            + "    ]\n"
            )
        );
  }

  @Test
  void getCompanyByInnShouldReturnCompany() throws Exception {
    // given
    aggregateTemplate.insert(bank);
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/spark/companies/07725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "{\n"
            + "    \"inn\": \"07725038124\",\n"
            + "    \"ogrn\": \"01037739527077\",\n"
            + "    \"kpp\": \"0770401001\",\n"
            + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"statusName\": \"Действующая\",\n"
            + "    \"statusDate\": \"2020-11-30\"\n"
            + "  }\n"
            )
        );
  }
  @Test
  void saveCompanyShouldReturnCompanyWithCorrectArgs() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
        .content("{\n"
                 + "    \"inn\": \"07725038124\",\n"
                 + "    \"ogrn\": \"01037739527077\",\n"
                 + "    \"kpp\": \"0770401001\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n").contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "{\n"
            + "    \"inn\": \"07725038124\",\n"
            + "    \"ogrn\": \"01037739527077\",\n"
            + "    \"kpp\": \"0770401001\",\n"
            + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"statusName\": \"Действующая\",\n"
            + "    \"statusDate\": \"2020-11-30\"\n"
            + "  }\n"
            )
        );

    // then
    var result = sparkRepositoryDb.findByInn("07725038124").orElseThrow(() -> new IllegalArgumentException(
        "There is no company with inn \"07725038124\""));
    var rowNum = sparkRepositoryDb.findAll().spliterator().getExactSizeIfKnown();

    assertAll(
        () -> assertEquals(1,rowNum),
        () -> assertEquals("07725038124", result.getInn()),
        () -> assertEquals("0770401001", result.getKpp()),
        () -> assertEquals("01037739527077", result.getOgrn()),
        () -> assertEquals("АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"", result.getFullNameRus()),
        () -> assertEquals("АО\"БАНК ДОМ.РФ\"", result.getShortNameRus()),
        () -> assertEquals("Действующая", result.getStatusName()),
        () -> assertEquals(LocalDate
            .of(2020, 11, 30), result.getStatusDate())
    );
  }
  @Test
  void updateCompanyShouldReturnUpdatedCompany() throws Exception {
    // given
    var result = aggregateTemplate.insert(bank);
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/07725038124")
        .content("{\n"
                 + "        \"inn\": \"9705113553\",\n"
                 + "        \"ogrn\": \"5177746290288\",\n"
                 + "        \"kpp\": \"772501001\",\n"
                 + "        \"fullNameRus\": \"ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\\"СЕВЕНТЕК\\\"\",\n"
                 + "        \"shortNameRus\": \"ООО \\\"7ТЕК\\\"\",\n"
                 + "        \"statusName\": \"Actual\",\n"
                 + "        \"statusDate\": \"2021-01-30\"\n"
                 + "    }\n")
        .contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "{\n"
            + "        \"inn\": \"9705113553\",\n"
            + "        \"ogrn\": \"5177746290288\",\n"
            + "        \"kpp\": \"772501001\",\n"
            + "        \"fullNameRus\": \"ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\\"СЕВЕНТЕК\\\"\",\n"
            + "        \"shortNameRus\": \"ООО \\\"7ТЕК\\\"\",\n"
            + "        \"statusName\": \"Actual\",\n"
            + "        \"statusDate\": \"2021-01-30\"\n"
            + "    }\n"
            )
        );
    // then
    var rowNumber = aggregateTemplate.findAll(Company.class).spliterator().getExactSizeIfKnown();
    assertEquals(1, rowNumber);
  }

  @Test
  void updateShouldReturn404IfNoCompanyForUpdate() throws Exception {
    //given
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/07725038124")
        .content("{\n"
                 + "    \"inn\": \"77777777\",\n"
                 + "    \"ogrn\": \"01037739527077\",\n"
                 + "    \"kpp\": \"8888888888\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n").contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void deleteCompanyShouldDeleteEntities() throws Exception {
    // given
    var result = aggregateTemplate.insert(bank);
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/07725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    //then
    var allRows = sparkRepositoryDb.findAll();
    assertThat(allRows).isEmpty();
  }

  @Test
  void deleteCompanyShouldReturn404ifNotExist() throws Exception {
    // given
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/97725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
