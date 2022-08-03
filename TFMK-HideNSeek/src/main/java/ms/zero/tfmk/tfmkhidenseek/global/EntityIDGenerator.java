package ms.zero.tfmk.tfmkhidenseek.global;

import java.util.concurrent.atomic.AtomicInteger;

public class EntityIDGenerator {
    private static AtomicInteger entityIDGenerator = new AtomicInteger(-100);

    public static Integer generateEntityID() {
        return entityIDGenerator.decrementAndGet();
    }
}
