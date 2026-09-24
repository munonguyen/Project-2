package com.devon.building.api.admin;


import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.TransactionDTO;
import com.devon.building.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionAPI {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO){
        return ResponseEntity.ok().body(transactionService.createTransaction(transactionDTO));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateTransaction(@RequestBody @Valid TransactionDTO transactionDTO){
        return ResponseEntity.ok().body(transactionService.updateTransaction(transactionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable Long id) {
        return ResponseEntity.ok().body(transactionService.deleteTransaction(id));
    }


}
