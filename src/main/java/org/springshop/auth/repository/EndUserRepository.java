package org.springshop.auth.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springshop.auth.model.EndUser;

import java.util.Optional;

public interface EndUserRepository extends PagingAndSortingRepository<EndUser, Long> {
    Optional<EndUser> findByEmail(String email);
}
