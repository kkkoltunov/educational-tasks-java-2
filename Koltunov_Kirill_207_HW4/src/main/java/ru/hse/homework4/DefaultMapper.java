package ru.hse.homework4;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Класс, который реализует интерфейс Mapper и используется для сохранения и восстановления состояний объектов.
 */
public record DefaultMapper() implements Mapper {

    /**
     * Получение объекта из строки.
     *
     * @param clazz класс, сохранённый экземпляр которого находится в {@code input}
     * @param input строковое представление сохранённого экземпляра класса {@code clazz}
     * @param <T>   параметризованный тип объекта.
     * @return ссылка на объект.
     */
    @Override
    public <T> T readFromString(Class<T> clazz, String input) {
        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Public non-parametric constructor is absent! Name of class: " + clazz);
        }

        try {
            parseObjectFromString(instance, input); // todo тесты + описать json
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method could not found for some object!");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invoke a method or constructor that throws an underlying exception itself!");
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create an instance of a class using the newInstance method!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get access!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Some class not found!");
        }
        return instance;
    }

    /**
     * Получение объекта из InputStream'a.
     *
     * @param clazz       класс, сохранённый экземпляр которого находится в {@code inputStream}
     * @param inputStream поток ввода, содержащий строку в {@link java.nio.charset.StandardCharsets#UTF_8} кодировке
     * @param <T>         параметризованный тип объекта.
     * @return ссылка на объект.
     * @throws IOException выбрасывается при возникновении проблем с файлами.
     */
    @Override
    public <T> T read(Class<T> clazz, InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return readFromString(clazz, result.toString(StandardCharsets.UTF_8));
    }

    /**
     * Получение объекта из файла.
     *
     * @param clazz класс, сохранённый экземпляр которого находится в файле
     * @param file  файл, содержимое которого - строковое представление экземпляра {@code clazz}
     *              в {@link java.nio.charset.StandardCharsets#UTF_8} кодировке
     * @param <T>   параметризованный тип объекта.
     * @return ссылка на объект.
     * @throws IOException выбрасывается при возникновении проблем с файлами.
     */
    @Override
    public <T> T read(Class<T> clazz, File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        return readFromString(clazz, lines.get(0));
    }

    /**
     * Поиск поля по измененному названию с помощью PropertyName.
     *
     * @param object    объект, в котором необходимо найти поле.
     * @param substring название поля.
     * @return ссылка на поле, иначе null.
     */
    private Field findFieldForPropertyName(Object object, String substring) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields)
            if (!Objects.isNull(field.getAnnotation(PropertyName.class)) &&
                    Objects.equals(field.getAnnotation(PropertyName.class).value(), substring)) {
                return field;
            }

        return null;
    }

    /**
     * Поиск ссылки на объект из Enum.
     *
     * @param className  название Enum.
     * @param fieldValue название объекта Enum.
     * @return ссылку на перечисление Enum, иначе null.
     * @throws IllegalAccessException выбрасывается если нет доступа к полям.
     * @throws ClassNotFoundException выбрасывает если такого класса не существует.
     */
    private Object findEnumObjectForName(String className, String fieldValue) throws IllegalAccessException,
            ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldValue)) {
                if (!field.trySetAccessible()) {
                    throw new UnsupportedOperationException("Can't get access to enum field! Field name: " + field.getName());
                }
                return field.get(clazz);
            }
        }
        return null;
    }

    /**
     * Установка значения примитива в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @return true, если значение успешно установлено, иначе - false.
     * @throws IllegalAccessException выбрасывается если нет доступа к полям.
     */
    private boolean fieldSetValueForPrimitive(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws IllegalAccessException {
        if (fieldType.equals(String.class)) {
            field.set(instance, fieldValue.substring(1, fieldValue.length() - 1));
        } else if (fieldType.equals(char.class) || fieldType.equals(Character.class)) {
            field.set(instance, fieldValue.charAt(0));
        } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
            field.set(instance, Boolean.parseBoolean(fieldValue));
        } else if (fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
            field.set(instance, Byte.parseByte(fieldValue));
        } else if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
            field.set(instance, Short.parseShort(fieldValue));
        } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            field.set(instance, Integer.parseInt(fieldValue));
        } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
            field.set(instance, Long.parseLong(fieldValue));
        } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
            field.set(instance, Float.parseFloat(fieldValue));
        } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
            field.set(instance, Double.parseDouble(fieldValue));
        } else {
            return false;
        }

        return true;
    }

    /**
     * Установка значения LocalTime/LocalDate/LocalDateTime в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @throws IllegalAccessException выбрасывается если нет доступа к полям.
     */
    private void fieldSetValueForDateAndTime(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws IllegalAccessException {
        if (Objects.isNull(field.getAnnotation(DateFormat.class))) {
            if (fieldType.equals(LocalDateTime.class)) {
                field.set(instance, LocalDateTime.parse(fieldValue.substring(1, fieldValue.length() - 1)));
            } else if (fieldType.equals(LocalTime.class)) {
                field.set(instance, LocalTime.parse(fieldValue.substring(1, fieldValue.length() - 1)));
            } else if (fieldType.equals(LocalDate.class)) {
                field.set(instance, LocalDate.parse(fieldValue.substring(1, fieldValue.length() - 1)));
            }
        } else {
            if (fieldType.equals(LocalDateTime.class)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
                field.set(instance, LocalDateTime.parse(fieldValue.substring(1, fieldValue.length() - 1), dateTimeFormatter));
            } else if (fieldType.equals(LocalTime.class)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
                field.set(instance, LocalTime.parse(fieldValue.substring(1, fieldValue.length() - 1), dateTimeFormatter));
            } else if (fieldType.equals(LocalDate.class)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
                field.set(instance, LocalDate.parse(fieldValue.substring(1, fieldValue.length() - 1), dateTimeFormatter));
            }
        }
    }

    /**
     * Установка значения List/Set для примитивов в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @return true, если значение успешно установлено, иначе - false.
     * @throws IllegalAccessException выбрасывается если нет доступа к полям.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Игнорируются "сырые" типы и их добавление в лист. Сделано
    // для удобства при восстановлении объекта.
    private boolean fieldSetValueForListAndSetPrimitive(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws IllegalAccessException {
        List newList = new ArrayList();
        String[] values = fieldValue.split(";");
        if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(String.class)) {
            for (String string : values) {
                newList.add(string.substring(1, string.length() - 1));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Integer.class)) {
            for (String string : values) {
                newList.add(Integer.parseInt(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Double.class)) {
            for (String string : values) {
                newList.add(Double.parseDouble(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Byte.class)) {
            for (String string : values) {
                newList.add(Byte.parseByte(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Short.class)) {
            for (String string : values) {
                newList.add(Short.parseShort(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Long.class)) {
            for (String string : values) {
                newList.add(Long.parseLong(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Float.class)) {
            for (String string : values) {
                newList.add(Float.parseFloat(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Character.class)) {
            for (String string : values) {
                newList.add(string.charAt(0));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(Boolean.class)) {
            for (String string : values) {
                newList.add(Boolean.parseBoolean(string));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Установка значения List/Set для LocalTime/LocalDate/LocalDateTime в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @throws IllegalAccessException выбрасывается если нет доступа к полям.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})  // Игнорируются "сырые" типы и их добавление в лист. Сделано
    // для удобства при восстановлении объекта.
    private void fieldSetValueForListAndSetDateAndTime(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws IllegalAccessException {
        if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(LocalDate.class)) {
            List newList = new ArrayList();
            String[] values = fieldValue.split(";");
            if (Objects.isNull(field.getAnnotation(DateFormat.class))) {
                for (String string : values) {
                    newList.add(LocalDate.parse(string.substring(1, string.length() - 1)));
                }
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
                for (String string : values) {
                    newList.add(LocalDate.parse(string.substring(1, string.length() - 1), dateTimeFormatter));
                }
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(LocalTime.class)) {
            List newList = new ArrayList();
            String[] values = fieldValue.split(";");
            if (Objects.isNull(field.getAnnotation(DateFormat.class))) {
                for (String string : values) {
                    newList.add(LocalTime.parse(string.substring(1, string.length() - 1)));
                }
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
                for (String string : values) {
                    newList.add(LocalTime.parse(string.substring(1, string.length() - 1), dateTimeFormatter));
                }
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(LocalDateTime.class)) {
            List newList = new ArrayList();
            String[] values = fieldValue.split(";");
            if (Objects.isNull(field.getAnnotation(DateFormat.class))) {
                for (String string : values) {
                    newList.add(LocalDateTime.parse(string.substring(1, string.length() - 1)));
                }
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
                for (String string : values) {
                    newList.add(LocalDateTime.parse(string.substring(1, string.length() - 1), dateTimeFormatter));
                }
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet<>(newList));
            }
        }
    }

    /**
     * Установка значения List/Set для произвольных объектов в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @throws ClassNotFoundException    выбрасывается если нет доступа к классу.
     * @throws InvocationTargetException выбрасывается вызванным методом или конструктором.
     * @throws InstantiationException    выбрасывается если не удается создать экземпляр класса с помощью newInstance метода.
     * @throws IllegalAccessException    выбрасывается если нет доступа к полям.
     * @throws NoSuchMethodException     выбрасывается если искомый метод отсутствует.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})  // Игнорируются "сырые" типы и их добавление в лист. Сделано
    // для удобства при восстановлении объекта.
    private void fieldSetValueForListAndSetClassesAndRecords(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException,
            NoSuchMethodException {
        List newList = new ArrayList();
        if (((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getSuperclass().equals(Record.class)) {
            String[] collectionData = fieldValue.split(";");
            for (String collectionDatum : collectionData) {
                Class<?> clazz = Class.forName(((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getName());
                newList.add(createRecordObject(clazz.getConstructors()[0], clazz.getRecordComponents(), collectionDatum));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet(newList));
            }
        } else {
            String[] collectionData = fieldValue.split(";");
            for (int i = 0; i < collectionData.length; ++i) {
                Class<?> clazz = Class.forName(((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getName());
                newList.add(parseObjectFromString(clazz.getDeclaredConstructor().newInstance(), fieldValue));
            }
            if (fieldType.equals(List.class)) {
                field.set(instance, newList);
            } else {
                field.set(instance, new HashSet(newList));
            }
        }
    }

    /**
     * Установка значения List/Set для Enum в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @throws ClassNotFoundException выбрасывается если нет доступа к классу.
     * @throws IllegalAccessException выбрасывается если нет доступа к полям.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})  // Игнорируются "сырые" типы и их добавление в лист. Сделано
    // для удобства при восстановлении объекта.
    private void fieldSetValueForListAndSetEnum(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws ClassNotFoundException, IllegalAccessException {
        List newList = new ArrayList();
        String[] collectionData = fieldValue.split(";");
        for (String collectionDatum : collectionData) {
            newList.add(findEnumObjectForName(((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getName(),
                    collectionDatum.substring(1, collectionDatum.length() - 1)));
        }
        if (fieldType.equals(List.class)) {
            field.set(instance, newList);
        } else {
            field.set(instance, new HashSet(newList));
        }
    }

    /**
     * Выбор методов для установки значения List/Set в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @param fieldType  тип поля, для того, чтобы распарсить значение поля.
     * @throws IllegalAccessException    выбрасывается если нет доступа к полям.
     * @throws ClassNotFoundException    выбрасывается если нет доступа к классу.
     * @throws InvocationTargetException выбрасывается вызванным методом или конструктором.
     * @throws InstantiationException    выбрасывается если не удается создать экземпляр класса с помощью newInstance метода.
     * @throws NoSuchMethodException     выбрасывается если искомый метод отсутствует.
     */
    @SuppressWarnings("StatementWithEmptyBody")  // Игнорируются пустые блоки if. Сделано, чтобы поддержать декопмпозицию.
    private void fieldSetValueForListAndSet(Object instance, Field field, String fieldValue, Class<?> fieldType)
            throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException,
            NoSuchMethodException {
        fieldValue = fieldValue.substring(1, fieldValue.length() - 1);
        if (fieldSetValueForListAndSetPrimitive(instance, field, fieldValue, fieldType)) {
        } else if (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(LocalDate.class) ||
                ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(LocalDateTime.class) ||
                ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(LocalTime.class)) {
            fieldSetValueForListAndSetDateAndTime(instance, field, fieldValue, fieldType);
        } else if (!Objects.isNull(((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getAnnotation(Exported.class))) {
            fieldSetValueForListAndSetClassesAndRecords(instance, field, fieldValue, fieldType);
        } else if (((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).isEnum()) {
            fieldSetValueForListAndSetEnum(instance, field, fieldValue, fieldType);
        }
    }

    /**
     * Выбор методов для установки произвольного значения в поле.
     *
     * @param instance   объект, в поле которого необходимо установить значение.
     * @param field      поле, в которое необходимо установить значение.
     * @param fieldValue значение, которое необходимо установить в поле.
     * @return true, если установка была произведена успешно, false - иначе.
     * @throws IllegalAccessException    выбрасывается если нет доступа к полям.
     * @throws ClassNotFoundException    выбрасывается если нет доступа к классу.
     * @throws NoSuchMethodException     выбрасывается если искомый метод отсутствует.
     * @throws InvocationTargetException выбрасывается вызванным методом или конструктором.
     * @throws InstantiationException    выбрасывается если не удается создать экземпляр класса с помощью newInstance метода.
     * @throws NoSuchFieldException      выбрасывается если не удается найти поле по имени.
     */
    @SuppressWarnings("StatementWithEmptyBody") // Игнорируются пустые блоки if. Сделано, чтобы поддержать декопмпозицию.
    private boolean fieldSetValue(Object instance, Field field, String fieldValue) throws IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
            NoSuchFieldException {
        if (fieldValue.equals("null")) {
            field.set(instance, null);
            return true;
        }

        Class<?> fieldType = field.getType();
        if (fieldSetValueForPrimitive(instance, field, fieldValue, fieldType)) {
        } else if (fieldType.equals(LocalDateTime.class) || fieldType.equals(LocalTime.class) || fieldType.equals(LocalDate.class)) {
            fieldSetValueForDateAndTime(instance, field, fieldValue, fieldType);
        } else if (fieldType.isEnum()) {
            field.set(instance, findEnumObjectForName(field.getType().getName(), fieldValue.substring(1, fieldValue.length() - 1)));
        } else if (fieldType.equals(List.class) || fieldType.equals(Set.class)) {
            fieldSetValueForListAndSet(instance, field, fieldValue, fieldType);
        } else if (!Objects.isNull(field.getType().getAnnotation(Exported.class))) {
            Class<?> clazz = Class.forName(field.getType().getName());
            if (field.getType().getSuperclass().equals(Record.class)) {
                field.set(instance, createRecordObject(clazz.getConstructors()[0], clazz.getRecordComponents(), fieldValue));
            } else {
                field.set(instance, parseObjectFromString(clazz.getDeclaredConstructor().newInstance(), fieldValue));
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Создание примитива для Record класса.
     *
     * @param fieldValue  значение поля.
     * @param classObject класс для создаваемого объекта.
     * @return ссылка на объект, если он был успешно создан, иначе null.
     */
    private Object createObjectForRecorderClassPrimitive(String fieldValue, Class<?> classObject) {
        if (classObject.equals(String.class)) {
            return fieldValue.substring(1, fieldValue.length() - 1);
        } else if (classObject.equals(char.class) || classObject.equals(Character.class)) {
            return fieldValue.charAt(0);
        } else if (classObject.equals(boolean.class) || classObject.equals(Boolean.class)) {
            return Boolean.parseBoolean(fieldValue);
        } else if (classObject.equals(byte.class) || classObject.equals(Byte.class)) {
            return Byte.parseByte(fieldValue);
        } else if (classObject.equals(short.class) || classObject.equals(Short.class)) {
            return Short.parseShort(fieldValue);
        } else if (classObject.equals(int.class) || classObject.equals(Integer.class)) {
            return Integer.parseInt(fieldValue);
        } else if (classObject.equals(long.class) || classObject.equals(Long.class)) {
            return Long.parseLong(fieldValue);
        } else if (classObject.equals(float.class) || classObject.equals(Float.class)) {
            return Float.parseFloat(fieldValue);
        } else if (classObject.equals(double.class) || classObject.equals(Double.class)) {
            return Double.parseDouble(fieldValue);
        }

        return null;
    }

    /**
     * Создание LocalTime/LocalDate/LocalDateTime для Record класса.
     *
     * @param fieldValue  значение поля.
     * @param classObject класс для создаваемого объекта.
     * @return ссылка на объект, если он был успешно создан, иначе null.
     */
    private Object createObjectForRecorderClassDateAndTime(String fieldValue, Class<?> classObject) {
        if (Objects.isNull(classObject.getAnnotation(DateFormat.class))) {
            if (classObject.equals(LocalDateTime.class)) {
                return LocalDateTime.parse(fieldValue.substring(1, fieldValue.length() - 1));
            } else if (classObject.equals(LocalTime.class)) {
                return LocalTime.parse(fieldValue.substring(1, fieldValue.length() - 1));
            } else {
                return LocalDate.parse(fieldValue.substring(1, fieldValue.length() - 1));
            }
        } else {
            if (classObject.equals(LocalDateTime.class)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(classObject.getAnnotation(DateFormat.class).value());
                return LocalDateTime.parse(fieldValue.substring(1, fieldValue.length() - 1), dateTimeFormatter);
            } else if (classObject.equals(LocalTime.class)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(classObject.getAnnotation(DateFormat.class).value());
                return LocalTime.parse(fieldValue.substring(1, fieldValue.length() - 1), dateTimeFormatter);
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(classObject.getAnnotation(DateFormat.class).value());
                return LocalDate.parse(fieldValue.substring(1, fieldValue.length() - 1), dateTimeFormatter);
            }
        }
    }

    /**
     * Выбор метода для создания поля для Record класса.
     *
     * @param fieldValue  значение поля.
     * @param classObject класс для создаваемого объекта.
     * @return ссылка на объект, если он был успешно создан, иначе null.
     * @throws ClassNotFoundException выбрасывается если нет доступа к классу.
     * @throws IllegalAccessException выбрасывается если нет доступа к объекту класса.
     */
    private Object createObjectForRecorderClass(String fieldValue, Class<?> classObject) throws ClassNotFoundException,
            IllegalAccessException {
        if (fieldValue.equals("null")) {
            return null;
        }
        Object createObject = createObjectForRecorderClassPrimitive(fieldValue, classObject);
        if (!Objects.isNull(createObject)) {
            return createObject;
        } else if (classObject.equals(LocalDateTime.class) || classObject.equals(LocalTime.class) || classObject.equals(LocalDate.class)) {
            return createObjectForRecorderClassDateAndTime(fieldValue, classObject);
        } else if (classObject.isEnum()) {
            return findEnumObjectForName(classObject.getName(), fieldValue.substring(1, fieldValue.length() - 1));
        }

        return null;
    }

    /**
     * Создание массива строк, в которых находятся поля и их значения.
     *
     * @param input строка с полями и значениями.
     * @return массив строк, в которых находятся поля и их значения.
     */
    private String[] creteArrayOfFieldsAndValues(String input) {
        String[] fieldsAndValues = input.split(",");
        fieldsAndValues[0] = fieldsAndValues[0].substring(1);
        fieldsAndValues[fieldsAndValues.length - 1] =
                fieldsAndValues[fieldsAndValues.length - 1].substring(0, fieldsAndValues[fieldsAndValues.length - 1].length() - 1);
        return fieldsAndValues;
    }

    /**
     * Создание объекта Record класса.
     *
     * @param constructor      конструктор класса.
     * @param recordComponents поля класса.
     * @param input            строка с полями и значениями.
     * @return ссылка на объект Record класса.
     * @throws InvocationTargetException выбрасывается вызванным методом или конструктором.
     * @throws InstantiationException    выбрасывается если не удается создать экземпляр класса с помощью newInstance метода.
     * @throws IllegalAccessException    выбрасывается если нет доступа к объекту класса.
     * @throws ClassNotFoundException    выбрасывается если нет доступа к классу.
     */
    private Object createRecordObject(Constructor<?> constructor, RecordComponent[] recordComponents, String input)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String[] fieldsAndValues = creteArrayOfFieldsAndValues(input);
        Map<String, String> map = new LinkedHashMap<>();
        for (String fieldsAndValue : fieldsAndValues) {
            String[] parseForMap = fieldsAndValue.split("=");
            map.put(parseForMap[0].substring(1, parseForMap[0].length() - 1), parseForMap[1]);
        }

        Object[] objects = new Object[recordComponents.length];
        for (int i = 0; i < recordComponents.length; ++i) {
            objects[i] = createObjectForRecorderClass(map.get(recordComponents[i].getName()), recordComponents[i].getType());
        }
        return constructor.newInstance(objects);
    }

    /**
     * Замена служебных символов в строке.
     *
     * @param input строка, в которой необходимо заменить символы.
     * @return измененная строка.
     */
    private String fixStringForSuccessParse(String input) {
        char[] inputChar = input.toCharArray();
        int counterBracket = 0;
        for (int i = 1; i < inputChar.length; ++i) {
            if (inputChar[i] == '(') {
                ++counterBracket;
            }
            if (counterBracket > 0 && inputChar[i] == ',') {
                inputChar[i] = '&';
            }
            if (inputChar[i] == ')') {
                --counterBracket;
            }
        }

        return new String(inputChar);
    }

    /**
     * Создание словаря, в котором в роли ключа название поле, а в роли значения - значение поля.
     *
     * @param fieldsAndValues массив с данными.
     * @return словарь с полями и их значениями.
     */
    private Map<String, String> createMapForFieldsAndValues(String[] fieldsAndValues) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String fieldsAndValue : fieldsAndValues) {
            String[] parseForMap = fieldsAndValue.split("=");
            StringBuilder valueOfField = new StringBuilder();
            for (int i = 1; i < parseForMap.length; ++i) {
                if (i != 1) {
                    valueOfField.append("=");
                }
                valueOfField.append(parseForMap[i]);
            }

            map.put(parseForMap[0].substring(1, parseForMap[0].length() - 1), valueOfField.toString().replaceAll("&", ","));
        }

        return map;
    }

    /**
     * Создание объекта по данным из строки.
     *
     * @param instance объект, который необходимо восстановить.
     * @param input    строка с данными об объектах.
     * @param <T>      параметризованный тип объекта.
     * @return ссылка на объект.
     * @throws NoSuchMethodException     выбрасывается если искомый метод отсутствует.
     * @throws InvocationTargetException выбрасывается вызванным методом или конструктором.
     * @throws InstantiationException    выбрасывается если не удается создать экземпляр класса с помощью newInstance метода.
     * @throws IllegalAccessException    выбрасывается если нет доступа к полям.
     * @throws ClassNotFoundException    выбрасывается если нет доступа к классу.
     */
    private <T> T parseObjectFromString(T instance, String input) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        input = fixStringForSuccessParse(input);
        String[] fieldsAndValues = creteArrayOfFieldsAndValues(input);
        Map<String, String> map = createMapForFieldsAndValues(fieldsAndValues);

        boolean checkUnknownField = instance.getClass().getAnnotation(Exported.class).unknownPropertiesPolicy().equals(UnknownPropertiesPolicy.FAIL);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            Field field;
            try {
                field = instance.getClass().getDeclaredField(entry.getKey());
            } catch (NoSuchFieldException e) {
                field = findFieldForPropertyName(instance, entry.getKey());
                if (Objects.isNull(field) && checkUnknownField) {
                    throw new RuntimeException("Field not found! Field name: " + entry.getKey());
                } else if (Objects.isNull(field)) {
                    continue;
                }
            }
            if (isNotSupportedType(field)) {
                throw new IllegalArgumentException("This type of field not supported! Field name: " + field.getName());
            }
            try {
                if (!field.trySetAccessible()) {
                    throw new UnsupportedOperationException("Can't get access to enum field! Field name: " + field.getName());
                }
                if (!fieldSetValue(instance, field, entry.getValue())) {
                    throw new RuntimeException("Field value not set! Field name: " + field.getName());
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Field not found! Field name: " + entry.getKey());
            }
        }
        return instance;
    }

    /**
     * Сохранение состояние объекта в строку.
     * @param object объект для сохранения.
     * @return строка с данными объекта.
     */
    @Override
    public String writeToString(Object object) {
        checkClassBeforeWrite(object);
        List<Field> fields = getFields(object);
        StringBuilder stringBuilder = writeObjectToString(object, fields);

        return stringBuilder.toString();
    }

    /**
     * Сохранение состояние объекта в OutputStream.
     * @param object объект для сохранения.
     * @param outputStream stream для вывода в него строки сохраненного объекта.
     * @throws IOException выбрасывается при возникновении проблем с файлами.
     */
    @Override
    public void write(Object object, OutputStream outputStream) throws IOException {
        outputStream.write(writeToString(object).getBytes(StandardCharsets.UTF_8));
    }

    /**
     *
     * @param object объект для сохранения.
     * @param file файл, в которой необходимо сохранить объект.
     * @throws IOException выбрасывается при возникновении проблем с файлами.
     */
    @Override
    public void write(Object object, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file.getAbsolutePath(), false)) {
            writer.write(writeToString(object));
            writer.flush();
        }
    }

    /**
     * Проверка свойств класса перед выполнением сохранения.
     * @param object проверяемый класс.
     */
    private void checkClassBeforeWrite(Object object) {
        if (Objects.isNull(object)) {
            throw new NullPointerException("Object is null!");
        }

        if (Objects.isNull(object.getClass().getAnnotation(Exported.class))) {
            System.out.println(object.getClass());
            throw new IllegalArgumentException("Class not marked with @Exported.");
        }

        if (!object.getClass().getSuperclass().equals(Record.class)) {
            try {
                object.getClass().getConstructor();
            } catch (NoSuchMethodException ignored) {
                throw new RuntimeException("Public non-parametric constructor is absent! Name of class: " + object.getClass());
            }
        }
    }

    /**
     * Получение полей класса и установка доступа к ним.
     * @param object класс, поля которого необходимо получить.
     * @return список полей.
     */
    private List<Field> getFields(Object object) {
        List<Field> fields = new CopyOnWriteArrayList<>(List.of(object.getClass().getDeclaredFields()));
        for (var field : fields) {
            if (!field.trySetAccessible()) {
                throw new UnsupportedOperationException("Can't get access to enum field! Field name: " + field.getName());
            }
            if (isNotSupportedType(field)) {
                throw new IllegalArgumentException("This type of field not supported! Field name: " + field.getName());
            }
            if (!Objects.isNull(field.getAnnotation(Ignored.class))) {
                fields.remove(field);
            }
        }
        return fields;
    }

    /**
     * Сохранение состояния объекта в строку.
     * @param object объект, который необходимо сохранить.
     * @param fields поля, которые необходимо сохранить.
     * @return строка с полями объекта.
     */
    private StringBuilder writeObjectToString(Object object, List<Field> fields) {
        NullHandling nullHandlingInfo = object.getClass().getAnnotation(Exported.class).nullHandling();
        boolean flagComma = false;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        boolean nullObject;
        for (Field field : fields) {
            try {
                if (Objects.isNull(field.get(object)) && nullHandlingInfo.equals(NullHandling.EXCLUDE)) {
                    continue;
                } else if (Objects.isNull(field.get(object)) && nullHandlingInfo.equals(NullHandling.INCLUDE)) {
                    nullObject = true;
                } else {
                    nullObject = false;
                }
            } catch (IllegalAccessException ignored) {
                throw new RuntimeException("Access denied! Name of field: " + field.getName() + ".");
            }

            if (flagComma) {
                stringBuilder.append(",");
            }

            stringBuilder.append("\"");
            if (Objects.isNull(field.getAnnotation(PropertyName.class))) {
                stringBuilder.append(field.getName());
            } else {
                stringBuilder.append(field.getAnnotation(PropertyName.class).value());
            }

            if (nullObject) {
                stringBuilder.append("\"=null");
            } else {
                stringBuilder.append("\"=");
                try {
                    createObjectJSON(stringBuilder, field, object);
                } catch (IllegalAccessException ignored) {
                    throw new RuntimeException("Access denied! Name of field: " + field.getName() + ".");
                }
            }
            flagComma = true;
        }
        stringBuilder.append(")");
        return stringBuilder;
    }

    /**
     * Проверка неподдерживаемых типов для сохранения и восстановления.
     * @param field поле, тип которого необходимо проверить.
     * @return true, если проверка прошла успешно, иначе - false.
     */
    private boolean isNotSupportedType(Field field) {
        if (Objects.isNull(field)) {
            return true;
        }
        if (field.getType().isSynthetic()) {
            return true;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            return true;
        }
        if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
            return false;
        }
        if (field.getType().equals(Byte.class) || field.getType().equals(Short.class) ||
                field.getType().equals(Integer.class) || field.getType().equals(Long.class) ||
                field.getType().equals(Float.class) || field.getType().equals(Double.class) ||
                field.getType().equals(Character.class) || field.getType().equals(Boolean.class)) {
            return false;
        }
        if (!Objects.isNull(field.getType().getAnnotation(Exported.class))) {
            return false;
        }
        if (field.getType().equals(List.class) || field.getType().equals(Set.class)) {
            return false;
        }
        if (field.getType().isEnum()) {
            return false;
        }
        if (field.getType().equals(LocalDate.class) || field.getType().equals(LocalTime.class) ||
                field.getType().equals(LocalDateTime.class)) {
            return false;
        }
        return true;
    }

    /**
     * Сохранение состояния List.
     * @param stringBuilder строка, в которую происходит сохранение.
     * @param field поле, которое необходимо сохранить.
     * @param object объект, поле которого сохраняется.
     * @throws IllegalAccessException выбрасывается, если нет доступа к полю.
     */
    private void createListJSON(StringBuilder stringBuilder, Field field, Object object) throws IllegalAccessException {
        List<?> list = (List<?>) field.get(object);
        boolean flagComma = false;

        stringBuilder.append("[");
        for (var item : list) {
            if (isNotSupportedTypeForCollection(item)) {
                throw new IllegalArgumentException("This type of collection not supported! Field name: " + field.getName());
            }
            if (flagComma) {
                stringBuilder.append(";");
            }
            createCollectionObjectJSON(stringBuilder, field, item);
            flagComma = true;
        }
        stringBuilder.append("]");
    }

    /**
     * Сохранение состояния Set.
     * @param stringBuilder строка, в которую происходит сохранение.
     * @param field поле, которое необходимо сохранить.
     * @param object объект, поле которого сохраняется.
     * @throws IllegalAccessException выбрасывается, если нет доступа к полю.
     */
    private void createSetJSON(StringBuilder stringBuilder, Field field, Object object) throws IllegalAccessException {
        Set<?> list = (Set<?>) field.get(object);
        boolean flagComma = false;

        stringBuilder.append("{");
        for (var item : list) {
            if (isNotSupportedTypeForCollection(item)) {
                throw new IllegalArgumentException("This type of collection not supported! Field name: " + field.getName());
            }
            if (flagComma) {
                stringBuilder.append(";");
            }
            createCollectionObjectJSON(stringBuilder, field, item);
            flagComma = true;
        }
        stringBuilder.append("}");
    }

    /**
     * Сохранение состояния LocalDate/LocalTime/LocalDateTime.
     * @param stringBuilder строка, в которую происходит сохранение.
     * @param field поле, которое необходимо сохранить.
     * @param object объект, поле которого сохраняется.
     * @throws IllegalAccessException выбрасывается, если нет доступа к полю.
     */
    private void createDateTimeJSON(StringBuilder stringBuilder, Field field, Object object) throws IllegalAccessException {
        if (!Objects.isNull(field.getAnnotation(DateFormat.class))) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
            String date = "null";
            if (field.getType().equals(LocalDate.class)) {
                date = dateTimeFormatter.format((LocalDate) field.get(object));
            } else if (field.getType().equals(LocalTime.class)) {
                date = dateTimeFormatter.format((LocalTime) field.get(object));
            } else if (field.getType().equals(LocalDateTime.class)) {
                date = dateTimeFormatter.format((LocalDateTime) field.get(object));
            }
            stringBuilder.append("\"");
            stringBuilder.append(date);
            stringBuilder.append("\"");
        } else {
            stringBuilder.append("\"");
            stringBuilder.append(field.get(object).toString());
            stringBuilder.append("\"");
        }
    }

    /**
     * Выбор метода для сохранения состояния объекта.
     * @param stringBuilder строка, в которую происходит сохранение.
     * @param field поле, которое необходимо сохранить.
     * @param object объект, поле которого сохраняется.
     * @throws IllegalAccessException выбрасывается, если нет доступа к полю.
     */
    private void createObjectJSON(StringBuilder stringBuilder, Field field, Object object) throws IllegalAccessException {
        if (field.getType().isPrimitive() || field.getType().equals(Byte.class) ||
                field.getType().equals(Short.class) || field.getType().equals(Integer.class) ||
                field.getType().equals(Long.class) || field.getType().equals(Float.class) ||
                field.getType().equals(Double.class) || field.getType().equals(Character.class) ||
                field.getType().equals(Boolean.class)) {
            stringBuilder.append(field.get(object).toString());
        }
        if (field.getType().equals(String.class) || field.getType().isEnum()) {
            stringBuilder.append("\"");
            stringBuilder.append(field.get(object).toString());
            stringBuilder.append("\"");
        }
        if (field.getType().equals(LocalDate.class) || field.getType().equals(LocalTime.class)
                || field.getType().equals(LocalDateTime.class)) {
            createDateTimeJSON(stringBuilder, field, object);
        }
        if (field.getType().equals(List.class)) {
            createListJSON(stringBuilder, field, object);
        }
        if (field.getType().equals(Set.class)) {
            createSetJSON(stringBuilder, field, object);
        }
        if (!Objects.isNull(field.getType().getAnnotation(Exported.class))) {
            Object classObject = field.get(object);
            List<Field> fields = getFields(classObject);
            stringBuilder.append(writeObjectToString(classObject, fields));
        }
    }

    /**
     * Проверка поддержания типа для сохранения List/Set.
     * @param value значение для проверки.
     * @return true, если тип не подходит, false - иначе.
     */
    private boolean isNotSupportedTypeForCollection(Object value) {
        if (Objects.isNull(value)) {
            return true;
        }
        if (value.getClass().isPrimitive() || value.getClass().equals(String.class)) {
            return false;
        }
        if (value.getClass().equals(Byte.class) || value.getClass().equals(Short.class) ||
                value.getClass().equals(Integer.class) || value.getClass().equals(Long.class) ||
                value.getClass().equals(Float.class) || value.getClass().equals(Double.class) ||
                value.getClass().equals(Character.class) || value.getClass().equals(Boolean.class)) {
            return false;
        }
        if (!Objects.isNull(value.getClass().getAnnotation(Exported.class))) {
            return false;
        }
        if (value.getClass().isEnum()) {
            return false;
        }
        if (value.getClass().equals(LocalDate.class) || value.getClass().equals(LocalTime.class) ||
                value.getClass().equals(LocalDateTime.class)) {
            return false;
        }
        return true;
    }

    /**
     * Сохранение List/Set типа LocalDate/LocalTime/LocalDateTime.
     * @param stringBuilder строка, в которую происходит сохранение.
     * @param field поле, которое необходимо сохранить.
     * @param object объект, поле которого сохраняется.
     */
    private void createCollectionDateTimeJSON(StringBuilder stringBuilder, Field field, Object object) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(field.getAnnotation(DateFormat.class).value());
        String date = "null";
        if (object.getClass().equals(LocalDate.class)) {
            date = dateTimeFormatter.format((LocalDate) object);
        } else if (object.getClass().equals(LocalTime.class)) {
            date = dateTimeFormatter.format((LocalTime) object);
        } else if (object.getClass().equals(LocalDateTime.class)) {
            date = dateTimeFormatter.format((LocalDateTime) object);
        }
        stringBuilder.append("\"");
        stringBuilder.append(date);
        stringBuilder.append("\"");
    }

    /**
     * Выбор метода для сохранения коллекции поля.
     * @param stringBuilder строка, в которую происходит сохранение.
     * @param field поле, которое необходимо сохранить.
     * @param object объект, поле которого сохраняется.
     */
    private void createCollectionObjectJSON(StringBuilder stringBuilder, Field field, Object object) {
        if (object.getClass().isPrimitive() || object.getClass().equals(Byte.class) ||
                object.getClass().equals(Short.class) || object.getClass().equals(Integer.class) ||
                object.getClass().equals(Long.class) || object.getClass().equals(Float.class) ||
                object.getClass().equals(Double.class) || object.getClass().equals(Character.class) ||
                object.getClass().equals(Boolean.class)) {
            stringBuilder.append(object);
        }
        if (object.getClass().equals(LocalDate.class) || object.getClass().equals(LocalTime.class) ||
                object.getClass().equals(LocalDateTime.class)) {
            if (Objects.isNull(field.getAnnotation(DateFormat.class))) {
                stringBuilder.append("\"");
                stringBuilder.append(object);
                stringBuilder.append("\"");
            } else {
                createCollectionDateTimeJSON(stringBuilder, field, object);
            }
        }
        if (object.getClass().equals(String.class) || object.getClass().isEnum()) {
            stringBuilder.append("\"");
            stringBuilder.append(object);
            stringBuilder.append("\"");
        }
        if (!Objects.isNull(object.getClass().getAnnotation(Exported.class))) {
            List<Field> fields = getFields(object);
            stringBuilder.append(writeObjectToString(object, fields));
        }
    }
}
