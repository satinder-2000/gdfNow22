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
import org.gdf.form.NgoAddressForm;
import org.gdf.form.NgoForm;
import org.gdf.model.Access;
import org.gdf.model.Country;
import org.gdf.model.EntityType;
import org.gdf.model.Ngo;
import org.gdf.model.NgoAddress;
import org.gdf.model.NgoCategory;
import org.gdf.model.OnHold;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.CountryRepository;
import org.gdf.repository.NgoCategoryRepository;
import org.gdf.repository.NgoRepository;
import org.gdf.repository.OnHoldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path="/ngoregister")
public class NgoRegistrationController {
	
private static Logger logger=LoggerFactory.getLogger(UserRegisterController.class);
	
	@Autowired
	private NgoCategoryRepository ngoCategoryRepository;
	
	@Autowired
	private NgoRepository ngoRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private EmailService emailService;
	
	//@RequestMapping(value = "/ngo",method = RequestMethod.GET)
	@GetMapping("/ngo")
	String initNewNgo(NgoForm ngoForm) {
		return "/ngoregister/ngo";
	}
	
	@RequestMapping(value = "/ngo", method = RequestMethod.POST)
	public RedirectView addNewNgo(HttpServletRequest request, NgoForm ngoForm, BindingResult bindingResult){

		if (bindingResult.hasErrors()) {
			new RedirectView("/ngoregister/ngo");
		} else {
			// Check if Ngo is already registered.
			Access access = accessRepository.findByEmail(ngoForm.getEmail());
			if (access != null) {
				throw new RegistrationException("Email taken already");
			} 
			Ngo ngo = new Ngo();
			ngo.setName(ngoForm.getName());
			ngo.setWebsite(ngoForm.getWebsite());
			ngo.setDescription(ngoForm.getDescription());
			ngo.setEmail(ngoForm.getEmail());
			NgoCategory ngoCategory=ngoCategoryRepository.getNgoCategory(ngoForm.getType(), ngoForm.getSubtype());
			ngo.setNgoCategory(ngoCategory);
			ngo.setCreatedOn(LocalDateTime.now());
			ngo.setUpdatedOn(LocalDateTime.now());

			try {

				InputStream profileFileIS = ngoForm.getProfileImage().getInputStream();// request.getPart("profileImage").getInputStream();
				byte[] profileImageBytes = new byte[profileFileIS.available()];
				ngo.setImage(profileImageBytes);
				ngo.setProfileFile(request.getPart("profileImage").getSubmittedFileName());
				HttpSession session = request.getSession(true);
				session.setAttribute("ngo.register.ngo", ngo);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new RegistrationException("Issue while storing Profile file");
			} catch (ServletException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
			
		}
		
		return new RedirectView("/ngoregister/ngoAddress");
	}

	@RequestMapping(value = "/ngoAddress",method = RequestMethod.GET)
	String initNgoAddress(NgoAddressForm ngoAddressForm) {
		return "/ngoregister/ngoAddress";
	}
	
	@RequestMapping(value = "/ngoAddress",method = RequestMethod.POST)
	String addNgoAddress(HttpServletRequest request, NgoAddressForm addressForm,BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			return "/ngoregister/ngoAddress";
		}else {
			//Ngo Address now
	        NgoAddress address=new NgoAddress();
	        address.setAddressLine(addressForm.getAddressLine());
	        address.setPhone(addressForm.getPhone());
	        address.setPostcode(addressForm.getPostcode());
	        address.setCity(addressForm.getCity());
	        address.setState(addressForm.getState());
	        String countryCode=addressForm.getCountryCode();
	        Country country=countryRepository.findByCode(countryCode);
	        address.setCountry(country);
	        HttpSession session=request.getSession();
	        Ngo ngo=(Ngo)session.getAttribute("ngo.register.ngo");
	        ngo.setNgoAddress(address);
	        ngo=ngoRepository.save(ngo);
	        logger.info("Ngo created with ID: "+ngo.getId());
	        
	        //Create OnHold record as well
	        OnHold onHold=new OnHold();
	        onHold.setEntityType(EntityType.NGO);
	        onHold.setEmail(ngo.getEmail());
	        onHold.setEntityId(ngo.getId());
	        onHold=onHoldRepository.save(onHold);
	        logger.info("OnHold created for ngo: "+onHold.getEmail());
	        session.removeAttribute("ngo.register.ngo");
	        //Before redirect let's send the email
	        emailService.sendNgoRegConfirmEmail(ngo);
			return "/ngoregister/success";
			
		}
	}
	
	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public @ResponseBody List<String> findAllNgoTypes(){
		return ngoCategoryRepository.getNgoTypes();
	}
	
	@RequestMapping(value = "/subtypes", method = RequestMethod.GET)
	public @ResponseBody List<String> findAllNgoSubTypes(@RequestParam(value = "type", required = true) String type){
		return ngoCategoryRepository.getNgoSubTypes(type);
	}

}
