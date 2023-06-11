package com.openai.prompt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PromptControllerTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getPromptDTOs() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/prompt")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
			.andExpect(jsonPath("$.[0].model", is("gpt-3.5-turbo-0301")))
			.andExpect(jsonPath("$.[0].prompts", hasSize(greaterThanOrEqualTo(1))))
			.andExpect(jsonPath("$.[1].responses", hasSize(greaterThanOrEqualTo(1))))
			.andExpect(jsonPath("$.[1].total_tokens", greaterThanOrEqualTo(10)));
	}

	@Test
	public void getPromptRecords() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/prompt/records")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
			.andExpect(jsonPath("$.[0].model", is("gpt-3.5-turbo-0301")))
			.andExpect(jsonPath("$.[1].model", is("gpt-3.5-turbo-0301")));
	}

	@Test
	public void getPromptRecord() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/prompt/records/2")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.model", is("gpt-3.5-turbo-0301")))
			.andExpect(jsonPath("$.max_tokens", is(5)));
	}

	@Test
	public void findPromptDTOs() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/prompt/find/response?keyword=hello")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
			.andExpect(jsonPath("$.[0].model", is("gpt-3.5-turbo-0301")))
			.andExpect(jsonPath("$.[0].prompts", hasSize(greaterThanOrEqualTo(1))))
			.andExpect(jsonPath("$.[0].responses", hasSize(greaterThanOrEqualTo(1))))
			.andExpect(jsonPath("$.[0].total_tokens", greaterThanOrEqualTo(10)));
	}

	@Test
	public void getUsage() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/usage?month=6")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.total_requests", notNullValue()))
			.andExpect(jsonPath("$.total_usage", notNullValue()))
			.andExpect(jsonPath("$.total_cost", notNullValue()));
	}
}
