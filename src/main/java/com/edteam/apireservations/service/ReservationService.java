package com.edteam.apireservations.service;

import com.edteam.apireservations.connector.CatalogConnector;
import com.edteam.apireservations.connector.response.CityDTO;
import com.edteam.apireservations.dto.ReservationDTO;
import com.edteam.apireservations.dto.SegmentDTO;
import com.edteam.apireservations.enums.APIError;
import com.edteam.apireservations.exception.EdteamException;
import com.edteam.apireservations.model.Reservation;
import com.edteam.apireservations.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationService {
    private ReservationRepository repository;
    private ConversionService conversionService; // se fusiona con los Mappers

    private CatalogConnector connector;

    @Autowired
    public ReservationService(
            ReservationRepository repository,
            ConversionService conversionService,
            CatalogConnector connector
    ) {
        this.repository = repository;
        this.conversionService = conversionService;
        this.connector = connector;
    }

    public List<ReservationDTO> getReservations() {
        return conversionService.convert(repository.getReservations(), List.class); // convierte de Reservation a ReservationDTO
    }

    public ReservationDTO getReservationById(Long id) {
        Optional<Reservation> result = repository.getReservationById(id);
        if (result.isEmpty()) { // ? si el registro no existe se lanza una excepción
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        return conversionService.convert(result.get(), ReservationDTO.class);
    }

    public ReservationDTO save(ReservationDTO reservation) {
        if(Objects.nonNull(reservation.getId())) { // checar que no exista duplicidad
            throw new EdteamException(APIError.RESERVATION_WITH_SAME_ID);
        }
        checkCity(reservation);
        Reservation transformed = conversionService.convert(reservation, Reservation.class); // Conversión de ReservationDTO a Reservation
        // para persistir los datos en el repository
        Reservation result = repository.save(Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class); // Conversión de Reservation a ReservationDTO para devolver los datos
    }

    public ReservationDTO update(Long id, ReservationDTO reservation) {
        if(getReservationById(id) == null) {
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        checkCity(reservation);
        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        Reservation result = repository.update(id, Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public void delete(Long id) {
        if(getReservationById(id) == null) {
            throw new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }

        repository.delete(id);
    }

    private void checkCity(ReservationDTO reservationDTO) {
        for (SegmentDTO segmentDTO: reservationDTO.getItinerary().getSegment()) {
            CityDTO origin = connector.getCity(segmentDTO.getOrigin());
            CityDTO destination = connector.getCity(segmentDTO.getDestination());

            if(origin == null || destination == null) {
                throw new EdteamException(APIError.VALIDATION_ERROR);
            } else {
                System.out.println(origin.getName());
                System.out.println(destination.getName());
            }
        }
    }
}
