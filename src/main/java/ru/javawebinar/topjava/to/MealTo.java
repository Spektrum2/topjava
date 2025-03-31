package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import static org.hibernate.proxy.HibernateProxyHelper.getClassWithoutInitializingProxy;

public class MealTo {
    private final Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(@JsonProperty("id") Integer id,
                  @JsonProperty("dateTime") LocalDateTime dateTime,
                  @JsonProperty("description") String description,
                  @JsonProperty("calories") int calories,
                  @JsonProperty("excess") boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClassWithoutInitializingProxy(this) != getClassWithoutInitializingProxy(o)) return false;
        return getId() != null && getId().equals(((MealTo) o).getId());
    }

    @Override
    public final int hashCode() {
        return getClassWithoutInitializingProxy(this).hashCode();
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
