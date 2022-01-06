package org.gdf.repository;

import java.util.List;

import org.gdf.model.NgoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NgoCategoryRepository extends JpaRepository<NgoCategory, Integer> {
	
	@Query("select distinct nc.type from NgoCategory nc")
	public List<String> getNgoTypes();
	
	
	@Query("select distinct nc.subtype from NgoCategory nc where nc.type=:type")
	public List<String> getNgoSubTypes(@Param("type") String type);
	
	@Query("select nc from NgoCategory nc where nc.type=:type and nc.subtype=:subtype")
	public NgoCategory getNgoCategory(@Param("type") String category,@Param("subtype") String subCategory); 


}
