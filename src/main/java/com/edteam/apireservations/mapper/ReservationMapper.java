package com.edteam.apireservations.mapper;

import com.edteam.apireservations.dto.ReservationDTO;
import com.edteam.apireservations.model.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface ReservationMapper extends Converter<Reservation, ReservationDTO> {

    @Override
    ReservationDTO convert(Reservation source);

}
