package com.kulik.airbnb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/booking")
public class BookingController {

    @PostMapping
    ResponseEntity<?> createNewBook() {
        return null;
    }

    @GetMapping("/history")
    ResponseEntity<?> getBookingHistory(@RequestParam(value = "limit", required = false, defaultValue = "100")
                                                int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0")
                                                int offset) {
        return null;
    }

    @DeleteMapping
    ResponseEntity<?> deleteBooking(@RequestParam(value = "booking_id") int id) {
        return null;
    }
}
