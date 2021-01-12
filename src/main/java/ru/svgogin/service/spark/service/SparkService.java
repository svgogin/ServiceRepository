package ru.svgogin.service.spark.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.entity.Company;
import ru.svgogin.service.spark.repository.SparkRepository;


@Service
public class SparkService {
  private final SparkRepository sparkRepository;

  public SparkService(SparkRepository sparkRepository) {
    this.sparkRepository = sparkRepository;
  }

  public Iterable<CompanyDto> findAll() {
    Iterable<Company> companies = sparkRepository.findAll();
    return StreamSupport.stream(companies.spliterator(), false)
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public CompanyDto findByInn(String inn) {
    return toDto(sparkRepository.findByInn(inn));
  }

  public CompanyDto update(String inn, CompanyDto companyDto) {
    return toDto(sparkRepository.update(inn, toEntity(companyDto)));
  }

  public CompanyDto save(CompanyDto companyDto) {
    return toDto(sparkRepository.save(toEntity(companyDto)));
  }

  public void delete(String inn) {
    sparkRepository.delete(inn);
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
