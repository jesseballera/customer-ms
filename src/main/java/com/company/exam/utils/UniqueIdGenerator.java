package com.company.exam.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.AbstractEntityPersister;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ThreadLocalRandom;

public class UniqueIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        AbstractEntityPersister persister = (AbstractEntityPersister) session.getEntityPersister(object.getClass().getName(), object);

        String tableName = persister.getTableName();
        String idColumnName = persister.getIdentifierColumnNames()[0];

        Connection connection = session.connection();

        while (true) {
            // Generate a random number strictly between 10,000,000 and 99,999,999
            long generatedId = ThreadLocalRandom.current().nextLong(10000000L, 100000000L);

            // Dynamically verify if this ID exists in the specific table
            String sql = String.format("SELECT 1 FROM %s WHERE %s = ?", tableName, idColumnName);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, generatedId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return generatedId; // Unique ID found, return it
                    }
                }
            } catch (Exception e) {
                throw new HibernateException("Failed to validate unique 8-digit ID", e);
            }
        }
    }
}

