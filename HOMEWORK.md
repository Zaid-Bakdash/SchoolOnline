# Lab 6: Backend API - Homework Assignment

**Finalization status:** Not finalized — some controller tests fail (see test reports).

## Overview

This homework assignment reinforces REST API development concepts. You'll practice building complete REST controllers, implementing proper HTTP methods, handling requests and responses, and testing APIs with MockMvc.

## Learning Objectives

By completing this homework, you will:
- Create REST controllers with proper HTTP mappings
- Implement CRUD operations with appropriate HTTP methods
- Use DTOs for request/response handling
- Apply proper HTTP status codes
- Implement exception handling with @ControllerAdvice
- Write controller tests with @WebMvcTest and MockMvc
- Apply REST API best practices and conventions

## Prerequisites

- Completed Lab 6: Backend API
- Understanding of REST principles and HTTP methods
- Familiarity with Spring MVC annotations
- Knowledge of request/response handling
- Understanding of HTTP status codes

---

## Assignment 1: Student REST API

### Task
Create a complete REST API for student management with full CRUD operations.

### Requirements

1. **Create StudentDTO** in `src/main/java/com/bootcamp/onlineschool/dto/StudentDTO.java`:
   - Fields: `id`, `name`, `email`, `gpa`
   - Add validation annotations: `@NotNull`, `@NotBlank`, `@Email`, `@Min`, `@Max`
   - Include constructors, getters, setters

2. **Create StudentController** in `src/main/java/com/bootcamp/onlineschool/controller/StudentController.java`:
   - Annotate with `@RestController` and `@RequestMapping("/api/students")`
   - Use constructor injection for StudentService
   - Implement endpoints:
     - `POST /api/students` - Create student
       - Accept `@RequestBody @Valid StudentDTO`
       - Return `ResponseEntity<StudentDTO>` with status 201 Created
       - Set Location header with created resource URI
     - `GET /api/students` - Get all students
       - Return `ResponseEntity<List<StudentDTO>>` with status 200 OK
       - Support optional query parameter `name` for filtering
     - `GET /api/students/{id}` - Get student by ID
       - Use `@PathVariable String id`
       - Return `ResponseEntity<StudentDTO>` with status 200 OK
       - Return 404 Not Found if student doesn't exist
     - `PUT /api/students/{id}` - Update student
       - Accept `@PathVariable String id` and `@RequestBody @Valid StudentDTO`
       - Return `ResponseEntity<StudentDTO>` with status 200 OK
       - Return 404 Not Found if student doesn't exist
     - `DELETE /api/students/{id}` - Delete student
       - Use `@PathVariable String id`
       - Return `ResponseEntity<Void>` with status 204 No Content
       - Return 404 Not Found if student doesn't exist
     - `GET /api/students/high-achievers?gpa={threshold}` - Get high achievers
       - Use `@RequestParam(defaultValue = "3.5") Double gpa`
       - Return filtered list of students

3. **Update StudentService** to work with DTOs:
   - Add methods to convert between Student and StudentDTO
   - Ensure all service methods throw appropriate exceptions
   - Use proper exception types (StudentNotFoundException, etc.)

4. **Create test class** `StudentControllerTest.java`:
   - Use `@WebMvcTest(StudentController.class)`
   - Inject `MockMvc` with `@Autowired`
   - Mock StudentService with `@MockBean`
   - Write minimum 12 tests covering:
     - POST - successful creation
     - POST - validation errors (invalid email, GPA out of range)
     - GET all - with and without data
     - GET by ID - found and not found
     - PUT - successful update and not found
     - DELETE - successful deletion and not found
     - GET high achievers - with different thresholds
   - Use `MockMvc` methods: `perform()`, `andExpect()`, `andDo()`
   - Verify JSON responses with `jsonPath()`

### Expected Deliverables
- StudentDTO.java (with validation)
- StudentController.java (complete REST API)
- Updated StudentService.java
- StudentControllerTest.java (minimum 12 tests)

---

## Assignment 2: Course REST API with Advanced Features

### Task
Create a REST API for course management with enrollment operations and filtering.

### Requirements

1. **Create CourseDTO** in `src/main/java/com/bootcamp/onlineschool/dto/CourseDTO.java`:
   - Fields: `id`, `courseName`, `credits`, `instructor`, `maxStudents`, `enrolledStudents`
   - Add validation annotations
   - Include computed field: `availableSeats`

