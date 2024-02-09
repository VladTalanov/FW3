package com.max;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    private final String user = "Toto";
    private final LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
    private final LocalDateTime to = LocalDateTime.of(2024, 1, 2, 0, 0, 0);

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceTest.class);

    @Spy
    private BookingService bookingService = new BookingService();


    @Test
    void successCreateBookTest() throws CantBookException {
        logger.info("тест успешного создания записи запущен");
        logger.debug("Создание заглушек для методов: createBook, checkTimeInBD");
        Mockito.when(bookingService.createBook(user, from, to)).thenReturn(true);
        Mockito.when(bookingService.checkTimeInBD(from, to)).thenReturn(true);
        logger.debug("Заглушки для методов: createBook, checkTimeInBD - созданы");

        assertTrue(bookingService.book(user, from, to));

        verify(bookingService).checkTimeInBD(from, to);
        verify(bookingService).createBook(user, from, to);
        verify(bookingService).book(user, from, to);

    }

    @Test
    void failCreateBookTest() throws CantBookException {
        logger.info("тест возврата Exception запущен");
        logger.debug("Создание заглушки для метода: checkTimeInBD");
        Mockito.when(bookingService.checkTimeInBD(any(), any())).thenReturn(false);
        logger.debug("Заглушка для метода: checkTimeInBD - создана");
        assertThrows(CantBookException.class, () -> bookingService.book(user, from, to));

        verify(bookingService).checkTimeInBD(from, to);
        verify(bookingService).book(user, from, to);
        verify(bookingService, never()).createBook(any(), any(), any());
    }
}
