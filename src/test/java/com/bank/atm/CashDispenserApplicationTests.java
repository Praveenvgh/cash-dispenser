package com.bank.atm;


import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
class CashDispenserApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	private String baseUrl = "/atm/v1";
	
	@Test
	@WithUserDetails("admin")
	@Order(1)
	public void shouldReturnCurrentBalance() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/currentBalance"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.TWENTY").value(200))
		.andExpect(MockMvcResultMatchers.jsonPath("$.FIFTY").value(200));
	}
	
	@Test
	@WithUserDetails("guest")
	@Order(2)
	public void shouldWithdrawCash() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/withdraw/520"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.TWENTY").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.FIFTY").value(10));
	}
	
	@Test
	@WithUserDetails("admin")
	@Order(3)
	public void shouldReturnCurrentBalanceAfterWithdraw() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/currentBalance"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.TWENTY").value(199))
		.andExpect(MockMvcResultMatchers.jsonPath("$.FIFTY").value(190));
	}
	
	@Test
	@WithUserDetails("admin")
	@Order(4)
	public void shouldReloadCash() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/reloadBalance").contentType("application/json").content("{\"TWENTY\":1,\"FIFTY\":10}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.content().string("Cash reloaded"));
	}
	
	@Test
	@WithUserDetails("admin")
	@Order(5)
	public void shouldReturnCurrentBalanceAfterReload() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/currentBalance"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.TWENTY").value(200))
		.andExpect(MockMvcResultMatchers.jsonPath("$.FIFTY").value(200));
	}
	
	@Test
	@WithUserDetails("guest")
	@Order(6)
	public void shouldReturnErrorForInvalidAmount() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/withdraw/10"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("This machine dispenses only 50 and 20 notes, kindly input an amount which can be met with mentioned currency notes"));
	}
	
	@Test
	@WithUserDetails("guest")
	@Order(7)
	public void shouldReturnErrorForOverdrawAmount() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/withdraw/52000"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Amount requested exceeds available balance, kindly input another amount"));
	}
}
