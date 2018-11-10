package engineer.thomas_werner.dashboard.utilities;

import java.util.Optional;

public interface Supplier<T> {

    Optional<T> get(String key);

}
