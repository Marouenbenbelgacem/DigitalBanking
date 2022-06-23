package com.ensi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensi.entities.AccountOperation;

public interface AccountOperationRepository  extends JpaRepository<AccountOperation,Long>{

}
