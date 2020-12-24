package ru.svgogin.service.spark.entity;

import java.util.Date;

public class Company {
  private String inn;
  private String ogrn;
  private String kpp;
  private String fullNameRus;
  private String shortNameRus;
  private String statusName;
  private Date statusDate;

  public Company(String inn, String ogrn, String kpp, String fullNameRus, String shortNameRus, String statusName,
                 Date statusDate) {
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

  public Date getStatusDate() {
    return statusDate;
  }
}
