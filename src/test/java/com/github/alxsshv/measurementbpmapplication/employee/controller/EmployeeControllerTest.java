package com.github.alxsshv.measurementbpmapplication.employee.controller;

import com.github.alxsshv.measurementbpmapplication.MeasurementBpmApplication;
import com.github.alxsshv.measurementbpmapplication.employee.entity.Employee;
import com.github.alxsshv.measurementbpmapplication.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = MeasurementBpmApplication.class)
public class EmployeeControllerTest {

   private final TestRestTemplate template = new TestRestTemplate();

   @LocalServerPort
   private int port;

   @Autowired
   private EmployeeRepository storage;

   @TempDir
   private static File tempDir;

   @DynamicPropertySource
   public static void configureProperties(DynamicPropertyRegistry registry){
      String employeeStoragePath = tempDir.toString();
      registry.add("paths.employee-storage-path", () -> employeeStoragePath);
   }

   @BeforeEach
   void fillingStorage() {
      Employee employee = new Employee();
      employee.setId("1");
      employee.setName("Иван");
      employee.setPatronymic("Иванович");
      employee.setSurname("Иванов");
      employee.setSnils("11111111111");
      storage.save(employee);
   }

   @AfterEach
   void clearStorage() {
      storage.deleteAll();
   }

   @Test
   @DisplayName("Test getEmployeeList if list is not empty")
   void testGetEmployeeListIfListNotEmpty(){
      ResponseEntity<Employee[]> response = template.getRestTemplate()
              .getForEntity("http://localhost:"+ port +"/employee", Employee[].class);
      Assertions.assertNotNull(response.getBody());
      Assertions.assertEquals(1,response.getBody().length);
   }

   @Test
   @DisplayName("Test getEmployeeList if list is empty")
   void testGetEmployeeListIfListEmpty() {
      storage.deleteAll();
      ResponseEntity<Employee[]> response = template.getRestTemplate()
              .getForEntity("http://localhost:"+ port +"/employee", Employee[].class);
      Assertions.assertNotNull(response.getBody());
      Assertions.assertTrue(Arrays.stream(response.getBody()).toList().isEmpty());
   }

   @Test
   @DisplayName("Test getEmployee")
   void testGetEmployee(){
      ResponseEntity<Employee> response = template.getRestTemplate()
              .getForEntity("http://localhost:"+ port +"/employee/1", Employee.class);
      Assertions.assertNotNull(response.getBody());
      Assertions.assertEquals("Иван",response.getBody().getName());
   }

