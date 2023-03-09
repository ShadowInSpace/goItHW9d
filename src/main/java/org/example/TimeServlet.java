package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Date date = new Date();
        SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss zz");
        Cookie timeZone = new Cookie("lastTimezone", "UTC");

        Cookie[] cookies = req.getCookies();
        String timeZoneID = req.getParameter("timezone");
        if (timeZoneID != null) {
            timeZone.setValue(timeZoneID);
        } else if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastTimezone")) {
                    timeZone.setValue(cookie.getValue());
                }
            }
        }

        FORMATTER.setTimeZone(TimeZone.getTimeZone(timeZone.getValue()));
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("date", FORMATTER.format(date))
        );
        resp.addCookie(timeZone);
        engine.process("timezone", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
