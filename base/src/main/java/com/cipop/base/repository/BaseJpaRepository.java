package com.cipop.base.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

@Transactional(
    readOnly = true,
    rollbackFor = Exception.class
)
public abstract class BaseJpaRepository<T, ID> implements JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    private Class<T> domainClass;
    private JpaEntityInformation<T, ?> entityInformation;
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    private PersistenceProvider provider;
    @Nullable
    private CrudMethodMetadata metadata;

    @Autowired
    public void setEm(EntityManager em) {
            this.em = em;
            this.provider = PersistenceProvider.fromEntityManager(em);
            this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);

    }

    public BaseJpaRepository(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    @Nullable
    protected CrudMethodMetadata getRepositoryMethodMetadata() {
        return this.metadata;
    }

    protected Class<T> getDomainClass() {
        return this.entityInformation.getJavaType();
    }

    private String getDeleteAllQueryString() {
        return QueryUtils.getQueryString("delete from %s x", this.entityInformation.getEntityName());
    }

    private String getCountQueryString() {
        String countQuery = String.format("select count(%s) from %s x", this.provider.getCountQueryPlaceholder(), "%s");
        return QueryUtils.getQueryString(countQuery, this.entityInformation.getEntityName());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        this.delete(this.findById(id).orElseThrow(() -> {
            return new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", this.entityInformation.getJavaType(), id), 1);
        }));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        this.em.remove(this.em.contains(entity) ? entity : this.em.merge(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        Iterator var2 = entities.iterator();

        while(var2.hasNext()) {
            T entity = (T) var2.next();
            this.delete(entity);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInBatch(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        if (entities.iterator().hasNext()) {
            QueryUtils.applyAndBind(QueryUtils.getQueryString("delete from %s x", this.entityInformation.getEntityName()), entities, this.em).executeUpdate();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll() {
        Iterator var1 = this.findAll().iterator();

        while(var1.hasNext()) {
            T element = (T) var1.next();
            this.delete(element);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAllInBatch() {
        this.em.createQuery(this.getDeleteAllQueryString()).executeUpdate();
    }

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        Class<T> domainType = this.getDomainClass();
        if (this.metadata == null) {
            return Optional.ofNullable(this.em.find(domainType, id));
        } else {
            LockModeType type = this.metadata.getLockModeType();
            return Optional.ofNullable(type == null ? this.em.find(domainType, id) : this.em.find(domainType, id, type));
        }
    }

    @Override
    public T getOne(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return this.em.getReference(this.getDomainClass(), id);
    }

    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        if (this.entityInformation.getIdAttribute() == null) {
            return this.findById(id).isPresent();
        } else {
            String placeholder = this.provider.getCountQueryPlaceholder();
            String entityName = this.entityInformation.getEntityName();
            Iterable<String> idAttributeNames = this.entityInformation.getIdAttributeNames();
            String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);
            TypedQuery<Long> query = this.em.createQuery(existsQuery, Long.class);
            if (!this.entityInformation.hasCompositeId()) {
                query.setParameter(idAttributeNames.iterator().next(), id);
                return query.getSingleResult() == 1L;
            } else {
                Iterator var7 = idAttributeNames.iterator();

                while(var7.hasNext()) {
                    String idAttributeName = (String)var7.next();
                    Object idAttributeValue = this.entityInformation.getCompositeIdAttributeValue(id, idAttributeName);
                    boolean complexIdParameterValueDiscovered = idAttributeValue != null && !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
                    if (complexIdParameterValueDiscovered) {
                        return this.findById(id).isPresent();
                    }

                    query.setParameter(idAttributeName, idAttributeValue);
                }

                return query.getSingleResult() == 1L;
            }
        }
    }

    @Override
    public List<T> findAll() {
        return this.getQuery((Specification)null, Sort.unsorted()).getResultList();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");
        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        } else if (!this.entityInformation.hasCompositeId()) {
            ByIdsSpecification<T> specification = new ByIdsSpecification(this.entityInformation);
            TypedQuery<T> query = this.getQuery(specification, Sort.unsorted());
            return query.setParameter(specification.parameter, ids).getResultList();
        } else {
            List<T> results = new ArrayList();
            Iterator var3 = ids.iterator();

            while(var3.hasNext()) {
                ID id = (ID) var3.next();
                this.findById(id).ifPresent(results::add);
            }

            return results;
        }
    }
    @Override
    public List<T> findAll(Sort sort) {
        return this.getQuery(null, sort).getResultList();
    }

//    public List<T> findAll(Group group) {
//        return this.getQuery((Specification<T>)null, group).getResultList();
//    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return (Page)(isUnpaged(pageable) ? new PageImpl(this.findAll()) : this.findAll((Specification)null, pageable));
    }

    @Override
    public Optional<T> findOne(@Nullable Specification<T> spec) {
        try {
            return Optional.of(this.getQuery(spec, Sort.unsorted()).setMaxResults(1).getSingleResult());
        } catch (NoResultException var3) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll(@Nullable Specification<T> spec) {
        return this.getQuery(spec, Sort.unsorted()).getResultList();
    }

    public List<T> findAll(@Nullable Specification<T> spec, String fetchColumn) {
        return this.getQuery(spec, Sort.unsorted(), fetchColumn).getResultList();
    }

    @Override
    public Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = this.getQuery(spec, pageable);
        return (Page)(isUnpaged(pageable) ? new PageImpl(query.getResultList()) : this.readPage(query, this.getDomainClass(), pageable, spec));
    }


    public Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable, String fetchColumn) {
        TypedQuery<T> query = this.getQuery(spec, pageable, fetchColumn);
        return (Page)(isUnpaged(pageable) ? new PageImpl(query.getResultList()) : this.readPage(query, this.getDomainClass(), pageable, spec));
    }

    @Override
    public List<T> findAll(@Nullable Specification<T> spec, Sort sort) {
        return this.getQuery(spec, sort).getResultList();
    }

    @Deprecated
    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return executeCountQuery(this.getCountQuery(new ExampleSpecification(example), example.getProbeType()));
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return !this.getQuery(new ExampleSpecification(example), example.getProbeType(), Sort.unsorted()).getResultList().isEmpty();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return this.getQuery(new ExampleSpecification(example), example.getProbeType(), Sort.unsorted()).getResultList();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return this.getQuery(new ExampleSpecification(example), example.getProbeType(), sort).getResultList();
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        ExampleSpecification<S> spec = new ExampleSpecification<>(example);
        Class<S> probeType = example.getProbeType();
        TypedQuery<S> query = this.getQuery(new ExampleSpecification<>(example), probeType, pageable);
        return (Page)(isUnpaged(pageable) ? new PageImpl<>(query.getResultList()) : this.readPage(query, probeType, pageable, spec));
    }

    @Override
    public long count() {
        return this.em.createQuery(this.getCountQueryString(), Long.class).getSingleResult();
    }

    @Override
    public long count(@Nullable Specification<T> spec) {
        return executeCountQuery(this.getCountQuery(spec, this.getDomainClass()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <S extends T> S save(S entity) {
        if (this.entityInformation.isNew(entity)) {
            this.em.persist(entity);
            return entity;
        } else {
            return this.em.merge(entity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <S extends T> S saveAndFlush(S entity) {
        S result = this.save(entity);
        this.flush();
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<S> result = new ArrayList<>();
        Iterator var3 = entities.iterator();

        while(var3.hasNext()) {
            S entity = (S) var3.next();
            result.add(this.save(entity));
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void flush() {
        this.em.flush();
    }

    protected <S extends T> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, @Nullable Specification<S> spec) {
        if (pageable.isPaged()) {
            query.setFirstResult((int)pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> executeCountQuery(this.getCountQuery(spec, domainClass)));
    }

    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Pageable pageable) {
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return this.getQuery(spec, this.getDomainClass(), sort);
    }

    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Pageable pageable, String fetchColumn) {
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return this.getQuery(spec, this.getDomainClass(), sort, fetchColumn);
    }

    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Pageable pageable) {
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return this.getQuery(spec, domainClass, sort);
    }

    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Sort sort) {
        return this.getQuery(spec, this.getDomainClass(), sort);
    }

    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Sort sort, String fetchColumn) {
        return this.getQuery(spec, this.getDomainClass(), sort, fetchColumn);
    }

    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Sort sort, String fetchColumn) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery<S> query = builder.createQuery(domainClass);
        Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
        root.fetch(fetchColumn, JoinType.LEFT);
        query.select(root);
        query.distinct(true);
        if (sort.isSorted()) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }

        return this.applyRepositoryMethodMetadata(this.em.createQuery(query));
    }

    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Sort sort) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery<S> query = builder.createQuery(domainClass);
        Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
        query.select(root);
        if (sort.isSorted()) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }
        return this.applyRepositoryMethodMetadata(this.em.createQuery(query));
    }

