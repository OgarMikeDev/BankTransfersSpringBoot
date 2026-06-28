package org.example.repository;

import jakarta.persistence.LockModeType;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhoneNumber(Long phoneNumber);

    /*
    TODO
     Без блокировки обе транзакции успешно завершаются (потеря обновления), а с блокировкой – только одна
     Блокировка гарантирует корректность (одна транзакция выполняет списание, вторая падает).
     Без @Lock: Успешных: 2, Провалившихся: 0, Без блокировки, баланс: 400;
     С @Lock: Успешных: 1, Провалившихся: 1, Без блокировки, баланс: 400.
     Вторая транзакция должна падать, потому что она пытается списать деньги, которых уже нет.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM users u WHERE u.phoneNumber = :phone")
    User findByPhoneNumberForUpdate(@Param("phone") Long phoneNumber);
}
