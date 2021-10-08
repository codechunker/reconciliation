package com.tutuka.reconciliation.service;

import com.tutuka.reconciliation.dto.ReconciliationResponse;
import com.tutuka.reconciliation.exception.InvalidFileException;
import com.tutuka.reconciliation.model.Transaction;
import com.tutuka.reconciliation.utils.FileUtils;
import com.tutuka.reconciliation.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.tutuka.reconciliation.utils.AppUtils.extractTransactionsFromFile;
import static com.tutuka.reconciliation.utils.AppUtils.filterTransactions;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService{



    private long transactionCount;
    private long matchedTranCount;
    private long unmatchedTranCount;


    @Override
    public ReconciliationResponse compareTransactions(MultipartFile tutukaFile, MultipartFile clientFile) {
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
        System.out.println("unmatched client: "+unmatchedClientTransactions.size());
        HashMap<Integer, Transaction> tranMap2 = extractTransactionsFromFile(clientTempFile);
        int totalClientTran = tranMap2.size();
        List<Transaction> unmatchedTutukaTransactions = filterTransactions(tutukaTempFile);
        System.out.println("unmatched tutuka: "+unmatchedTutukaTransactions.size());

        //Card Campaign
        //Card Campaign
        return Mapper.mapTransactionsToReconciled(totalTutukaTran, totalClientTran, unmatchedTutukaTransactions, unmatchedClientTransactions, new ArrayList<>());
    }






}
