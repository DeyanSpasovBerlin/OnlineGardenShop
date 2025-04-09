package finalproject.onlinegardenshop.runner;


import finalproject.onlinegardenshop.entity.OrderItems;
import finalproject.onlinegardenshop.entity.Orders;
import finalproject.onlinegardenshop.entity.Products;
import finalproject.onlinegardenshop.entity.Users;
import finalproject.onlinegardenshop.entity.enums.DeliveryMethod;
import finalproject.onlinegardenshop.entity.enums.OrdersStatus;
import finalproject.onlinegardenshop.repository.OrderItemsRepository;
import finalproject.onlinegardenshop.repository.OrdersRepository;
import finalproject.onlinegardenshop.repository.ProductsRepository;
import finalproject.onlinegardenshop.repository.UsersRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class TestDataGenerator {//implements CommandLineRunner -
    // if we want to generate test data with every new run implement CommandLineRunner

    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final Random random = new Random();

    public TestDataGenerator(UsersRepository usersRepository,
                             ProductsRepository productsRepository,
                             OrdersRepository ordersRepository,
                             OrderItemsRepository orderItemsRepository) {
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
    }
//if we want to generate test data with every new run
//    @Override
//    public void run(String... args) {
//        generateOrders();
//    }

    public void generateOrders() {
        List<Users> users = usersRepository.findAll();
        List<Products> products = productsRepository.findAll();

        if (users.isEmpty() || products.isEmpty()) {
            System.out.println("⚠ Не са намерени потребители или продукти!");
            return;
        }

        for (int i = 0; i < 10; i++) {
            Users user = users.get(random.nextInt(users.size()));
            Orders order = new Orders();
            order.setUsers(user);
            order.setStatus(OrdersStatus.CREATED); // Важно
            order.setDeliveryAddress("Görlitzer Str. 13, 10997 Berlin");
            order.setContactPhone("+49901123456");
            order.setDeliveryMethod(DeliveryMethod.COURIER_DELIVERY);
            order.setEmailSent(true); // или false, ако искаш да тестваш изпращане
            LocalDateTime randomDate = getRandomDateWithinLast180Days();
            order.setCreatedAt(randomDate);
            order.setUpdatedAt(randomDate);
            double totalPrice = 0.0;
            int itemsCount = random.nextInt(3) + 1;
            for (int j = 0; j < itemsCount; j++) {
                Products product = products.get(random.nextInt(products.size()));
                int quantity = random.nextInt(3) + 1;
                OrderItems item = new OrderItems();
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setPriceAtPurchase(product.getPrice());
                item.setOrder(order); // важно!
                totalPrice += quantity * product.getPrice();
                order.getOrderItems().add(item);
            }

            order.setTotalPrice(totalPrice);
            ordersRepository.save(order); // автоматично ще запише и orderItems, защото CascadeType.ALL
        }

        System.out.println("✅ Генерирани са тестови поръчки със статус CREATED за последните 180 дни.");
    }

    private LocalDateTime getRandomDateWithinLast180Days() {
        long daysBack = random.nextInt(180);
        long hoursBack = random.nextInt(24);
        long minutesBack = random.nextInt(60);
        return LocalDateTime.now().minusDays(daysBack).minusHours(hoursBack).minusMinutes(minutesBack);
    }



}
