
# 📘 Стъпки за демонстрация на дипломния проект (Cloud + Docker)

Този файл описва логиката и стъпките, използвани за временното изключване на автоматичните времеви отметки (`@CreationTimestamp`, `@UpdateTimestamp`) с цел създаване на реалистични тестови поръчки, както и тяхното последващо възстановяване за продукционен режим.

---

## 🧪 1. Деактивиране на автоматичните времеви анотации

```java
// @CreationTimestamp
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

// @UpdateTimestamp
private LocalDateTime updatedAt;
```

**Причина:** Нужно е създаване на тестови поръчки със случайни дати назад до 180 дни, за да се демонстрира коректна работа на статистическите контролери.

---

## 🧰 2. Генериране на тестови данни

- Използван е endpoint-ът `/test-data/generate-orders`.
- Поръчките са със статус `CREATED`, съдържат реалистични `OrderItems` и `totalPrice`.
- Датите са случайни в рамките на последните 180 дни.

```java
LocalDateTime randomDate = LocalDateTime.now()
    .minusDays(random.nextInt(180))
    .minusHours(random.nextInt(24))
    .minusMinutes(random.nextInt(60));
```

---

## ✅ 3. Възстановяване на автоматичните анотации

След създаването на тестовите данни, автоматичните анотации се активират отново:

```java
@CreationTimestamp
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

@UpdateTimestamp
private LocalDateTime updatedAt;
```

---

## 🔄 4. Финален push към master

След извършване на горните стъпки:
- Създаден е commit със заглавие например:
  ```
  ✅ Restore auto timestamps after test data generation
  ```
- Извършен е push към master клона в GitHub.
- Проектът е готов за демонстрация в cloud среда (например GitHub Codespaces + Docker).

---

## 🧠 Забележка

Тези промени не са необходими в реална продукционна среда. Те служат **единствено за демонстрационни и тестови цели.**
