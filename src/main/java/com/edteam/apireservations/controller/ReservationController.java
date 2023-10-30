package com.edteam.apireservations.controller;

import com.edteam.apireservations.dto.ReservationDTO;
import com.edteam.apireservations.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated // para indicar que se usaran decoradores de validación
@RestController() // Para indicar que resivirá peticiones HTTP
@RequestMapping("/reservation") // Indicar el nombre del path a donde se harán las request a este controller
public class ReservationController {

    private ReservationService service;

    @Autowired
    public ReservationController(
            ReservationService service
    ) {
        this.service = service;
    }

    @GetMapping // GET - devolver la lista de reservaciones
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        List<ReservationDTO> response = service.getReservations();
        return new ResponseEntity<>(response, HttpStatus.OK); // devolver la response con código 200
    }

    @GetMapping("/{id}") // GET /reservation/id - recibir argumentos en la URL
    public ResponseEntity<ReservationDTO> getReservationsById(@PathVariable Long id) { // Devolver la info de una reserva en particular
        ReservationDTO response = service.getReservationById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping // POST - crear una reservación     @Valid - Indicar que valide la reservation de acuerdo a los decoradores de los DTO's
    public ResponseEntity<ReservationDTO> save(@Valid @RequestBody ReservationDTO reservation) {

        ReservationDTO response = service.save(reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // código 201
    }

    @PutMapping("/{id}") // UPDATE - modificar una reservación
    public ResponseEntity<ReservationDTO> update(@Min(1) @PathVariable Long id, @Valid @RequestBody ReservationDTO reservation) {
        ReservationDTO response = service.update(id, reservation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // DELETE - eliminar una reservación     @Min(1) - Mínimo valor del Long id es 1
    public ResponseEntity<Void> delete(@Min(1) @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
