package com.safeTrust.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safeTrust.assignment.constant.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    @JsonIgnore
    private Long id;

    @NotBlank(message = Message.NAME_MUST_NOT_BE_EMPTY)
    @Size(max = 150, message = Message.NAME_MUST_NOT_EXCEED_150_CHARACTERS)
    private String name;

    @Email(message = Message.INVALID_EMAIL_FORMAT)
    private String email;

    @Pattern(regexp = "^0\\d{9,10}$", message = Message.INVALID_PHONE_FORMAT)
    private String telephoneNumber;

    @Size(max = 255, message = Message.ADDRESS_MUST_NOT_EXCEED_255_CHARACTERS)
    private String postalAddress;
}
