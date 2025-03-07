package com.safeTrust.assignment.service.impl;

import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.exception.ResourceNotFoundException;
import com.safeTrust.assignment.mapper.ContactMapper;
import com.safeTrust.assignment.repository.ContactRepository;
import com.safeTrust.assignment.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Override
    public Page<ContactDto> getContacts(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public ContactDto getOneContact(Long id) {
        return contactRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    public ContactDto createContact(ContactDto contactDto) {
        return mapper.toDto(contactRepository.save(mapper.toEntity(contactDto)));
    }

    @Override
    @Transactional
    public ContactDto updateContact(Long id, ContactDto contactDto) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        return mapper.toDto(contactRepository.save(mapper.toEntity(contactDto)));
    }

    @Override
    @Transactional
    public Map<String, String> deleteContact(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contactRepository.deleteById(id);
        log.info("Deleted contact with id: {}", id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully deleted contact");
        return response;
    }
}
