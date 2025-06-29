package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.ProgramMission;
import com.au.model.ProgramMissionComponent;
import com.au.service.JwtTokenService;
import com.au.service.ProgramMissionComponentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/ProgramMissionComponent")
@CrossOrigin
public class ProgramMissionComponentController {

	@Autowired
	private ProgramMissionComponentService a_service;
	
	Logger log = LoggerFactory.getLogger(ProgramMissionController.class);
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping
	public ResponseEntity<ProgramMissionComponent> saveAliasName(@RequestBody @Valid ProgramMissionComponent bs,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		bs.setCreatedBy(jwtDetails.getUserId());
		bs.setCreatedUsername(jwtDetails.getUserName());
		ProgramMissionComponent bs1 = a_service.saveProgramMissionComponent(bs);
		return new ResponseEntity<ProgramMissionComponent>(bs1, HttpStatus.CREATED);
	}

	@GetMapping
	public List<ProgramMissionComponent> listAll() {
		return a_service.listAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProgramMissionComponent> get(@PathVariable Integer id) {
		try {

			ProgramMissionComponent product = a_service.get(id);
			return new ResponseEntity<ProgramMissionComponent>(product, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<ProgramMissionComponent>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProgramMissionComponent> update(@RequestBody ProgramMissionComponent bs, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		try {
			ProgramMissionComponent existProduct = a_service.get(id);
			bs.setModifiedBy(jwtDetails.getUserId());
			bs.setModifiedUsername(jwtDetails.getUserName());
			a_service.saveProgramMissionComponent(bs);
			return new ResponseEntity<ProgramMissionComponent>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<ProgramMissionComponent>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		a_service.delete(id);
	}
}
