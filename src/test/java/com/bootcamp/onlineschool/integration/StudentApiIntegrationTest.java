package com.bootcamp.onlineschool.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StudentApiIntegrationTest extends IntegrationTestBase {

    @Test
    public void createAndGetStudent() {
        String url = baseUrl("/api/students");
        Map<String,Object> req = Map.of("id","S100","name","Alice","email","alice@school.edu","gpa",3.7);
        ResponseEntity<Map> resp = restTemplate.postForEntity(url, req, Map.class);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertTrue(resp.getHeaders().getLocation().toString().contains("/api/students/S100"));

        ResponseEntity<Map[]> all = restTemplate.getForEntity(url, Map[].class);
        assertEquals(HttpStatus.OK, all.getStatusCode());
        assertTrue(all.getBody().length >= 1);
    }

    @Test
    public void getById_NotFound() {
        String url = baseUrl("/api/students/NOPE");
        ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    public void updateStudent() {
        String base = baseUrl("/api/students");
        Map<String,Object> req = Map.of("id","S200","name","Bob","email","bob@school.edu","gpa",2.5);
        restTemplate.postForEntity(base, req, Map.class);

        Map<String,Object> update = Map.of("id","S200","name","Bobby","email","bob@school.edu","gpa",3.0);
        HttpEntity<Map<String,Object>> ent = new HttpEntity<>(update);
        ResponseEntity<Map> r = restTemplate.exchange(base + "/S200", HttpMethod.PUT, ent, Map.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals("Bobby", r.getBody().get("name"));
    }

    @Test
    public void deleteStudent() {
        String base = baseUrl("/api/students");
        Map<String,Object> req = Map.of("id","S300","name","Carol","email","carol@school.edu","gpa",3.2);
        restTemplate.postForEntity(base, req, Map.class);

        ResponseEntity<Void> del = restTemplate.exchange(base + "/S300", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, del.getStatusCode());

        ResponseEntity<Map> get = restTemplate.getForEntity(base + "/S300", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, get.getStatusCode());
    }

    @Test
    public void validationErrorOnCreate() {
        String url = baseUrl("/api/students");
        Map<String,Object> req = Map.of("id","S400","name","Invalid","email","not-an-email","gpa",5.0);
        ResponseEntity<Map> resp = restTemplate.postForEntity(url, req, Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().containsKey("errors") || resp.getBody().containsKey("message"));
    }

    @Test
    public void filterByName() {
        String base = baseUrl("/api/students");
        restTemplate.postForEntity(base, Map.of("id","S500","name","Zachary","email","zach@school.edu","gpa",3.0), Map.class);
        ResponseEntity<Map[]> resp = restTemplate.getForEntity(base + "?name=Zach", Map[].class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test
    public void getHighAchievers() {
        String base = baseUrl("/api/students");
        restTemplate.postForEntity(base, Map.of("id","S600","name","High","email","high@school.edu","gpa",3.9), Map.class);
        ResponseEntity<Map[]> resp = restTemplate.getForEntity(base + "/high-achievers?gpa=3.5", Map[].class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().length >= 1);
    }

}
