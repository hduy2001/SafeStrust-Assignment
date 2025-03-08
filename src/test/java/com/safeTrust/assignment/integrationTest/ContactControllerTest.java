package com.safeTrust.assignment.integrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeTrust.assignment.dto.ContactDto;
import com.safeTrust.assignment.entity.ContactEntity;
import com.safeTrust.assignment.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long contactId;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        ContactEntity contact = new ContactEntity(null, "Duy", "Nguyen", "duy@yopmail.com", "0123456789", "HCM");
        contactId = contactRepository.save(contact).getId();
    }

    @Test
    void shouldGetAllContacts() throws Exception {
        mockMvc.perform(get("/api/v1/contact")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Nguyen Duy"));
    }

    @Test
    void shouldGetOneContact() throws Exception {
        mockMvc.perform(get("/api/v1/contact/" + contactId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nguyen Duy"))
                .andExpect(jsonPath("$.email").value("duy@yopmail.com"))
                .andExpect(jsonPath("$.telephoneNumber").value("0123456789"))
                .andExpect(jsonPath("$.postalAddress").value("HCM"));
    }


    @Test
    void shouldCreateNewContact() throws Exception {
        ContactDto newContact = new ContactDto(null, "Nguyen Duy", "Nguyen@example.com", "0123456789", "UK");

        mockMvc.perform(post("/api/v1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContact)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nguyen Duy"));
    }

    @Test
    void shouldUpdateContact() throws Exception {
        ContactDto updatedContact = new ContactDto(null, "duy updated", "duyUpdated@yopmail.com", "0987654321", "Canada");

        mockMvc.perform(put("/api/v1/contact/" + contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedContact)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("duy updated"))
                .andExpect(jsonPath("$.email").value("duyUpdated@yopmail.com"));
    }

    @Test
    void shouldDeleteContact() throws Exception {
        mockMvc.perform(delete("/api/v1/contact/" + contactId))
                .andExpect(status().isOk());

        assertTrue(contactRepository.findById(contactId).isEmpty());
    }

    @Test
    void shouldSearchContacts() throws Exception {
        mockMvc.perform(get("/api/v1/contact/search")
                        .param("keyword", "Nguyen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Nguyen Duy"));
    }
}