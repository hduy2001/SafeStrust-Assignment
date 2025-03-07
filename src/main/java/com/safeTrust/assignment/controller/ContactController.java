package com.safeTrust.assignment.controller;

import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${base-path}/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseEntity.ok(contactService.getContacts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getOneContact(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ContactDto request) {
        return ResponseEntity.ok(contactService.createContact(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ContactDto request) {
        return ResponseEntity.ok(contactService.updateContact(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }
}
