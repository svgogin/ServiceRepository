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
  @Column("fullnamerus")
  private final String fullNameRus;
  @Column("shortnamerus")
  private final String shortNameRus;
  @Column("statusname")
  private final String statusName;
  @Column("statusdate")
  private final LocalDate statusDate;
  @Id
  @Column("id")
  private BigInteger id;

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
