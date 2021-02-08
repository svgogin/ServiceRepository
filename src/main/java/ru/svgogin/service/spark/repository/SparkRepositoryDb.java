package ru.svgogin.service.spark.repository;

import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.svgogin.service.spark.entity.Company;

public interface SparkRepositoryDb extends CrudRepository<Company, BigInteger> {
  Optional<Company> findByInn(String inn);

  boolean existsByInn(String inn);

  void deleteByInn(String inn);
}
