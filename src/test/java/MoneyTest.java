import com.nhnacademy.gwjs.service.Bank;
import com.nhnacademy.gwjs.entity.Currency;
import com.nhnacademy.gwjs.entity.Money;
import com.nhnacademy.gwjs.exception.MintNegativeValueMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


public class MoneyTest {
    //SUT
    Bank bank;

    @BeforeEach
    void setUp(){
        bank = new Bank();
    }


    @Test
    @DisplayName("원화 생성 성공")
    void mintMoney1_success(){
        Money money = bank.mintMoney(1000, Currency.WON);
        assertThat(money).isNotNull();
        assertThat(money.getAmount()).isEqualTo(1000);
    }

    @Test
    @DisplayName("달러 생성 성공")
    void mintMoney2_success(){
        Money money = bank.mintMoney(1.5, Currency.DOLLAR);
        assertThat(money).isNotNull();
        assertThat(money.getAmount()).isEqualTo(1.5);
    }

    //음수를 입력하여 돈 생성 실패
    @Test
    @DisplayName("원화 생성 실패 - 음수 입력")
    void mintMoney1_fail(){
        double amount = -1000;

        assertThatThrownBy(()->bank.mintMoney(amount, Currency.WON))
                .isInstanceOf(MintNegativeValueMoneyException.class)
                .hasMessageContaining("Amount must be positive number.",amount);
    }

    @Test
    @DisplayName("달러 생성 실패 - 음수 입력")
    void mintMoney2_fail(){
        double amount = -1.5;

        assertThatThrownBy(()->bank.mintMoney(amount, Currency.DOLLAR))
                .isInstanceOf(MintNegativeValueMoneyException.class)
                .hasMessageContaining("Amount must be positive number.",amount);
    }

    @Test
    @DisplayName("equals 비교 실패 - 통화 일치, 금액 불일치")
    void money_equals_fail1(){
        Money money1 = bank.mintMoney(2000, Currency.WON);
        Money money2 = bank.mintMoney(1000, Currency.WON);

        boolean equals = money1.equals(money2);

        assertThat(equals).isFalse();

    }
    @Test
    @DisplayName("equals 비교 실패 - 통화 불일치, 금액 일치")
    void money_equals_fail2(){
        Money money1 = bank.mintMoney(2000, Currency.WON);
        Money money2 = bank.mintMoney(2000, Currency.DOLLAR);

        boolean equals = money1.equals(money2);

        assertThat(equals).isFalse();

    }
    @Test
    @DisplayName("equals 비교 실패 - 통화 불일치, 금액 불일치")
    void money_equals_fail3(){
        Money money1 = bank.mintMoney(2000, Currency.WON);
        Money money2 = bank.mintMoney(1000, Currency.DOLLAR);

        boolean equals = money1.equals(money2);

        assertThat(equals).isFalse();

    }


    @Test
    @DisplayName("equals 비교 성공 - 통화 일치, 금액 일치")
    void money_equals_success(){
        Money money1 = bank.mintMoney(1.1, Currency.DOLLAR);
        Money money2 = bank.mintMoney(1.1, Currency.DOLLAR);

        boolean equals = money1.equals(money2);

        assertThat(equals).isTrue();

    }

    //종류 0 금액 0 / 종류 0 금액 x / 종류 x 금액 0 / 종류 x 금액 x





}