2. **Create CourseController** in `src/main/java/com/bootcamp/onlineschool/controller/CourseController.java`:
   - Annotate with `@RestController` and `@RequestMapping("/api/courses")`
   - Implement standard CRUD endpoints (POST, GET all, GET by ID, PUT, DELETE)
   - Add specialized endpoints:
     - `POST /api/courses/{id}/enroll` - Enroll a student
       - Return 200 OK if successful
       - Return 400 Bad Request if course is full
       - Return 404 Not Found if course doesn't exist
     - `POST /api/courses/{id}/unenroll` - Unenroll a student
       - Return 200 OK if successful
       - Return 400 Bad Request if no students enrolled
       - Return 404 Not Found if course doesn't exist
     - `GET /api/courses/available` - Get courses with available seats
       - Return only courses that are not full
     - `GET /api/courses/instructor/{name}` - Get courses by instructor
       - Use `@PathVariable String name`
       - Return filtered list
     - `GET /api/courses/search?name={name}&minCredits={credits}` - Search courses
       - Use `@RequestParam` for optional filters
       - Support multiple filter combinations

3. **Update CourseService**:
   - Add DTO conversion methods
   - Implement enrollment logic with validation
   - Throw appropriate exceptions for business rule violations

4. **Create test class** `CourseControllerTest.java`:
   - Use `@WebMvcTest(CourseController.class)`
   - Write minimum 15 tests covering:
     - All CRUD operations
     - Enrollment/unenrollment scenarios
     - Available courses filtering
     - Search with different parameter combinations
     - Error cases (course full, not found, etc.)

### Expected Deliverables
- CourseDTO.java (with validation and computed fields)
- CourseController.java (complete REST API with specialized endpoints)
- Updated CourseService.java
- CourseControllerTest.java (minimum 15 tests)

---

## Assignment 3: Global Exception Handling and Error Responses

### Task
Implement centralized exception handling for consistent error responses across all APIs.

### Requirements

1. **Create ErrorResponse** in `src/main/java/com/bootcamp/onlineschool/dto/ErrorResponse.java`:
   - Fields:
     - `timestamp` (LocalDateTime)
     - `status` (int)
     - `error` (String)
     - `message` (String)
     - `path` (String)
     - `errors` (List<String>) - for validation errors
   - Include constructors and getters

2. **Create custom exceptions**:
   - `ResourceNotFoundException` - for 404 errors
   - `BadRequestException` - for 400 errors
   - `ConflictException` - for 409 errors (duplicate resources)

3. **Create GlobalExceptionHandler** in `src/main/java/com/bootcamp/onlineschool/exception/GlobalExceptionHandler.java`:
   - Annotate with `@ControllerAdvice`
   - Handle exceptions:
     - `@ExceptionHandler(ResourceNotFoundException.class)` - Return 404
     - `@ExceptionHandler(BadRequestException.class)` - Return 400
     - `@ExceptionHandler(ConflictException.class)` - Return 409
     - `@ExceptionHandler(MethodArgumentNotValidException.class)` - Return 400 with validation errors
     - `@ExceptionHandler(HttpMessageNotReadableException.class)` - Return 400 for malformed JSON
     - `@ExceptionHandler(Exception.class)` - Return 500 for unexpected errors
   - Each handler should:
     - Create ErrorResponse with appropriate details
     - Extract request path from HttpServletRequest
     - Return ResponseEntity with proper status code
     - Log the exception

4. **Update controllers**:
   - Remove try-catch blocks
   - Let exceptions propagate to GlobalExceptionHandler
   - Throw appropriate custom exceptions

5. **Create test class** `GlobalExceptionHandlerTest.java`:
   - Use `@WebMvcTest` with controllers
   - Write minimum 10 tests covering:
     - ResourceNotFoundException handling
     - Validation error handling (multiple fields)
     - BadRequestException handling
     - Malformed JSON handling
     - Unexpected exception handling
     - Verify error response structure
     - Verify HTTP status codes

### Expected Deliverables
- ErrorResponse.java
- Custom exception classes (3 exceptions)
- GlobalExceptionHandler.java
- Updated controllers (removed try-catch)
- GlobalExceptionHandlerTest.java (minimum 10 tests)

---

## Assignment 4: API Integration Testing

### Task
Create integration tests that test the full request-response cycle.

### Requirements

