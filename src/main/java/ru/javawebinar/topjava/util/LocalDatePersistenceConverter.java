package ru.javawebinar.topjava.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements
        AttributeConverter<LocalDate, Date> {
    @Override
    public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
//        System.out.println(java.sql.Date.valueOf(entityValue));
        return java.sql.Date.valueOf(entityValue);
    }

    @Override
    public LocalDate convertToEntityAttribute(java.sql.Date databaseValue) {
//        System.out.println("W R O N G  D A T E > " + DateTimeUtil.parseLocalDate(databaseValue.toLocalDate().toString()));
        return DateTimeUtil.parseLocalDate(databaseValue.toLocalDate().toString());
    }
}
