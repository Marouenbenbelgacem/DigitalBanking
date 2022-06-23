package com.ensi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ensi.dto.CustomerDto;
import com.ensi.exceptions.CustomerNotFountException;
import com.ensi.services.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
	
	private BankAccountService bankService;
	
	@GetMapping("/customers")
	public List <CustomerDto> customers(){
		return bankService.listCustomers();
	}
	@GetMapping("/customers/{id}")
	public CustomerDto getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFountException {
		
		return bankService.getCustomer(customerId);
		
	}
	@PostMapping("/customers")
	public CustomerDto saveCustomer (@RequestBody CustomerDto customerDto) {
		
		return bankService.saveCustomer(customerDto);

	}
	@PutMapping("/customers/{customerId}")
	public CustomerDto updateCustomer (@PathVariable Long customerId,@RequestBody CustomerDto customerDto) {
		
		customerDto.setId(customerId);
	return 	bankService.updateCustomer(customerDto);

	}
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		bankService.deleteCustomer(id);
		
	}

}
