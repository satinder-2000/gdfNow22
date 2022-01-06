package org.gdf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gdf.exception.RegistrationException;
import org.gdf.form.DeederAddressForm;
import org.gdf.form.DeederForm;
import org.gdf.model.Country;
import org.gdf.model.Deeder;
import org.gdf.model.DeederAddress;
import org.gdf.model.EntityType;
import org.gdf.model.OnHold;
import org.gdf.model.User;
import org.gdf.repository.CountryRepository;
import org.gdf.repository.DeederRepository;
import org.gdf.repository.OnHoldRepository;
import org.gdf.repository.UserRepository;
import org.gdf.util.GDFConstants;
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
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path="/nominate")
public class NominateDeederController {
	
private static Logger logger=LoggerFactory.getLogger(DeederRegisterController.class);
	
	@Autowired
	private DeederRepository deederRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/deeder")
	String initNewDeeder(DeederForm deederForm, HttpServletRequest request, @RequestParam(name = "user") int id) {
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute(GDFConstants.LOGGED_IN_USER);
		if (user==null) {
			user=userRepository.findById(id).get();
			session.setAttribute(GDFConstants.LOGGED_IN_USER, user);
		}
		return "/nominate/deeder";
	}
	
	@RequestMapping(value = "/deeder", method = RequestMethod.POST)
	public RedirectView nominateDeeder(HttpServletRequest request, DeederForm deederForm,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			new RedirectView("/nominate/deeder");
		}else {
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
	        deeder.setConfirmed(false);//Need to switch to true when Deeder approves the nomination via email.
	        deeder.setNominated(true);
	        
	        
	        try {
	        	if (deederForm.getProfileImage()==null) {
	        		//TODO Implement logic to generate Image with initials.
	        	}else {
	        		InputStream profileFileIS=deederForm.getProfileImage().getInputStream();
					byte[] profileImageBytes=new byte[profileFileIS.available()];
					deeder.setImage(profileImageBytes);
					deeder.setProfileFile(request.getPart("profileImage").getSubmittedFileName());
					HttpSession session=request.getSession(true);
					session.setAttribute("deeder.nominate.deeder", deeder);
	        	}
	        } catch (IOException e1) {
				e1.printStackTrace();
				throw new RegistrationException("Issue while storing Profile file");
			} catch (ServletException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
		}
		
		return new RedirectView("/nominate/deederAddress");
	}
	
	@GetMapping("/deederAddress")
	String initNewDeeder(DeederAddressForm DeederAddressForm) {
		return "/nominate/deederAddress";
	}
	
	@PostMapping("/deederAddress")
	String addDeederAddress(HttpServletRequest request, DeederAddressForm addressForm,BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			return "/nominate/deederAddress";
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
	        HttpSession session=request.getSession();
	        Deeder deeder=(Deeder)session.getAttribute("deeder.nominate.deeder");
	        deeder.setDeederAddress(address);
	        User user=(User)session.getAttribute(GDFConstants.LOGGED_IN_USER);
			user.getDeeders().add(deeder);
	        deeder=deederRepository.save(deeder);
	        logger.info("Deeder created with ID: "+deeder.getId());
	        session.setAttribute("deeder.register.deeder",deeder);
	        //Create OnHold record as well
	        OnHold onHold=new OnHold();
	        onHold.setEntityType(EntityType.DEEDER);
	        onHold.setEmail(deeder.getEmail());
	        onHold.setEntityId(deeder.getId());
	        onHold=onHoldRepository.save(onHold);
	        logger.info("OnHold created for nominated deeder: "+onHold.getEmail());
	        session.removeAttribute("deeder.register.deeder");
			return "/nominate/success";
			
		}
	}
	
	@GetMapping("/confirm")
	String confirmDeeder(DeederForm deederForm, DeederAddressForm deederAddressForm) {
		return "/deederregister/confirm";
	}

}
