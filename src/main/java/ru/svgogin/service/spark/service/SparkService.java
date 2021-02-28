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

  public CompanyDto update(String inn, CompanyDto companyDto) {
    var companyForUpdate = sparkRepositoryDb.findByInn(inn);
    if (companyForUpdate.isPresent()) {
      Company company = actualize(companyForUpdate.get(), companyDto);
      return toDto(aggregateTemplate.update(company));
    } else {
      throw new IllegalArgumentException("Company with Inn = " + inn + " doesn't exist");
    }
  }

  public CompanyDto save(CompanyDto companyDto) {
    Company company = toEntity(companyDto);
    if (sparkRepositoryDb.existsByInn(company.getInn())) {
      throw new IllegalArgumentException("Company with Inn = "
                                         + company.getInn()
                                         + " already exists");
    } else {
      return toDto(aggregateTemplate.insert(company));
    }
  }

  public void delete(String inn) {
    var companyOptional = sparkRepositoryDb.findByInn(inn);
    if (companyOptional.isPresent()) {
      BigInteger id = companyOptional.get().getId();
      sparkRepositoryDb.deleteById(id);
    } else {
      throw new IllegalArgumentException("Company with Inn = "
                                         + inn
                                         + " doesn't exist");
    }
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
        company.getInn(),
        company.getOgrn(),
        company.getKpp(),
        company.getFullNameRus(),
        company.getShortNameRus(),
        company.getStatusName(),
        company.getStatusDate());
  }

  private Company actualize(Company company, CompanyDto companyDto) {
    return new Company(
        company.getId(),
        requireNonNullElse(companyDto.getInn(), company.getInn()),
        requireNonNullElse(companyDto.getOgrn(), company.getOgrn()),
        requireNonNullElse(companyDto.getKpp(), company.getKpp()),
        requireNonNullElse(companyDto.getFullNameRus(), company.getFullNameRus()),
        requireNonNullElse(companyDto.getShortNameRus(), company.getShortNameRus()),
        requireNonNullElse(companyDto.getStatusName(), company.getStatusName()),
        requireNonNullElse(companyDto.getStatusDate(), company.getStatusDate()));
  }
}
