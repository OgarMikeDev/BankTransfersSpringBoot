package org.example;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(classes = Main.class)
public class TestTransactions {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private long fromPhone = 111L;
    private long toPhone   = 222L;
    private int  amount    = 600;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User from = new User();
        from.setPhoneNumber(fromPhone);
        from.setName("Алексей");
        from.setSum(1000);
        from.setBlock(false);
        userRepository.save(from);

        User to = new User();
        to.setPhoneNumber(toPhone);
        to.setName("Борис");
        to.setSum(0);
        to.setBlock(false);
        userRepository.save(to);
    }

    // ============= ТЕСТ БЕЗ БЛОКИРОВКИ (с READ_UNCOMMITTED) =============
    @Test
    void testWithoutLock_ShouldResultNegativeBalance() throws InterruptedException {
        // Временно закомментируйте @Lock в репозитории для этого теста
        runConcurrentTransfers(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);

        User fromAfter = userRepository.findByPhoneNumber(fromPhone);
        System.out.println("Без блокировки, баланс: " + fromAfter.getSum());
        // Ожидаем -200 (обе транзакции списали)
        Assertions.assertEquals(-200, fromAfter.getSum(), "Баланс должен быть -200");
    }

    // ============= ТЕСТ С БЛОКИРОВКОЙ (обычный уровень) =============
    @Test
    void testWithLock_ShouldPreventNegativeBalance() throws InterruptedException {
        // Убедитесь, что @Lock в репозитории РАСКОММЕНТИРОВАН
        runConcurrentTransfers(TransactionDefinition.ISOLATION_REPEATABLE_READ);

        User fromAfter = userRepository.findByPhoneNumber(fromPhone);
        System.out.println("С блокировкой, баланс: " + fromAfter.getSum());
        Assertions.assertEquals(400, fromAfter.getSum(), "Баланс должен быть 400");
    }

    // Общий метод для запуска двух параллельных транзакций с заданным уровнем изоляции
    private void runConcurrentTransfers(int isolationLevel) throws InterruptedException {
        int threadCount = 2;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();

                    TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
                    txTemplate.setIsolationLevel(isolationLevel); // Устанавливаем уровень изоляции
                    txTemplate.execute(new TransactionCallbackWithoutResult() {
                        @Override
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            User from = userRepository.findByPhoneNumberForUpdate(fromPhone);
                            if (from.getSum() < amount) {
                                throw new RuntimeException("Недостаточно средств");
                            }
                            User to = userRepository.findByPhoneNumber(toPhone);
                            from.setSum(from.getSum() - amount);
                            to.setSum(to.getSum() + amount);
                            userRepository.save(from);
                            userRepository.save(to);
                        }
                    });

                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await();

        System.out.println("Успешных: " + successCount.get() + ", Провалившихся: " + failCount.get());
    }
}