package ru.svgogin.service.spark.web;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.service.SparkService;

@WebMvcTest(value = SparkController.class)
public class SparkControllerTest {

  private final CompanyDto bankDto = new CompanyDto(
      BigInteger.valueOf(1),
      "07725038124",
      "01037739527077",
      "0770401001",
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
    Mockito.when(sparkService.findAll())
        .thenReturn(List.of(bankDto));
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
            + "  }\n"
            + "]")
        );

    // then
    Mockito.verify(sparkService).findAll();
  }

  @Test
  void getCompanyByInnShouldReturnCompany() throws Exception {
    // given
    Mockito.when(sparkService.findByInn("07725038124"))
        .thenReturn(Optional.of(bankDto));
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

    // then
    Mockito.verify(sparkService).findByInn("07725038124");
  }

  @Test
  void saveCompanyShouldReturnCompanyWithCorrectArgs() throws Exception {
    // given
    Mockito.when(sparkService.save(any())).then(returnsFirstArg());
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
    Mockito.verify(sparkService).save(companyDtoArgumentCaptor.capture());
    CompanyDto result = companyDtoArgumentCaptor.getValue();
    assertAll(
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
  void updateCompanyShouldReturnCompanyWithCorrectArgs() throws Exception {
    // given
    Mockito.when(sparkService.findByInn(any())).thenReturn(Optional.of(bankDto));
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
        .andExpect(MockMvcResultMatchers.status().isOk());

    // then
    Mockito.verify(sparkService).update(eq(bankDto), companyDtoArgumentCaptor.capture());
    CompanyDto result = companyDtoArgumentCaptor.getValue();
    assertAll(
        () -> assertEquals("77777777", result.getInn()),
        () -> assertEquals("8888888888", result.getKpp()),
        () -> assertEquals("01037739527077", result.getOgrn()),
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
    Mockito.when(sparkService.findByInn(any())).thenReturn(Optional.empty());
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
    Mockito.when(sparkService.findByInn(any())).thenReturn(Optional.of(bankDto));
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/07725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    //then
    Mockito.verify(sparkService).delete(argThat(id -> id.equals(BigInteger.valueOf(1))));
  }

  @Test
  void deleteCompanyShouldReturn404ifNotExist() throws Exception {
    // given
    Mockito.when(sparkService.findByInn(any())).thenReturn(Optional.empty());
    //when
    mockMvc.perform(MockMvcRequestBuilders.delete("/spark/companies/97725038124"))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
