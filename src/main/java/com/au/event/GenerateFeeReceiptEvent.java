package com.au.event;

import com.au.model.BankImportTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateFeeReceiptEvent {
    List<BankImportTransaction> bankImportTransactionList;
}
