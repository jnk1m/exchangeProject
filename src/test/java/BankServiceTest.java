import com.nhnacademy.gwjs.service.Bank;
import com.nhnacademy.gwjs.entity.Currency;
import com.nhnacademy.gwjs.entity.Money;
import com.nhnacademy.gwjs.exception.NegativeArithmeticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BankServiceTest {
    //SUT
    Bank bank;

    @BeforeEach
    void setUp(){
        bank = new Bank();
    }

    //원화 더하는 연산 성공
    @Test
    void add_won_success(){
        Money money1 = bank.mintMoney(1000,Currency.WON);
        Money money2 = bank.mintMoney(1000,Currency.WON);

        Money addMoney = bank.add(money1, money2);

        assertThat(addMoney).isNotNull();
        assertThat(addMoney.getAmount()).isEqualTo(2000);
    }


    @Test
    void add_won_success_with_decimal(){
        Money money1 = bank.mintMoney(1000.1, Currency.WON);
        Money money2 = bank.mintMoney(1000,Currency.WON);

        Money addMoney = bank.add(money1, money2);

        assertThat(addMoney).isNotNull();
        assertThat(addMoney.getAmount()).isEqualTo(2000);
    }

    //달러 더하는 연산 성공
    @Test
    void add_dollar_success(){
        Money money1 = bank.mintMoney(5.25,Currency.DOLLAR);
        Money money2 = bank.mintMoney(5.25,Currency.DOLLAR);

        Money addMoney = bank.add(money1, money2);
        assertThat(addMoney).isNotNull();
        assertThat(addMoney.getAmount()).isEqualTo(10.50);
    }

    //돈 빼기 연산 성공
    @Test
    void subtraction_money_success(){
        Money money1 = bank.mintMoney(2000, Currency.WON);
        Money money2 = bank.mintMoney(1000, Currency.WON);

        Money subtractionMoney = bank.subtraction(money1, money2);

        assertThat(subtractionMoney).isNotNull();
        assertThat(subtractionMoney.getAmount()).isEqualTo(1000);

    }

    //돈 빼기 연산 실패
    @Test
    void subtraction_money_fail(){
        double amount1 = 1000;
        double amount2 = 2000;
        Money money1 = bank.mintMoney(amount1, Currency.WON);
        Money money2 = bank.mintMoney(amount2, Currency.WON);

        assertThatThrownBy(()->bank.subtraction(money1,money2))
                .isInstanceOf(NegativeArithmeticException.class)
                .hasMessageContaining("The result of the operation is negative",(amount1-amount2));
    }

    @Test
    @DisplayName("원화에서 달러 환전 성공")
    void exchange_won_to_dollar_success(){
        Money money = bank.mintMoney(2500,Currency.WON);

        Money dollar = bank.exchange(money);

        assertThat(dollar).isNotNull();
        assertThat(dollar.getAmount()).isEqualTo(2.13);
    }
    @Test
    @DisplayName("원화에서 달러 환전 및 반올림 성공")
    void exchange_won_to_dollar_success_decimal(){
        Money money = bank.mintMoney(2555,Currency.WON);

        Money dollar = bank.exchange(money);

        assertThat(dollar).isNotNull();
        assertThat(dollar.getAmount()).isEqualTo(2.17);
    }

    @Test
    @DisplayName("달러에서 원화 환전 성공")
    void exchange_dollar_to_won_success(){
        Money money = bank.mintMoney(5.25,Currency.DOLLAR);

        Money won = bank.exchange(money);

        assertThat(won).isNotNull();
        assertThat(won.getAmount()).isEqualTo(4460);
    }

    @Test
    @DisplayName("달러에서 원화 환전 및 반올림 성공")
    void exchange_dollar_to_won_success_decimal(){
        Money money = bank.mintMoney(2.355,Currency.DOLLAR);

        Money won = bank.exchange(money);

        assertThat(won).isNotNull();
        assertThat(won.getAmount()).isEqualTo(2000);
    }

    @Test
    @DisplayName("환전 수수료 적용 여부")
    void exchangeFee_won_to_dollar_charged(){
        Money money = bank.mintMoney(15,Currency.DOLLAR);

        double amountAfterFeeDeduction = bank.calculateExchangeFee(money);

        assertThat(amountAfterFeeDeduction).isEqualTo(12.75);

    }

}
