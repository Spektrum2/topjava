<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Create meal</title>
    <link rel="stylesheet" href="styleForm.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Create Meal</h2>
<form action="meals"  method="post">
    <input type="hidden" name="id" value="${meal.id}">
    <label for="date">DateTime:</label>
    <input type="datetime-local" class="date" id="date" name="date" value="${meal.dateTime}" required>
    <br>
    <label for="description">Description:</label>
    <input type="text" size="30" class="description" id="description" name="description" value="${meal.description}" required>
    <br>
    <label for="calories">Calories:</label>
    <input type="text" class="calories" id="calories" name="calories" value="${meal.calories}" required>
    <br>
    <button type="submit">Save</button>
    <button onclick="window.location.href='meals?action=list'">Cancel</button>
</form>
</body>
</html>
