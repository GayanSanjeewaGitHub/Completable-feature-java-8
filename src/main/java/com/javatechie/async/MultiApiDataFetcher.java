package com.javatechie.async;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MultiApiDataFetcher {

    // Method to fetch data from multiple sources with allOf
    public static CompletableFuture<List<String>> fetchDataFromAllApis(List<CompletableFuture<String>> apiCalls) {
        return CompletableFuture.allOf(apiCalls.toArray(new CompletableFuture[0]))
                .thenApply(v -> apiCalls.stream()
                        .map(CompletableFuture::join) // Wait for each to complete
                        .collect(Collectors.toList())
                );
    }

    // Method to fetch data as soon as any one of the sources completes using anyOf
    public static CompletableFuture<String> fetchDataFromAnyApi(List<CompletableFuture<String>> apiCalls) {
        return CompletableFuture.anyOf(apiCalls.toArray(new CompletableFuture[0]))
                .thenApply(result -> (String) result); // Cast result to the expected type
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Simulate API calls to fetch product data from multiple sources
        CompletableFuture<String> reviews = CompletableFuture.supplyAsync(() -> "Product reviews data");
        CompletableFuture<String> ratings = CompletableFuture.supplyAsync(() -> "Product ratings data");
        CompletableFuture<String> pricing = CompletableFuture.supplyAsync(() -> "Product pricing data");

        List<CompletableFuture<String>> deliveryEstimates = Arrays.asList(reviews, ratings, pricing);

        // Fetch all data once all API calls complete
        CompletableFuture<List<String>> allData = MultiApiDataFetcher.fetchDataFromAllApis(deliveryEstimates);

        // Print consolidated data from all APIs
        allData.thenAccept(data -> {
            System.out.println("Consolidated product data:");
            data.forEach(System.out::println);
        }).join();
    }


    
}
