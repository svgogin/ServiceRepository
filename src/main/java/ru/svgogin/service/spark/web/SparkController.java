package ru.svgogin.service.spark.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.svgogin.service.spark.dto.CompanyDto;
import ru.svgogin.service.spark.service.SparkService;
import java.util.function.Supplier;


@RestController
@RequestMapping("/spark/companies")
public class SparkController {
  private final SparkService sparkService;

  public SparkController(SparkService sparkService) {
    this.sparkService = sparkService;
  }

  @GetMapping()
  public Iterable<CompanyDto> getCompanies() {
    return sparkService.findAll();
  }

  @GetMapping("/{inn}")
  public ResponseEntity<CompanyDto> getCompanyByInn(@PathVariable("inn") String inn) {
    var company = sparkService.findByInn(inn);
    return company.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping()
  public CompanyDto saveCompany(@RequestBody CompanyDto companyDto) {
    return sparkService.save(companyDto);
  }

  @PutMapping("/{inn}")
  public CompanyDto updateCompany(@PathVariable("inn") String inn,
                                  @RequestBody CompanyDto companyDto) {
    return sparkService.update(inn, companyDto);
  }

  @DeleteMapping("/{inn}")
  public void deleteCompany(@PathVariable("inn") String inn) {
    sparkService.delete(inn);
  }
}
