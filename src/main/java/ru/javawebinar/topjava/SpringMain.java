package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.meal.MealRestController;

public class SpringMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        MealRestController mealRestController = appCtx.getBean(MealRestController.class);

        System.out.println("All meals for the current user:");
        mealRestController.getAll().forEach(System.out::println);
    }
}
