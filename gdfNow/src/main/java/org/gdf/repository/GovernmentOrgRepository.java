package org.gdf.repository;

import java.util.List;

import org.gdf.model.GovernmentOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GovernmentOrgRepository extends JpaRepository<GovernmentOrg, Integer> {
	
	@Query("select distinct go.ministry from GovernmentOrg go")
	public List<String> getMinistries();
	
	@Query("select distinct go.department from GovernmentOrg go where go.ministry=:ministry")
	public List<String> getDepartments(@Param("ministry") String ministry);
	
	@Query("select go from GovernmentOrg go where go.ministry=:ministry and go.department=:department")
	public GovernmentOrg getGovernmentOrg(String ministry, String department);

}