1. **Create IntegrationTestBase** (optional base class):
   - Common setup for integration tests
   - Helper methods for creating test data
   - Utility methods for assertions

2. **Create StudentApiIntegrationTest**:
   - Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`
   - Inject `TestRestTemplate`
   - Write minimum 8 tests:
     - Create student and verify response
     - Get all students and verify list
     - Get student by ID
     - Update student and verify changes
     - Delete student and verify removal
     - Test validation errors
     - Test not found scenarios
     - Test filtering by name

3. **Create CourseApiIntegrationTest**:
   - Use `@SpringBootTest` with random port
   - Write minimum 10 tests:
     - All CRUD operations
     - Enrollment workflow (create course, enroll, verify count)
     - Unenrollment workflow
     - Get available courses
     - Search with filters
     - Test course full scenario
     - Test concurrent enrollments

4. **Create EndToEndWorkflowTest**:
   - Test complete workflows across multiple APIs
   - Write minimum 5 tests:
     - Create students and courses, enroll students
     - Update course details and verify student access
     - Delete course and verify student records
     - Test high achievers with enrolled courses
     - Test instructor course management

### Expected Deliverables
- StudentApiIntegrationTest.java (minimum 8 tests)
- CourseApiIntegrationTest.java (minimum 10 tests)
- EndToEndWorkflowTest.java (minimum 5 tests)

---

## Bonus Challenges (Optional)

### Challenge 1: HATEOAS Support
Add hypermedia links to responses:
- Add Spring HATEOAS dependency
- Extend DTOs with links
- Add self, collection, and related resource links
- Update tests to verify links

### Challenge 2: API Versioning
Implement API versioning:
- Create v1 and v2 controllers
- Use URI versioning (/api/v1/students, /api/v2/students)
- Implement different response formats for each version
- Write tests for both versions

### Challenge 3: Request/Response Logging
Implement logging interceptor:
- Create HandlerInterceptor for logging
- Log all incoming requests (method, URI, parameters)
- Log all outgoing responses (status, body)
- Add correlation ID for request tracking
- Write tests to verify logging

### Challenge 4: Rate Limiting
Implement simple rate limiting:
- Create annotation @RateLimit
- Implement interceptor to track request counts
- Return 429 Too Many Requests when limit exceeded
- Make limits configurable per endpoint
- Write tests for rate limiting

### Challenge 5: API Documentation
Add Swagger/OpenAPI documentation:
- Add SpringDoc OpenAPI dependency
- Annotate controllers with @Operation, @ApiResponse
- Document request/response models
- Add examples for DTOs
- Access Swagger UI at /swagger-ui.html

---

## Running Your Application and Tests

### Start the application:
```bash
mvn spring-boot:run
```

Application runs on `http://localhost:8080`

### Test endpoints with curl:

```bash
# Create student
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"id":"S001","name":"John Doe","email":"john@school.edu","gpa":3.8}'

# Get all students
curl http://localhost:8080/api/students

# Get student by ID
curl http://localhost:8080/api/students/S001

# Update student
curl -X PUT http://localhost:8080/api/students/S001 \
  -H "Content-Type: application/json" \
  -d '{"id":"S001","name":"John Doe","email":"john@school.edu","gpa":3.9}'

# Delete student
curl -X DELETE http://localhost:8080/api/students/S001

# Get high achievers
curl "http://localhost:8080/api/students/high-achievers?gpa=3.5"

# Create course
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"id":"CS101","courseName":"Intro to CS","credits":3,"instructor":"Dr. Smith","maxStudents":30}'

# Enroll in course
curl -X POST http://localhost:8080/api/courses/CS101/enroll

# Get available courses
curl http://localhost:8080/api/courses/available
```

### Run all tests:
```bash
mvn clean test
```

### Run specific test class:
```bash
mvn test -Dtest=StudentControllerTest
mvn test -Dtest=CourseApiIntegrationTest
```

### Run only unit tests:
```bash
mvn test -Dtest=*ControllerTest
```

### Run only integration tests:
```bash
mvn test -Dtest=*IntegrationTest
```

---

## Submission Checklist

Before submitting, ensure you have:

