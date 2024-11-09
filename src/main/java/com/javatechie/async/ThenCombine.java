package com.javatechie.async;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.async.dto.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ThenCombine {


    // Mock method to fetch all employees asynchronously
    public static CompletableFuture<List<Employee>> fetchEmployees() {
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

    // Mock method to fetch average performance rating asynchronously
    public static CompletableFuture<Double> fetchAverageRating() {
        return CompletableFuture.supplyAsync(() -> 3.5);
    }

    // Method to use `thenCombine` to get employees with above-average ratings
    public static CompletableFuture<List<Employee>> getEmployeesAboveAverageRating() {
        return fetchEmployees().thenCombine(fetchAverageRating(), (employees, avgRating) ->
                employees.stream()
                        .filter(emp -> emp.getRating() > avgRating) // Only employees with rating above the average
                        .collect(Collectors.toList())
        );
    }

    public static void main(String[] args) {
        // Execute the combined task
        getEmployeesAboveAverageRating().thenAccept(employees -> {
            System.out.println("Employees with above-average ratings:");
            employees.forEach(emp -> System.out.println(emp.getFirstName() + " " + emp.getLastName() + " - Rating: " + emp.getRating()));
        }).join(); // Wait for the asynchronous tasks to complete
    }
}
