package ru.javawebinar.topjava.webcontroller;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private MealDao dao;

    public MealServlet() {
        super();
        dao = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");
        response.setContentType("text/html;charset=utf-8");
        request.setAttribute("name", "Подсчет калорий");
        request.setAttribute("dtf", MealDao.dtf);

        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("meals", dao.getAllMeals());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                log.debug("delete Meal № " + id);
                dao.deleteMeal(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                //     meal = meals.get(Integer.parseInt(id));
                break;
            case "add":
                RequestDispatcher view = request.getRequestDispatcher("/oneMeal.jsp");
                view.forward(request, response);

                return;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        Integer calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(id, dateTime, description, calories);
        log.debug("save Meal: " + meal);
        dao.save(meal);
        response.sendRedirect("meals");
    }


}
