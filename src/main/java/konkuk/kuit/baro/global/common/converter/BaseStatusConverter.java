package konkuk.kuit.baro.global.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import konkuk.kuit.baro.global.common.model.BaseStatus;

@Converter
public class BaseStatusConverter implements AttributeConverter<BaseStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BaseStatus baseStatus) {
        return baseStatus.getCode();
    }

    @Override
    public BaseStatus convertToEntityAttribute(Integer integer) {
        return BaseStatus.ofCode(integer);
    }
}
