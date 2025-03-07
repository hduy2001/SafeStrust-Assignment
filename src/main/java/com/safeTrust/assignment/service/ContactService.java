package com.safeTrust.assignment.service;

import com.safeTrust.assignment.dto.ContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ContactService {
    Page<ContactDto> getContacts(Pageable pageable);
    ContactDto getOneContact(Long id);
    ContactDto createContact(ContactDto contactDto);
    ContactDto updateContact(Long id, ContactDto contactDto);
    Map<String, String> deleteContact(Long id);
}
