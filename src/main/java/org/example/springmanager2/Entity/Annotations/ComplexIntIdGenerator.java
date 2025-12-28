package org.example.springmanager2.Entity.Annotations;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class ComplexIntIdGenerator implements IdentifierGenerator {

    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        int start = 1000; // default start
        try {
            ComplexIntId annotation = object.getClass().getDeclaredField("id").getAnnotation(ComplexIntId.class);
            if (annotation != null) {
                start = annotation.start();
            }
        } catch (NoSuchFieldException ignored) {}

        // Complex generation logic:
        // Example: start + current atomic counter + random 0-99
        int random = (int)(Math.random() * 100); // 0-99
        int generatedId = start + counter.incrementAndGet() * 100 + random;

        return generatedId;
    }
}
