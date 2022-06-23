package com.ensi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ensi.dto.BankAccountDto;
import com.ensi.dto.CurrentAccountDto;
import com.ensi.dto.CustomerDto;
import com.ensi.dto.SavingAccountDto;
import com.ensi.entities.BankAccount;
import com.ensi.entities.CurrentAccount;
import com.ensi.entities.Customer;
import com.ensi.entities.SavingAccount;
import com.ensi.exceptions.BalanceNotSufficientException;
import com.ensi.exceptions.BankAccountNotFoundException;
import com.ensi.exceptions.CustomerNotFountException;

public interface BankAccountService {

	CustomerDto saveCustomer (CustomerDto customerDto);
	CustomerDto getCustomer (Long customerId) throws CustomerNotFountException;

	CustomerDto updateCustomer(CustomerDto customerDto);
	
	void deleteCustomer(Long customerId);
	
	CurrentAccountDto saveCurrentAccount (double initialBalance, double overDraft, Long customerId) throws CustomerNotFountException;
	
	SavingAccountDto saveSavingAccount (double initialBalance, double interestRate, Long customerId) throws CustomerNotFountException;
	
	List<CustomerDto> listCustomers();
	List<BankAccountDto> listBankAccount();
	
	BankAccountDto getBankAccount (String accountId) throws BankAccountNotFoundException;
	
	void debit (String accountId, double amount, String descreption) throws BankAccountNotFoundException, BalanceNotSufficientException;
	
	void credit (String accountId, double amount, String descreption) throws BankAccountNotFoundException, BalanceNotSufficientException;
	
	void transfer (String accountSourceId, double amount, String accountDestinationId ) throws BankAccountNotFoundException, BalanceNotSufficientException;
	

	
}
