package ru.svgogin.service.spark.web;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.exception.NoSuchEntityException;
import ru.svgogin.service.spark.service.SparkService;

@WebMvcTest(value = SparkController.class)
public class SparkControllerTest {

  private final CompanyDto bankDto = new CompanyDto(
      BigInteger.valueOf(1),
      "7725038124",
      "1037739527077",
      "770401001",
      "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
      "АО\"БАНК ДОМ.РФ\"",
      "Действующая",
      LocalDate.of(2020, 11, 30));
  @Captor
  ArgumentCaptor<CompanyDto> companyDtoArgumentCaptor;
  @MockBean
  private SparkService sparkService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  void getCompaniesShouldReturnAllCompanies() throws Exception {
    // given
    when(sparkService.findAll()).thenReturn(List.of(bankDto));
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/spark/companies"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "[\n"
            + "  {\n"
            + "    \"inn\": \"7725038124\",\n"
            + "    \"ogrn\": \"1037739527077\",\n"
            + "    \"kpp\": \"770401001\",\n"
            + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"statusName\": \"Действующая\",\n"
            + "    \"statusDate\": \"2020-11-30\"\n"
            + "  }\n"
            + "]")
        );

    // then
    verify(sparkService).findAll();
  }

  @Test
  void getCompanyByInnShouldReturnCompany() throws Exception {
    // given
    when(sparkService.findByInn("7725038124")).thenReturn(bankDto);
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/spark/companies/7725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "{\n"
            + "    \"inn\": \"7725038124\",\n"
            + "    \"ogrn\": \"1037739527077\",\n"
            + "    \"kpp\": \"770401001\",\n"
            + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"statusName\": \"Действующая\",\n"
            + "    \"statusDate\": \"2020-11-30\"\n"
            + "  }\n"
            )
        );

    // then
    verify(sparkService).findByInn("7725038124");
  }

  @Test
  void saveCompanyShouldReturnCompanyWithCorrectArgs() throws Exception {
    // given
    when(sparkService.save(any())).then(returnsFirstArg());
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
        .content("{\n"
                 + "    \"inn\": \"7725038124\",\n"
                 + "    \"ogrn\": \"1037739527077\",\n"
                 + "    \"kpp\": \"770401001\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n").contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            "{\n"
            + "    \"inn\": \"7725038124\",\n"
            + "    \"ogrn\": \"1037739527077\",\n"
            + "    \"kpp\": \"770401001\",\n"
            + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
            + "    \"statusName\": \"Действующая\",\n"
            + "    \"statusDate\": \"2020-11-30\"\n"
            + "  }\n"
            )
        );

    // then
    verify(sparkService).save(companyDtoArgumentCaptor.capture());
    CompanyDto result = companyDtoArgumentCaptor.getValue();
    assertAll(
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
  void updateCompanyShouldReturnCompanyWithCorrectArgs() throws Exception {
    // given
    when(sparkService.update(any(),any())).thenReturn(bankDto);
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/7725038124")
        .content("{\n"
                 + "    \"inn\": \"7777777777\",\n"
                 + "    \"ogrn\": \"1037739527077\",\n"
                 + "    \"kpp\": \"888888888\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n")
        .contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());

    // then
    verify(sparkService).update(any(),companyDtoArgumentCaptor.capture());
    CompanyDto result = companyDtoArgumentCaptor.getValue();
    assertAll(
        () -> assertEquals("7777777777", result.getInn()),
        () -> assertEquals("888888888", result.getKpp()),
        () -> assertEquals("1037739527077", result.getOgrn()),
        () -> assertEquals("АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"", result.getFullNameRus()),
        () -> assertEquals("АО\"БАНК ДОМ.РФ\"", result.getShortNameRus()),
        () -> assertEquals("Действующая", result.getStatusName()),
        () -> assertEquals(LocalDate
            .of(2020, 11, 30), result.getStatusDate())
    );
  }

  @Test
  void updateShouldReturn404IfNoCompanyForUpdate() throws Exception {
    //given
    when(sparkService.update(any(), any())).thenThrow(NoSuchEntityException.class);
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/7725038124")
        .content("{\n"
                 + "    \"inn\": \"7777777777\",\n"
                 + "    \"ogrn\": \"1037739527077\",\n"
                 + "    \"kpp\": \"888888888\",\n"
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
    when(sparkService.delete(any())).thenReturn(bankDto);
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/7725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    //then
    verify(sparkService).delete(argThat(inn -> inn.equals("7725038124")));
  }

  @Test
  void deleteCompanyShouldReturn404ifNotExist() throws Exception {
    // given
    when(sparkService.delete(any())).thenThrow(NoSuchEntityException.class);
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/9772503812"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void getCompanyByInnShouldThrowException_WhenPathInnIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/spark/companies/77725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"getCompanyByInn.inn: Inn should contain 10 or 12 digits\"\n"
            + "}]\n"
            )
        );
  }

  @Test
  void saveCompanyShouldThrowException_WhenOgrnInBodyIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
        .content("{\n"
                 + "    \"inn\": \"7725038124\",\n"
                 + "    \"ogrn\": \"10377395270778\",\n"
                 + "    \"kpp\": \"770401001\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n").contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"InvalidFormat (ogrn)\"\n"
            + "}]\n"
            )
        );
  }

  @Test
  void saveCompanyShouldThrowException_WhenInnInBodyIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
        .content("{\n"
                 + "    \"inn\": \"77250381240\",\n"
                 + "    \"ogrn\": \"1037739527077\",\n"
                 + "    \"kpp\": \"770401001\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n").contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"InvalidFormat (inn)\"\n"
            + "}]\n"
            )
        );
  }

  @Test
  void saveCompanyShouldThrowException_WhenKppInBodyIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/spark/companies/")
        .content("{\n"
                 + "    \"inn\": \"7725038120\",\n"
                 + "    \"ogrn\": \"1037739527071\",\n"
                 + "    \"kpp\": \"7704010012\",\n"
                 + "    \"fullNameRus\": \"АКЦИОНЕРНОЕ ОБЩЕСТВО \\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"shortNameRus\": \"АО\\\"БАНК ДОМ.РФ\\\"\",\n"
                 + "    \"statusName\": \"Действующая\",\n"
                 + "    \"statusDate\": \"2020-11-30\"\n"
                 + "  }\n").contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"InvalidFormat (kpp)\"\n"
            + "}]\n"
            )
        );
  }

  @Test
  void updateCompanyShouldThrowException_WhenPathInnIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/77250381242")
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
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"updateCompany.inn: Inn should contain 10 or 12 digits\"\n"
            + "}]\n"
            )
        );
  }

  @Test
  void updateCompanyShouldThrowException_WhenBodyIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/spark/companies/7725038124")
        .content("{\n"
                 + "        \"inn\": \"97051135531\",\n"
                 + "        \"ogrn\": \"51777462902880\",\n"
                 + "        \"kpp\": \"772501001\",\n"
                 + "        \"fullNameRus\": \"ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \\\"СЕВЕНТЕК\\\"\",\n"
                 + "        \"shortNameRus\": \"ООО \\\"7ТЕК\\\"\",\n"
                 + "        \"statusName\": \"Actual\",\n"
                 + "        \"statusDate\": \"2021-01-30\"\n"
                 + "    }\n")
        .contentType("application/json"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"InvalidFormat (ogrn)\"\n"
            + "},"
            + "{"
            + "   \"code\": \"ERROR003\",\n"
            + "   \"message\": \"InvalidFormat (inn)\"\n"
            + "}]\n"
            )
        );
  }

  @Test
  void deleteCompanyByInnShouldThrowException_WhenPathInnIsNotValid() throws Exception {
    // given
    // when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/77725038120"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().json(
            "\n"
            + "[{\n"
            + "    \"code\": \"ERROR003\",\n"
            + "    \"message\": \"deleteCompany.inn: Inn should contain 10 or 12 digits\"\n"
            + "}]\n"
            )
        );
  }
}
