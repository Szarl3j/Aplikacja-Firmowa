package com.aplikacja.Aplikacja.firmowa.Repositories;

import com.aplikacja.Aplikacja.firmowa.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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
    Optional<User>findByEmail(String email);

    Boolean existsByEmail(String email);

}
