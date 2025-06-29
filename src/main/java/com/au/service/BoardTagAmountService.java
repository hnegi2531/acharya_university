package com.au.service;

import com.au.dto.BoardTagAmountDto;
import com.au.dto.BoardTagAmountWithTotalPaid;
import com.au.model.BoardTagAmount;
import com.au.repository.BoardTagAmountRepository;
import com.au.repository.PaidBoardDueRepository;
import com.au.repository.RTGSFeeHistoryRepository;
import com.au.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Author: Anjan
 * Date: 19-12-2024
 * Description: AcerpStdTagBoardAmountService class
 */

@Service
@RequiredArgsConstructor
public class BoardTagAmountService {

    private final BoardTagAmountRepository acerpStdTagBoardAmountRepository;
    private final ModelMapper modelMapper;
    private final RTGSFeeHistoryRepository rtgsFeeHistoryRepository;
    private final PaidBoardDueRepository paidBoardDueRepository;

    public ResponseEntity<Object> saveAll(BoardTagAmountWithTotalPaid boardTag) {
        boardTag.getBoardTagAmountDtoList().stream().forEach(at -> {
            BoardTagAmount acerpStdTagBoardAmount = modelMapper.map(at, BoardTagAmount.class);
            BoardTagAmount savedAcerpStdTagBoardAmount = acerpStdTagBoardAmountRepository.save(acerpStdTagBoardAmount);
            paidBoardDueRepository.callStudentPaidBoardDue(at.getStudent_id());
        });
        rtgsFeeHistoryRepository.updatePaidAndBalanceAmount(boardTag.getPaidAmount(),boardTag.getRtgsBalanceAmount(),boardTag.getRtgsFeeHistoryId());

        return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");

    }

    public ResponseEntity<Object> update(Integer id, BoardTagAmountDto acerpStdTagBoardAmountDto) {
        Optional<BoardTagAmount> optionalAcerpStdTagBoardAmount = acerpStdTagBoardAmountRepository.findById(id);

        if(!optionalAcerpStdTagBoardAmount.isPresent()){
            return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "No data found with id: " + id);
        }
        optionalAcerpStdTagBoardAmount = Optional.ofNullable(modelMapper.map(acerpStdTagBoardAmountDto, BoardTagAmount.class));
        BoardTagAmount savedAcerpStdTagBoardAmount = acerpStdTagBoardAmountRepository.save(optionalAcerpStdTagBoardAmount.get());
        return ResponseHandler.generateResponse(true, HttpStatus.OK, savedAcerpStdTagBoardAmount);
    }

    public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
        Page<Object> response1 = acerpStdTagBoardAmountRepository.listAll1(pageable, keyword );
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
    }

    public ResponseEntity<Object> listAll2(Pageable pageable) {
        Page<Object> response = acerpStdTagBoardAmountRepository.listAll2(pageable);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
    }
}
