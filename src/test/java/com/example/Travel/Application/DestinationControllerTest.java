package com.example.Travel.Application;

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

import com.example.Travel.Application.Controller.DestinationController;
import com.example.Travel.Application.Dto.DestinationDto;
import com.example.Travel.Application.Dto.DestinationResponse;
import com.example.Travel.Application.Services.DestinationService;
import com.example.Travel.Application.Security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DestinationController.class)
@AutoConfigureMockMvc(addFilters = false)
class DestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter; // mock filter

    @MockBean
    private AuthenticationManager authenticationManager; // mock auth manager

    @MockBean
    private DestinationService destinationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddDestination() throws Exception {
        DestinationDto dto = new DestinationDto();
        dto.setName("Paris");
        DestinationResponse response = new DestinationResponse();
        response.setId(1L);
        response.setName("Paris");

        when(destinationService.addDestination(any(DestinationDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Paris"));
    }

    @Test
    void testGetDestinations() throws Exception {
        DestinationResponse dest1 = new DestinationResponse();
        dest1.setId(1L);
        dest1.setName("Paris");

        DestinationResponse dest2 = new DestinationResponse();
        dest2.setId(2L);
        dest2.setName("London");

        when(destinationService.getUserDestinations()).thenReturn(List.of(dest1, dest2));

        mockMvc.perform(get("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Paris"))
                .andExpect(jsonPath("$[1].name").value("London"));
    }

    @Test
    void testUpdateDestination() throws Exception {
        DestinationDto dto = new DestinationDto();
        dto.setName("Rome");

        DestinationResponse response = new DestinationResponse();
        response.setId(1L);
        response.setName("Rome");

        when(destinationService.updateDestination(eq(1L), any(DestinationDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/destinations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rome"));
    }

    @Test
    void testDeleteDestination() throws Exception {
        doNothing().when(destinationService).deleteDestination(1L);

        mockMvc.perform(delete("/api/destinations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Destination deleted"));
    }

    @Test
    void testMarkVisited() throws Exception {
        DestinationResponse response = new DestinationResponse();
        response.setId(1L);
        response.setVisited(true);

        when(destinationService.markVisited(1L)).thenReturn(response);

        mockMvc.perform(put("/api/destinations/1/visit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.visited").value(true));
    }
}
