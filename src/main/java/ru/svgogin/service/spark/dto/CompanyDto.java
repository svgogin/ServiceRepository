package ru.svgogin.service.spark.dto;

import java.time.LocalDate;
import java.util.Date;

public class CompanyDto {
  private final String inn;
  private final String ogrn;
  private final String kpp;
  private final String fullNameRus;
  private final String shortNameRus;
  private final String statusName;
  private final LocalDate statusDate;

  public CompanyDto(String inn, String ogrn, String kpp, String fullNameRus, String shortNameRus, String statusName,
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
