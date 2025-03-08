package com.safeTrust.assignment.mapper;

import com.safeTrust.assignment.constant.Constant;
import com.safeTrust.assignment.constant.Fields;
import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.entity.ContactEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactMapper {
    @Mapping(target = Fields.LAST_NAME, expression = "java(splitFullName(contactDto.getName())[0])")
    @Mapping(target = Fields.FIRST_NAME, expression = "java(splitFullName(contactDto.getName())[1])")
    ContactEntity toEntity(ContactDto contactDto);

    @Mapping(target = Fields.NAME, expression = "java(contactEntity.getFullName())")
    ContactDto toDto(ContactEntity contactEntity);

    @Mapping(target = Fields.ID, ignore = true)
    @Mapping(target = Fields.LAST_NAME, expression = "java(splitFullName(contactDto.getName())[0])")
    @Mapping(target = Fields.FIRST_NAME, expression = "java(splitFullName(contactDto.getName())[1])")
    void updateEntity(@MappingTarget ContactEntity entity, ContactDto contactDto);


    default String[] splitFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return new String[]{Constant.EMPTY, Constant.EMPTY};
        }
        String[] parts = fullName.split(Constant.BLANK, 2);
        return parts.length == 2 ? parts : new String[]{parts[0], Constant.EMPTY};
    }
}
