package ru.svgogin.service.spark.service;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.StreamSupport.stream;
import static ru.svgogin.service.spark.errordto.ErrorDto.ErrorCode.ERROR001;
import static ru.svgogin.service.spark.errordto.ErrorDto.ErrorCode.ERROR002;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.exception.NoSuchEntityException;
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
    log.info("All companies were successfully returned.");
    return stream(companies.spliterator(), false)
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public CompanyDto findByInn(String inn) {
    var companyDto = sparkRepositoryDb.findByInn(inn)
        .map(this::toDto)
        .orElseThrow(() ->
            new NoSuchEntityException(String.format("%s (%s)", ERROR002.label, inn)));
    log.info("Company with inn {} was successfully returned.", inn);
    return companyDto;
  }

  public CompanyDto update(String inn, CompanyDto companyDto) {
    var companyDtoEx = sparkRepositoryDb.findByInn(inn)
        .map(this::toDto)
        .orElseThrow(() ->
            new NoSuchEntityException(String.format("%s (%s)", ERROR002.label, inn)));
    Company companyForUpdate = actualize(companyDtoEx, companyDto);
    var updatedCompany = aggregateTemplate.update(companyForUpdate);
    log.info("Company with inn {} was successfully updated.", inn);
    return toDto(updatedCompany);
  }

  public CompanyDto save(CompanyDto companyDto) {
    var inn = companyDto.getInn();
    var companyInDb = sparkRepositoryDb.existsByInn(inn);
    if (companyInDb) {
      throw new EntityAlreadyExistsException(String.format("%s (%s)", ERROR001.label, inn));
    }
    var savedCompany = aggregateTemplate.insert(toEntity(companyDto));
    log.info("Company with inn {} was successfully saved.", inn);
    return toDto(savedCompany);
  }

  public CompanyDto delete(String inn) {
    var companyDto = sparkRepositoryDb.findByInn(inn)
        .map(this::toDto)
        .orElseThrow(() ->
            new NoSuchEntityException(String.format("%s (%s)", ERROR002.label, inn)));
    sparkRepositoryDb.deleteById(companyDto.getId());
    log.info("Company with inn {} was successfully deleted.", inn);
    return companyDto;
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
