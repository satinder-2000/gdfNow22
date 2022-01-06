package org.gdf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.gdf.form.DeederForm;
import org.gdf.EmailService;
import org.gdf.exception.RegistrationException;
import org.gdf.form.DeederAddressForm;
import org.gdf.model.Access;
import org.gdf.model.Country;
import org.gdf.model.Deeder;
import org.gdf.model.DeederAddress;
import org.gdf.model.EntityType;
import org.gdf.model.OnHold;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.CountryRepository;
import org.gdf.repository.DeederRepository;
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
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path="/deederregister")
public class DeederRegisterController {
	
	private static Logger logger=LoggerFactory.getLogger(DeederRegisterController.class);
	
	@Autowired
	private DeederRepository deederRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/deeder")
	String initNewDeeder(DeederForm deederForm) {
		return "/deederregister/deeder";
	}
	
	@RequestMapping(value = "/deeder", method = RequestMethod.POST)
	public RedirectView addNewDeeder(HttpServletRequest request, DeederForm deederForm,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			new RedirectView("/deederregister/deeder");
		}else {
			//Check if Deeder is already registered.
			Access access= accessRepository.findByEmail(deederForm.getEmail());
			if (access!=null) {
				throw new RegistrationException("Email taken already");
			}
			
			Deeder deeder=new Deeder();
			deeder.setFirstName(deederForm.getFirstName());
			deeder.setLastName(deederForm.getLastName());
			deeder.setEmail(deederForm.getEmail());
			deeder.setGender(deederForm.getGender());
			String dobStr=deederForm.getDobStr();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
			LocalDate dob= LocalDate.parse(dobStr, formatter);
			deeder.setDob(dob);
			deeder.setPhone(deederForm.getPhone());
			deeder.setMobile(deederForm.getMobile());
			deeder.setAbout(deederForm.getAbout());
			deeder.setCreatedOn(LocalDateTime.now());
	        deeder.setUpdatedOn(LocalDateTime.now());
	        deeder.setConfirmed(true);
	        deeder.setNominated(false);
	        
	        
	        try {
	        
				InputStream profileFileIS=deederForm.getProfileImage().getInputStream();//request.getPart("profileImage").getInputStream();
				byte[] profileImageBytes=new byte[profileFileIS.available()];
				deeder.setImage(profileImageBytes);
				deeder.setProfileFile(request.getPart("profileImage").getSubmittedFileName());
				HttpSession session=request.getSession(true);
				session.setAttribute("deeder.register.deeder", deeder);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new RegistrationException("Issue while storing Profile file");
				
			} catch (ServletException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
		}
		
		return new RedirectView("/deederregister/deederAddress");
	}
	
	@GetMapping("/deederAddress")
	String initNewDeeder(DeederAddressForm DeederAddressForm) {
		return "/deederregister/deederAddress";
	}
	
	@PostMapping("/deederAddress")
	String addDeederAddress(HttpServletRequest request, DeederAddressForm addressForm,BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
	
			return "/deederregister/deederAddress";
		}else {
			//Deeder Address now
	        DeederAddress address=new DeederAddress();
	     
	        address.setAddressLine(addressForm.getAddressLine());
	        address.setPostCode(addressForm.getPostCode());
	        address.setCity(addressForm.getCity());
	        address.setState(addressForm.getState());
	        String countryCode=addressForm.getCountryCode();
	        Country country=countryRepository.findByCode(countryCode);
	        address.setCountry(country);
	        address.setCreatedOn(LocalDateTime.now());
	        address.setUpdatedOn(LocalDateTime.now());
	        HttpSession session=request.getSession();
	        Deeder deeder=(Deeder)session.getAttribute("deeder.register.deeder");
	        deeder.setDeederAddress(address);
	        deeder=deederRepository.save(deeder);
	        logger.info("Deeder created with ID: "+deeder.getId());
	        //Create OnHold record as well
	        OnHold onHold=new OnHold();
	        onHold.setEntityType(EntityType.DEEDER);
	        onHold.setEmail(deeder.getEmail());
	        onHold.setEntityId(deeder.getId());
	        onHold=onHoldRepository.save(onHold);
	        logger.info("OnHold created for deeder: "+onHold.getEmail());
	        session.setAttribute("deeder.register.deeder",deeder);
	        //Before redirect let's send the email
	        emailService.sendDeederRegConfirmEmail(deeder);
			return "/deederregister/success";
			
		}
	}
	
	@GetMapping("/confirm")
	String confirmDeeder(DeederForm deederForm, DeederAddressForm deederAddressForm) {
		return "/deederregister/confirm";
	}


}
