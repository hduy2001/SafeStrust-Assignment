package com.safeTrust.assignment.service.impl;

import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.entity.ContactEntity;
import com.safeTrust.assignment.mapper.ContactMapper;
import com.safeTrust.assignment.repository.ContactRepository;
import com.safeTrust.assignment.service.ContactService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Nested
    class HappyCase {
        @Test
        void getContacts_ShouldReturnPagedContacts() {
            Pageable pageable = PageRequest.of(0, 2);
            List<ContactEntity> contactEntities = Arrays.asList(
                    new ContactEntity(1L, "John Doe", "john@example.com", "123456789", "USA"),
                    new ContactEntity(2L, "Jane Doe", "jane@example.com", "987654321", "UK")
            );
            Page<ContactEntity> contactPage = new PageImpl<>(contactEntities, pageable, contactEntities.size());

            when(contactRepository.findAll(pageable)).thenReturn(contactPage);
            when(contactMapper.toDto(any(ContactEntity.class)))
                    .thenAnswer(invocation -> {
                        ContactEntity entity = invocation.getArgument(0);
                        return new ContactDto(entity.getId(), entity.getName(), entity.getEmail(), entity.getTelephoneNumber(), entity.getPostalAddress());
                    });

            Page<ContactDto> result = contactService.getContacts(pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
            assertEquals("John Doe", result.getContent().get(0).getName());
            assertEquals("Jane Doe", result.getContent().get(1).getName());
            verify(contactRepository, times(1)).findAll(pageable);
            verify(contactMapper, times(2)).toDto(any(ContactEntity.class));
        }
    }

    @Nested
    class UnHappyCase {

    }
}