package org.gdf.repository;

import org.gdf.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccessRepository extends JpaRepository<Access, Integer> {
	
	@Query("SELECT a FROM Access a WHERE a.email = ?1")
	public Access findByEmail(String email);

}
