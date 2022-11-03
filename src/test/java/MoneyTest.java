import com.nhnacademy.gwjs.service.Bank;
import com.nhnacademy.gwjs.entity.Currency;
import com.nhnacademy.gwjs.entity.Money;
import com.nhnacademy.gwjs.exception.MintNegativeValueMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class MoneyTest {
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    @DisplayName("원화 생성 성공")
    void mintMoney1_success() {
        Money money = bank.createMoney(1000, Currency.WON);
        assertThat(money).isNotNull();
        assertThat(money.getAmount()).isEqualTo(1000);
    }

    @Test
    @DisplayName("달러 생성 성공")
    void mintMoney2_success() {
        Money money = bank.createMoney(1.5, Currency.DOLLAR);
        assertThat(money).isNotNull();
        assertThat(money.getAmount()).isEqualTo(1.5);
    }


    @Test
    @DisplayName("원화 생성 실패 - 음수 입력")
    void mintMoney1_mintNegativeValueMoneyException() {
        double amount = -1000;

        assertThatThrownBy(() -> bank.createMoney(amount, Currency.WON))
                .isInstanceOf(MintNegativeValueMoneyException.class)
                .hasMessageContaining("Amount must be positive number.", amount);
    }

    @Test
    @DisplayName("달러 생성 실패 - 음수 입력")
    void mintMoney2_mintNegativeValueMoneyException() {
        double amount = -1.5;

        assertThatThrownBy(() -> bank.createMoney(amount, Currency.DOLLAR))
                .isInstanceOf(MintNegativeValueMoneyException.class)
                .hasMessageContaining("Amount must be positive number.", amount);
    }

    @Test
    @DisplayName("equals 비교 실패 - 통화 일치, 금액 불일치")
    void currency_match_amount_mismatch() {
        Money money1 = bank.createMoney(2000, Currency.WON);
        Money money2 = bank.createMoney(1000, Currency.WON);

        boolean equals = money1.equals(money2);

        assertThat(equals).isFalse();

    }

    @Test
    @DisplayName("equals 비교 실패 - 통화 불일치, 금액 일치")
    void currency_mismatch_amount_match() {
        Money money1 = bank.createMoney(2000, Currency.WON);
        Money money2 = bank.createMoney(2000, Currency.DOLLAR);

        boolean equals = money1.equals(money2);

        assertThat(equals).isFalse();

    }

    @Test
    @DisplayName("equals 비교 실패 - 통화 불일치, 금액 불일치")
    void currency_mismatch_amount_mismatch() {
        Money money1 = bank.createMoney(2000, Currency.WON);
        Money money2 = bank.createMoney(1000, Currency.DOLLAR);

        boolean equals = money1.equals(money2);

        assertThat(equals).isFalse();

    }


    @Test
    @DisplayName("equals 비교 성공 - 통화 일치, 금액 일치")
    void currency_match_amount_match() {
        Money money1 = bank.createMoney(1.1, Currency.DOLLAR);
        Money money2 = bank.createMoney(1.1, Currency.DOLLAR);

        boolean equals = money1.equals(money2);

        assertThat(equals).isTrue();
    }


}
