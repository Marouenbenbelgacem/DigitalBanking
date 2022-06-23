package com.ensi.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ensi.dto.AccountOperationDto;
import com.ensi.dto.CurrentAccountDto;
import com.ensi.dto.CustomerDto;
import com.ensi.dto.SavingAccountDto;
import com.ensi.entities.AccountOperation;
import com.ensi.entities.CurrentAccount;
import com.ensi.entities.Customer;
import com.ensi.entities.SavingAccount;
@Service

public class BankAccountMapperImpl {
	
	
	public CustomerDto fromCustomer (Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		
		BeanUtils.copyProperties(customer, customerDto);
		return customerDto;
	}	
	public Customer fromCustomerDTO (CustomerDto customerDto) {
		Customer customer = new Customer();
		
		BeanUtils.copyProperties(customerDto, customer);
		return customer;
	}
	
	
	public CurrentAccountDto fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentAccountDto currentAccountDto=new CurrentAccountDto();
        BeanUtils.copyProperties(currentAccount,currentAccountDto);
        currentAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentAccountDto.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDto;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentAccountDto currentBankAccountDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDto()));
        return currentAccount;
    }
    
    public SavingAccountDto fromSavingAccount (SavingAccount savingAccount) {
    	
    	SavingAccountDto savingAccountDto  = new SavingAccountDto();
    	BeanUtils.copyProperties(savingAccount, savingAccountDto);
    	savingAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
    	savingAccountDto.setType(savingAccount.getClass().getSimpleName());
    	return savingAccountDto;
    }
    
    public SavingAccount fromSavingAccountDto (SavingAccountDto savingAccountDto) {
    	
    	SavingAccount savingAccount = new SavingAccount();
    	BeanUtils.copyProperties(savingAccountDto, savingAccount);
    	savingAccount.setCustomer(fromCustomerDTO(savingAccountDto.getCustomerDto()));

    	return savingAccount;
    	
    }
    public AccountOperationDto fromAccountOperation(AccountOperation accountOperation) {
    	 AccountOperationDto accountOperationDto = new AccountOperationDto();
    	 BeanUtils.copyProperties(accountOperation, accountOperationDto);
    	
    	return accountOperationDto;
    }
    

}
