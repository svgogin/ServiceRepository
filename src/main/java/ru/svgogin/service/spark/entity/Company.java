package ru.svgogin.service.spark.entity;

import java.math.BigInteger;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Company {
  @Id
  @Column("id")
  private BigInteger id;
  @Column("inn")
  private final String inn;
  @Column("ogrn")
  private final String ogrn;
  @Column("kpp")
  private final String kpp;
  @Column("fullNameRus")
  private final String fullNameRus;
  @Column("shortNameRus")
  private final String shortNameRus;
  @Column("statusName")
  private final String statusName;
  @Column("statusDate")
  private final LocalDate statusDate;


  public Company(String inn,
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
