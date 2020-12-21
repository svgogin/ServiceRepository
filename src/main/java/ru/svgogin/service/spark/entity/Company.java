package ru.svgogin.service.spark.entity;

import java.util.Date;

public class Company {
  private String inn;
  private String ogrn;
  private String kpp;
  private String full_name_rus;
  private String short_name_rus;
  private String status_name;
  private Date status_date;

  public Company(String inn, String ogrn, String kpp, String full_name_rus, String short_name_rus, String status_name,
                 Date status_date) {
    this.inn = inn;
    this.ogrn = ogrn;
    this.kpp = kpp;
    this.full_name_rus = full_name_rus;
    this.short_name_rus = short_name_rus;
    this.status_name = status_name;
    this.status_date = status_date;
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

  public String getFull_name_rus() {
    return full_name_rus;
  }

  public String getShort_name_rus() {
    return short_name_rus;
  }

  public String getStatus_name() {
    return status_name;
  }

  public Date getStatus_date() {
    return status_date;
  }
}
