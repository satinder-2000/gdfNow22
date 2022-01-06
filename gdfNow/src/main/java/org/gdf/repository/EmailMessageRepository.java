package org.gdf.repository;

import java.util.List;

import org.gdf.model.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailMessageRepository extends JpaRepository<EmailMessage, Integer> {
	
	@Query("SELECT em FROM EmailMessage em WHERE em.template = ?1")
	public List<EmailMessage> findByTemplate(String template);

}
