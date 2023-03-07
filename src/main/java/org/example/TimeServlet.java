package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");

        Date now = new Date();
        SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss zz");
        String timeZoneID = req.getParameter("timezone");
        TimeZone timeZone;
        if (timeZoneID == null) {
            timeZone = TimeZone.getTimeZone("UTC");
        } else {
            timeZone = TimeZone.getTimeZone(timeZoneID);
        }
        FORMATTER.setTimeZone(timeZone);

        resp.getWriter().write((FORMATTER.format(now)));

        resp.getWriter().close();

    }
}
