package com.safeTrust.assignment.service.impl;

import com.safeTrust.assignment.constant.Fields;
import com.safeTrust.assignment.constant.Message;
import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.entity.ContactEntity;
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
                .orElseThrow(() -> new ResourceNotFoundException(Message.CONTACT_NOT_FOUND_WITH_ID + id));
    }

    @Override
    @Transactional
    public ContactDto createContact(ContactDto contactDto) {
        return mapper.toDto(contactRepository.save(mapper.toEntity(contactDto)));
    }

    @Override
    @Transactional
    public ContactDto updateContact(Long id, ContactDto contactDto) {
        ContactEntity existContact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.CONTACT_NOT_FOUND_WITH_ID + id));
        mapper.updateEntity(existContact, contactDto);
        return mapper.toDto(contactRepository.save(existContact));
    }

    @Override
    @Transactional
    public Map<String, String> deleteContact(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.CONTACT_NOT_FOUND_WITH_ID + id));
        contactRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put(Fields.MESSAGE, Message.SUCCESSFULLY_DELETE_CONTACT_WITH_ID + id);
        return response;
    }

    @Override
    public Page<ContactDto> searchContacts(String keyword, Pageable pageable) {
        Page<ContactEntity> entityPage = contactRepository.searchContacts(keyword, pageable);
        return entityPage.map(mapper::toDto);
    }
}
