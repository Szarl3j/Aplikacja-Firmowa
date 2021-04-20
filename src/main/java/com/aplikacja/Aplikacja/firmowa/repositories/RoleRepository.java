package com.aplikacja.Aplikacja.firmowa.repositories;

import com.aplikacja.Aplikacja.firmowa.model.Role;
import com.aplikacja.Aplikacja.firmowa.model.ERoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERoles name);
}
