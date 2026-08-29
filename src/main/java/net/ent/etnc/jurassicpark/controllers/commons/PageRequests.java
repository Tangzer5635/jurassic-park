package net.ent.etnc.jurassicpark.controllers.commons;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public final class PageRequests {

    private PageRequests() {
    }

    public static PageRequest of(Integer page, Integer size, String sort) {
        List<Sort.Order> orders = new ArrayList<>();

        for (String sortParam : sort.split(",")) {
            String[] parts = sortParam.split(":");
            String property = parts[0].trim();

            if (parts.length > 1) {
                orders.add(new Sort.Order(Sort.Direction.fromString(parts[1].trim()), property));
            } else {
                orders.add(Sort.Order.asc(property));
            }
        }

        return PageRequest.of(page - 1, size, Sort.by(orders));
    }
}