   @Test
   @DisplayName("Test addEmployee success")
   void testAddEmployeeSuccess(){
      Employee employee = new Employee();
      employee.setId("2");
      employee.setName("Петр");
      employee.setPatronymic("Петрович");
      employee.setSurname("Петров");
      employee.setSnils("77777777777");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:"+ port +"/employee",employee, String.class);
      Assertions.assertEquals(201,response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test addEmployee if employee already exist")
   void testAddEmployeeIfEmployeeAlreadyExist(){
      Employee employee = new Employee();
      employee.setId("1");
      employee.setName("Иван");
      employee.setPatronymic("Иванов");
      employee.setSurname("Иванов");
      employee.setSnils("11111111111");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:"+ port + "/employee",employee, String.class);
      Assertions.assertEquals(400,response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test addEmployee if employee surname is null")
   void testAddEmployeeIfSurnameIsNull(){
      Employee employee = new Employee();
      employee.setId("2");
      employee.setName("Петр");
      employee.setPatronymic("Петрович");
      employee.setSnils("77777777777");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:"+ port + "/employee", employee, String.class);
      Assertions.assertEquals(400,response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test addEmployee if employee name is null")
   void testAddEmployeeIfNameIsNull(){
      Employee employee = new Employee();
      employee.setId("2");
      employee.setSurname("Петров");
      employee.setPatronymic("Петрович");
      employee.setSnils("77777777777");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:" + port + "/employee", employee, String.class);
      Assertions.assertEquals(400, response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test addEmployee if employee patronymic is null")
   void testAddEmployeeIfPatronymicIsNull(){
      Employee employee = new Employee();
      employee.setId("2");
      employee.setSurname("Петров");
      employee.setName("Петр");
      employee.setSnils("77777777777");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:" + port + "/employee",employee, String.class);
      Assertions.assertEquals(400,response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test addEmployee if employee snils is not correct")
   void testAddEmployeeIfSnilsIsNotCorrect(){
      Employee employee = new Employee();
      employee.setId("3");
      employee.setName("Петр");
      employee.setPatronymic("Петрович");
      employee.setSurname("Петров");
      employee.setSnils("3333333333");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:" + port + "/employee", employee, String.class);
      Assertions.assertEquals(400, response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test addEmployee if employee snils is null")
   void testAddEmployeeIfSnilsNull(){
      Employee employee = new Employee();
      employee.setId("3");
      employee.setName("Петр");
      employee.setPatronymic("Петрович");
      employee.setSurname("Петров");
      ResponseEntity<String> response = template.getRestTemplate()
              .postForEntity("http://localhost:" + port + "/employee", employee, String.class);
      Assertions.assertEquals(400, response.getStatusCode().value());
   }

   @Test
   @DisplayName("Test editEmployee success")
   void testEditEmployeeSuccess()  {
      String expectedName = "Павел";
      Employee employee = new Employee();
      employee.setId("1");
      employee.setName(expectedName);
      employee.setPatronymic("Иванович");
      employee.setSurname("Иванов");
      employee.setSnils("11111111111");
      template.getRestTemplate()
              .put("http://localhost:" + port + "/employee/1", employee);
      Employee actualEmployee = storage.findById("1").orElseThrow();
      Assertions.assertEquals(expectedName, actualEmployee.getName());
   }

   @Test
   @DisplayName("Test editEmployee if employee not found")
   void testEditEmployeeIfEmployeeNotFound() {
      String employeeId = "398";
      Employee employee = new Employee();
      employee.setId(employeeId);
      employee.setName("Павел");
      employee.setPatronymic("Иванович");
      employee.setSurname("Павлов");
      employee.setSnils("77777777777");
      template.getRestTemplate().put("http://localhost:" + port + "/employee/" + employeeId, employee);
      Optional<Employee> employeeOpt = storage.findById(employeeId);
      Assertions.assertTrue(employeeOpt.isEmpty());
   }

   @Test
   @DisplayName("Test editEmployee if employee id is null")
   void testEditEmployeeIfEmployeeIdIsNull()  {
      String urlId = "398";
      Employee employee = new Employee();
      employee.setName("Павел");
      employee.setPatronymic("Иванович");
      employee.setSurname("Павлов");
      employee.setSnils("77777777777");
      template.getRestTemplate().put("http://localhost:" + port + "/employee/" + urlId, employee);
      Optional<Employee> employeeOpt = storage.findById(urlId);
      Assertions.assertTrue(employeeOpt.isEmpty());
   }

   @Test
   @DisplayName("Test editEmployee if employee name is null")
   void testEditEmployeeIfEmployeeNameNull()  {
      String employeeId = "1";
      String updatedSurname = "Павлов";
      Employee employee = new Employee();
      employee.setId(employeeId);
      employee.setPatronymic("Иванович");
      employee.setSurname(updatedSurname);
      employee.setSnils("11111111111");
      template.getRestTemplate().put("http://localhost:" + port + "/employee/" + employeeId, employee);
      Employee actualEmployee = storage.findById(employeeId).orElseThrow();
      Assertions.assertEquals("Иванов",actualEmployee.getSurname());
   }

   @Test
   @DisplayName("Test deleteEmployee success")
   void testDeleteEmployeeSuccess() {
      int firstSize = storage.findAll().size();
      template.getRestTemplate().delete("http://localhost:" + port + "/employee/1");
      int secondSize = storage.findAll().size();
      Assertions.assertNotEquals(firstSize,secondSize);
   }


}
