package org.gdf.repository;

import java.util.List;

import org.gdf.model.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Integer> {
	
	@Query("select distinct bc.type from BusinessCategory bc")
	public List<String> getBusinessCategories();
	
	
	@Query("select distinct bc.subtype from BusinessCategory bc where bc.type=:type")
	public List<String> getBusinessSubCategories(@Param("type") String type);
	
	@Query("select bc from BusinessCategory bc where bc.type=:type and bc.subtype=:subtype")
	public BusinessCategory getBusinessCategory(@Param("type") String category,@Param("subtype") String subCategory); 

}
