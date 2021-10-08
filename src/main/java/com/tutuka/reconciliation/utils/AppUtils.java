package com.tutuka.reconciliation.utils;

import com.tutuka.reconciliation.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class AppUtils {
    private static final String DELIMITER = ",";
    private static HashMap<Integer, Transaction> tranMap = new HashMap<>();
    private static final HashMap<Integer, Transaction> duplicateMap = new HashMap<>();
    private AppUtils() {
        //DO NOT INITIALIZE
    }

    /**
     * extract transactions in file and store in memory for comparison with another file
     * @param tutukaTempFile
     */
    public static HashMap<Integer, Transaction> extractTransactionsFromFile(File tutukaTempFile) {
        tranMap.clear();
        duplicateMap.clear();
        try {
            InputStream fileInputStream = new FileInputStream(tutukaTempFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            tranMap = bufferedReader.lines().skip(1)//skip header
                    .map(mapToTransaction)
                    .collect(HashMap::new, (map, transaction) -> map.put(transaction.hashCode(), transaction), Map::putAll);
            bufferedReader.close();
        }catch (IOException e) {
            throw new IllegalArgumentException("Error extracting transactions");
        }

        return tranMap;
    }

    /**
     * filter transaction row in csv not found with one already uploaded in memory
     * @param file
     */
    public static List<Transaction> filterTransactions(File file) {
        List<Transaction> unmatchedTransactions;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            unmatchedTransactions = bufferedReader.lines().skip(1)//skip header
                    .map(mapToTransaction).filter(isUnmatched()).distinct().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error extracting Transactions from file");
        }
        return unmatchedTransactions;
    }

    /**
     * filter files that cannot be matched with those already uploaded.
     * for transaction in file and memory, if both have the same hashcode,
     * use equals for extra confirmation
     */
    private static Predicate<Transaction> isUnmatched() {
        return tranFromFile -> {
            int hashCode = tranFromFile.hashCode();
            if (tranMap.containsKey(hashCode) ) {
                Transaction tranInMemory = tranMap.get(hashCode);
                //There is a non-duplicate match, so do not filter
                return !tranFromFile.equals(tranInMemory) || duplicateMap.containsKey(hashCode);
            } else {
                return true;
            }
        };
    }

    private static  final Function<String, Transaction> mapToTransaction = (row) -> {
        Transaction transaction = new Transaction();
        try {
            String[] cells = row.split(DELIMITER);
            transaction.setProfileName(cells[0]);
            transaction.setTransactionDate(cells[1]);
            transaction.setTransactionAmount(cells[2]);
            transaction.setTransactionNarrative(cells[3]);
            transaction.setTransactionDescription(cells[4]);
            transaction.setTransactionID(new BigInteger((cells[5])));
            transaction.setTransactionType(cells[6]);
            transaction.setWalletReference(cells[7]);
            return transaction;
        } catch (Exception e) {
//            log.error("", e);
            return transaction;
        }
    };
}
