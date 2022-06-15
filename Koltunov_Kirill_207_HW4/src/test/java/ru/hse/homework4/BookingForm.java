package ru.hse.homework4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Exported(nullHandling = NullHandling.INCLUDE, unknownPropertiesPolicy = UnknownPropertiesPolicy.IGNORE)
@SuppressWarnings("All")
public class BookingForm {
    Integer g = 1;
    @PropertyName("i")
    Boolean b = true;
    char c = 'h';
    int anInt = 131313;
    Character character = 's';
    String string = "sf123f";
    Guest guest = new Guest();
    TestRecord1 test = new TestRecord1("NullHandling.INCLUDE");

    List<Integer> integers = new ArrayList<Integer>(List.of(1, 2, 34));
    List<Double> doubles = new ArrayList<Double>(List.of(1313.13, 13.11111, 1.111111));
    private List<NullHandling> nullHandlings = new ArrayList<>(List.of(NullHandling.INCLUDE));
    private List<TestRecord1> tests = new ArrayList<>(List.of(new TestRecord1("abc"), new TestRecord1("1234567")));

    private Set<Guest> guests = new HashSet<>(List.of(new Guest(), new Guest()));
    Set<Integer> integersset = new HashSet<>(List.of(1, 2, 34));
    Set<Double> doubl2es = new HashSet<>(List.of(1313.13, 13.11111, 1.111111));
    private Set<NullHandling> nullHandlings1 = new HashSet<>(List.of(NullHandling.INCLUDE, NullHandling.EXCLUDE));
    private Set<TestRecord1> tests1 = new HashSet<>(List.of(new TestRecord1("abc"), new TestRecord1("1234567")));

    private NullHandling nullHandling = NullHandling.INCLUDE;
    private UnknownPropertiesPolicy unknownPropertiesPolicy = UnknownPropertiesPolicy.FAIL;

    @DateFormat("uuuu-MMMM-dd HH:mm:ss")
    private LocalDateTime localDateTime = LocalDateTime.MAX;
    @DateFormat("uuuu-MMMM-dd")
    private LocalDate localDate = LocalDate.MAX;
    @DateFormat("HH:mm:ss")
    private LocalTime localTime = LocalTime.MAX;
    @DateFormat("uuuu-MMMM-dd HH:mm:ss")
    List<LocalDateTime> localDateTimes = new ArrayList<>(List.of(LocalDateTime.now(), LocalDateTime.MAX));
}
