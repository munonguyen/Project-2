package com.devon.building.service;

import com.devon.building.entity.Transaction;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    List<Transaction> findByCustomerIdAndCode(Long customerId, String code);
    ResponseDTO createTransaction(TransactionDTO transactionDTO);
    ResponseDTO updateTransaction(TransactionDTO transactionDTO);
    ResponseDTO deleteTransaction(Long id);


}
