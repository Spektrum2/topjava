package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.Repository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private Repository repository;
    private static final Logger log = getLogger(MealServlet.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new MealRepository();
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        repository.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "meals" : action) {
            case "edit":
                log.debug("redirect to editMeal");
                Meal meal = repository.findById(Integer.parseInt(req.getParameter("id")));
                req.setAttribute("meal", meal);
                req.setAttribute("form", true);
                req.getRequestDispatcher("./mealForm.jsp").forward(req, resp);
                break;
            case "delete":
                log.debug("delete meal");
                repository.deleteById(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect("meals");
                break;
            case "new":
                log.debug("redirect to addMeal");
                req.setAttribute("form", false);
                req.setAttribute("date", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                req.getRequestDispatcher("./mealForm.jsp").forward(req, resp);
                break;
            case "meals":
            default:
                log.debug("redirect to meals");
                req.setAttribute("meals", filteredByStreams(repository.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
                req.getRequestDispatcher("./meals.jsp").forward(req, resp);
                break;
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (id == null || id.isEmpty()) {
            log.debug("add new meal");
            repository.create(new Meal(null, date, description, calories));
        } else {
            log.debug("update meal");
            repository.update(new Meal(Integer.parseInt(id), date, description, calories));
        }

        resp.sendRedirect("meals");
    }
}
