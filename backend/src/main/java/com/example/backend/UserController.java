package com.example.backend;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/show")
	public String displayy() {
		List<User> list = userService.display();
		for(User u:list) System.out.println(u.getId()+" "+u.getEmail());
		return "index";
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
	    if (userRepository.existsByEmail(user.getEmail())) {
	        return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
	    }
	    userService.registerUser(user);
	    return ResponseEntity.ok(Map.of("message", "User registered successfully"));
	}


	
	
	 @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
	        String email = credentials.get("email");
	        String password = credentials.get("password");

	        Optional<User> userOpt = userRepository.findByEmail(email);
	        if (userOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
	        }

	        User user = userOpt.get();
	        if (!user.getPassword().equals(password)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid password"));
	        }

	        // Later: return JWT token here
	        return ResponseEntity.ok(Map.of(
	            "message", "Login successful",
	            "role", user.getRole(),
	            "userId", user.getId()
	        ));
	    }
	
	
	
}