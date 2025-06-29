package com.au;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.au.response.ResponseHandler;

//@RunWith(SpringRunner.class) // For JUnit 4; In JUnit 5, we can use @ExtendWith
@ExtendWith(SpringExtension.class)
@SpringBootTest 
public class ResponseHandlerTest {

	@Test
	public void toConvertCommaSeperatedIdsAsListTest() {
		System.out.println("Starting");
		String input = "1,2,3,4,5";
		List<Integer> result = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());

		List<Integer> actualResult = ResponseHandler.toConvertCommaSeperatedIdsAsList(input);

		assertThat(result).hasSameElementsAs(actualResult);
		System.out.println("Finished");
	}

}
