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
  private final Company bank = new Company(
      null,
      "7725038124",
      "1037739527077",
      "770401001",
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
                """
                    [
                      {
                        "inn": "7725038124",
                        "ogrn": "1037739527077",
                        "kpp": "770401001",
                        "fullNameRus": "АКЦИОНЕРНОЕ ОБЩЕСТВО \\"БАНК ДОМ.РФ\\"",
                        "shortNameRus": "АО\\"БАНК ДОМ.РФ\\"",
                        "statusName": "Действующая",
                        "statusDate": "2020-11-30"
                      },
                        {
                            "inn": "9705113553",
                            "ogrn": "5177746290288",
                            "kpp": "772501001",
                            "fullNameRus": "ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\"СЕВЕНТЕК\\"",
                            "shortNameRus": "ООО \\"7ТЕК\\"",
                            "statusName": "Actual",
                            "statusDate": "2021-01-30"
                        }
                        ]
                    """
            )
        );
  }

  @Test
  void getCompanyByInnShouldReturnCompany() throws Exception {
    // given
    aggregateTemplate.insert(bank);
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/spark/companies/7725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
                """
                    {
                        "inn": "7725038124",
                        "ogrn": "1037739527077",
                        "kpp": "770401001",
                        "fullNameRus": "АКЦИОНЕРНОЕ ОБЩЕСТВО \\"БАНК ДОМ.РФ\\"",
                        "shortNameRus": "АО\\"БАНК ДОМ.РФ\\"",
                        "statusName": "Действующая",
                        "statusDate": "2020-11-30"
                      }
                    """
            )
        );
  }

  @Test
  void saveCompanyShouldReturnCompanyWithCorrectArgs() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
            .content("""
                {
                    "inn": "7725038124",
                    "ogrn": "1037739527077",
                    "kpp": "770401001",
                    "fullNameRus": "АКЦИОНЕРНОЕ ОБЩЕСТВО \\"БАНК ДОМ.РФ\\"",
                    "shortNameRus": "АО\\"БАНК ДОМ.РФ\\"",
                    "statusName": "Действующая",
                    "statusDate": "2020-11-30"
                  }
                """).contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
                """
                    {
                        "inn": "7725038124",
                        "ogrn": "1037739527077",
                        "kpp": "770401001",
                        "fullNameRus": "АКЦИОНЕРНОЕ ОБЩЕСТВО \\"БАНК ДОМ.РФ\\"",
                        "shortNameRus": "АО\\"БАНК ДОМ.РФ\\"",
                        "statusName": "Действующая",
                        "statusDate": "2020-11-30"
                      }
                    """
            )
        );

    // then
    var result = sparkRepositoryDb.findByInn("7725038124").orElseThrow(() -> new IllegalArgumentException(
        "There is no company with inn \"7725038124\""));

    assertAll(
        () -> assertThat(sparkRepositoryDb.findAll()).hasSize(1),
        () -> assertEquals("7725038124", result.getInn()),
        () -> assertEquals("770401001", result.getKpp()),
        () -> assertEquals("1037739527077", result.getOgrn()),
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
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/7725038124")
            .content("""
                {
                        "inn": "9705113553",
                        "ogrn": "5177746290288",
                        "kpp": "772501001",
                        "fullNameRus": "ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\"СЕВЕНТЕК\\"",
                        "shortNameRus": "ООО \\"7ТЕК\\"",
                        "statusName": "Actual",
                        "statusDate": "2021-01-30"
                    }
                """)
            .contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
                """
                    {
                            "inn": "9705113553",
                            "ogrn": "5177746290288",
                            "kpp": "772501001",
                            "fullNameRus": "ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\"СЕВЕНТЕК\\"",
                            "shortNameRus": "ООО \\"7ТЕК\\"",
                            "statusName": "Actual",
                            "statusDate": "2021-01-30"
                        }
                    """
            )
        );
    // then
    assertThat(sparkRepositoryDb.findAll()).hasSize(1);
  }

  @Test
  void updateShouldReturn404IfNoCompanyForUpdate() throws Exception {
    //given
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/7725038124")
            .content("""
                {
                    "inn": "7777777777",
                    "ogrn": "1037739527077",
                    "kpp": "888888888",
                    "fullNameRus": "АКЦИОНЕРНОЕ ОБЩЕСТВО \\"БАНК ДОМ.РФ\\"",
                    "shortNameRus": "АО\\"БАНК ДОМ.РФ\\"",
                    "statusName": "Действующая",
                    "statusDate": "2020-11-30"
                  }
                """).contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void deleteCompanyShouldDeleteEntities() throws Exception {
    // given
    var result = aggregateTemplate.insert(bank);
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/7725038124"))
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
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/9772503812"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void saveCompanyShouldThrowExceptionWhenExists() throws Exception {
    // given
    aggregateTemplate.insert(bank);
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
            .content("""
                {
                    "inn": "7725038124",
                    "ogrn": "1037739527077",
                    "kpp": "770401001",
                    "fullNameRus": "АКЦИОНЕРНОЕ ОБЩЕСТВО \\"БАНК ДОМ.РФ\\"",
                    "shortNameRus": "АО\\"БАНК ДОМ.РФ\\"",
                    "statusName": "Действующая",
                    "statusDate": "2020-11-30"
                  }
                """).contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.content().json(
            """
                [{
                    "code": "ERROR001",
                    "message": "EntityAlreadyExists (7725038124)"
                }]
                """
        ));
  }
}
