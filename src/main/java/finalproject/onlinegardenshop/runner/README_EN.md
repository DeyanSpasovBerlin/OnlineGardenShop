# 📘 Steps for Demonstrating the Diploma Project (Cloud + Docker)

[🇧🇬 View in Bulgarian](README_BG.md) | [🇬🇧 View in English](README_EN.md)

This document outlines the logic and steps used to temporarily disable automatic timestamp annotations (`@CreationTimestamp`, `@UpdateTimestamp`) in order to create realistic test orders, as well as how to re-enable them afterward for production mode.

---

## 🧪 1. Disabling Automatic Timestamp Annotations

```java
// @CreationTimestamp
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

// @UpdateTimestamp
private LocalDateTime updatedAt;
```

**Reason:** It is necessary to create test orders with random dates going back up to 180 days in order to demonstrate the correct functionality of the statistical controllers.

---

## 🧰 2. Generating Test Data

- The endpoint `/test-data/generate-orders` was used.
- The orders have the status `CREATED`, contain realistic `OrderItems`, and include a calculated `totalPrice`.
- The dates are randomly distributed within the last 180 days.

```java
LocalDateTime randomDate = LocalDateTime.now()
    .minusDays(random.nextInt(180))
    .minusHours(random.nextInt(24))
    .minusMinutes(random.nextInt(60));
```
