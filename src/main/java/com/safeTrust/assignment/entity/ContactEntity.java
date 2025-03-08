package com.safeTrust.assignment.entity;

import com.safeTrust.assignment.constant.Constant;
import com.safeTrust.assignment.constant.Fields;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = Fields.CONTACT)
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String telephoneNumber;
    private String postalAddress;

    public String getFullName() {
        return Optional.ofNullable(lastName).filter(l -> !l.isBlank())
                .map(l -> Optional.ofNullable(firstName).filter(f -> !f.isBlank())
                        .map(f -> l + Constant.BLANK + f)
                        .orElse(l))
                .orElse(Optional.ofNullable(firstName).filter(f -> !f.isBlank()).orElse(Constant.EMPTY));
    }
}
