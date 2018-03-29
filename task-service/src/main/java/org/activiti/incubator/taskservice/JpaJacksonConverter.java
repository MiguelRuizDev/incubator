package org.activiti.incubator.taskservice;


/*
        import java.io.IOException;
        import javax.persistence.AttributeConverter;

        import com.fasterxml.jackson.core.JsonProcessingException;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import org.activiti.engine.ActivitiException;

public class JpaJsonConverter<T> implements AttributeConverter<T, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private Class<T> entityClass;

    public JpaJsonConverter(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public String convertToDatabaseColumn(T entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new ActivitiException("Unable to serialize object.",
                    e);
        }
    }

    @Override
    public T convertToEntityAttribute(String entityTextRepresentation) {
        try {
            return objectMapper.readValue(entityTextRepresentation,
                    entityClass);
        } catch (IOException e) {
            throw new ActivitiException("Unable to deserialize object.",
                    e);
        }
    }
}


*/