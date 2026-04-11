package org.example.gateway.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteRoleService {

    public boolean isAllowed(String path, HttpMethod method, List<String> roles) {
        if (path == null || method == null || roles == null || roles.isEmpty()) {
            return false;
        }

        if (hasRole(roles, "ADMIN")) {
            return true;
        }

        // =========================
        // ADVENTURE SERVICE
        // =========================

        if (path.startsWith("/api/adventures")) {
            if (method == HttpMethod.GET) return hasAnyRole(roles, "GUEST", "GUIDE");
            return false;
        }

        if (path.startsWith("/api/reviews")) {
            if (method == HttpMethod.GET || method == HttpMethod.POST) {
                return hasAnyRole(roles, "GUEST", "GUIDE");
            }
            return false;
        }

        if (path.startsWith("/api/categories")) {
            return method == HttpMethod.GET && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        if (path.startsWith("/api/gear")) {
            if (method == HttpMethod.GET) return hasAnyRole(roles, "GUEST", "GUIDE");
            if (method == HttpMethod.POST || method == HttpMethod.PUT) return hasRole(roles, "GUIDE");
            return false;
        }

        if (path.matches("^/api/time-slots/\\d+/book$")) {
            return method == HttpMethod.POST && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        if (path.startsWith("/api/time-slots")) {
            if (method == HttpMethod.GET) return hasAnyRole(roles, "GUEST", "GUIDE");
            if (method == HttpMethod.POST || method == HttpMethod.PUT) return hasRole(roles, "GUIDE");
            return false;
        }

        // =========================
        // GUIDE SERVICE
        // =========================

        if (path.matches("^/api/guides/\\d+/(premium-adventures|total-revenue|is-overloaded)$")) {
            return hasRole(roles, "GUIDE");
        }

        if (path.matches("^/api/guides/\\d+/ready-check$")) {
            return hasRole(roles, "GUIDE");
        }

        if (path.matches("^/api/guides/\\d+/apply-bonus$")) {
            return false; // samo ADMIN prolazi gore
        }

        if (path.startsWith("/api/guides")) {
            return method == HttpMethod.GET && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        if (path.startsWith("/api/languages")) {
            return method == HttpMethod.GET && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        if (path.startsWith("/api/guide-languages")) {
            return method == HttpMethod.GET && hasRole(roles, "GUIDE");
        }

        if (path.startsWith("/api/certifications")) {
            return method == HttpMethod.GET && hasRole(roles, "GUIDE");
        }

        // =========================
        // REZERVACIJE SERVICE
        // =========================

        if (path.startsWith("/api/bookings")) {
            if (method == HttpMethod.GET) return hasAnyRole(roles, "GUEST", "GUIDE");
            if (method == HttpMethod.POST) return hasAnyRole(roles, "GUEST", "GUIDE");
            if (method == HttpMethod.PUT) return hasAnyRole(roles, "GUEST", "GUIDE");
            return false;
        }

        if (path.startsWith("/api/addon-items")) {
            if (method == HttpMethod.GET) return hasAnyRole(roles, "GUEST", "GUIDE");
            if (method == HttpMethod.POST || method == HttpMethod.PUT) return hasAnyRole(roles, "GUEST", "GUIDE");
            return false;
        }

        if (path.startsWith("/api/guests")) {
            if (method == HttpMethod.GET) return hasAnyRole(roles, "GUEST", "GUIDE");
            if (method == HttpMethod.POST || method == HttpMethod.PUT) return hasAnyRole(roles, "GUEST", "GUIDE");
            return false;
        }

        if (path.startsWith("/api/cancellation-policies")) {
            return method == HttpMethod.GET && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        if (path.startsWith("/api/booking-status-logs")) {
            return method == HttpMethod.GET && hasRole(roles, "GUIDE");
        }

        // =========================
        // FINANSIJE SERVICE
        // =========================

        if (path.startsWith("/api/invoices")) {
            if (method == HttpMethod.GET) return hasRole(roles, "GUIDE");
            return false;
        }

        if (path.startsWith("/api/transactions")) {
            if (method == HttpMethod.GET) return hasRole(roles, "GUIDE");
            if (method == HttpMethod.POST || method == HttpMethod.PUT) return hasRole(roles, "GUIDE");
            return false;
        }

        if (path.startsWith("/api/payment-methods")) {
            return method == HttpMethod.GET && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        if (path.startsWith("/api/taxrates")) {
            return method == HttpMethod.GET && hasAnyRole(roles, "GUEST", "GUIDE");
        }

        return false;
    }

    private boolean hasRole(List<String> roles, String expectedRole) {
        return roles.stream().anyMatch(role -> role.equalsIgnoreCase(expectedRole));
    }

    private boolean hasAnyRole(List<String> roles, String... expectedRoles) {
        for (String expectedRole : expectedRoles) {
            if (hasRole(roles, expectedRole)) {
                return true;
            }
        }
        return false;
    }
}