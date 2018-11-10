package engineer.thomas_werner.dashboard.utilities;

import lombok.Getter;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class CacheTest {

    private Supplier<String> supplier;
    private FailingSupplier failingSupplier;

    @Before
    public void setUp() {
        supplier = new StringSupplier();
        failingSupplier = new FailingSupplier();
    }

    @Test
    public void testInitialLoad() {
        final Cache<String> cache = new Cache<>(supplier, 1, SECONDS);
        final Optional<String> result = cache.get("");
        assertEquals(true, result.isPresent());
        assertEquals("_0", result.get());
    }

    @Test
    public void testRepeatedLoadsBeforeTimeout() {
        final Cache<String> cache = new Cache<>(supplier, 1, SECONDS);
        cache.get("");
        cache.get("");
        cache.get("");
        final Optional<String> result = cache.get("");
        assertEquals(true, result.isPresent());
        assertEquals("_0", result.get());
    }

    @Test
    public void testRepeatedLoadsAfterTimeout() throws InterruptedException {
        final Cache<String> cache = new Cache<>(supplier, 1, SECONDS);
        cache.get("");

        Thread.sleep(1_234);

        cache.get("");
        cache.get("");
        final Optional<String> result = cache.get("");
        assertEquals(true, result.isPresent());
        assertEquals("_1", result.get());
    }

    @Test
    public void testRepeatedLoadsOfVariousKeyBeforeTimeout() {
        final Cache<String> cache = new Cache<>(supplier, 1, SECONDS);
        cache.get("A");
        cache.get("A");
        cache.get("B");

        final Optional<String> aResult = cache.get("A");
        assertEquals(true, aResult.isPresent());
        assertEquals("A_0", aResult.get());

        final Optional<String> bResult = cache.get("B");
        assertEquals(true, bResult.isPresent());
        assertEquals("B_0", bResult.get());
    }

    @Test
    public void testRepeatedLoadsOfVariousKeyAfterTimeout() throws InterruptedException {
        final Cache<String> cache = new Cache<>(supplier, 1, SECONDS);
        cache.get("A");
        cache.get("B");

        Thread.sleep(1_234);

        cache.get("A");
        cache.get("A");
        cache.get("B");
        cache.get("B");

        final Optional<String> aResult = cache.get("A");
        assertEquals(true, aResult.isPresent());
        assertEquals("A_1", aResult.get());

        final Optional<String> bResult = cache.get("B");
        assertEquals(true, bResult.isPresent());
        assertEquals("B_1", bResult.get());
    }

    @Test
    public void testResourceNotFound() {
        final Cache<String> cache = new Cache<>(failingSupplier, 1, SECONDS);
        assertEquals(0, failingSupplier.getAttempts());
        assertEquals(false, cache.get("A").isPresent());
        assertEquals(1, failingSupplier.getAttempts());
        assertEquals(false, cache.get("A").isPresent());
        assertEquals(2, failingSupplier.getAttempts());
    }

    private static class StringSupplier implements Supplier<String> {

        private Map<String, Integer> value = new HashMap<>();

        @Override
        public Optional<String> get(final String key) {
            Integer appendix = value.get(key);
            if(null == appendix) {
                appendix = 0;
            } else {
                appendix += 1;
            }
            value.put(key, appendix);
            return Optional.of(key +"_" +appendix);
        }

    }

    private static class FailingSupplier implements Supplier<String> {

        @Getter
        private int attempts;

        @Override
        public Optional<String> get(final String key) {
            attempts++;
            return Optional.empty();
        }

    }

}
