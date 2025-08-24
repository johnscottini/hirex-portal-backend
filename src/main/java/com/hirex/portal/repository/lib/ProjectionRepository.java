package com.hirex.portal.repository.lib;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface ProjectionRepository<T, TID> extends Repository<T, TID> {

    <PROJ> PROJ findOne(FactoryExpression<PROJ> factoryExpression, Predicate predicate);

    <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression);

    <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Sort sort);

    <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, OrderSpecifier<?>... orders);

    <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate);

    <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, Sort sort);

    <PROJ> List<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, OrderSpecifier<?>... orders);

    <PROJ> Page<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, Pageable pageable);

    <PROJ> Page<PROJ> findAll(FactoryExpression<PROJ> factoryExpression, Predicate predicate, long offset, long size, Sort sort);
}

