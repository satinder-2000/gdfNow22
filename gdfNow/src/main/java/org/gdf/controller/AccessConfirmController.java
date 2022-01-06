package org.gdf.controller;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.gdf.EmailService;
import org.gdf.form.AccessConfirmForm;
import org.gdf.model.Access;
import org.gdf.model.OnHold;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.OnHoldRepository;
import org.gdf.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(path="/accessconfirm")
public class AccessConfirmController {
	
	private static Logger logger=LoggerFactory.getLogger(AccessConfirmController.class);
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private EmailService emailService;
	
	
	@GetMapping("")
	String initAccessConfirm(@RequestParam(name = "id") String email, AccessConfirmForm  accessConfirmForm, HttpServletRequest request) {
		accessConfirmForm.setEmail(email);
		return "/accessConfirm";
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	String createAccess(HttpServletRequest request, AccessConfirmForm  accessConfirmForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/accessConfirm";
		}else {
			OnHold onHold=onHoldRepository.findByEmail(accessConfirmForm.getEmail());
			Access access=new Access();
			access.setEmail(onHold.getEmail());
			access.setPassword(PasswordUtil.generateSecurePassword(accessConfirmForm.getPassword(), accessConfirmForm.getEmail()));
			access.setEntityType(onHold.getEntityType());
			access.setCreatedOn(LocalDateTime.now());
			access.setFailedAttempts(0);
			access=accessRepository.save(access);
			logger.info(String.format("Access created for %s with id of %d",access.getEmail(),access.getId()));
			onHoldRepository.delete(onHold);
			//Finally, send email
			emailService.sendAccessConfirmEmail(access.getEmail());
			String toReturn="/home";
			switch(access.getEntityType()) {
			case USER: toReturn="/home/user";
			break;
			case DEEDER: toReturn="/home/deeder";
			break;
			case BUSINESS: toReturn="/home/bisiness";
			break;
			case GOVERNMENT: toReturn="/home/government";
			break;
			case NGO: toReturn="/home/ngo";
			break;
			default: toReturn="/home";
			
			}
			return toReturn;
		}
	}
}
