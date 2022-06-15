package ru.hse.homework4;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Exported(nullHandling = NullHandling.INCLUDE)
@SuppressWarnings("All")
public class TestSet {
    private Set<Integer> fieldWrapperInteger;
    private Set<Short> fieldWrapperShort;
    private Set<Long> fieldWrapperLong;
    private Set<Byte> fieldWrapperByte;
    private Set<Float> fieldWrapperFloat;
    private Set<Double> fieldWrapperDouble;
    private Set<Character> fieldWrapperCharacter;
    private Set<Boolean> fieldWrapperBoolean;
    private Set<String> string;

    public void initializeAllFields() {
        fieldWrapperInteger = new HashSet<>(List.of(-4, -3, -2, -1, 0, 1, 2, 3, 4));
        fieldWrapperShort = new HashSet<>(List.of((short) 1, (short) 2, (short) 3, (short) 4));
        fieldWrapperLong = new HashSet<>(List.of((long) 1));
        fieldWrapperByte = new HashSet<>(List.of((byte) 1));
        fieldWrapperFloat = new HashSet<>(List.of((float) -1.234567));
        fieldWrapperDouble = new HashSet<>(List.of(1.2345678987654321, 2.0));
        fieldWrapperCharacter = new HashSet<>(List.of('1', '2', 'A', 'B'));
        fieldWrapperBoolean = new HashSet<>(List.of(true, false, true));
        string = new HashSet<>(List.of("it's first string", "it's second"));
    }
}
