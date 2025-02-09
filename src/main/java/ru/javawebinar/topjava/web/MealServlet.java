package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.JavaMealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final JavaMealRepository mealRepo = new JavaMealRepository();
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("list".equals(action)) {
            log.debug("redirect to meals");
            req.setAttribute("meals", mealRepo.findAll());
            req.getRequestDispatcher("./meals.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            log.debug("update");
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal= mealRepo.findById(id).orElse(null);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("./mealForm.jsp").forward(req, resp);
        } else if ("delete".equals(action)) {
            log.debug("delete meal");
            int id = Integer.parseInt(req.getParameter("id"));
            mealRepo.deleteById(id);
            resp.sendRedirect("meals?action=list");
        } else if ("new".equals(action)) {
            req.getRequestDispatcher("./mealForm.jsp").forward(req, resp);
        } else {
            req.setAttribute("meals", mealRepo.findAll());
            req.getRequestDispatcher("./meals.jsp").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (id == null || id.isEmpty()) {
            log.debug("add new meal");
            mealRepo.save(new Meal(date, description, calories));
        } else {
            log.debug("update meal");
            int mealId = Integer.parseInt(id);
            Meal updatedMeal = new Meal(date, description, calories);
            updatedMeal.setId(mealId);
            mealRepo.update(updatedMeal);
        }

        resp.sendRedirect("meals?action=list");
    }
}
