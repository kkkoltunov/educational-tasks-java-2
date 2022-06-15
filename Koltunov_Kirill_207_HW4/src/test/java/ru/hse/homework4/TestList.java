package ru.hse.homework4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Exported(nullHandling = NullHandling.INCLUDE)
@SuppressWarnings("All")
public class TestList {
    private List<Integer> fieldWrapperInteger;
    private List<Short> fieldWrapperShort;
    private List<Long> fieldWrapperLong;
    private List<Byte> fieldWrapperByte;
    private List<Float> fieldWrapperFloat;
    private List<Double> fieldWrapperDouble;
    private List<Character> fieldWrapperCharacter;
    private List<Boolean> fieldWrapperBoolean;
    private List<String> string;

    public void initializeAllFields() {
        fieldWrapperInteger = new ArrayList<>(List.of(-4, -3, -2, -1, 0, 1, 2, 3, 4));
        fieldWrapperShort = new ArrayList<>(List.of((short) 1, (short) 2, (short) 3, (short) 4));
        fieldWrapperLong = new ArrayList<>(List.of((long) 1));
        fieldWrapperByte = new ArrayList<>(List.of((byte) 1));
        fieldWrapperFloat = new ArrayList<>(List.of((float) -1.234567));
        fieldWrapperDouble = new ArrayList<>(List.of(1.2345678987654321, 2.0));
        fieldWrapperCharacter = new ArrayList<>(List.of('1', '2', 'A', 'B'));
        fieldWrapperBoolean = new ArrayList<>(List.of(true, false, true));
        string = new ArrayList<>(List.of("it's first string", "it's second"));
    }
}
