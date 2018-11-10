package engineer.thomas_werner.dashboard.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Cache<T> {

    private final ReentrantLock lock = new ReentrantLock();
    private final Supplier<T> supplier;
    private final long maxAgeValue;
    private final TemporalUnit maxAgeUnit;

    @Getter
    @Setter
    private String name;
    private Map<String, ValueWithTimeStamp<T>> values = new ConcurrentHashMap<>();

    public Cache(final Supplier<T> supplier, final long maxAgeValue, final TemporalUnit maxAgeUnit) {
        this.supplier = supplier;
        this.maxAgeValue = maxAgeValue;
        this.maxAgeUnit = maxAgeUnit;
    }

    public Optional<T> get(final String key) {
        ValueWithTimeStamp<T> value = values.get(key);
        if(isFresh(value))
            return Optional.of(value.getValue());

        updateValue(key);
        value = values.get(key);
        return null == value ? Optional.empty() : Optional.of(value.getValue());
    }

    private boolean isFresh(final ValueWithTimeStamp<T> value) {
        return (null != value) && LocalDateTime.now().minus(maxAgeValue, maxAgeUnit).isBefore(value.getTimeStamp());
    }

    private void updateValue(final String key) {
        lock.lock();
        try {
            if(!isFresh(values.get(key))) {                                                                             // might have been updated while waiting for lock
                supplier.get(key).ifPresent(t -> values.put(key, new ValueWithTimeStamp<>(t, LocalDateTime.now())));
                log.info(Optional.ofNullable(name).orElse("unknown cache") + ": refreshed " + key);
            }
        } finally {
            lock.unlock();
        }
    }

    @AllArgsConstructor
    @Getter
    private static class ValueWithTimeStamp<U> {
        private final U value;
        private final LocalDateTime timeStamp;
    }

}
