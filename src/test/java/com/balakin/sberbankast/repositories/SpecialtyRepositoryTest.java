package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Specialty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class SpecialtyRepositoryIT {

    @Autowired
    SpecialtyRepository specialtyRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByDescription() {

        Optional<Specialty> specialtyOptional = specialtyRepository.findByDescription("Other");

        Assertions.assertEquals("Other",specialtyOptional.get().getDescription());
    }
}