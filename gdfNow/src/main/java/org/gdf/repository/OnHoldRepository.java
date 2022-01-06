package org.gdf.repository;

import org.gdf.model.OnHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OnHoldRepository extends JpaRepository<OnHold, Integer> {
	
	@Query("SELECT oh FROM OnHold oh WHERE oh.email = ?1")
	public OnHold findByEmail(String email);

}
