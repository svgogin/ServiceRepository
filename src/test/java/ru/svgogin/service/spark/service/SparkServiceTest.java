package ru.svgogin.service.spark.service;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.repository.SparkRepositoryDb;

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
      "07725038124",
      "01037739527077",
      "0770401001",
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

  @Test
  @DisplayName("update should call aggregateTemplate.update and update entities")
  void updateShouldCallRepository() {
    //given
    Mockito.when(sparkRepositoryDb.findByInn("7725038124"))
        .thenReturn(java.util.Optional.of(bank)
        );
    Mockito.when(aggregateTemplate.update(any())).then(returnsFirstArg());
    //when
    var result = sparkService.update("7725038124",bankDto);
    //then
    Mockito.verify(aggregateTemplate).update(ArgumentMatchers.any(Company.class));
    Assertions.assertNotNull(result);
    Assertions.assertSame(bank.getFullNameRus(),result.getFullNameRus());
    Assertions.assertNotEquals(bank.getInn(),result.getInn());
  }

  @Test
  @DisplayName("save should insert entities to the DB")
  void save() {
    //given
    Mockito.when(aggregateTemplate.insert(any())).then(returnsFirstArg());
    //when
    var result = sparkService.save(bankDto);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getInn(),"07725038124");
    Assertions.assertNotEquals(result.getInn(),"7725038124");
  }

  @Test
  @DisplayName("delete should delete entities from the DB")
  void deleteShouldDeleteEntities(){
    //given
    Mockito.when(sparkRepositoryDb.findByInn("7725038124"))
        .thenReturn(java.util.Optional.of(bank)
        );
    //when
    sparkService.delete("7725038124");
    //then
    Mockito.verify(sparkRepositoryDb).deleteById(BigInteger.valueOf(1));
  }
  @Test
  @DisplayName("delete should throw exception if doesn't exist")
  void deleteShouldThrowException() {
    //given
    Mockito.when(sparkRepositoryDb.findByInn("7725038124")).thenReturn(Optional.empty());
    //when
    Assertions.assertThrows(IllegalArgumentException.class, () -> sparkService.delete("7725038124"));
  }
}
