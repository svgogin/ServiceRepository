package ru.svgogin.service.spark.web;

import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.svgogin.service.spark.exception.EntityAlreadyExistsException;
import ru.svgogin.service.spark.service.SparkService;


@RestController
@RequestMapping("/spark/companies")
public class SparkController {
  private final SparkService sparkService;
  private static final Logger log = LoggerFactory.getLogger(SparkController.class);

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
    return company.map(ResponseEntity::ok).orElseGet(() ->
        new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping()
  public ResponseEntity<CompanyDto> saveCompany(@RequestBody CompanyDto companyDto) {
    var companyInDb = sparkService.findByInn(companyDto.getInn());
    if (companyInDb.isEmpty()) {
      return new ResponseEntity<>(sparkService.save(companyDto), HttpStatus.OK);
    } else {
      throw new EntityAlreadyExistsException("Company with inn " + companyDto.getInn() + " already exists");
    }
  }

  @PutMapping("/{inn}")
  public ResponseEntity<CompanyDto> updateCompany(@PathVariable("inn") String inn,
                                                  @RequestBody CompanyDto companyDto) {
    var companyFromDb = sparkService.findByInn(inn);
    return companyFromDb.map(dto ->
        new ResponseEntity<>(sparkService.update(dto, companyDto), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{inn}")
  public ResponseEntity<CompanyDto> deleteCompany(@PathVariable("inn") String inn) {
    var companyOptional = sparkService.findByInn(inn);
    if (companyOptional.isPresent()) {
      BigInteger id = companyOptional.get().getId();
      sparkService.delete(id);
      return new ResponseEntity<>(companyOptional.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
