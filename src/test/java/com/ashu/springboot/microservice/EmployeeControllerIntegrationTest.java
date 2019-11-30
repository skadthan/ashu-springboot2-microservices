package com.ashu.springboot.microservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.ashu.springboot.microservice.model.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AshuMicroServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port + "/ashu-microservices/api/v1";
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetAllEmployees() {
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
				HttpMethod.GET, entity, String.class);
		
		
		System.out.println("response.getBody(): " + response.getBody());
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetEmployeeById() {
		Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/1", Employee.class);
		System.out.println(employee.getFirstName());
		assertNotNull(employee);
	}

	@Test
	public void testCreateEmployee() {
		Employee employee = new Employee();
		employee.setEmailId("admin@gmail.com");
		employee.setFirstName("admin");
		employee.setLastName("admin");
		System.out.println("getRootUrl(): "+ getRootUrl());
		
		ResponseEntity<Employee> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", employee, Employee.class);
		assertNotNull(postResponse);
		System.out.println("postResponse.getBody(): " + postResponse.getBody());
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateEmployee() {
		int id = 1;
		Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		employee.setFirstName("admin1");
		employee.setLastName("admin2");

		restTemplate.put(getRootUrl() + "/employees/" + id, employee);

		Employee updatedEmployee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		assertNotNull(updatedEmployee);
	}

	@Test
	public void testDeleteEmployee() {
		int id = 2;
		Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		assertNotNull(employee);

		restTemplate.delete(getRootUrl() + "/employees/" + id);

		try {
			employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
	
}
