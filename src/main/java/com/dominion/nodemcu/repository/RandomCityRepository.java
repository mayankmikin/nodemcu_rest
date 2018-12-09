package com.dominion.nodemcu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dominion.nodemcu.entity.RandomCity;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RepositoryRestResource(collectionResourceRel = "city", path = "city")
public interface RandomCityRepository extends CrudRepository<RandomCity, Long> {
}
