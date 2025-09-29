package com.hirex.users.repository;

import com.hirex.config.lib.repository.EntityRepository;
import com.hirex.users.domain.User;

public interface UserRepository extends EntityRepository<User, Long> {
}
