package ru.svgogin.service.spark.entity;

import java.math.BigInteger;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


public class Company {
  @Column("inn")
  private final String inn;
  @Column("ogrn")
  private final String ogrn;
  @Column("kpp")
  private final String kpp;
  @Column("full_name_rus")
  private final String fullNameRus;
  @Column("short_name_rus")
  private final String shortNameRus;
  @Column("status_name")
  private final String statusName;
  @Column("status_date")
  private final LocalDate statusDate;
  @Id
  private final BigInteger id;

  public Company(
      BigInteger id,
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
