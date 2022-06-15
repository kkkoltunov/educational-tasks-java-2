package ru.hse.homework4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.RECORD_COMPONENT,
        ElementType.FIELD,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
    /**
     * Шаблон для сохранения и восстановления дат
     */
    String value();
}
