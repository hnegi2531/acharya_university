package com.au.event;

import java.util.List;

import com.au.dto.SettlementList;
import com.au.dto.TransactionSettlementDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSettlementEvent {

	private TransactionSettlementDTO transactionSettlementDTO;
}
