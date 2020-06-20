package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private InMemoryMealRepository dao;

    public MealServlet() {
        dao = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");
        response.setContentType("text/html;charset=utf-8");
        request.setAttribute("dtf", MealsUtil.DATE_TIME_FORMATTER);

        String action = request.getParameter("action");

        int id = 0;
        switch (action == null ? "all" : action) {
            case "delete":
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("delete Meal № " + id);
                dao.delete(id);
                response.sendRedirect("meals");
                return;
            case "update":
                id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("meal", dao.getMeal(id));
            case "create":
                if (id == 0) {
                    log.debug("create new meal");
                } else {
                    log.debug("update meal № " + id);
                }
                RequestDispatcher view = request.getRequestDispatcher("/oneMeal.jsp");
                view.forward(request, response);
            case "all":
            default:
                log.debug("get all meals");
                request.setAttribute("meals",
                        MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String idString = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal;
        if (idString == null || idString.equals("")) {
            meal = new Meal(dateTime, description, calories);
        } else {
            Integer id = Integer.parseInt(idString);
            meal = new Meal(id, dateTime, description, calories);
        }

        log.debug("save Meal: " + meal);
        dao.save(meal);
        response.sendRedirect("meals");
    }
}
