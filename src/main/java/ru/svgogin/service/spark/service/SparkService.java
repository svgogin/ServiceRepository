package ru.svgogin.service.spark.service;

import org.springframework.stereotype.Service;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class SparkService {
  private final Map<String, Company> COMPANY_MAP = new LinkedHashMap<>();

  @PostConstruct
  public void init() {
    save(new Company("7729355614","1027700262270","770401001","АКЦИОНЕРНОЕ ОБЩЕСТВО \"ДОМ.РФ\"","АО\"ДОМ.РФ\"","Действующая",
        LocalDate.of(2020,12,31)));
    save(new Company("7725038124","1037739527077","770401001","АКЦИОНЕРНОЕ ОБЩЕСТВО \"БАНК ДОМ.РФ\"","АО\"БАНК ДОМ.РФ\"","Действующая",
        LocalDate.of(2020,11,30)));
  }

  public Iterable<Company> findAll() {
    return COMPANY_MAP.values();
  }

  public void save(Company company) {
    if (COMPANY_MAP.containsKey(company.getInn())) {
      throw new IllegalArgumentException("Company inn = " + company.getInn() + " already exists");
    }
    COMPANY_MAP.put(company.getInn(), company);
  }

  public Company findByInn(String inn) {
    return COMPANY_MAP.get(inn);
  }
  private Company toEntity(CompanyDto companyDto) {
    return new Company(companyDto.getInn(), companyDto.getOgrn(), companyDto.getKpp(), companyDto.getFullNameRus(),
        companyDto.getShortNameRus(), companyDto.getStatusName(), companyDto.getStatusDate());
  }

  private CompanyDto toDto(Company company) {
    return new CompanyDto(company.getInn(), company.getOgrn(), company.getKpp(), company.getFullNameRus(),
        company.getShortNameRus(), company.getStatusName(), company.getStatusDate());
  }
}
