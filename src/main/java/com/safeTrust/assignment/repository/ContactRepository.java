package com.safeTrust.assignment.repository;

import com.safeTrust.assignment.constant.Fields;
import com.safeTrust.assignment.entity.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    @Query("SELECT c FROM ContactEntity c " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ContactEntity> searchContacts(@Param(Fields.KEYWORD) String keyword, Pageable pageable);
}
