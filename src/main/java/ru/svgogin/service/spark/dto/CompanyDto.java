package ru.svgogin.service.spark.dto;

import java.math.BigInteger;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CompanyDto {
  private final BigInteger id;
  @NotBlank(message = "Inn is mandatory")
  @Pattern(regexp = "^(\\d{10}|\\d{12})$")
  private final String inn;
  @NotBlank(message = "Ogrn is mandatory")
  @Pattern(regexp = "^(\\d{13}|\\d{15})$")
  private final String ogrn;
  @Pattern(regexp = "^(\\d{9})$")
  private final String kpp;
  private final String fullNameRus;
  private final String shortNameRus;
  private final String statusName;
  private final LocalDate statusDate;


  public CompanyDto(BigInteger id,
                    String inn,
                    String ogrn,
                    String kpp,
                    String fullNameRus,
                    String shortNameRus,
                    String statusName,
                    LocalDate statusDate) {
    this.id = id;
    this.inn = inn;
    this.ogrn = ogrn;
    this.kpp = kpp;
    this.fullNameRus = fullNameRus;
    this.shortNameRus = shortNameRus;
    this.statusName = statusName;
    this.statusDate = statusDate;
  }

  public BigInteger getId() {
    return id;
  }

  public String getInn() {
    return inn;
  }

  public String getOgrn() {
    return ogrn;
  }

  public String getKpp() {
    return kpp;
  }

  public String getFullNameRus() {
    return fullNameRus;
  }

  public String getShortNameRus() {
    return shortNameRus;
  }

  public String getStatusName() {
    return statusName;
  }

  public LocalDate getStatusDate() {
    return statusDate;
  }
}
