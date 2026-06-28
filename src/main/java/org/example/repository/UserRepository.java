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
     Без @Lock (потеря обновления)
     Изначально: 1000
     Т1: читает 1000, списывает 600 → записывает 400
     Т2: читает 1000 (снимок), списывает 600 → записывает 400
     Итог: баланс 400, но списано 1200 (вместо 600).
     Если бы транзакции выполнялись последовательно, после Т1 осталось бы 400, а Т2 должна была упасть.
     Без блокировки система этого не заметила – потеряла 600 рублей.
     С @Lock (корректное поведение)
     Изначально: 1000
     Т1: захватывает блокировку, читает 1000, списывает 600 → записывает 400, коммит, отпускает блокировку.
     Т2: ждёт, после коммита Т1 читает актуальный баланс 400, проверка 400 >= 600 → false, бросает исключение, откат.
     Итог: баланс 400, списано 600 – ровно столько, сколько разрешено.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM users u WHERE u.phoneNumber = :phone")
    User findByPhoneNumberForUpdate(@Param("phone") Long phoneNumber);
}
