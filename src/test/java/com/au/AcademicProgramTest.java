package com.au;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.au.exception.AcademicProgramNotFoundException;
import com.au.model.AcademicProgram;
import com.au.repository.AcademicProgramRepository;
import com.au.service.AcademicProgramService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest 
public class AcademicProgramTest {
	
	
	 	@Mock
	    private AcademicProgramRepository academicProgramRepository;

	    @InjectMocks
	    private AcademicProgramService academicProgramService;
	    
	   
	    private AcademicProgram academicProgram;

	    @BeforeEach
	    public void setUp() {
	    	System.out.println("academicProgram initialized: " + academicProgram);
	        academicProgram = new AcademicProgram();
	        academicProgram.setAc_year_id(1);
	        academicProgram.setProgram_id(2);
	        System.out.println("academicProgram initialized: " + academicProgram);
	    }
	    
	    @Test
	    public void testSaveAcademicProgram_ShouldThrowException_WhenProgramExists() {
	        when(academicProgramRepository.getProgram(academicProgram.getAc_year_id(), academicProgram.getProgram_id()))
	                .thenReturn(1);  
	        System.out.println(academicProgramRepository.getProgram(academicProgram.getAc_year_id(), academicProgram.getProgram_id()));
	        try {
	            academicProgramService.save_AcademicProgram(academicProgram);
	            System.out.println("Test Satrted");
	            fail("Expected AcademicProgramNotFoundException to be thrown");
	        } catch (AcademicProgramNotFoundException e) {
	            // Expected exception
	            assertEquals("Academic Program already exists", e.getMessage());
	        }
	    }
	    
	    @Test
	    public void testSaveAcademicProgram_ShouldSave_WhenProgramDoesNotExist() {
	        when(academicProgramRepository.getProgram(academicProgram.getAc_year_id(), academicProgram.getProgram_id()))
	                .thenReturn(0);  
	        when(academicProgramRepository.save(academicProgram)).thenReturn(academicProgram);  

	        
	        AcademicProgram result = academicProgramService.save_AcademicProgram(academicProgram);

	        // Assert: Verify that the save method was called and the returned object is correct
	        assertNotNull(result);
	        assertEquals(academicProgram, result);
	        System.out.println("cretion ");
	        verify(academicProgramRepository, times(1)).save(academicProgram);  // Verify save was called once
	    }


}
