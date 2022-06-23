package com.ensi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensi.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount,String>{

}
