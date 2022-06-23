package com.ensi.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ensi.dto.BankAccountDto;
import com.ensi.dto.CurrentAccountDto;
import com.ensi.dto.CustomerDto;
import com.ensi.dto.SavingAccountDto;
import com.ensi.entities.AccountOperation;
import com.ensi.entities.BankAccount;
import com.ensi.entities.CurrentAccount;
import com.ensi.entities.Customer;
import com.ensi.entities.SavingAccount;
import com.ensi.enums.OperationType;
import com.ensi.exceptions.BalanceNotSufficientException;
import com.ensi.exceptions.BankAccountNotFoundException;
import com.ensi.exceptions.CustomerNotFountException;
import com.ensi.mapper.BankAccountMapperImpl;
import com.ensi.repositories.AccountOperationRepository;
import com.ensi.repositories.BankAccountRepository;
import com.ensi.repositories.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

	private CustomerRepository customerRepository;
	private BankAccountRepository bankAccountRepository;
	private AccountOperationRepository accountOperationRepository;
	private BankAccountMapperImpl dtoMapper;

	@Override
	public CustomerDto saveCustomer(CustomerDto customerDto) {

		Customer customer = dtoMapper.fromCustomerDTO(customerDto);
		Customer savedCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}
	
	@Override
	public CustomerDto updateCustomer(CustomerDto customerDto) {

		Customer customer = dtoMapper.fromCustomerDTO(customerDto);
		Customer savedCustomer = customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}
	
	@Override 
		public void deleteCustomer (Long customerId) {
		customerRepository.deleteById(customerId);
	}
	
	@Override
	public CustomerDto getCustomer(Long customerId) throws CustomerNotFountException {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFountException("Customer Not Found"));

		return dtoMapper.fromCustomer(customer);
	}
	

	@Override
	public CurrentAccountDto saveCurrentAccount(double initialBalance, double overDraft, Long customerId)
			throws CustomerNotFountException {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null)
			throw new CustomerNotFountException("Customer not found");
		CurrentAccount currentAccount = new CurrentAccount();
		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setCreatedAt(new Date());
		currentAccount.setBalance(initialBalance);
		currentAccount.setOverDraft(overDraft);
		currentAccount.setCustomer(customer);
		CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
		return dtoMapper.fromCurrentBankAccount(savedBankAccount);
	}

	@Override
	public SavingAccountDto saveSavingAccount(double initialBalance, double interestRate, Long customerId)
			throws CustomerNotFountException {

		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null)
			throw new CustomerNotFountException("Customer not found");
		SavingAccount savingAccount = new SavingAccount();
		savingAccount.setId(UUID.randomUUID().toString());
		savingAccount.setCreatedAt(new Date());
		savingAccount.setBalance(initialBalance);
		savingAccount.setInterestRate(interestRate);
		savingAccount.setCustomer(customer);
		SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
		return dtoMapper.fromSavingAccount(savedBankAccount);

	}

	@Override
	public List<CustomerDto> listCustomers() {

		List<Customer> customers = customerRepository.findAll();
		List<CustomerDto> customersDto = customers.stream().map(customer -> dtoMapper.fromCustomer(customer))
				.collect(Collectors.toList());

		return customersDto;
	}

	@Override
	public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {
		
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Bank Account Not Found"));
		if (bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return dtoMapper.fromSavingAccount(savingAccount);
		}else {
			CurrentAccount currentAccount = (CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentBankAccount(currentAccount);
		}
	}

	@Override
	public void debit(String accountId, double amount, String descreption) throws BankAccountNotFoundException, BalanceNotSufficientException {

		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Bank Accuont Not Found"));
		if (bankAccount.getBalance()<amount) {
			throw new BalanceNotSufficientException("Balance not sufficient");
		}
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setAmount(amount);
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setDescription(descreption);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance() - amount);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void credit(String accountId, double amount, String descreption) throws BankAccountNotFoundException, BalanceNotSufficientException {

		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountNotFoundException("Bank Accuont Not Found"));
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setAmount(amount);
		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setDescription(descreption);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance() + amount);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	public void transfer(String accountSourceId, double amount, String accountDestinationId) throws BankAccountNotFoundException, BalanceNotSufficientException {
debit(accountSourceId, amount, "Transfer to"+accountDestinationId);
		credit(accountSourceId, amount, "Transfert From"+accountDestinationId);
	}

	@Override
	public List<BankAccountDto> listBankAccount() {	
		List<BankAccount> bankAccounts = bankAccountRepository.findAll();
		List<BankAccountDto> bankAccountDtos = bankAccounts.stream().map(bankAccount -> {
			if (bankAccount instanceof SavingAccount) {
				SavingAccount savingAccount = (SavingAccount) bankAccount;
				return dtoMapper.fromSavingAccount(savingAccount);
			} else {
				CurrentAccount currentAccount = (CurrentAccount) bankAccount;
				return dtoMapper.fromCurrentBankAccount(currentAccount);
			}
		}).collect(Collectors.toList());
		return bankAccountDtos;

	}

}
