package com.devon.building.service.impl;

import com.devon.building.converter.TransactionConverter;
import com.devon.building.entity.Customer;
import com.devon.building.entity.Transaction;
import com.devon.building.exception.DataInvalidException;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.TransactionDTO;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.TransactionRepository;
import com.devon.building.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TransactionConverter transactionConverter;


    @Override
    public List<Transaction> findByCustomerIdAndCode(Long customerId, String code) {
        if (customerId == null || code == null || code.isBlank()) {
            throw new DataInvalidException("Customer ID và Transaction code không hợp lệ");
        }
        return transactionRepository.findByCustomerIdAndCodeAndActiveTrue(customerId, code);
    }

    @Override
    @Transactional
    public ResponseDTO createTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.getCustomerId() == null) {
            throw new DataInvalidException("Khách hàng không hợp lệ");
        }
        Customer customer = customerRepository.findByIdAndActiveTrue(transactionDTO.getCustomerId())
                .orElseThrow(() -> new DataInvalidException("Khách hàng không tồn tại"));

        Transaction transaction = transactionConverter.toTransaction(transactionDTO);
        transaction.setCustomer(customer);
        transaction.setActive(true);
        transactionRepository.save(transaction);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Thêm giao dịch thành công");
        responseDTO.setData(transactionConverter.transactionDTO(transaction));
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.getId() == null) {
            throw new DataInvalidException("Giao dịch này không tồn tại");
        }
        Transaction existTransaction = transactionRepository.findByIdAndActiveTrue(transactionDTO.getId())
                .orElseThrow(() -> new DataInvalidException("Giao dịch này không tồn tại"));

        existTransaction.setNote(transactionDTO.getNote());
        transactionRepository.save(existTransaction);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Chỉnh sửa thành công");
        responseDTO.setData(transactionConverter.transactionDTO(existTransaction));
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO deleteTransaction(Long id) {
        if (id == null) {
            throw new DataInvalidException("Giao dịch không tồn tại");
        }
        Transaction transaction = transactionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DataInvalidException("Giao dịch không tồn tại trong hệ thống"));
        transaction.setActive(false);
        transactionRepository.save(transaction);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Xoá giao dịch thành công");
        return responseDTO;
    }
}
