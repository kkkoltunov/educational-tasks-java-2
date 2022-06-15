package ru.hse.homework4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMapperTest {
    @Test
    void testPrimitiveTypes() {
        TestPrimitive testPrimitive = new TestPrimitive();
        testPrimitive.initializeAllFields();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(testPrimitive);
        TestPrimitive newTestPrimitive = mapper.readFromString(TestPrimitive.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestPrimitive));
    }

    @Test
    void testPrimitiveTypesWithoutInitialize() {
        TestPrimitive testPrimitive = new TestPrimitive();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(testPrimitive);
        TestPrimitive newTestPrimitive = mapper.readFromString(TestPrimitive.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestPrimitive));
    }

    @Test
    void testList() {
        TestList testLists = new TestList();
        testLists.initializeAllFields();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(testLists);
        TestList newTestLists = mapper.readFromString(TestList.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestLists));
    }

    @Test
    void testListWithoutInitialize() {
        TestList testLists = new TestList();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(testLists);
        TestList newTestLists = mapper.readFromString(TestList.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestLists));
    }

    @Test
    void testSet() {
        TestSet testSet = new TestSet();
        testSet.initializeAllFields();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(testSet);
        TestSet newTestSet = mapper.readFromString(TestSet.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestSet));
    }

    @Test
    void testSetWithoutInitialize() {
        TestSet testSet = new TestSet();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(testSet);
        TestSet newTestSet = mapper.readFromString(TestSet.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestSet));
    }

    @Test
    void testAllTypes1() {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setComment("comment");
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(reviewComment);
        ReviewComment newReviewComment = mapper.readFromString(ReviewComment.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newReviewComment));
    }

    @Test
    void testAllTypes2() {
        BookingForm bookingForm = new BookingForm();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(bookingForm);
        BookingForm newBookingForm = mapper.readFromString(BookingForm.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newBookingForm));
    }

    @Test
    void testRecord() {
        NewClass newClass = new NewClass();
        Mapper mapper = new DefaultMapper();
        String stringOfObject = mapper.writeToString(newClass);
        NewClass newTestRecord2 = mapper.readFromString(NewClass.class, stringOfObject);
        System.out.println(stringOfObject);
        assertEquals(stringOfObject, mapper.writeToString(newTestRecord2));
    }
}