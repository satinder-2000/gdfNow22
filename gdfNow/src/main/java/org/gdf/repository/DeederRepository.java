package org.gdf.repository;

import org.gdf.model.Deeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeederRepository extends JpaRepository<Deeder, Integer> {
	
	@Query("select d from Deeder d where d.email=?1")
	public Deeder findByEmail(String email);
	
}
