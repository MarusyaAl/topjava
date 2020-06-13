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
            request.setAttribute("meals", dao.getAllMeals());  //MealsUtil.getMealsTo()
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteMeal(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                //     meal = meals.get(Integer.parseInt(id));
                break;
            default:
                throw new IllegalArgumentException("Action" + action + "is illegal");
        }
        //   request.setAttribute("meal", getMealsTo());
        //   request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
