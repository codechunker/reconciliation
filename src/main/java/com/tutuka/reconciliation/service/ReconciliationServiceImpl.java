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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService{

    private static final String DELIMITER = ",";

    @Override
    public ReconciliationResponse compareTransactions(MultipartFile tutukaFile, MultipartFile clientFile) {
        File tutukaTempFile = FileUtils.moveToTempDir(tutukaFile);
        File clientTempFile = FileUtils.moveToTempDir(clientFile);
        List<String> errorMessages = FileUtils.validateFiles(tutukaFile, clientFile);
        if (errorMessages.size() != 0) {
            throw new InvalidFileException("Invalid File", HttpStatus.BAD_REQUEST.name(), errorMessages);
        }
        log.info("[+] Reconciling {} and {} files", tutukaFile.getOriginalFilename(), clientFile.getOriginalFilename());

        //Add to set to remove duplicate transactions
        Set<Transaction> tutukaTempHold = extractTransactionsFromFile(tutukaTempFile);
        Set<Transaction> clientTempHold = extractTransactionsFromFile(clientTempFile);

        //Copy to another set to prevent changes from affecting the original Set
        Set<Transaction> tutukaTransactions = new HashSet<>(tutukaTempHold);
        Set<Transaction> clientTransactions = new HashSet<>(clientTempHold);

        Set<Transaction> unmatchedTutukaTransactions = filterUnmatchedTransactions(tutukaTempHold, clientTempHold);
        Set<Transaction> unmatchedClientTransactions = filterUnmatchedTransactions(clientTempHold, tutukaTempHold);
        return Mapper.mapTransactionsToReconciled(tutukaTransactions, clientTransactions, unmatchedTutukaTransactions, unmatchedClientTransactions, new HashSet<>(), "");
    }

    /**
     * transactions1.removeAll(transactions2) get all the transactions in
     * transaction1 that could not be found in transactions2. This is in turn,
     * returns unmatched transactions
     * @param transactions1
     * @param transactions2
     * @return
     */
    private Set<Transaction> filterUnmatchedTransactions(Set<Transaction> transactions1, Set<Transaction> transactions2) {
        boolean foundUnmatched = transactions1.removeAll(transactions2);
        return foundUnmatched ? transactions1 : new HashSet<>();
    }

    private Set<Transaction> extractTransactionsFromFile(File tutukaTempFile) {
        Set<Transaction> transactions;
        try {
            InputStream fileInputStream = new FileInputStream(tutukaTempFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            Stream<String> skipHeader = bufferedReader.lines().skip(1);
            transactions = skipHeader.map(mapToTransaction).collect(Collectors.toCollection(HashSet::new));
            bufferedReader.close();
        }catch (IOException e) {
            throw new IllegalArgumentException("Error extracting transactions");
        }
        return transactions;
    }

    private final Function<String, Transaction> mapToTransaction = (row) -> {
        Transaction transaction = new Transaction();
        try {
            String[] cells = row.split(DELIMITER);
            transaction.setProfileName(cells[0].trim());
            transaction.setTransactionDate(cells[1].trim());
            transaction.setTransactionAmount(cells[2].trim());
            transaction.setTransactionNarrative(cells[3].trim());
            transaction.setTransactionDescription(cells[4].trim());
            transaction.setTransactionID(cells[5].trim());
            transaction.setTransactionType(cells[6].trim());
            transaction.setWalletReference(cells[7].trim());
            return transaction;
        } catch (Exception e) {
            log.error("", e);
            return transaction;
        }
    };

}
