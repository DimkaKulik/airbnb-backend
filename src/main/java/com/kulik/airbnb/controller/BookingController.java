package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.Booking;
import com.kulik.airbnb.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    ResponseEntity<?> createNewBook(@RequestBody Booking booking) {
        //try {
            bookingService.createBooking(booking);
            return ResponseEntity.ok("You have successfully booked your hut.");
        //} catch (Exception e) {
         //   return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        //}
    }

    @GetMapping("/history")
    ResponseEntity<?> getBookingHistory(@RequestParam(value = "limit", required = false, defaultValue = "100")
                                                int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0")
                                                int offset) {
        try {
            List<Booking> history = bookingService.getBookingHistory(limit, offset);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    ResponseEntity<?> deleteBooking(@RequestParam(value = "booking_id") int id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.ok("Booking deleted.");
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete booking", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
