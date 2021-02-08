package ru.svgogin.service.spark.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.repository.SparkRepositoryDb;

@Service
public class SparkService {
  private static final Logger log = LoggerFactory.getLogger(SparkService.class);
  private final SparkRepositoryDb sparkRepositoryDb;
  private final JdbcAggregateTemplate aggregateTemplate;

  public SparkService(SparkRepositoryDb sparkRepositoryDb,
                      JdbcAggregateTemplate aggregateTemplate) {
    this.sparkRepositoryDb = sparkRepositoryDb;
    this.aggregateTemplate = aggregateTemplate;
  }

  public Iterable<CompanyDto> findAll() {
    Iterable<Company> companies = this.sparkRepositoryDb.findAll();
    return StreamSupport.stream(companies.spliterator(), false)
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public Optional<CompanyDto> findByInn(String inn) {
    return this.sparkRepositoryDb.findByInn(inn).map(this::toDto);
  }

  public CompanyDto update(String inn, CompanyDto companyDto) {
    if (this.sparkRepositoryDb.existsByInn(inn)) {
      return toDto(aggregateTemplate.update(toEntity(companyDto)));
    } else {
      throw new IllegalArgumentException("Company with Inn = " + inn + " doesn't exist");
    }
  }

  public CompanyDto save(CompanyDto companyDto) {
    Company company = this.toEntity(companyDto);
    if (this.sparkRepositoryDb.existsByInn(company.getInn())) {
      throw new IllegalArgumentException("Company with Inn = "
                                         + company.getInn()
                                         + " already exists");
    } else {
      return toDto(aggregateTemplate.insert(company));
    }
  }

  public void delete(String inn) {
    this.sparkRepositoryDb.deleteByInn(inn);
  }

  private Company toEntity(CompanyDto companyDto) {
    return new Company(companyDto.getInn(),
        companyDto.getOgrn(),
        companyDto.getKpp(),
        companyDto.getFullNameRus(),
        companyDto.getShortNameRus(),
        companyDto.getStatusName(),
        companyDto.getStatusDate());
  }

  private CompanyDto toDto(Company company) {
    return new CompanyDto(company.getInn(),
        company.getOgrn(),
        company.getKpp(),
        company.getFullNameRus(),
        company.getShortNameRus(),
        company.getStatusName(),
        company.getStatusDate());
  }
}
