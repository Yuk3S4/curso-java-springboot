package com.edteam.apireservations.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ReservationDTO {

    private Long id;

    @Valid // Para validar de acuerdo a los decoradores de PassengerDTO
    @NotEmpty(message = "You need at least one passenger")
    private List<PassengerDTO> passengers;

    @Valid
    private ItineraryDTO itinerary;

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public ItineraryDTO getItinerary() {
        return itinerary;
    }

    public void setItinerary(ItineraryDTO itinerary) {
        this.itinerary = itinerary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
