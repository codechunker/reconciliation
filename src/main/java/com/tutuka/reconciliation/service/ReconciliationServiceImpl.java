package com.tutuka.reconciliation.service;

import com.tutuka.reconciliation.dto.ReconciliationResponse;
import com.tutuka.reconciliation.exception.InvalidFileException;
import com.tutuka.reconciliation.model.SuggestedTransaction;
import com.tutuka.reconciliation.model.Transaction;
import com.tutuka.reconciliation.utils.FileUtils;
import com.tutuka.reconciliation.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.tutuka.reconciliation.utils.AppUtils.*;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService{


    @Override
    public ReconciliationResponse compareTransactions(MultipartFile tutukaFile, MultipartFile clientFile) {
        try {
            File tutukaTempFile = FileUtils.moveToTempDir(tutukaFile);
            File clientTempFile = FileUtils.moveToTempDir(clientFile);
            List<String> errorMessages = FileUtils.validateFiles(tutukaFile, clientFile);
            if (errorMessages.size() != 0) {
                throw new InvalidFileException("Invalid File", HttpStatus.BAD_REQUEST.name(), errorMessages);
            }
            log.info("[+] Reconciling {} and {} files", tutukaFile.getOriginalFilename(), clientFile.getOriginalFilename());

            HashMap<Integer, Transaction> tranMap = extractTransactionsFromFile(tutukaTempFile);
            int totalTutukaTran = tranMap.size();
            List<Transaction> unmatchedClientTransactions = filterTransactions(clientTempFile);
            HashMap<Integer, Transaction> tranMap2 = extractTransactionsFromFile(clientTempFile);
            int totalClientTran = tranMap2.size();
            List<Transaction> unmatchedTutukaTransactions = filterTransactions(tutukaTempFile);
            suggestTransactions(unmatchedTutukaTransactions, unmatchedClientTransactions);//suggest for first file
            log.info("[+] Done Reconciling {} and {} files", tutukaFile.getOriginalFilename(), clientFile.getOriginalFilename());
            return Mapper.mapTransactionsToReconciled(totalTutukaTran, totalClientTran, unmatchedTutukaTransactions, unmatchedClientTransactions);
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }




}
