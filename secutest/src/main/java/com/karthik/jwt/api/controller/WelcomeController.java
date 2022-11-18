package com.karthik.jwt.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.karthik.jwt.api.entity.AuthRequest;
import com.karthik.jwt.api.entity.User;
import com.karthik.jwt.api.repository.UserRepository;
import com.karthik.jwt.api.util.JwtUtil;

@RestController
public class WelcomeController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String welcome() {
        return "Welcome!!";
    }
    @PostMapping("/register")
	public User createEmployee(@RequestBody User user)
	{
		return this.userRepository.save(user);
	}
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        } 
        catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
	@PutMapping("/update/{id}")
	public String update(@PathVariable(value = "id") int id, @RequestBody User user) {
		User u = userRepository.findById(id);
		u.setUserName(user.getUserName());
		u.setPassword(user.getPassword());
		u.setEmail(user.getEmail());
		u.setStock(user.getStock());
		userRepository.save(u);
		return "Changed Sucessfully";
	}
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable(value = "id") int id) {
		userRepository.deleteById(id);
		return "Deleted Sucessfully";
	}
}
