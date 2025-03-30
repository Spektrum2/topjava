## Meal REST API Testing

### 1. Получить все meals пользователя
```bash
curl -s "http://localhost:8080/topjava/rest/user/meals"
```

### 2. Фильтрация meals по датам/времени
#### Только по датам
```bash
curl -s "http://localhost:8080/topjava/rest/user/meals/filter?startDate=2020-01-30&endDate=2020-01-31"
```
#### По датам и времени
```bash
curl -s "http://localhost:8080/topjava/rest/user/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=10:00&endTime=13:00"
```

### 3. Получить конкретный meal
```bash
curl -s "http://localhost:8080/topjava/rest/user/meals/100003"
```

### 4. Создать новый meal
```bash
curl -X POST "http://localhost:8080/topjava/rest/user/meals" \
  -H "Content-Type: application/json" \
  -d '{"dateTime":"2023-05-15T12:00:00","description":"New meal","calories":500}'
```

### 5. Обновить meal
```bash
curl -X PUT "http://localhost:8080/topjava/rest/user/meals/100003" \
  -H "Content-Type: application/json" \
  -d '{"dateTime":"2020-01-30T10:00:00","description":"Updated meal","calories":400}'
```

### 6. Удалить meal
```bash
curl -X DELETE "http://localhost:8080/topjava/rest/user/meals/100003"
```