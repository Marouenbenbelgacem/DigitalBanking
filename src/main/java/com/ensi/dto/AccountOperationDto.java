package com.ensi.dto;

import java.util.Date;
import com.ensi.enums.OperationType;

import lombok.Data;
@Data
public class AccountOperationDto {

		private Long id;
	    private Date operationDate;
	    private double amount;
	    private OperationType type;
	    private String description;
}
