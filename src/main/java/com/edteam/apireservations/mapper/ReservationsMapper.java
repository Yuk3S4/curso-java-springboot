package com.edteam.apireservations.mapper;

import com.edteam.apireservations.dto.ReservationDTO;
import com.edteam.apireservations.model.Reservation;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

public interface ReservationsMapper extends Converter<List<Reservation>, List<ReservationDTO>> {

    @Override
    List<ReservationDTO> convert(List<Reservation> source);

}
