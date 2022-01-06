package org.gdf.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gdf.exception.LoginException;
import org.gdf.form.LoginForm;
import org.gdf.model.Access;
import org.gdf.model.EntityType;
import org.gdf.model.User;
import org.gdf.repository.AccessRepository;
import org.gdf.repository.UserRepository;
import org.gdf.util.GDFConstants;
import org.gdf.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path="/login")
public class LogInController {
	
	private static Logger logger=LoggerFactory.getLogger(LogInController.class);
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("")
	String initLogIn(LoginForm loginForm) {
		return "/login";
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String performLogIn(HttpServletRequest request, LoginForm loginForm,BindingResult bindingResult) throws IOException {
		logger.info("LOGIN WILL BE PERFORMED");
		Access access= accessRepository.findByEmail(loginForm.getEmail());
		String toReturn=null;
		if (access.getFailedAttempts()==3) {
			throw new LoginException("All Login attempts used. Account locked");
		}else {
			//First, confirm the password
			String providedPassword=loginForm.getPassword();
			String dbPassword=access.getPassword();
			boolean passwordVerified=PasswordUtil.verifyUserPassword(providedPassword, dbPassword, loginForm.getEmail());
			int attempts=access.getFailedAttempts();
            if (passwordVerified && attempts==0){
               toReturn="/home/user"; 
            }else if (passwordVerified && attempts>0){
                access.setFailedAttempts(0);
                access=accessRepository.save(access);
                EntityType acType=access.getEntityType();
                HttpSession session=request.getSession();
                switch (acType) {
				case USER: {
					User user=userRepository.findByEmail(access.getEmail());
					session.setAttribute(GDFConstants.LOGGED_IN_USER, user);
					toReturn = "home/user";
					break;
				}
				case DEEDER: {
					toReturn = "home/deeder";
					break;
				}
				case BUSINESS: {
					toReturn = "home/business";
					break;
				}
				case GOVERNMENT: {
					toReturn = "home/government";
					break;
				}
				case NGO: {
					toReturn = "home/ngo";
					break;
				}
				}
             }else if (!passwordVerified){
                int count=++attempts;
                access.setFailedAttempts(count);
                access=accessRepository.save(access);
                logger.info("Attemps in the DB now: "+access.getFailedAttempts());
                if (count==3)throw new LoginException("Account Locked. 3 Login attempts expired");
                else throw new LoginException("Login failed. "+(3-count)+" attemps left");
            }
		}
		return toReturn;
	}

}
