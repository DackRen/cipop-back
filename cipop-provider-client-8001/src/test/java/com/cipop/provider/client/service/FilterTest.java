//package com.cipop.providerclient.service;
//
//import com.ftl.oms.domain.Batch;
//import com.ftl.oms.domain.enums.Status;
//import com.ftl.oms.repository.BatchRepository;
//import com.ftl.oms.specification.RSQLSpecification;
//import com.ftl.oms.specification.filter.ComparisonFilter;
//import com.ftl.oms.specification.filter.Filter;
//import com.ftl.oms.specification.filter.FilterParser;
//import com.ftl.oms.specification.filter.LogicalFilter;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.client.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.client.context.jdbc.Sql;
//import org.springframework.client.context.junit.jupiter.SpringExtension;
//
//import javax.persistence.EntityManager;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import static com.ftl.oms.specification.filter.FilterConstant.OR;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@ComponentScan("com.ftl.oms.service")
//@Sql("/sql/filter.sql")
//class FilterTest {
//    @Autowired
//    private EntityManager entityManager;
//    @Autowired
//    private BatchRepository batchRepository;
//
//    // localhost:9000/api/admin/batches/array?filter[status]=CREATED&filter[and][id][eq]=000-00000003&filter[and][or][id][eq]=000-00000000&filter[and][or][id][ne]=000-00000001
//
//    @Test
//    void shouldTriggerResolverWhenHTTPRequest() throws Exception {
////        this.mvc.perform(MockMvcRequestBuilders.get("/api/admin/batches/array?filter[packageQuantity][gt]=4")
////            .contentType(MediaType.APPLICATION_JSON))
////            .andExpect(status().isOk())
////            .andExpect(content().contentType("application/json;charset=UTF-8"));
////            .andExpect(jsonPath("$.country").value("FR"));
//    }
//
//    /*
//    filter{
//        id: {eq: 4},
//        or: {
//            packageQuantity: {
//                gt: 4,
//                lt: 5
//            }
//        }
//        status: {eq: CREATED}
//    }
//     */
//    @Test
//    void shouldParseParamInUrlToFilter() throws Exception {
//        LinkedHashMap<String, String[]> params = new LinkedHashMap<>();
//        params.put("filter[id][eq]", new String[]{"4"});
//        params.put("filter[packageQuantity][gt]", new String[]{"4"});
//        params.put("filter[packageQuantity][lt]", new String[]{"5"});
//        params.put("filter[status][eq]", new String[]{"CREATED"});
//        params.put("filter[id][in]", new String[]{"4", "5"});
//
//        LogicalFilter filter = FilterParser.parse(params);
//
//        assertEquals("id", ((ComparisonFilter) filter.getFilters().get(0)).getField());
//        assertEquals("gt", ((ComparisonFilter) filter.getFilters().get(1)).getOperator());
//        assertEquals("5", ((ComparisonFilter) filter.getFilters().get(2)).getValue());
//
//        assertEquals("id", ((ComparisonFilter) filter.getFilters().get(4)).getField());
//        assertEquals("in", ((ComparisonFilter) filter.getFilters().get(4)).getOperator());
//        assertEquals("5", ((String[])(((ComparisonFilter) filter.getFilters().get(4)).getValue()))[1]);
//
//        assertEquals(5, filter.getFilters().size());
//
//    }
//
//    @Test
//    void shouldConvertFilterToPredicate() throws Exception {
//        RSQLSpecification<Batch> specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "gt", 4));
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Batch> cq = cb.createQuery(Batch.class);
//        Root<Batch> root = cq.from(Batch.class);
//
//        Predicate packageQuantityPredicate = specification.toPredicate(root, cq, cb);
//
//        assertNotNull(packageQuantityPredicate);
//    }
//
//    // TODO separate this client
//    @Test
//    void shouldFindAllByFilter(){
//        RSQLSpecification<Batch> specification;
//        List<Batch> batches;
//
//        // Data type
//        specification = new RSQLSpecification<>(new ComparisonFilter("client", "eq", "YUN"));
//        List<Batch> batchesByString = batchRepository.findAll(specification);
//        assertEquals( "YUN", batchesByString.get(0).getClient());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "eq", "1"));
//        List<Batch> batchesByInt = batchRepository.findAll(specification);
//        assertEquals( 1, batchesByInt.get(0).getPackageQuantity().intValue());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("status", "eq", "CREATED"));
//        List<Batch> batchesByEnum = batchRepository.findAll(specification);
//        assertEquals(Status.CREATED, batchesByEnum.get(0).getStatus());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("etd", "eq", "2017-02-01T17:20:00.000Z"));
//        List<Batch> batchesByDate = batchRepository.findAll(specification);
//        assertEquals("2017-02-01T17:20", batchesByDate.get(0).getEtd().toString());
//
//        // Operator
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "ne", "5"));
//        batches = batchRepository.findAll(specification);
//        assertTrue(batches.get(0).getPackageQuantity() != 4);
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "isnull", ""));
//        batches = batchRepository.findAll(specification);
//        assertEquals("000-00000012", batches.get(0).getId());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "isnotnull", ""));
//        batches = batchRepository.findAll(specification);
//        assertTrue(!batches.isEmpty());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "isempty", ""));
//        batches = batchRepository.findAll(specification);
//        assertEquals("000-00000011", batches.get(0).getId());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "isnotempty", ""));
//        batches = batchRepository.findAll(specification);
//        assertTrue(!batches.isEmpty());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "contains", "Contain"));
//        batches = batchRepository.findAll(specification);
//        assertEquals("000-00000010", batches.get(0).getId());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "doesnotcontain", "5"));
//        batches = batchRepository.findAll(specification);
//        assertEquals("000-00000009", batches.get(0).getId());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "startswith", "F"));
//        batches = batchRepository.findAll(specification);
//        assertEquals("000-00000009", batches.get(0).getId());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "endswith", "B"));
//        batches = batchRepository.findAll(specification);
//        assertEquals("000-00000009", batches.get(0).getId());
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "gt", "4"));
//        batches = batchRepository.findAll(specification);
//        assertTrue(batches.get(0).getPackageQuantity() > 4);
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "lt", "4"));
//        batches = batchRepository.findAll(specification);
//        assertTrue(batches.get(0).getPackageQuantity() < 4);
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "gte", "4"));
//        batches = batchRepository.findAll(specification);
//        assertTrue(batches.get(0).getPackageQuantity() >= 4);
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("packageQuantity", "lte", "4"));
//        batches = batchRepository.findAll(specification);
//        assertTrue(batches.get(0).getPackageQuantity() <= 4);
//
//        specification = new RSQLSpecification<>(new ComparisonFilter("flightNumber", "in", Arrays.asList("FcontainB", "FB").toArray()));
//        batches = batchRepository.findAll(specification);
//        assertEquals(2, batches.size());
//    }
//
//    @Test
//    void testInStatus(){
//        LinkedHashMap<String, String[]> params = new LinkedHashMap<>();
//        params.put("filter[status][in]", new String[]{"CLEARED", "VALIDATED"});
//
//        LogicalFilter filter = FilterParser.parse(params);
//        RSQLSpecification<Batch> specification = new RSQLSpecification<>(filter);
//        List<Batch> batches = batchRepository.findAll(specification);
//        assertEquals(10, batches.size());
//    }
//
//    @Test
//    void testFindAllWithGrateThanADate(){
//        RSQLSpecification<Batch> filter = new RSQLSpecification<Batch>(new ComparisonFilter("etd", "gt", "2017-08-04T13:20:00Z"));
//        List<Batch> batches = batchRepository.findAll(filter);
//        assertEquals(1, batches.size());
//        assertEquals("000-00000000", batches.get(0).getId());
//    }
//
//    @Test
//    void testFindAllWithOrFilter(){
//        ComparisonFilter filter1 = new ComparisonFilter("id", "eq", "000-00000000");
//        ComparisonFilter filter2 = new ComparisonFilter("id", "eq", "000-00000001");
//
//        ArrayList<Filter> orFilters = new ArrayList<>();
//        orFilters.add(filter1);
//        orFilters.add(filter2);
//
//        LogicalFilter orFilter = new LogicalFilter(OR, orFilters);
//
//        RSQLSpecification<Batch> rootFilter = new RSQLSpecification<>(orFilter);
//
//        List<Batch> batches = batchRepository.findAll(rootFilter);
//        assertEquals(2, batches.size());
//    }
//
//    @Test
//    void rSQLBuilder(){
////        RSQLSpecification<Batch> specification;
////        List<Batch> batches;
////
////        specification = new RSQLSpecification<>(new ComparisonFilter("client", "eq", "YUN"));
////        List<Batch> batchesByString = batchRepository.findAll(specification);
////        assertEquals( "YUN", batchesByString.get(0).getClient());
////        new RSQLBuilder(Batch.class).join()
//    }
//
//}
