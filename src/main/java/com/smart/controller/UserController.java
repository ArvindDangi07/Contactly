package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final DaoAuthenticationProvider authenticationProvider;
    
    @Autowired
    private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;

    UserController(DaoAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		
		String userName = principal.getName();
		System.out.println("USERNAME  : "+userName);
		
		//get the user  all data using userName(Email);
		
		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER : "+user);
		
		model.addAttribute("user", user);
		
		
	}
	
	// dashboard home
	
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		model.addAttribute("title","User_HOME");
		return "normal/user_dashboard";
	}
	
	
	// open add form handler
	@GetMapping("/add_contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
		
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
	                             @RequestParam("contactImage") MultipartFile file,
	                             Principal principal,
	                             HttpSession session,
	                             Model model) {

	    try {
	        String name = principal.getName();
	        User user = userRepository.getUserByUserName(name);

	        // Image handling
	        if (file.isEmpty()) {
	            contact.setImage("default.png");
	        } else {
	            contact.setImage(file.getOriginalFilename());
	            
	            File saveFile=new ClassPathResource("static/image").getFile();
	            
	            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + file.getOriginalFilename());

//	            File uploadDir = new File("uploads/img");
//	            if (!uploadDir.exists()) uploadDir.mkdirs();
//
//	            Path path = Paths.get(uploadDir.getAbsolutePath()
//	                    + File.separator + file.getOriginalFilename());

	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            System.out.println("Imgae is uploaded");
	        }

	        contact.setUser(user);
	        user.getContacts().add(contact);
	        userRepository.save(user);

	        session.setAttribute("message",
	                new Message("✅ Contact added successfully!", "success"));

	        // reset form after submission
	        model.addAttribute("contact", new Contact());

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("message",
	                new Message("❌ Something went wrong! Try again.", "danger"));
	    }

	    return "normal/add_contact_form"; // ✅ stays on same page
	}

	
	//show contacts controller
	//per page =5[n]
	//current page = 0[page]
	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal)
	{
		m.addAttribute("tiltle","Show User Contacts");
		
		//contact ki list bhejni hai
		
		String userName = principal.getName();
		User user= this.userRepository.getUserByUserName(userName);
		
		PageRequest pageable = PageRequest.of(page, 5);
		
	//	List<Contact> contacts = this.contactRepository.findContactsByUser(user.getId());
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage",page);
		
		m.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//showing particular contact details.
	
	@GetMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal)
	{
		
		
		

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		//one bug solve here
		
		String userName = principal.getName();
		User user= this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId()) {
			
			model.addAttribute("contact",contact);
		}
		
		return "normal/contact_detail";
	}
	
	
	//delete contact handler
	@GetMapping("/delete/{cid}/{page}")
	public String deleteContact(
	        @PathVariable("cid") Integer cId,
	        @PathVariable("page") Integer page,
	        Principal principal,
	        HttpSession session) {

	    Optional<Contact> optional = contactRepository.findById(cId);

	    if (optional.isEmpty()) {
	        session.setAttribute("message",
	                new Message("Contact not found!", "danger"));
	        return "redirect:/user/show_contacts/" + page;
	    }

	    Contact contact = optional.get();

	    // Logged-in user
	    String username = principal.getName();

	    // User owner check
	    if (!contact.getUser().getEmail().equals(username)) {
	        session.setAttribute("message",
	                new Message("You are not authorized to delete this contact!", "danger"));
	        return "redirect:/user/show_contacts/" + page;
	    }

	    // 1️⃣ Load user
	    User user = userRepository.getUserByUserName(username);

	    // 2️⃣ Remove contact from user's list
	    user.getContacts().remove(contact);
	    userRepository.save(user);

	    // 3️⃣ DELETE contact from DB
	    contactRepository.delete(contact);

	    // 4️⃣ Success message
	    session.setAttribute("message",
	            new Message("Contact deleted successfully!", "success"));

	    return "redirect:/user/show_contacts/" + page;
	}

	
	//update form handler
	
		@PostMapping("/update-contact/{cid}")
		public String updateForm(@PathVariable("cid") Integer cId, Model model, Principal principal) {

		    // logged-in user ko check karo
		    String username = principal.getName();

		    Contact contact = contactRepository.findById(cId).orElseThrow();

		    // ensure user cannot access others’ contacts
		    if (!contact.getUser().getEmail().equals(username)) {
		        return "error/403";  // unauthorized access
		    }

		    model.addAttribute("contact", contact);
		    model.addAttribute("title", "Update Contact");

		    return "normal/Update_form";
		}
	

	
	//open update form handler
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(
	        @ModelAttribute Contact contact,
	        @RequestParam(value = "contactImage", required = false) MultipartFile file,
	        Principal principal,
	        HttpSession session) {

	    try {
	        // 1. fetch existing
	        Contact oldContact = contactRepository.findById(contact.getcId()).orElseThrow();

	        // 2. set user
	        User user = userRepository.getUserByUserName(principal.getName());
	        contact.setUser(user);

	        // 3. image handling (optional)
	        if (file != null && !file.isEmpty()) {
	            File deleteFile = new ClassPathResource("static/img").getFile();
	            File oldFile = new File(deleteFile, oldContact.getImage());
	            if (oldFile.exists()) oldFile.delete();

	            Path path = Paths.get(deleteFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	            contact.setImage(file.getOriginalFilename());
	            System.out.println("IMAGE UPDATED SUCCESSFULLY");
	        } else {
	            // keep old image if no new file
	            contact.setImage(oldContact.getImage());
	        }

	        // 4. save (update)
	        contactRepository.save(contact);

	        // 5. set message as Message object (important)
	        session.setAttribute("message", new Message("Contact Updated Successfully!", "success"));

	    } catch (Exception e) {
	        e.printStackTrace();
	       
	    }

	    // correct redirect to show contact
	    return "redirect:/user/contact/" + contact.getcId();
	}
	
	// your profile handler
	@GetMapping("/profile")
	public String youtProfile() {
		return "normal/profile";
	}
		
	
	//Setting page handler
	@GetMapping("/setting")
	public String settingView() {
		
		return "normal/setting";
	}
	
	
}
