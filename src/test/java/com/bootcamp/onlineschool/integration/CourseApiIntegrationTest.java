package com.bootcamp.onlineschool.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CourseApiIntegrationTest extends IntegrationTestBase {

    @Test
    public void createGetAndDeleteCourse() {
        String base = baseUrl("/api/courses");
        Map<String,Object> req = Map.of("id","C100","courseName","Intro","credits",3,"instructor","Dr. A","maxStudents",2);
        ResponseEntity<Map> created = restTemplate.postForEntity(base, req, Map.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());

        ResponseEntity<Map[]> all = restTemplate.getForEntity(base, Map[].class);
        assertEquals(HttpStatus.OK, all.getStatusCode());

        ResponseEntity<Map> get = restTemplate.getForEntity(base + "/C100", Map.class);
        assertEquals(HttpStatus.OK, get.getStatusCode());

        ResponseEntity<Void> del = restTemplate.exchange(base + "/C100", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, del.getStatusCode());
    }

    @Test
    public void enrollAndUnenrollFlow() {
        String base = baseUrl("/api/courses");
        Map<String,Object> req = Map.of("id","C200","courseName","Algo","credits",4,"instructor","Dr. B","maxStudents",1);
        restTemplate.postForEntity(base, req, Map.class);

        ResponseEntity<Void> enroll = restTemplate.postForEntity(base + "/C200/enroll", null, Void.class);
        assertEquals(HttpStatus.OK, enroll.getStatusCode());

        // second enroll should be bad request (full)
        ResponseEntity<Void> enroll2 = restTemplate.postForEntity(base + "/C200/enroll", null, Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, enroll2.getStatusCode());

        ResponseEntity<Void> unenroll = restTemplate.postForEntity(base + "/C200/unenroll", null, Void.class);
        assertEquals(HttpStatus.OK, unenroll.getStatusCode());

        // unenroll again should be bad request (no students)
        ResponseEntity<Void> unenroll2 = restTemplate.postForEntity(base + "/C200/unenroll", null, Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, unenroll2.getStatusCode());
    }

    @Test
    public void availableAndSearch() {
        String base = baseUrl("/api/courses");
        restTemplate.postForEntity(base, Map.of("id","C300","courseName","Math","credits",3,"instructor","Prof X","maxStudents",10), Map.class);
        restTemplate.postForEntity(base, Map.of("id","C301","courseName","Advanced Math","credits",5,"instructor","Prof X","maxStudents",1), Map.class);

        ResponseEntity<Map[]> avail = restTemplate.getForEntity(base + "/available", Map[].class);
        assertEquals(HttpStatus.OK, avail.getStatusCode());
        assertTrue(avail.getBody().length >= 1);

        ResponseEntity<Map[]> instr = restTemplate.getForEntity(base + "/instructor/Prof%20X", Map[].class);
        assertEquals(HttpStatus.OK, instr.getStatusCode());
        assertTrue(instr.getBody().length >= 1);

        ResponseEntity<Map[]> search = restTemplate.getForEntity(base + "/search?name=Math&minCredits=4", Map[].class);
        assertEquals(HttpStatus.OK, search.getStatusCode());
        assertTrue(search.getBody().length >= 1);
    }

    @Test
    public void courseNotFound() {
        String base = baseUrl("/api/courses/nonexistent");
        ResponseEntity<Map> resp = restTemplate.getForEntity(base, Map.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}
