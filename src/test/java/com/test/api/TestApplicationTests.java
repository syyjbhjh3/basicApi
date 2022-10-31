package com.test.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.test.api.dto.UserResponseDto;
import com.test.api.entity.User;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
class TestApplicationTests {
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void singUpTest() throws Exception{
		mvc.perform(
			post("/szs/signup") 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON) 
				.characterEncoding("UTF-8") 
				.content( "{" + " \"userId\" : \"hong12\", " 
							  + " \"password\" : \"123456\", " 
							  + " \"name\": \"홍길동\", " 
							  + " \"regNo\": \"8608241655068\" " +"}") //dto로 변경
			)
			.andExpect(status().isOk())
			.andDo(print());
	}	

	@Test
	public void loginTest() throws Exception{
		mvc.perform(
			post("/szs/login") 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON) 
				.characterEncoding("UTF-8") 
				.content( "{" + " \"userId\" : \"hong12\", " 
							  + " \"password\" : \"123456\", ") //dto로 변경
			)
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	public void meTest() throws Exception{
		mvc.perform(
			get("/szs/me") 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON) 
				.characterEncoding("UTF-8")
				.header("Authorization", "Bearer eyJraWQiOiJkZWZhdWx0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjUzMTI3MDY4LCJpbmZvIjp7InVzZXJObyI6MSwidXNlcklkIjoiaG9uZzEyIn19.FTBA263h5091vxplydcIxZX48DCnhjHN8RKyZN1KFsk"))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	public void scrapTest() throws Exception{
		mvc.perform(
			post("/szs/scrap") 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON) 
				.characterEncoding("UTF-8") 
				.header("Authorization", "Bearer eyJraWQiOiJkZWZhdWx0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjUzMTI3MDY4LCJpbmZvIjp7InVzZXJObyI6MSwidXNlcklkIjoiaG9uZzEyIn19.FTBA263h5091vxplydcIxZX48DCnhjHN8RKyZN1KFsk"))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	public void refundTest() throws Exception{
		mvc.perform(
			get("/szs/refund") 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON) 
				.characterEncoding("UTF-8") 
				.header("Authorization", "Bearer eyJraWQiOiJkZWZhdWx0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjUzMTI3MDY4LCJpbmZvIjp7InVzZXJObyI6MSwidXNlcklkIjoiaG9uZzEyIn19.FTBA263h5091vxplydcIxZX48DCnhjHN8RKyZN1KFsk"))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
