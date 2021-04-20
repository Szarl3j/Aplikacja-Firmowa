package com.aplikacja.Aplikacja.firmowa.repositories;

import com.aplikacja.Aplikacja.firmowa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    <S extends User> S save(S user);

    @Override
    void deleteById(Long id);

    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(Long id);

    Optional<User>findByFirstNameAndLastName(String firstName, String lastName);

    Optional <User>findByLogin(String login);

    Optional<User>findByLoginAndEmail(String login, String email);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

}