//    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Group group) {
//        CriteriaBuilder builder = this.em.getCriteriaBuilder();
//        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
//        Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
//        query.multiselect(root.get(group.getAttribute()));
//        if (group.isGrouped()) {
//            query.groupBy(root.get(group.getAttribute()));
//        }
//        return this.applyRepositoryMethodMetadata(this.em.createQuery(query));
//    }

//    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Group group) {
//        return this.getQuery(spec, this.getDomainClass(), group);
//    }


    protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec, Class<S> domainClass) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<S> root = this.applySpecificationToCriteria(spec, domainClass, query);
        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        query.orderBy(Collections.emptyList());
        return this.em.createQuery(query);
    }

    private <S, U extends T> Root<U> applySpecificationToCriteria(@Nullable Specification<U> spec, Class<U> domainClass, CriteriaQuery<S> query) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(query, "CriteriaQuery must not be null!");
        Root<U> root = query.from(domainClass);
        if (spec == null) {
            return root;
        } else {
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            Predicate predicate = spec.toPredicate(root, query, builder);
            if (predicate != null) {
                query.where(predicate);
            }

            return root;
        }
    }

    private <S> TypedQuery<S> applyRepositoryMethodMetadata(TypedQuery<S> query) {
        if (this.metadata == null) {
            return query;
        } else {
            LockModeType type = this.metadata.getLockModeType();
            return type == null ? query : query.setLockMode(type);
        }
    }


    private static Long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        Long total = 0L;

        Long element;
        for(Iterator var3 = totals.iterator(); var3.hasNext(); total = total + (element == null ? 0L : element)) {
            element = (Long)var3.next();
        }

        return total;
    }

    private static boolean isUnpaged(Pageable pageable) {
        return pageable.isUnpaged();
    }

    private static class ExampleSpecification<T> implements Specification<T> {
        private final Example<T> example;

        ExampleSpecification(Example<T> example) {
            Assert.notNull(example, "Example must not be null!");
            this.example = example;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, this.example);
        }
    }

    private static final class ByIdsSpecification<T> implements Specification<T> {
        private final JpaEntityInformation<T, ?> entityInformation;
        @Nullable
        ParameterExpression<Iterable> parameter;

        ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<?> path = root.get(this.entityInformation.getIdAttribute());
            this.parameter = cb.parameter(Iterable.class);
            return path.in(this.parameter);
        }
    }
}

