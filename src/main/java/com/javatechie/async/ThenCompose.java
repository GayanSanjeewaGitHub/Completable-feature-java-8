package com.javatechie.async;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.async.dto.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
public class ThenCompose {

    // Async method to fetch employees from JSON file
    public static CompletableFuture<List<Employee>> fetchEmployeesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(new File("employees.json"), new TypeReference<List<Employee>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    // Another async method to filter employees based on a condition
    public static CompletableFuture<List<Employee>> filterEmployeesBySalary(double minSalary) {
        return fetchEmployeesAsync().thenCompose(employees -> CompletableFuture.supplyAsync(() ->
                employees.stream()
                        .filter(emp -> emp.getSalary() >= minSalary)
                        .collect(Collectors.toList())
        ));
    }

    public static void main(String[] args) {
        // Fetch and filter employees with a minimum salary of 50000
        filterEmployeesBySalary(50000).thenAccept(filteredEmployees -> {
            System.out.println("Filtered Employees:");
            filteredEmployees.forEach(employee ->
                    System.out.println(employee.getFirstName() + " - " + employee.getSalary()));
        }).join(); // join to wait for async tasks to complete
    }
}
