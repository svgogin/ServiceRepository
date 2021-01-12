package ru.svgogin.service.spark.repository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.svgogin.service.spark.entity.Company;

@Repository
public class SparkRepository {
  private final Map<String, Company> companyMap = new LinkedHashMap<>();

  @PostConstruct
  public void init() {
    save(new Company("7729355614",
        "1027700262270",
        "770401001",
        "АКЦИОНЕРНОЕ ОБЩЕСТВО \"ДОМ.РФ\"",
        "АО\"ДОМ.РФ\"",
        "Действующая",
        LocalDate.of(2020, 12, 31)));

    save(new Company("7725038124",
        "1037739527077",
        "770401001",
        "АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"",
        "АО\"БАНК ДОМ.РФ\"",
        "Действующая",
        LocalDate.of(2020, 11, 30)));
  }

  public Iterable<Company> findAll() {
    return companyMap.values();
  }

  public Company save(Company company) {
    if (companyMap.containsKey(company.getInn())) {
      throw new IllegalArgumentException("Company inn = " + company.getInn()
                                         + " already exists");
    }
    companyMap.put(company.getInn(), company);
    return company;
  }

  public Company findByInn(String inn) {
    return companyMap.get(inn);
  }

  public Company update(String inn, Company company) {
    if (companyMap.containsKey(inn)) {
      companyMap.replace(inn, company);
      return company;
    } else {
      throw new IllegalArgumentException("Company Inn = " + inn + " doesn't exist");
    }
  }

  public void delete(String inn) {
    if (companyMap.containsKey(inn)) {
      companyMap.remove(inn);
    } else {
      throw new IllegalArgumentException("Company Inn = " + inn + " doesn't exist");
    }
  }
}

