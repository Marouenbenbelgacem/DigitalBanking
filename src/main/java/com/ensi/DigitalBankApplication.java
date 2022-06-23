package com.ensi;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ensi.dto.CurrentAccountDto;
import com.ensi.dto.CustomerDto;
import com.ensi.dto.BankAccountDto;
import com.ensi.dto.SavingAccountDto;
import com.ensi.entities.AccountOperation;
import com.ensi.entities.CurrentAccount;
import com.ensi.entities.Customer;
import com.ensi.entities.SavingAccount;
import com.ensi.enums.AccountStatus;
import com.ensi.enums.OperationType;
import com.ensi.services.BankAccountService;

import lombok.extern.slf4j.Slf4j;

import com.ensi.exceptions.CustomerNotFountException;
import com.ensi.repositories.AccountOperationRepository;
import com.ensi.repositories.BankAccountRepository;
import com.ensi.repositories.CustomerRepository;

@SpringBootApplication
public class DigitalBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankApplication.class, args);
	}

	
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) throws CustomerNotFountException {

		return args -> {

			Stream.of("Krim", "Malek", "Amine", "Amal").forEach(name -> {
				CustomerDto customerDto = new CustomerDto();
				customerDto.setName(name);
				customerDto.setEmail(name + "gmail.com");
				bankAccountService.saveCustomer(customerDto);
			});

			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveCurrentAccount((Math.random() * 90000), 9000, customer.getId());
					bankAccountService.saveSavingAccount((Math.random() * 120000), 5.5, customer.getId());
				} catch (CustomerNotFountException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			});

			List<BankAccountDto> bankAccounts = bankAccountService.listBankAccount();

			for (BankAccountDto bankAccountDto : bankAccounts) {
				for (int i = 0; i < 10; i++) {
					String accountId;
					if (bankAccountDto instanceof SavingAccountDto) {
						accountId = ((SavingAccountDto) bankAccountDto).getId();

					} else {
						accountId = ((CurrentAccountDto) bankAccountDto).getId();
					}
					
					bankAccountService.credit(accountId, 100000 + Math.random() * 1200000, "Credit");
					bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");
				}
			}
		};
		
	}
	
	
	//@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };

    }
    
	}
