package com.safeTrust.assignment.mapper;

import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.entity.ContactEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactMapper {
    ContactEntity toEntity(ContactDto contactDto);

    ContactDto toDto(ContactEntity contactEntity);
}