- [ ] All DTO classes with validation annotations
- [ ] All controller classes with @RestController
- [ ] All endpoints properly mapped with HTTP methods
- [ ] GlobalExceptionHandler with @ControllerAdvice
- [ ] ErrorResponse class for consistent error format
- [ ] Custom exception classes
- [ ] All test classes with proper annotations
- [ ] All tests passing (`mvn clean test` shows 0 failures)
- [ ] Minimum test counts met (12 + 15 + 10 + 8 + 10 + 5 = 60 tests)
- [ ] Application starts successfully
- [ ] All endpoints accessible via curl
- [ ] Proper HTTP status codes used
- [ ] Request/response bodies properly formatted as JSON
- [ ] Validation working correctly
- [ ] Exception handling working correctly

### Expected Test Results
```
[INFO] Tests run: 60+, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Code Quality Guidelines

### REST Controller Best Practices

1. **Controller Structure**
   ```java
   @RestController
   @RequestMapping("/api/students")
   public class StudentController {
       private final StudentService studentService;
       
       public StudentController(StudentService studentService) {
           this.studentService = studentService;
       }
       
       @PostMapping
       public ResponseEntity<StudentDTO> createStudent(
               @Valid @RequestBody StudentDTO dto) {
           StudentDTO created = studentService.createStudent(dto);
           URI location = ServletUriComponentsBuilder
               .fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(created.getId())
               .toUri();
           return ResponseEntity.created(location).body(created);
       }
       
       @GetMapping
       public ResponseEntity<List<StudentDTO>> getAllStudents(
               @RequestParam(required = false) String name) {
           List<StudentDTO> students = name != null 
               ? studentService.findByName(name)
               : studentService.getAllStudents();
           return ResponseEntity.ok(students);
       }
       
       @GetMapping("/{id}")
       public ResponseEntity<StudentDTO> getStudent(@PathVariable String id) {
           StudentDTO student = studentService.getStudentById(id);
           return ResponseEntity.ok(student);
       }
       
       @PutMapping("/{id}")
       public ResponseEntity<StudentDTO> updateStudent(
               @PathVariable String id,
               @Valid @RequestBody StudentDTO dto) {
           StudentDTO updated = studentService.updateStudent(id, dto);
           return ResponseEntity.ok(updated);
       }
       
       @DeleteMapping("/{id}")
       public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
           studentService.deleteStudent(id);
           return ResponseEntity.noContent().build();
       }
   }
   ```

2. **DTO with Validation**
   ```java
   public class StudentDTO {
       private String id;
       
       @NotBlank(message = "Name is required")
       @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
       private String name;
       
       @NotBlank(message = "Email is required")
       @Email(message = "Email must be valid")
       private String email;
       
       @NotNull(message = "GPA is required")
       @Min(value = 0, message = "GPA must be at least 0.0")
       @Max(value = 4, message = "GPA must be at most 4.0")
       private Double gpa;
       
       // Constructors, getters, setters
   }
   ```

3. **Exception Handling**
   ```java
   @ControllerAdvice
   public class GlobalExceptionHandler {
       
       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<ErrorResponse> handleResourceNotFound(
               ResourceNotFoundException ex, HttpServletRequest request) {
           ErrorResponse error = new ErrorResponse(
               LocalDateTime.now(),
               HttpStatus.NOT_FOUND.value(),
               "Not Found",
               ex.getMessage(),
               request.getRequestURI(),
               null
           );
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
       }
       
       @ExceptionHandler(MethodArgumentNotValidException.class)
       public ResponseEntity<ErrorResponse> handleValidationErrors(
               MethodArgumentNotValidException ex, HttpServletRequest request) {
           List<String> errors = ex.getBindingResult()
               .getFieldErrors()
               .stream()
               .map(error -> error.getField() + ": " + error.getDefaultMessage())
               .collect(Collectors.toList());
           
           ErrorResponse error = new ErrorResponse(
               LocalDateTime.now(),
               HttpStatus.BAD_REQUEST.value(),
               "Validation Failed",
               "Invalid request body",
               request.getRequestURI(),
               errors
           );
           return ResponseEntity.badRequest().body(error);
       }
   }
   ```

4. **Controller Testing with MockMvc**
   ```java
   @WebMvcTest(StudentController.class)
   public class StudentControllerTest {
       
       @Autowired
       private MockMvc mockMvc;
       
       @MockBean
       private StudentService studentService;
       
       @Autowired
       private ObjectMapper objectMapper;
       
       @Test
       public void testCreateStudent() throws Exception {
           StudentDTO dto = new StudentDTO("S001", "John", "john@school.edu", 3.8);
           when(studentService.createStudent(any())).thenReturn(dto);
           
           mockMvc.perform(post("/api/students")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(dto)))
                   .andExpect(status().isCreated())
                   .andExpect(header().exists("Location"))
                   .andExpect(jsonPath("$.name").value("John"))
                   .andExpect(jsonPath("$.email").value("john@school.edu"));
       }
       
       @Test
       public void testCreateStudent_ValidationError() throws Exception {
           StudentDTO dto = new StudentDTO("S001", "", "invalid-email", 5.0);
           
           mockMvc.perform(post("/api/students")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(dto)))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.errors").isArray());
       }
   }
   ```

5. **Integration Testing**
   ```java
   @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
   public class StudentApiIntegrationTest {
       
       @Autowired
       private TestRestTemplate restTemplate;
       
       @Test
       public void testCreateAndRetrieveStudent() {
           StudentDTO dto = new StudentDTO("S001", "John", "john@school.edu", 3.8);
           
           ResponseEntity<StudentDTO> createResponse = restTemplate.postForEntity(
               "/api/students", dto, StudentDTO.class);
           
           assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
           assertNotNull(createResponse.getBody());
           
           ResponseEntity<StudentDTO> getResponse = restTemplate.getForEntity(
               "/api/students/S001", StudentDTO.class);
           
           assertEquals(HttpStatus.OK, getResponse.getStatusCode());
           assertEquals("John", getResponse.getBody().getName());
       }
   }
   ```

### HTTP Status Codes
- `200 OK` - Successful GET, PUT
- `201 Created` - Successful POST (include Location header)
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation errors, malformed request
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Unexpected server error

### REST API Conventions
- Use plural nouns for resource names (/students, /courses)
- Use HTTP methods correctly (GET, POST, PUT, DELETE)
- Use path variables for resource IDs (/students/{id})
- Use query parameters for filtering (?name=John&gpa=3.5)
- Return appropriate status codes
- Include Location header for created resources
- Use consistent error response format

---

## Common Pitfalls to Avoid

- ❌ Forgetting `@RestController` annotation
- ❌ Not using `@RequestBody` for POST/PUT payloads
- ❌ Not using `@PathVariable` for URL parameters
- ❌ Returning wrong HTTP status codes
- ❌ Not validating request bodies with `@Valid`
- ❌ Not handling validation errors properly
- ❌ Exposing internal exceptions to clients
- ❌ Not using DTOs (exposing domain models directly)
- ❌ Not testing error scenarios
- ❌ Not using MockMvc for controller tests
- ❌ Testing service logic in controller tests
- ❌ Not clearing test data between integration tests

---

## Tips for Success

1. **Start with DTOs** - Define request/response contracts first
2. **Test as you build** - Write tests for each endpoint
3. **Use Postman or curl** - Manually test endpoints during development
4. **Follow REST conventions** - Use proper HTTP methods and status codes
5. **Keep controllers thin** - Business logic belongs in services
6. **Handle all error cases** - Test both success and failure paths
7. **Use MockMvc properly** - Mock service layer, test controller logic only
8. **Write integration tests** - Test full request/response cycle
9. **Validate inputs** - Use validation annotations
10. **Document your API** - Add comments explaining endpoints

---

## Resources

### Documentation
- [Spring Web MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [REST API Best Practices](https://restfulapi.net/)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [Bean Validation](https://beanvalidation.org/)

### Example Code
- StudentService.java - Service layer example
- CourseService.java - Service with business logic

### Key Concepts
- REST API Design Principles
- HTTP Methods (GET, POST, PUT, DELETE)
- HTTP Status Codes
- Request/Response Handling
- Data Transfer Objects (DTOs)
- Bean Validation
- Exception Handling with @ControllerAdvice
- Testing with MockMvc and TestRestTemplate

---

## Questions and Support

If you encounter issues:

1. **Check application starts** - Run `mvn spring-boot:run`
2. **Test with curl** - Verify endpoints are accessible
3. **Check logs** - Spring Boot provides detailed error messages
4. **Verify annotations** - Ensure @RestController, @RequestMapping are present
5. **Test validation** - Send invalid data to verify validation works
6. **Use MockMvc** - Proper way to test controllers
7. **Check JSON format** - Ensure request bodies are valid JSON
8. **Ask for help** - Reach out during office hours

---

**Remember: Well-designed REST APIs are the foundation of modern web applications. Master these concepts to build production-ready services!** 🚀
