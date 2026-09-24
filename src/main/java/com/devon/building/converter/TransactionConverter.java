package com.devon.building.converter;

import com.devon.building.entity.Transaction;
import com.devon.building.model.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConverter {
    private final ModelMapper modelMapper;

    public Transaction toTransaction(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);

    }

    public TransactionDTO toTransactionDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    public TransactionDTO transactionDTO(Transaction transaction) {
        return toTransactionDTO(transaction);
    }
}
