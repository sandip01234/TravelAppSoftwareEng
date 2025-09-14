package com.example.Travel.Application.Services;

/**
 * ███████╗ █████╗ ███╗   ██╗██████╗ ██╗██████╗
 * ██╔════╝██╔══██╗████╗  ██║██╔══██╗██║██╔══██╗
 * ███████╗███████║██╔██╗ ██║██║  ██║██║██████╔╝
 * ╚════██║██╔══██║██║╚██╗██║██║  ██║██║██╔═══╝
 * ███████║██║  ██║██║ ╚████║██████╔╝██║██║
 * ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝ ╚═╝╚═╝
 *
 * @author HP VICTUS on 9/14/2025
 */

import com.example.Travel.Application.Dto.DestinationDto;
import com.example.Travel.Application.Dto.DestinationResponse;
import com.example.Travel.Application.Entity.Category;
import com.example.Travel.Application.Entity.Destination;
import com.example.Travel.Application.Entity.User;
import com.example.Travel.Application.Repository.CategoryRepository;
import com.example.Travel.Application.Repository.DestinationRepository;
import com.example.Travel.Application.exception.ResourceNotFoundException;
import com.example.Travel.Application.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtil securityUtil;

    public DestinationService(DestinationRepository destinationRepository, CategoryRepository categoryRepository, SecurityUtil securityUtil) {
        this.destinationRepository = destinationRepository;
        this.categoryRepository = categoryRepository;
        this.securityUtil = securityUtil;
    }

    public DestinationResponse addDestination(DestinationDto dto) {
        User currentUser = securityUtil.getCurrentUser();
        Destination destination = mapToEntity(dto, new Destination());
        destination.setUser(currentUser);
        destination = destinationRepository.save(destination);
        return mapToResponse(destination);
    }

    public List<DestinationResponse> getUserDestinations() {
        User currentUser = securityUtil.getCurrentUser();
        return destinationRepository.findByUserId(currentUser.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DestinationResponse updateDestination(Long id, DestinationDto dto) {
        Destination destination = getDestinationByIdAndUser(id);
        mapToEntity(dto, destination);
        destination.setUpdatedAt(LocalDateTime.now());
        destination = destinationRepository.save(destination);
        return mapToResponse(destination);
    }

    public void deleteDestination(Long id) {
        Destination destination = getDestinationByIdAndUser(id);
        destinationRepository.delete(destination);
    }

    public DestinationResponse markVisited(Long id) {
        Destination destination = getDestinationByIdAndUser(id);
        destination.setVisited(true);
        destination.setUpdatedAt(LocalDateTime.now());
        destination = destinationRepository.save(destination);
        return mapToResponse(destination);
    }

    private Destination getDestinationByIdAndUser(Long id) {
        User currentUser = securityUtil.getCurrentUser();
        return destinationRepository.findById(id)
                .filter(d -> d.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
    }

    private Destination mapToEntity(DestinationDto dto, Destination entity) {
        entity.setName(dto.getName());
        entity.setVisited(dto.getVisited() != null ? dto.getVisited() : false);
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : "MEDIUM");
        entity.setNotes(dto.getNotes());
        if (dto.getCategoryIds() != null) {
            Set<Category> categories = dto.getCategoryIds().stream()
                    .map(categoryRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            entity.setCategories(categories);
        }
        return entity;
    }

    private DestinationResponse mapToResponse(Destination entity) {
        DestinationResponse response = new DestinationResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setVisited(entity.getVisited());
        response.setPriority(entity.getPriority());
        response.setEstinamtedCost(entity.getEstimated_cost());
        response.setNotes(entity.getNotes());
        response.setCategories(entity.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));
        return response;
    }
}
