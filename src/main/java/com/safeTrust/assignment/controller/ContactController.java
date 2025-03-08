package com.safeTrust.assignment.controller;

import com.safeTrust.assignment.constant.Constant;
import com.safeTrust.assignment.constant.Message;
import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = Constant.CONTACT_API, description = Message.API_FOR_MANAGING_CONTACTS)
@RestController
@RequestMapping("${base-path}/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @Operation(summary = Constant.GET_ALL_CONTACTS, description = Message.GET_ALL_CONTACTS)
    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(contactService.getContacts(pageable));
    }

    @Operation(summary = Constant.GET_A_CONTACT, description = Message.GET_A_CONTACT)
    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getOneContact(id));
    }

    @Operation(summary = Constant.CREATE_A_NEW_CONTACT, description = Message.CREATE_A_NEW_CONTACT)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ContactDto request) {
        return new ResponseEntity<>(contactService.createContact(request), HttpStatus.CREATED);
    }

    @Operation(summary = Constant.UPDATE_A_CONTACT, description = Message.UPDATE_A_CONTACT)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ContactDto request) {
        return ResponseEntity.ok(contactService.updateContact(id, request));
    }

    @Operation(summary = Constant.DELETE_A_CONTACT, description = Message.DELETE_A_CONTACT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }

    @Operation(summary = Constant.SEARCH_CONTACTS, description = Message.SEARCH_CONTACTS)
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String keyword, Pageable pageable) {
        return ResponseEntity.ok(contactService.searchContacts(keyword, pageable));
    }
}
