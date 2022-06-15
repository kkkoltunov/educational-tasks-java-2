package ru.hse.homework4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Exported(nullHandling = NullHandling.INCLUDE)
public class ReviewComment {
    @PropertyName("Pochtiguest")
    Guest guest = new Guest();

    private String comment;
    int asf = 1111;
    private String author;
    private Boolean fdsdf = null;
    private boolean resolved;
    private Character aChar;

    @DateFormat("uuuu-MMMM-dd HH:mm:ss")
    private LocalDateTime localDateTime;

    private List<Integer> list;
    private List<Exported> guestList;
    private List<String> liset;
    private Set<Integer> set = new HashSet<>(List.of(1, 2, 3));
    private LocalDateTime localDateTime1 = LocalDateTime.now();
    private LocalDate localDate = LocalDate.MAX;
    private LocalTime localTime = LocalTime.MIDNIGHT;

    @DateFormat("uuuu-MMMM-dd HH:mm:ss")
    List<LocalDateTime> localDateTimes;

    void setComment(String comment) {
        this.comment = comment;
        list = new ArrayList<>(List.of(1222222,2,3,4,5));
        liset = new ArrayList<>(List.of("1211111111111", "!11111111112", "124e"));
        localDateTimes = new ArrayList<>(List.of(LocalDateTime.MIN, LocalDateTime.MAX));
        localDateTime1 = LocalDateTime.now();
    }

    void setAuthor(String author) {
        this.author = author;
    }

    void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}

