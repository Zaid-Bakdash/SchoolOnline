package com.bootcamp.onlineschool.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EndToEndWorkflowTest extends IntegrationTestBase {

    @Test
    public void createStudentsAndCoursesAndEnroll() {
        String sBase = baseUrl("/api/students");
        String cBase = baseUrl("/api/courses");

        restTemplate.postForEntity(sBase, Map.of("id","E1","name","Stu1","email","s1@school.edu","gpa",3.6), Map.class);
        restTemplate.postForEntity(cBase, Map.of("id","EC1","courseName","E-Course","credits",2,"instructor","Prof E","maxStudents",5), Map.class);

        ResponseEntity<Void> enroll = restTemplate.postForEntity(cBase + "/EC1/enroll", null, Void.class);
        assertEquals(HttpStatus.OK, enroll.getStatusCode());
    }

    @Test
    public void updateCourseAndVerify() {
        String cBase = baseUrl("/api/courses");
        restTemplate.postForEntity(cBase, Map.of("id","EU1","courseName","Old","credits",2,"instructor","I1","maxStudents",5), Map.class);

        Map<String,Object> updated = Map.of("id","EU1","courseName","New Name","credits",3,"instructor","I1","maxStudents",10);
        ResponseEntity<Map> r = restTemplate.exchange(cBase + "/EU1", HttpMethod.PUT, new HttpEntity<>(updated), Map.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals("New Name", r.getBody().get("courseName"));
    }

    @Test
    public void deleteCourseAndVerifyStudentsUnaffected() {
        String sBase = baseUrl("/api/students");
        String cBase = baseUrl("/api/courses");
        restTemplate.postForEntity(sBase, Map.of("id","ED1","name","DelStu","email","d1@school.edu","gpa",3.1), Map.class);
        restTemplate.postForEntity(cBase, Map.of("id","EDC","courseName","ToDelete","credits",1,"instructor","I","maxStudents",2), Map.class);
        restTemplate.postForEntity(cBase + "/EDC/enroll", null, Void.class);

        restTemplate.delete(cBase + "/EDC");

        ResponseEntity<Map> stu = restTemplate.getForEntity(sBase + "/ED1", Map.class);
        assertEquals(HttpStatus.OK, stu.getStatusCode());
    }

    @Test
    public void highAchieversWithCourses() {
        String sBase = baseUrl("/api/students");
        restTemplate.postForEntity(sBase, Map.of("id","EH1","name","HA","email","ha@school.edu","gpa",3.95), Map.class);
        ResponseEntity<Map[]> resp = restTemplate.getForEntity(sBase + "/high-achievers?gpa=3.5", Map[].class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test
    public void instructorCourseManagement() {
        String cBase = baseUrl("/api/courses");
        restTemplate.postForEntity(cBase, Map.of("id","I1","courseName","M1","credits",3,"instructor","Teach","maxStudents",3), Map.class);
        ResponseEntity<Map[]> byInstructor = restTemplate.getForEntity(cBase + "/instructor/Teach", Map[].class);
        assertEquals(HttpStatus.OK, byInstructor.getStatusCode());
        assertTrue(byInstructor.getBody().length >= 1);
    }

}
 
