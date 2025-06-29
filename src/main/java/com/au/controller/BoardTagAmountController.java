package com.au.controller;

import com.au.dto.BoardTagAmountDto;
import com.au.dto.BoardTagAmountWithTotalPaid;
import com.au.dto.JwtDetails;
import com.au.model.BoardTagAmount;
import com.au.response.ResponseHandler;
import com.au.service.BoardTagAmountService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Author: Anjan
 * Date: 19-12-2024
 * Description: AcerpStdTagBoardAmountController class
 */

@RestController
@RequestMapping("/api/${secretkey9}")
@RequiredArgsConstructor
public class BoardTagAmountController {

    private final BoardTagAmountService acerpStdTagBoardAmountService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/studentTagBoardAmount")
    public ResponseEntity<Object> save(@RequestBody BoardTagAmountWithTotalPaid boardTag,
                                       @RequestHeader("Authorization") String jwtToken) throws IOException {

        if(RateLimitController.bucket.tryConsume(1)) {
            JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
            boardTag.getBoardTagAmountDtoList().stream().forEach(bt -> {
                bt.setCreated_by(jwtDetails.getUserId());
                bt.setCreated_username(jwtDetails.getUserName());
            });
            return acerpStdTagBoardAmountService.saveAll(boardTag);
        }
        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
    }

    @PutMapping("/updateStudentTagBoardAmount")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody BoardTagAmountDto boardTagAmountDto
            , @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException{

        if(RateLimitController.bucket.tryConsume(1)) {
        	JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
            boardTagAmountDto.setModified_by(jwtDetails.getUserId());
            boardTagAmountDto.setModified_username(jwtDetails.getUserName());
            return acerpStdTagBoardAmountService.update(id, boardTagAmountDto);
        }
        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
    }

    @GetMapping("/studentTagBoardAmountDetails")
    public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
                                           @RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
        Sort sorted = Sort.by(Sort.Direction.DESC, sort );
        if(keyword != null) {
            Pageable pageable = PageRequest.of(page, page_size,sorted);
            return  acerpStdTagBoardAmountService.listAll1(pageable, keyword);
        }else {
            Pageable pageable1 = PageRequest.of(page, page_size,sorted);
            return acerpStdTagBoardAmountService.listAll2(pageable1);
        }
    }



}
