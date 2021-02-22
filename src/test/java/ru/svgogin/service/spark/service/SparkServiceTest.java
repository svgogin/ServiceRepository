package ru.svgogin.service.spark.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.repository.SparkRepositoryDb;

import javax.lang.model.UnknownEntityException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class SparkServiceTest {

  private SparkRepositoryDb sparkRepositoryDb;
  private SparkService sparkService;
  private final Company bank = new Company(
      BigInteger.valueOf(1),
      "7725038124",
      "1037739527077",
      "770401001",
      "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
      "АО\"БАНК ДОМ.РФ\"",
      "Действующая",
      LocalDate.of(2020, 11, 30));
  private final Company test7tec = new Company(
      BigInteger.valueOf(2),
      "9705113553",
      "5177746290288",
      "772501001",
      "ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"СЕВЕНТЕК\"",
      "ООО \"7ТЕК\"",
      "Actual",
      LocalDate.of(2021, 1, 30));
  private final CompanyDto bankDto = new CompanyDto(
      "7725038124",
      "1037739527077",
      "770401001",
      "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
      "АО\"БАНК ДОМ.РФ\"",
      "Действующая",
      LocalDate.of(2020, 11, 30));
  private final JdbcAggregateTemplate aggregateTemplate = Mockito.mock(JdbcAggregateTemplate.class);
  private final String bankInn = "7725038124";

  @BeforeEach
  void setUp() {
    System.out.println("set up");
    sparkRepositoryDb = Mockito.mock(SparkRepositoryDb.class);

    sparkService = new SparkService(sparkRepositoryDb, aggregateTemplate);
  }

  @AfterEach
  void tearDown() {
    System.out.println("tear down");
  }

  @Test
  @DisplayName("findAll should call repository.findAll")
  void findAllShouldCallRepository() {
    // when
    sparkService.findAll();
    // then
    Mockito.verify(sparkRepositoryDb).findAll();
  }

  @Test
  @DisplayName("findAll should return dtos")
  void findAllShouldReturnDto() {
  // given
    Mockito.when(sparkRepositoryDb.findAll())
        .thenReturn(List.of(test7tec,
            bank)
            );
  // when
  var result = sparkService.findAll();
  // then
    Assertions.assertEquals(2, result.spliterator().getExactSizeIfKnown());
  var companyDto = result.iterator().next();
    Assertions.assertEquals("9705113553", companyDto.getInn());
    Assertions.assertEquals("772501001", companyDto.getKpp());
    Assertions.assertEquals("5177746290288", companyDto.getOgrn());
    Assertions.assertEquals("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"СЕВЕНТЕК\"", companyDto.getFullNameRus());
    Assertions.assertEquals("ООО \"7ТЕК\"", companyDto.getShortNameRus());
    Assertions.assertEquals("Actual", companyDto.getStatusName());
    Assertions.assertEquals(LocalDate
        .of(2021, 1, 30), companyDto.getStatusDate());
}

  @Test
  @DisplayName("findByInn should call RepositoryDb.findByInn")
  void findByInnShouldCallRepository() {
    // when
    sparkService.findByInn(ArgumentMatchers.anyString());
    // then
    Mockito.verify(sparkRepositoryDb).findByInn(ArgumentMatchers.anyString());
  }


  @Test
  @DisplayName("findByInn should return Dto with the same inn as in-parameter")
  void findByInnShouldReturnObjectWithInn() {
    // given
    String inn = "9705113553";
    Mockito.when(sparkRepositoryDb.findByInn(inn))
        .thenReturn(java.util.Optional.of(test7tec)
        );
    //when
    var result = sparkService.findByInn(inn).orElse(bankDto);
    // then
    Assertions.assertEquals("9705113553", result.getInn());
    Assertions.assertEquals("772501001", result.getKpp());
    Assertions.assertEquals("5177746290288", result.getOgrn());
    Assertions.assertEquals("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"СЕВЕНТЕК\"", result.getFullNameRus());
    Assertions.assertEquals("ООО \"7ТЕК\"", result.getShortNameRus());
    Assertions.assertEquals("Actual", result.getStatusName());
    Assertions.assertEquals(LocalDate
        .of(2021, 1, 30), result.getStatusDate());
  }

  //Matchers Problem
  @Test
  @DisplayName("update should call aggregateTemplate.update")
  void updateShouldCallRepository() {
    // when
    sparkService.update(eq("String by matcher"),any(CompanyDto.class));
    //then
    Mockito.verify(aggregateTemplate).update(ArgumentMatchers.any(Company.class));
  }

  /*@Test
  @DisplayName("update should update companies in DB")
  void updateShouldChangeCompaniesInDb() {
    //given
    Mockito.when(sparkRepositoryDb.findById(any(BigInteger.class))).thenReturn(java.util.Optional.of(test7tec));
    Mockito.when(aggregateTemplate.update(any(Company.class))).then(AdditionalAnswers.returnsFirstArg());
    // when
    var result = sparkService.update("9705113553",bankDto);
    //then
    Assertions.assertEquals("7725038124",result.getInn());
  }
*/
  /*@Test
  @DisplayName("save should insert entities to the DB")
  void save() {
    //given
    Mockito.when(aggregateTemplate.insert(bank)).thenReturn(bank);
    //when
    var result = sparkService.save(bankDto);
    //then
    Assertions.assertEquals(result.getInn(),bank.getInn());
  }*/
/*
  @Test
  void delete() {
  }*/
}
