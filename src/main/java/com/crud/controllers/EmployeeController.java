,package com.crud.controllers;

import com.crud.ResourseNotFoundException;
import com.crud.model.Employee;
import com.crud.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById (@PathVariable(value="id") Integer employeeId)
        throws ResourseNotFoundException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourseNotFoundException("Employee not found for this id: "+employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value="id") Integer employeeId,
                                                   @RequestBody Employee employeeDetails) throws ResourseNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourseNotFoundException("Employee not found for this id: "+employeeId));
        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setDepartment(employeeDetails.getDepartment());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public Map<String,Boolean> deleteEmployee(@PathVariable(value="id") Integer employeeId)
        throws ResourseNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourseNotFoundException("Employee not found for this id: "+employeeId));
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return response;
    }
}
