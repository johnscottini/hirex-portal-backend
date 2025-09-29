package com.hirex.config.lib.persistence;

import com.hirex.config.lib.repository.ProjectionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import java.io.Serializable;

public class CustomRepositoryFactory extends JpaRepositoryFactory {

    private final EntityManager entityManager;

    public CustomRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (ProjectionRepository.class.isAssignableFrom(
                metadata.getRepositoryInterface())) {

            JpaEntityInformation<?, Serializable> entityInformation =
                    getEntityInformation(metadata.getDomainType());

            Object queryableFragment = getTargetRepositoryViaReflection(
                    ProjectionRepositoryImpl.class, entityInformation, entityManager);

            fragments = fragments.append(RepositoryFragment.implemented(queryableFragment));
        }

        return fragments;
    }
}
