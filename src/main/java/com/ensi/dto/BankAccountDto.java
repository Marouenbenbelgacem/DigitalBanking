package com.ensi.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ensi.entities.AccountOperation;
import com.ensi.entities.Customer;
import com.ensi.enums.AccountStatus;

import lombok.Data;
@Data
public class BankAccountDto {

	 private String type;
	
	
}
