package com.example.Travel.Application.Controller;

import com.example.Travel.Application.Dto.DestinationDto;
import com.example.Travel.Application.Dto.DestinationResponse;
import com.example.Travel.Application.Services.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@Tag(name = "Destinations", description = "API for managing travel destinations")
@SecurityRequirement(name = "bearerAuth") // Apply JWT requirement to all endpoints in this controller
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @PostMapping
    @Operation(summary = "Add a new destination", description = "Creates a new destination for the current user")
    public ResponseEntity<DestinationResponse> addDestination(@RequestBody DestinationDto dto) {
        return ResponseEntity.ok(destinationService.addDestination(dto));
    }

    @GetMapping
    @Operation(summary = "Get all destinations", description = "Retrieves all destinations for the current user")
    public ResponseEntity<List<DestinationResponse>> getDestinations() {
        return ResponseEntity.ok(destinationService.getUserDestinations());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a destination", description = "Updates an existing destination by ID")
    public ResponseEntity<DestinationResponse> updateDestination(@PathVariable Long id, @RequestBody DestinationDto dto) {
        return ResponseEntity.ok(destinationService.updateDestination(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a destination", description = "Deletes a destination by ID")
    public ResponseEntity<?> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.ok("Destination deleted");
    }

    @PutMapping("/{id}/visit")
    @Operation(summary = "Mark destination as visited", description = "Marks a destination as visited by ID")
    public ResponseEntity<DestinationResponse> markVisited(@PathVariable Long id) {
        return ResponseEntity.ok(destinationService.markVisited(id));
    }
}