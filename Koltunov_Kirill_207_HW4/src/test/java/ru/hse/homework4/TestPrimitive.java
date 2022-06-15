package ru.hse.homework4;

@Exported(nullHandling = NullHandling.INCLUDE)
@SuppressWarnings("All")
public class TestPrimitive {
    private int fieldInt;
    private Integer fieldWrapperInteger;
    private short fieldShort;
    private Short fieldWrapperShort;
    private long fieldLong;
    private Long fieldWrapperLong;
    private byte fieldByte;
    private Byte fieldWrapperByte;
    private float fieldFloat;
    private Float fieldWrapperFloat;
    private double fieldDouble;
    private Double fieldWrapperDouble;
    private char fieldChar;
    private Character fieldWrapperCharacter;
    private boolean fieldBoolean;
    private Boolean fieldWrapperBoolean;
    private String string;

    public void initializeAllFields() {
        fieldInt = 1;
        fieldWrapperInteger = 2;
        fieldShort = 3;
        fieldWrapperShort = 4;
        fieldLong = 5L;
        fieldWrapperLong = 6L;
        fieldByte = 7;
        fieldWrapperByte = 8;
        fieldFloat = (float) 9.2;
        fieldWrapperFloat = (float) 10.5;
        fieldDouble = 11.23456789;
        fieldWrapperDouble = 12.1234567890;
        fieldChar = 'A';
        fieldWrapperCharacter = 'B';
        fieldBoolean = true;
        fieldWrapperBoolean = true;
        string = "This is string!";
    }
}
