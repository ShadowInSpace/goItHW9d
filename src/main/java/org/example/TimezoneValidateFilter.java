package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.TimeZone;

@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {
    protected void doFilter(HttpServletRequest req,
                            HttpServletResponse resp,
                            FilterChain chain) throws ServletException, IOException {

        String timezone = req.getHeader("timezone");
        String[] tzIDs = TimeZone.getAvailableIDs();
        boolean isValid = false;
        if (timezone != null) {
            for (String id : tzIDs) {
                if (timezone.equals(id)) {
                    isValid = true;
                    break;
                }
            }
        } else {
            isValid = true;
        }

        if (isValid) {
            chain.doFilter(req, resp);
        } else {
            resp.setStatus(400);

            resp.setContentType("text/html; charset=utf-8");
            resp.getWriter().write("Invalid timezone");
            resp.getWriter().close();
        }
    }
}
