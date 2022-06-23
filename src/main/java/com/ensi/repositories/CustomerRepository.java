package com.ensi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensi.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long>  {

}
