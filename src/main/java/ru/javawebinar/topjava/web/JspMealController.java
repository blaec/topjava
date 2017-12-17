package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping(path = "/meals")
    public String setMeals(Model model,  HttpServletRequest request) {
        String action = request.getParameter("action");
        int userId = AuthorizedUser.id();

        switch (action == null ? "all" : action) {
            case "delete":
                service.delete(getId(request), userId);
                return "redirect:meals";
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        service.get(getId(request), userId);
                request.setAttribute("meal", meal);
                model.addAttribute("meal", meal);
                return "mealForm";
            case "all":
            default:
                model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
                return "meals";
        }
    }

    @PostMapping(path = "/meals")
    public String filterMeals(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        int userId = AuthorizedUser.id();
        String returnOutput = "";

        if (action == null) {
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            if (request.getParameter("id").isEmpty()) {
                service.create(meal, userId);
            } else {
                meal.setId(getId(request));
                service.update(meal, userId);
            }
            returnOutput = "redirect:meals";

        } else if ("filter".equals(action)) {
//            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
//            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
//            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
//            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
//            request.setAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
//            request.getRequestDispatcher("/meals.jsp").forward(request, response);

            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            assert startDate != null;
            assert startTime != null;
            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            assert endDate != null;
            assert endTime != null;
            LocalDateTime end = LocalDateTime.of(endDate, endTime);
            request.setAttribute("meals", service.getBetweenDateTimes(start, end, userId));
            returnOutput = "meals";
        }
        return returnOutput;
    }


    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
