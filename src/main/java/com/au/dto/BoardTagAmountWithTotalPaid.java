package com.au.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardTagAmountWithTotalPaid {

    private List<BoardTagAmountDto> boardTagAmountDtoList;
    private Integer rtgsFeeHistoryId;
    private Double rtgsBalanceAmount;
    private Double paidAmount;


}
