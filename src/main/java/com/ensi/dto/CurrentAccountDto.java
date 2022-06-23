package com.ensi.dto;

import java.util.Date;

import com.ensi.enums.AccountStatus;

import lombok.Data;
@Data

public class CurrentAccountDto extends BankAccountDto{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDto customerDto;
    private double overDraft;
}

