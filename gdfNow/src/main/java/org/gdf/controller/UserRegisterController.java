package org.gdf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.gdf.EmailService;
import org.gdf.exception.RegistrationException;
import org.gdf.form.UserAddressForm;
import org.gdf.form.UserForm;
import org.gdf.model.Access;
import org.gdf.model.Country;
import org.gdf.model.EntityType;
import org.gdf.model.OnHold;
import org.gdf.model.User;
import org.gdf.model.UserAddress;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.CountryRepository;
import org.gdf.repository.OnHoldRepository;
import org.gdf.repository.UserRepository;
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
@RequestMapping(path="/userregister")
public class UserRegisterController {
	
	private static Logger logger=LoggerFactory.getLogger(UserRegisterController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OnHoldRepository onHoldRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/user")
	String initNewUser(UserForm userForm) {
		return "/userregister/user";
	}
	
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public RedirectView addNewUser(HttpServletRequest request, UserForm userForm, BindingResult bindingResult){

		if (bindingResult.hasErrors()) {
			// return "/userregister/user";
			new RedirectView("/userregister/user");
		} else {
			// Check if Deeder is already registered.
			Access access = accessRepository.findByEmail(userForm.getEmail());
			if (access != null) {
				throw new RegistrationException("Email taken already");
			} 
			User user = new User();
			user.setFirstName(userForm.getFirstName());
			user.setLastName(userForm.getLastName());
			user.setEmail(userForm.getEmail());
			String dobStr = userForm.getDobStr();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
			LocalDate dob = LocalDate.parse(dobStr, formatter);
			user.setDob(dob);
			user.setCreatedOn(LocalDateTime.now());
			user.setUpdatedOn(LocalDateTime.now());

			try {

				InputStream profileFileIS = userForm.getProfileImage().getInputStream();// request.getPart("profileImage").getInputStream();
				byte[] profileImageBytes = new byte[profileFileIS.available()];
				user.setImage(profileImageBytes);
				user.setProfileFile(request.getPart("profileImage").getSubmittedFileName());
				HttpSession session = request.getSession(true);
				session.setAttribute("user.register.user", user);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new RegistrationException("Issue while storing Profile file");
			} catch (ServletException e1) {
				e1.printStackTrace();
				throw new RuntimeException();
			}
			
		}
		
		return new RedirectView("/userregister/userAddress");
	}

	@GetMapping("/userAddress")
	String initUserAddress(UserAddressForm userAddressForm) {
		return "/userregister/userAddress";
	}
	
	@PostMapping("/userAddress")
	String addUserAddress(HttpServletRequest request, UserAddressForm addressForm,BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			return "/userregister/userAddress";
		}else {
			//User Address now
	        UserAddress address=new UserAddress();
	        address.setAddressLine(addressForm.getAddressLine());
	        address.setPostCode(addressForm.getPostCode());
	        address.setCity(addressForm.getCity());
	        address.setState(addressForm.getState());
	        address.setCreatedOn(LocalDateTime.now());
	        address.setUpdatedOn(LocalDateTime.now());
	        String countryCode=addressForm.getCountryCode();
	        Country country=countryRepository.findByCode(countryCode);
	        address.setCountry(country);
	        HttpSession session=request.getSession();
	        User user=(User)session.getAttribute("user.register.user");
	        user.setUserAddress(address);
	        user=userRepository.save(user);
	        logger.info("User created with ID: "+user.getId());
	        
	        //Create OnHold record as well
	        OnHold onHold=new OnHold();
	        onHold.setEntityType(EntityType.USER);
	        onHold.setEmail(user.getEmail());
	        onHold.setEntityId(user.getId());
	        onHold=onHoldRepository.save(onHold);
	        logger.info("OnHold created for user: "+onHold.getEmail());
	        session.removeAttribute("user.register.user");
	        //Before redirect let's send the email
	        emailService.sendUserRegConfirmEmail(user);
			return "/userregister/success";
			
		}
	}
	
	@GetMapping("/confirm")
	String confirmUser(UserForm userForm, UserAddressForm userAddressForm) {
		return "/userregister/confirm";
	}
	

}
