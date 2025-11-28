package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.SmartContactManagerApplication;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.validation.Valid;

@Controller
public class HomeController {

    private final SmartContactManagerApplication smartContactManagerApplication;
	
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    
    
	@Autowired
	private UserRepository userRepository;


    HomeController(SmartContactManagerApplication smartContactManagerApplication) {
        this.smartContactManagerApplication = smartContactManagerApplication;
    }
	
	
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home_Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","about_Smart Contact Manager");
		return "about";
	}
	
	
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Register_Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	
	//handler for registering user
	@PostMapping("/do_register")
	public String registerUser(
	        @Valid @ModelAttribute("user") User user,
	        BindingResult result,
	        @RequestParam(value = "terms", defaultValue = "false") boolean terms,
	        Model model,
	        RedirectAttributes redirectAttributes) {

	    try {

	        // terms check
	        if (!terms) {
	            redirectAttributes.addFlashAttribute("message",
	                    new Message("You have not agreed the terms and conditions", "alert-danger"));
	            return "redirect:/signup";
	        }

	        // validation errors
	        if (result.hasErrors()) {
	            return "signup";
	        }

	        // default values
	        user.setRole("ROLE_USER");
	        user.setEnabled(true);
	        user.setImageUrl("default.png");
	        user.setPassword(passwordEncoder.encode(user.getPassword()));

	        // save
	        userRepository.save(user);

	        // success message
	        redirectAttributes.addFlashAttribute("message",
	                new Message("Successfully Registered!!", "alert-success"));

	        return "redirect:/signup";

	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message",
	                new Message("Something Went Wrong!", "alert-danger"));
	        return "redirect:/signup";
	    }
	}
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login";
	}

}
	

