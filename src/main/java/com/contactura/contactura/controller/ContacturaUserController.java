package com.contactura.contactura.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.ContacturaUser;
import com.contactura.contactura.repository.ContacturaUserRepository;

@CrossOrigin()
@RestController
@RequestMapping({"/user"}) //http://localhost:8090/user
public class ContacturaUserController {
	
	@Autowired
	private ContacturaUserRepository repository;
	
//	@RequestMapping("/login")
//	@GetMapping
//	public String login(HttpServletRequest request) {
//		String token = request.getHeader("Authorization")
//				.substring("Basic".length()).trim();
//		return token;
//	}
	
	@RequestMapping("/login")
	@GetMapping
	public List<String> login(HttpServletRequest request) throws UnsupportedEncodingException{
		String authorization = request.getHeader("Authorization").substring("Basic".length()).trim();
		byte[] baseCred = Base64.getDecoder().decode(authorization);
		String credentialsParsed = new String(baseCred, StandardCharsets.UTF_8);
		String[] values = credentialsParsed.split(":",2);
		ContacturaUser user = repository.findByUsername(values[0]);
		
		String token = request.getHeader("Authorization").substring("Basic".length()).trim();
		return Arrays.asList(Boolean.toString(user.isAdmin()), token);
	}	
	
	
//	List All - http://localhost:8090/contactura
	@GetMapping
	public List<?> findAll() {
		return repository.findAll();
	}
	
// 	Find By Id - http://localhost:8090/user/{id}
	@GetMapping(value = "{id}")
	public ResponseEntity<?> findById(@PathVariable long id) {
		return repository.findById(id)
				.map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());
	}
	
//	Create - http://localhost:8090/user
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ContacturaUser create(@RequestBody ContacturaUser user) {
		user.setPassword(encryptPassword(user.getPassword()));
		return repository.save(user);
	}
	
//	Update - http://localhost:8090/user/{id}
	@PutMapping(value = "{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable long id, @RequestBody ContacturaUser user) {
		return repository.findById(id)
				.map(record -> {
					record.setUsername(user.getUsername());
					record.setPassword(encryptPassword(user.getPassword()));
					record.setName(user.getName());
					record.setAdmin(user.isAdmin());
					ContacturaUser update = repository.save(record);
					return ResponseEntity.ok().body(update);
				}).orElse(ResponseEntity.notFound().build());	
				
	}
	
//	Delete - http://localhost:8090/user/{id}
	@DeleteMapping(path = {"/{id}"})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable long id){
		return repository.findById(id)
				.map(record -> {
					repository.deleteById(id);
					return ResponseEntity.ok().build();
				}).orElse(ResponseEntity.notFound().build());
	}
	
	private String encryptPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordCrypt = passwordEncoder.encode(password);
		
		return passwordCrypt;
	}

}
