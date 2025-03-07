package com.safeTrust.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 150, message = "Tên không được dài quá 100 ký tự")
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String telephoneNumber;

    @Size(max = 255, message = "Địa chỉ không được dài quá 255 ký tự")
    private String postalAddress;
}
