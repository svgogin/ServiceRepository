package ru.svgogin.service.spark.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.svgogin.service.spark.entity.Company;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SparkRepositoryTest {
  @Autowired
  private SparkRepositoryDb sparkRepositoryDb;

  @AfterEach
  void tearDown() {
    sparkRepositoryDb.deleteAll();
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

  @Test
  void shouldReturnNotExistsForEmptyDatabase() {
    // when
    boolean exists = sparkRepositoryDb.existsByInn("1234567890");
    // then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldReturnEntityIfExists () {
    //given
    // when
    var result = sparkRepositoryDb.save(bank);
    //then
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
  void shouldUpdateEntityIfExists () {
    //given
    var result = sparkRepositoryDb.save(bank);

    Company bankUpd = new Company(
        result.getId(),
        "7777711111",
        "1234567891234",
        "0770401001",
        "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
        "АО\"БАНК ДОМ.РФ\"",
        "Действующая",
        LocalDate.of(2020, 11, 30));
    //when
    var update = sparkRepositoryDb.save(bankUpd); //works as update, because bankUpd has the same id result
    //then
    var allRows = sparkRepositoryDb.findAll();

    assertAll(
        () -> assertEquals(1, allRows.spliterator().getExactSizeIfKnown()),
        () -> assertEquals(result.getId(), update.getId()),
        () -> assertEquals("7777711111", update.getInn()),
        () -> assertEquals("0770401001", update.getKpp()),
        () -> assertEquals("1234567891234", update.getOgrn()),
        () -> assertEquals("АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"", update.getFullNameRus()),
        () -> assertEquals("АО\"БАНК ДОМ.РФ\"", update.getShortNameRus()),
        () -> assertEquals("Действующая", update.getStatusName()),
        () -> assertEquals(LocalDate
            .of(2020, 11, 30), update.getStatusDate())
    );
  }

  @Test
  void shouldReturnNotExistsAfterDelete () {
    //given
    var savedBank = sparkRepositoryDb.save(bank);
    //when
    sparkRepositoryDb.deleteById(savedBank.getId());
    //then
    boolean exists = sparkRepositoryDb.existsByInn("07725038124");
    var allRows = sparkRepositoryDb.findAll();
    assertThat(exists).isFalse();
    assertThat(allRows).isEmpty();
  }
}
