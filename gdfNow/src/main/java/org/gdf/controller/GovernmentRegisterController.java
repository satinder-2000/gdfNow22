package org.gdf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gdf.EmailService;
import org.gdf.exception.RegistrationException;
import org.gdf.form.GovernmentAddressForm;
import org.gdf.form.GovernmentForm;
import org.gdf.model.Access;
import org.gdf.model.Country;
import org.gdf.model.EntityType;
import org.gdf.model.Government;
import org.gdf.model.GovernmentAddress;
import org.gdf.model.GovernmentOrg;
import org.gdf.model.OnHold;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.CountryRepository;
import org.gdf.repository.GovernmentOrgRepository;
import org.gdf.repository.GovernmentRepository;
import org.gdf.repository.OnHoldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path="/governmentregister")
public class GovernmentRegisterController {
	
	private static Logger logger=LoggerFactory.getLogger(GovernmentRegisterController.class);
	
	@Autowired
	private GovernmentOrgRepository governmentOrgRepository;
	
	@Autowired
	private GovernmentRepository governmentRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/government")
	String initNewGovernment(GovernmentForm governmentForm) {
		return "/governmentregister/government";
	}
	
	@RequestMapping(value = "/government", method = RequestMethod.POST)
	public RedirectView addNewgovernment(HttpServletRequest request, GovernmentForm governmentForm, BindingResult bindingResult){

		if (bindingResult.hasErrors()) {
			new RedirectView("/governmentregister/government");
		} else {
			// Check if government is already registered.
			Access access = accessRepository.findByEmail(governmentForm.getEmail());
			if (access != null) {
				throw new RegistrationException("Email taken already");
			}
			Government government=new Government();
			government.setName(governmentForm.getName());
			government.setEmail(governmentForm.getEmail());
			government.setDescription(governmentForm.getDescription());
			government.setEmail(governmentForm.getEmail());
			government.setWebsite(governmentForm.getWebsite());
			String ministry=governmentForm.getMinistry();
			String department=governmentForm.getDepartment();
			GovernmentOrg governmentOrg = governmentOrgRepository.getGovernmentOrg(ministry, department);
			government.setGovernmentOrg(governmentOrg);
			government.setConfirmed(true);
			government.setCreatedOn(LocalDateTime.now());
			government.setUpdatedOn(LocalDateTime.now());
			try {

				InputStream profileFileIS = governmentForm.getProfileImage().getInputStream();
				byte[] profileImageBytes = new byte[profileFileIS.available()];
				government.setImage(profileImageBytes);
				government.setProfileFile(request.getPart("profileImage").getSubmittedFileName());
				HttpSession session = request.getSession(true);
				session.setAttribute("government.register.government", government);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new RegistrationException("Issue while storing Profile file");
			} catch (ServletException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
		}
		return new RedirectView("/governmentregister/governmentAddress");
	}
	
	@GetMapping("/governmentAddress")
	String initGovernmentAddress(GovernmentAddressForm governmentAddressForm) {
		return "/governmentregister/governmentAddress";
	}
	
	@PostMapping("/governmentAddress")
	String addgovernmentAddress(HttpServletRequest request, GovernmentAddressForm addressForm,BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			return "/governmentregister/governmentAddress";
		}else {
			//government Address now
	        GovernmentAddress address=new GovernmentAddress();
	        address.setAddressLine(addressForm.getAddressLine());
	        address.setPostcode(addressForm.getPostcode());
	        address.setCity(addressForm.getCity());
	        address.setState(addressForm.getState());
	        String countryCode=addressForm.getCountryCode();
	        Country country=countryRepository.findByCode(countryCode);
	        address.setCountry(country);
	        HttpSession session=request.getSession();
	        Government government=(Government)session.getAttribute("government.register.government");
	        government.setGovernmentAddress(address);
	        government=governmentRepository.save(government);
	        logger.info("government created with ID: "+government.getId());
	        
	        //Create OnHold record as well
	        OnHold onHold=new OnHold();
	        onHold.setEntityType(EntityType.GOVERNMENT);
	        onHold.setEmail(government.getEmail());
	        onHold.setEntityId(government.getId());
	        onHold=onHoldRepository.save(onHold);
	        logger.info("OnHold created for government: "+onHold.getEmail());
	        session.removeAttribute("government.register.government");
	        //Before redirect let's send the email
	        emailService.sendGovernmentRegConfirmEmail(government);
			return "/governmentregister/success";
			
		}
	}
	
	@RequestMapping(value = "/ministries", method = RequestMethod.GET)
	public @ResponseBody List<String> findAllGovernmentMinistries(){
		return governmentOrgRepository.getMinistries();
	}
	
	@RequestMapping(value = "/departments", method = RequestMethod.GET)
	public @ResponseBody List<String> findAllGovernmentDepartments(@RequestParam(value = "ministry", required = true) String ministry){
		return governmentOrgRepository.getDepartments(ministry);
	}



}
