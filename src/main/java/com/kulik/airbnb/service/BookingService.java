package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.impl.BookingDao;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.model.Booking;
import com.kulik.airbnb.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private static final int LIMIT = 100;

    private final BookingDao bookingDao;
    private final UserDao userDao;

    public BookingService(BookingDao bookingDao, UserDao userDao) {
        this.bookingDao = bookingDao;
        this.userDao = userDao;
    }

    public void createBooking(Booking booking) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userDao.getByEmail(authenticatedUserEmail);
        booking.setUserId(authenticatedUser.getId());
        bookingDao.insertBooking(booking);
    }

    public List<Booking> getUserBookingHistory(int limit, int offset) {
        if (limit > LIMIT) {
            limit = LIMIT;
        }

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userDao.getByEmail(authenticatedUserEmail);

        return bookingDao.getUserBookingsPage(Math.toIntExact(authenticatedUser.getId()), limit, offset);
    }

    public List<Booking> getProductBookingHistory(int id, int limit, int offset) {
        if (limit > LIMIT) {
            limit = LIMIT;
        }

        return bookingDao.getProductBookingsPage(id, limit, offset);
    }

    public void deleteBooking(int bookingId) throws Exception {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userDao.getByEmail(authenticatedUserEmail);

        Booking booking = bookingDao.getBookingById(bookingId);

        if (authenticatedUser.getId().equals(booking.getUserId())) {
            bookingDao.deleteBookingById(bookingId);
        } else {
            throw new Exception("Cannot delete foreign booking.");
        }
    }
}
