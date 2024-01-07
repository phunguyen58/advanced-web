package com.ptudw.web.repository;

import com.ptudw.web.domain.Authority;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Authority findOneByName(String name);
}
