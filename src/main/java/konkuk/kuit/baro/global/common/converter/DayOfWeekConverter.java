package konkuk.kuit.baro.global.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import konkuk.kuit.baro.domain.schedule.model.DayOfWeek;

@Converter
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        return dayOfWeek.getCode();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer code) {
        return DayOfWeek.ofCode(code);
    }
}
