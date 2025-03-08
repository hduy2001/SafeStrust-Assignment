package com.safeTrust.assignment.unitTest;

import com.safeTrust.assignment.constant.Fields;
import com.safeTrust.assignment.constant.Message;
import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.entity.ContactEntity;
import com.safeTrust.assignment.exception.ResourceNotFoundException;
import com.safeTrust.assignment.mapper.ContactMapper;
import com.safeTrust.assignment.repository.ContactRepository;
import com.safeTrust.assignment.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    private ContactEntity contactEntity;
    private ContactDto contactDto;

    @BeforeEach
    void setUp() {
        contactEntity = new ContactEntity(1L, "Duy", "Nguyen", "duy@yopmail.com", "123456789", "USA");
        contactDto = new ContactDto(1L, "Nguyen Duy", "duy@yopmail.com", "123456789", "USA");
    }

    @Nested
    class HappyCase {
        @Test
        void getContacts_ShouldReturnPagedContacts() {
            Pageable pageable = PageRequest.of(0, 2);
            List<ContactEntity> contactEntities = Arrays.asList(
                    new ContactEntity(1L, "Duy", "Nguyen", "duy@yopmail.com", "0123456789", "USA"),
                    new ContactEntity(2L, "Duy2", "Nguyen", "duy2@yopmail.com", "0123456780", "UK")
            );
            Page<ContactEntity> contactPage = new PageImpl<>(contactEntities, pageable, contactEntities.size());

            when(contactRepository.findAll(pageable)).thenReturn(contactPage);
            when(contactMapper.toDto(any(ContactEntity.class)))
                    .thenAnswer(invocation -> {
                        ContactEntity entity = invocation.getArgument(0);
                        return new ContactDto(entity.getId(), entity.getFullName(), entity.getEmail(), entity.getTelephoneNumber(), entity.getPostalAddress());
                    });

            Page<ContactDto> result = contactService.getContacts(pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
            assertEquals("Nguyen Duy", result.getContent().get(0).getName());
            assertEquals("Nguyen Duy2", result.getContent().get(1).getName());
            verify(contactRepository, times(1)).findAll(pageable);
            verify(contactMapper, times(2)).toDto(any(ContactEntity.class));
        }

        @Test
        void shouldReturnContact_whenContactExists() {
            when(contactRepository.findById(1L)).thenReturn(Optional.of(contactEntity));
            when(contactMapper.toDto(contactEntity)).thenReturn(contactDto);

            ContactDto result = contactService.getOneContact(1L);

            assertNotNull(result);
            assertEquals("Nguyen Duy", result.getName());
        }

        @Test
        void shouldCreateNewContact() {
            when(contactMapper.toEntity(contactDto)).thenReturn(contactEntity);
            when(contactRepository.save(contactEntity)).thenReturn(contactEntity);
            when(contactMapper.toDto(contactEntity)).thenReturn(contactDto);

            ContactDto result = contactService.createContact(contactDto);

            assertNotNull(result);
            assertEquals("Nguyen Duy", result.getName());
        }

        @Test
        void shouldUpdateContact_whenExists() {
            ContactDto updatedDto = new ContactDto(1L, "Duy Updated", "duyUpdated@yopmail.com", "0123456789", "UK");
            ContactEntity updatedEntity = new ContactEntity(1L, "Duy", "Updated", "duyUpdated@yopmail.com", "0123456789", "UK");

            when(contactRepository.findById(1L)).thenReturn(Optional.of(contactEntity));
            doNothing().when(contactMapper).updateEntity(contactEntity, updatedDto);
            when(contactRepository.save(contactEntity)).thenReturn(updatedEntity);
            when(contactMapper.toDto(updatedEntity)).thenReturn(updatedDto);

            ContactDto result = contactService.updateContact(1L, updatedDto);

            assertNotNull(result);
            assertEquals("Duy Updated", result.getName());
        }

        @Test
        void shouldDeleteContact_whenExists() {
            when(contactRepository.findById(1L)).thenReturn(Optional.of(contactEntity));

            Map<String, String> expectedResponse = new HashMap<>();
            expectedResponse.put(Fields.MESSAGE, Message.SUCCESSFULLY_DELETE_CONTACT_WITH_ID + 1);

            Map<String, String> result = contactService.deleteContact(1L);

            assertEquals(expectedResponse, result);
            verify(contactRepository, times(1)).deleteById(1L);
        }

        @Test
        void shouldReturnPagedContacts_whenSearching() {
            List<ContactEntity> entityList = Collections.singletonList(contactEntity);
            Page<ContactEntity> entityPage = new PageImpl<>(entityList);
            when(contactRepository.searchContacts(anyString(), any(Pageable.class))).thenReturn(entityPage);
            when(contactMapper.toDto(contactEntity)).thenReturn(contactDto);

            Page<ContactDto> result = contactService.searchContacts("Duy", Pageable.unpaged());

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("Nguyen Duy", result.getContent().get(0).getName());
        }
    }

    @Nested
    class UnHappyCase {
        @Test
        void shouldThrowException_whenContactNotFound() {
            when(contactRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> contactService.getOneContact(99L));
        }

        @Test
        void shouldThrowException_whenUpdatingNonExistentContact() {
            when(contactRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> contactService.updateContact(99L, contactDto));
        }

        @Test
        void shouldThrowException_whenDeletingNonExistentContact() {
            when(contactRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> contactService.deleteContact(99L));
            verify(contactRepository, never()).deleteById(anyLong());
        }
    }
}