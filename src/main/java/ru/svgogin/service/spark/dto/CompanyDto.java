package ru.svgogin.service.spark.dto;

import java.time.LocalDate;

public class CompanyDto {
  private final String inn;
  private final String ogrn;
  private final String kpp;
  private final String fullNameRus;
  private final String shortNameRus;
  private final String statusName;
  private final LocalDate statusDate;

  public CompanyDto(String inn,
                    String ogrn,
                    String kpp,
                    String fullNameRus,
                    String shortNameRus,
                    String statusName,
                    LocalDate statusDate) {
    this.inn = inn;
    this.ogrn = ogrn;
    this.kpp = kpp;
    this.fullNameRus = fullNameRus;
    this.shortNameRus = shortNameRus;
    this.statusName = statusName;
    this.statusDate = statusDate;
  }

  public String getInn() {
    return this.inn;
  }

  public String getOgrn() {
    return this.ogrn;
  }

  public String getKpp() {
    return this.kpp;
  }

  public String getFullNameRus() {
    return this.fullNameRus;
  }

  public String getShortNameRus() {
    return this.shortNameRus;
  }

  public String getStatusName() {
    return this.statusName;
  }

  public LocalDate getStatusDate() {
    return this.statusDate;
  }
}
