package com.tutuka.reconciliation.service;


import com.tutuka.reconciliation.dto.ReconciliationResponse;
import com.tutuka.reconciliation.model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ReconciliationService {
    ReconciliationResponse compareTransactions(MultipartFile tutukaFile, MultipartFile clientFile);
}
