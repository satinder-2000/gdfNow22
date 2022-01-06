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
import org.gdf.form.BusinessAddressForm;
import org.gdf.form.BusinessForm;
import org.gdf.model.Access;
import org.gdf.model.Business;
import org.gdf.model.BusinessAddress;
import org.gdf.model.BusinessCategory;
import org.gdf.model.Country;
import org.gdf.model.EntityType;
import org.gdf.model.OnHold;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.BusinessCategoryRepository;
import org.gdf.repository.BusinessRepository;
import org.gdf.repository.CountryRepository;
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
@RequestMapping(path="/businessregister")
public class BusinessRegisterController {
	
private static Logger logger=LoggerFactory.getLogger(BusinessRegisterController.class);
	
	@Autowired
	private BusinessRepository businessRepository;
	
	@Autowired
	private BusinessCategoryRepository businessCategoryRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/business")
	String initNewBusiness(BusinessForm businessForm) {
		return "/businessregister/business";
	}
	
	@RequestMapping(value = "/business", method = RequestMethod.POST)
	public RedirectView addNewBusiness(HttpServletRequest request, BusinessForm businessForm, BindingResult bindingResult){

		if (bindingResult.hasErrors()) {
			new RedirectView("/businessregister/business");
		} else {
			// Check if Business is already registered.
			Access access = accessRepository.findByEmail(businessForm.getEmail());
			if (access != null) {
				throw new RegistrationException("Email taken already");
			}
			Business business=new Business();
			business.setName(businessForm.getName());
			business.setEmail(businessForm.getEmail());
			business.setDescription(businessForm.getDescription());
			business.setEmail(businessForm.getEmail());
			business.setWebsite(businessForm.getWebsite());
			String category=businessForm.getBusinessCategory();
			String subCategory=businessForm.getBusinessSubCategory();
			BusinessCategory businessCategory= businessCategoryRepository.getBusinessCategory(category, subCategory);
			business.setBusinessCategory(businessCategory);
			business.setConfirmed(true);
			business.setCreatedOn(LocalDateTime.now());
			business.setUpdatedOn(LocalDateTime.now());
			try {

				InputStream profileFileIS = businessForm.getProfileImage().getInputStream();
				byte[] profileImageBytes = new byte[profileFileIS.available()];
				business.setImage(profileImageBytes);
				business.setProfileFile(request.getPart("profileImage").getSubmittedFileName());
				HttpSession session = request.getSession(true);
				session.setAttribute("business.register.business", business);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new RegistrationException("Issue while storing Profile file");
			} catch (ServletException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
		}
		return new RedirectView("/businessregister/businessAddress");
	}
	
	@GetMapping("/businessAddress")
	String initBusinessAddress(BusinessAddressForm businessAddressForm) {
		return "/businessregister/businessAddress";
	}
	
	@PostMapping("/businessAddress")
	String addBusinessAddress(HttpServletRequest request, BusinessAddressForm addressForm,BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			return "/businessregister/businessAddress";
		}else {
			//Business Address now
	        BusinessAddress address=new BusinessAddress();
	        address.setAddressLine(addressForm.getAddressLine());
	        address.setPostcode(addressForm.getPostcode());
	        address.setCity(addressForm.getCity());
	        address.setState(addressForm.getState());
	        String countryCode=addressForm.getCountryCode();
	        Country country=countryRepository.findByCode(countryCode);
	        address.setCountry(country);
	        HttpSession session=request.getSession();
	        Business business=(Business)session.getAttribute("business.register.business");
	        business.setBusinessAddress(address);
	        business=businessRepository.save(business);
	        logger.info("Nusiness created with ID: "+business.getId());
	        
	        //Create OnHold record as well
	        OnHold onHold=new OnHold();
	        onHold.setEntityType(EntityType.BUSINESS);
	        onHold.setEmail(business.getEmail());
	        onHold.setEntityId(business.getId());
	        onHold=onHoldRepository.save(onHold);
	        logger.info("OnHold created for Business: "+onHold.getEmail());
	        session.removeAttribute("business.register.business");
	        //Before redirect let's send the email
	        emailService.sendBusinessRegConfirmEmail(business);
			return "/businessrregister/success";
			
		}
	}
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public @ResponseBody List<String> findAllBusinessCategories(){
		return businessCategoryRepository.getBusinessCategories();
	}
	
	@RequestMapping(value = "/subcategories", method = RequestMethod.GET)
	public @ResponseBody List<String> findAllBusinessSubCategories(@RequestParam(value = "category", required = true) String category){
		return businessCategoryRepository.getBusinessSubCategories(category);
	}


}
