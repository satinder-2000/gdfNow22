package org.gdf.repository;

import org.gdf.model.Government;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GovernmentRepository extends JpaRepository<Government, Integer> {

}
