package com.edteam.apireservations.mapper;

import com.edteam.apireservations.dto.ReservationDTO;
import com.edteam.apireservations.model.Reservation;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface ReservationDTOMapper extends Converter<ReservationDTO, Reservation> {

    @Override
    Reservation convert(ReservationDTO source);

}
