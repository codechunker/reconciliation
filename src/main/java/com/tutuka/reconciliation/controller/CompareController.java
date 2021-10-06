package com.tutuka.reconciliation.controller;

import com.tutuka.reconciliation.dto.GenericResponse;
import com.tutuka.reconciliation.dto.ReconciliationResponse;
import com.tutuka.reconciliation.model.Transaction;
import com.tutuka.reconciliation.service.ReconciliationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/reconciliation")
public class CompareController {

    final ReconciliationService reconciliationService;

    public CompareController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @PostMapping(value = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> compareTransactions(@RequestParam("tutukaFile")MultipartFile tutukaFile, @RequestParam("clientFile")MultipartFile clientFile ) {
        ReconciliationResponse reconciledTransactions = reconciliationService.compareTransactions(tutukaFile, clientFile);
        return new ResponseEntity<>(new GenericResponse<>(HttpStatus.CREATED.getReasonPhrase(), "", reconciledTransactions), HttpStatus.CREATED);
    }

}
