package com.dominion.nodemcu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dominion.nodemcu.entity.Device;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RepositoryRestResource(collectionResourceRel = "device", path = "device")
public interface DeviceRepository extends CrudRepository<Device, Long> {

	//List<Device> findByFuelFormula(String fuelFormula);
	//List<Device> findByManufacturer(String lastName);
	Optional<Device> findById(Long id);
	Optional<Device> findByAccount(Long id);
	@Query("SELECT d FROM Device d WHERE d.serialId=:serialid")
	Optional<Device> findBySerialId(@Param("serialid") String serialid);
}
