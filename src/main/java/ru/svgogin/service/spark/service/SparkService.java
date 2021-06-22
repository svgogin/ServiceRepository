package ru.svgogin.service.spark.service;

import static java.util.Objects.requireNonNullElse;

import java.math.BigInteger;
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
    Iterable<Company> companies = sparkRepositoryDb.findAll();
    return StreamSupport.stream(companies.spliterator(), false)
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public Optional<CompanyDto> findByInn(String inn) {
    return sparkRepositoryDb.findByInn(inn).map(this::toDto);
  }

  public CompanyDto update(CompanyDto companyFromDb, CompanyDto companyDto) {
    Company companyForUpdate = actualize(companyFromDb, companyDto);
    return toDto(aggregateTemplate.update(companyForUpdate));
  }

  public CompanyDto save(CompanyDto companyDto) {
      return toDto(aggregateTemplate.insert(toEntity(companyDto)));
    }

  public void delete(BigInteger id) {
    sparkRepositoryDb.deleteById(id);
  }

  private Company toEntity(CompanyDto companyDto) {
    return new Company(
        null,
        companyDto.getInn(),
        companyDto.getOgrn(),
        companyDto.getKpp(),
        companyDto.getFullNameRus(),
        companyDto.getShortNameRus(),
        companyDto.getStatusName(),
        companyDto.getStatusDate());
  }

  private CompanyDto toDto(Company company) {
    return new CompanyDto(
        company.getId(),
        company.getInn(),
        company.getOgrn(),
        company.getKpp(),
        company.getFullNameRus(),
        company.getShortNameRus(),
        company.getStatusName(),
        company.getStatusDate());
  }

  private Company actualize(CompanyDto companyFromDb, CompanyDto companyDto) {
    return new Company(
        companyFromDb.getId(),
        requireNonNullElse(companyDto.getInn(), companyFromDb.getInn()),
        requireNonNullElse(companyDto.getOgrn(), companyFromDb.getOgrn()),
        requireNonNullElse(companyDto.getKpp(), companyFromDb.getKpp()),
        requireNonNullElse(companyDto.getFullNameRus(), companyFromDb.getFullNameRus()),
        requireNonNullElse(companyDto.getShortNameRus(), companyFromDb.getShortNameRus()),
        requireNonNullElse(companyDto.getStatusName(), companyFromDb.getStatusName()),
        requireNonNullElse(companyDto.getStatusDate(), companyFromDb.getStatusDate()));
  }
}
