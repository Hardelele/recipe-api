package com.github.hardelele.ra.services.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheServiceTest {

    @Autowired
    private CacheService<TestEntity1> cacheService1;

    @Autowired
    private CacheService<TestEntity2> cacheService2;

    private Map<UUID, TestEntity1> entities1;
    private Map<UUID, TestEntity2> entities2;

    private final UUID id1 = UUID.randomUUID();
    private final UUID id2 = UUID.randomUUID();

    @Before
    public void setUp() {
        entities1 = new HashMap<>();
        entities2 = new HashMap<>();

        entities1.put(id1, new TestEntity1(id1, "1"));
        entities2.put(id1, new TestEntity2(id1, "1"));

        cacheService1.add(id1, entities1.get(id1));
        cacheService2.add(id1, entities2.get(id1));
    }

    @Test
    public void getTest() {

        TestEntity1 expected1 = entities1.get(id1);
        TestEntity1 actual1 = cacheService1.get(id1, (Class<TestEntity1>) expected1.getClass());
        assertEquals(expected1, actual1);

        TestEntity2 expected2 = entities2.get(id1);
        TestEntity2 actual2 = cacheService2.get(id1, TestEntity2.class);
        assertEquals(expected2, actual2);
    }

    @Test
    public void addTest() {

        entities1.put(id2, new TestEntity1(id2,"2"));
        TestEntity1 expected1 = entities1.get(id2);
        entities2.put(id2, new TestEntity2(id2,"2"));
        TestEntity2 expected2 = entities2.get(id2);

        cacheService1.add(id2, entities1.get(id2));
        cacheService2.add(id2, entities2.get(id2));

        TestEntity1 actual1 = cacheService1.add(id2, entities1.get(id2));
        assertEquals(expected1, actual1);

        TestEntity2 actual2 = cacheService2.add(id2, entities2.get(id2));
        assertEquals(expected2, actual2);
    }

    @Data
    @AllArgsConstructor
    private static class TestEntity1 {
        private UUID id;
        private String testString;
    }

    @Data
    @AllArgsConstructor
    private static class TestEntity2 {
        private UUID id;
        private String testString;
    }
}
