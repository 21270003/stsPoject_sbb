package com.mysite.sbb.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService; 
	
	@GetMapping("/signup")
	public String goSingup(UserCreateForm userCreateForm) {
		return "signup_form";		
	}

	@GetMapping("/login")
	public String goSingin( ) {
		return "login_form";		
	}

	
	@PostMapping("/signup")
	public String goSinginOK(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if( bindingResult.hasErrors() ) { 
			return "signup_form";
		}
		if( !userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치 해야 합니다...");
			return "signup_form";
		}
		try {
			this.userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail() );
		}catch(DataIntegrityViolationException e) {
			bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다...");
			return "signup_form";
		}catch(Exception e) {
			bindingResult.reject("signupFailed", e.getMessage() );
			return "signup_form";
		}
		return "redirect:/";
	}

}
