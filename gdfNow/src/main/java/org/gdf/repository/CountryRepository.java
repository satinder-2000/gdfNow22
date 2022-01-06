package org.gdf.repository;

import org.gdf.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CountryRepository extends JpaRepository<Country, Integer> {
	
	@Query("SELECT c FROM Country c WHERE c.code = ?1")
	public Country findByCode(String countryCode);

}
