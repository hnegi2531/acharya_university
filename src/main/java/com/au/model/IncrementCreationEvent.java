package com.au.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncrementCreationEvent {

	private List<TemporaryIncrementCreation> incrementCreations;
	
}
