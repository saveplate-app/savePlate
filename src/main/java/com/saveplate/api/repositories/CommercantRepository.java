package com.saveplate.api.repositories;

import com.saveplate.api.entities.Commercant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercantRepository extends JpaRepository<Commercant,Long> {

}
