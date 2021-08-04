package ru.svgogin.service.spark.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.repository.SparkRepositoryDb;

class SparkServiceTest {

  private final Company bank = new Company(
      BigInteger.valueOf(1),
      "7725038124",
      "1037739527077",
      "770401001",
      "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
      "АО\"БАНК ДОМ.РФ\"",
      "Действующая",
      LocalDate.of(2020, 11, 30));
  private final CompanyDto bankFromDb = new CompanyDto(
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
      BigInteger.valueOf(1),
      "07725038124",
      "01037739527077",
      "0770401001",
      "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
      "АО\"БАНК ДОМ.РФ\"",
      "Действующая",
      LocalDate.of(2020, 11, 30));
  private final JdbcAggregateTemplate aggregateTemplate = mock(JdbcAggregateTemplate.class);
  private final String bankInn = "7725038124";
  private SparkRepositoryDb sparkRepositoryDb;
  private SparkService sparkService;

  @BeforeEach
  void setUp() {
    System.out.println("set up");
    sparkRepositoryDb = mock(SparkRepositoryDb.class);

    sparkService = new SparkService(sparkRepositoryDb, aggregateTemplate);
  }

  @Test
  @DisplayName("findAll should call repository.findAll")
  void findAllShouldCallRepository() {
    // when
    sparkService.findAll();
    // then
    verify(sparkRepositoryDb).findAll();
  }

  @Test
  @DisplayName("findAll should return dtos")
  void findAllShouldReturnDto() {
    // given
    when(sparkRepositoryDb.findAll()).thenReturn(List.of(test7tec, bank));
    // when
    var result = sparkService.findAll();
    // then
    var companyDto = result.iterator().next();
    assertAll(
        () -> assertEquals(2, result.spliterator().getExactSizeIfKnown()),
        () -> assertEquals("9705113553", companyDto.getInn()),
        () -> assertEquals("772501001", companyDto.getKpp()),
        () -> assertEquals("5177746290288", companyDto.getOgrn()),
        () -> assertEquals("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"СЕВЕНТЕК\"", companyDto.getFullNameRus()),
        () -> assertEquals("ООО \"7ТЕК\"", companyDto.getShortNameRus()),
        () -> assertEquals("Actual", companyDto.getStatusName()),
        () -> assertEquals(LocalDate
            .of(2021, 1, 30), companyDto.getStatusDate())
    );
  }

  @Test
  @DisplayName("findByInn should call RepositoryDb.findByInn")
  void findByInnShouldCallRepository() {
    // given
    String inn = "9705113553";
    when(sparkRepositoryDb.findByInn(inn)).thenReturn(Optional.of(test7tec));
    // when
    sparkService.findByInn("9705113553");
    // then
    verify(sparkRepositoryDb).findByInn(ArgumentMatchers.anyString());
  }


  @Test
  @DisplayName("findByInn should return Dto with the same inn as in-parameter")
  void findByInnShouldReturnObjectWithInn() {
    // given
    String inn = "9705113553";
    when(sparkRepositoryDb.findByInn(inn)).thenReturn(Optional.of(test7tec));
    //when
    var result = sparkService.findByInn(inn);
    // then
    assertAll(
        () -> assertEquals("9705113553", result.getInn()),
        () -> assertEquals("772501001", result.getKpp()),
        () -> assertEquals("5177746290288", result.getOgrn()),
        () -> assertEquals("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ \"СЕВЕНТЕК\"", result.getFullNameRus()),
        () -> assertEquals("ООО \"7ТЕК\"", result.getShortNameRus()),
        () -> assertEquals("Actual", result.getStatusName()),
        () -> assertEquals(LocalDate
            .of(2021, 1, 30), result.getStatusDate())
    );
  }

  @Test
  @DisplayName("update should call aggregateTemplate.update and update entities")
  void updateShouldCallRepository() {
    //given
    when(sparkRepositoryDb.findByInn("7725038124")).thenReturn(Optional.of(bank));
    when(aggregateTemplate.update(any())).then(returnsFirstArg());
    //when
    var result = sparkService.update("7725038124", bankDto);
    //then
    verify(aggregateTemplate).update(ArgumentMatchers.any(Company.class));
    assertAll(
        () -> assertNotNull(result),
        () -> assertSame(bank.getFullNameRus(), result.getFullNameRus()),
        () -> assertNotEquals(bank.getInn(), result.getInn())
    );
  }

  @Test
  @DisplayName("save should insert entities to the DB")
  void save() {
    //given
    when(aggregateTemplate.insert(any())).then(returnsFirstArg());
    //when
    var result = sparkService.save(bankDto);
    //then
    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(result.getInn(), "07725038124"),
        () -> assertNotEquals(result.getInn(), "7725038124")
    );
  }

  @Test
  @DisplayName("save should throw exception if company already exists")
  void saveShouldThrowException() {
    //given
    when(sparkRepositoryDb.existsByInn(bankDto.getInn())).thenReturn(true);
    //when
    assertThrows(EntityAlreadyExistsException.class, () -> sparkService.save(bankDto));
  }

  @Test
  @DisplayName("delete should delete entities from the DB")
  void deleteShouldDeleteEntities() {
    //given
    when(sparkRepositoryDb.findByInn("7725038124")).thenReturn(Optional.of(bank));
    //when
    sparkService.delete("7725038124");
    //then
    verify(sparkRepositoryDb).deleteById(bank.getId());
  }
}
