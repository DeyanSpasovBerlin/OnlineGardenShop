package finalproject.onlinegardenshop.controller;

import finalproject.onlinegardenshop.runner.TestDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-data")
public class TestDataController {
    private final TestDataGenerator testDataGenerator;

    @Autowired
    public TestDataController(TestDataGenerator testDataGenerator) {
        this.testDataGenerator = testDataGenerator;
    }

    @PostMapping("/generate-orders")
    public ResponseEntity<String> generateOrders() {
        testDataGenerator.generateOrders();
        return ResponseEntity.ok("Тестовите поръчки са създадени успешно.");
    }

}
