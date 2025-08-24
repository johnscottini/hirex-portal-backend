package com.hirex.portal.config.lib.persistence;
import com.hirex.portal.repository.lib.ProjectionRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import java.util.Collections;
import java.util.List;

public class ProjectionRepositoryImpl<TI> extends QuerydslJpaPredicateExecutor<TI>
        implements ProjectionRepository<TI, Long> {

    private static final EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;

    private final Querydsl querydsl;

    public ProjectionRepositoryImpl(JpaEntityInformation<TI, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager, resolver, null);
        EntityPath<TI> path = resolver.createPath(entityInformation.getJavaType());
        PathBuilder<TI> builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public <PROJ> PROJ findOne(FactoryExpression<PROJ> factoryExpression, Predicate predicate) {
        return createQuery(predicate).select(factoryExpression).fetchFirst();
    }

    @Override
    public <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression) {
        return createQuery().select(factoryExpression).fetch();
    }

    @Override
    public <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Sort sort) {
        return querydsl.applySorting(sort, createQuery().select(factoryExpression)).fetch();
    }

    @Override
    public <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, OrderSpecifier<?>... orders) {
        return findAll(factoryExpression, new QSort(orders));
    }

    @Override
    public <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate) {
        return createQuery(predicate).select(factoryExpression).fetch();
    }

    @Override
    public <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, Sort sort) {
        return querydsl.applySorting(sort, createQuery(predicate).select(factoryExpression)).fetch();
    }

    @Override
    public <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, OrderSpecifier<?>... orders) {
        return findAll(factoryExpression, predicate, new QSort(orders));
    }

    @Override
    public <PROJ> Page<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, Pageable pageable) {
        JPQLQuery<PROJ> query = querydsl.applyPagination(pageable, createQuery(predicate).select(
                factoryExpression));

        long total = createQuery(predicate).fetchCount();
        List<PROJ> content = total > pageable.getOffset() ? query.fetch()
                : Collections.emptyList();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public <PROJ> Page<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, long offset, long size, Sort sort) {
        var query = createQuery(predicate).select(factoryExpression);
        query.offset(offset);
        query.limit(size);
        querydsl.applySorting(sort, query);

        long total = createQuery(predicate).fetchCount();
        List<PROJ> content = total > offset ? query.fetch() : Collections.emptyList();

        int page = (int)(offset/size);
        if(offset % size > 0) {
            page++;
        }
        return new PageImpl<>(content, PageRequest.of(page, (int)size, sort), total);
    }
}
