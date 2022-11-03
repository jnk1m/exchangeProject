import com.nhnacademy.gwjs.exception.MismatchCurrencyException;
import com.nhnacademy.gwjs.exception.SameCurrencyException;
import com.nhnacademy.gwjs.service.Bank;
import com.nhnacademy.gwjs.entity.Currency;
import com.nhnacademy.gwjs.entity.Money;
import com.nhnacademy.gwjs.exception.NegativeArithmeticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BankServiceTest {
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }


    @Test
    @DisplayName("원화끼리 더하기 성공")
    void add_won_success() {
        double amount1 = 1000;
        double amount2 = 2000;

        Money money1 = bank.createMoney(amount1, Currency.WON);
        Money money2 = bank.createMoney(amount2, Currency.WON);

        Money addMoney = bank.addMoney(money1, money2);

        assertThat(addMoney).isNotNull();
        assertThat(addMoney.getAmount()).isEqualTo(amount1 + amount2);
    }


    @Test
    @DisplayName("원화 실수값 더하기 성공")
    void add_won_success_with_decimal() {
        Money money1 = bank.createMoney(1000.1, Currency.WON);
        Money money2 = bank.createMoney(1000, Currency.WON);

        Money addMoney = bank.addMoney(money1, money2);

        assertThat(addMoney).isNotNull();
        assertThat(addMoney.getAmount()).isEqualTo(2000);
    }

    @Test
    @DisplayName("달러끼리 더하기 연성")
    void add_dollar_success() {
        double amount1 = 5.25;
        double amount2 = 5.75;
        Money money1 = bank.createMoney(amount1, Currency.DOLLAR);
        Money money2 = bank.createMoney(amount2, Currency.DOLLAR);

        Money addMoney = bank.addMoney(money1, money2);
        assertThat(addMoney).isNotNull();
        assertThat(addMoney.getAmount()).isEqualTo(amount1 + amount2);
    }


    @Test
    @DisplayName("원화끼리 빼기 성공")
    void subtraction_money_success() {
        double amount1 = 3000;
        double amount2 = 2000;

        Money money1 = bank.createMoney(amount1, Currency.WON);
        Money money2 = bank.createMoney(amount2, Currency.WON);

        Money subtractionMoney = bank.subtractMoney(money1, money2);

        assertThat(subtractionMoney).isNotNull();
        assertThat(subtractionMoney.getAmount()).isEqualTo(amount1 - amount2);
    }

    @Test
    @DisplayName("통화의 종류가 달라 연산 불가")
    void add_mismatchCurrencyException() {
        double amount1 = 1000;
        double amount2 = 2000;
        Money money1 = bank.createMoney(amount1, Currency.WON);
        Money money2 = bank.createMoney(amount2, Currency.DOLLAR);

        assertThatThrownBy(() -> bank.addMoney(money1, money2))
                .isInstanceOf(MismatchCurrencyException.class)
                .hasMessageContaining("Currency mismatch", money1.getCurrency(), money2.getCurrency());

    }


    @Test
    @DisplayName("뺄셈의 결과가 음수가 되어 연산 실패")
    void subtraction_result_negativeArithmeticException() {
        double amount1 = 1000;
        double amount2 = 2000;
        Money money1 = bank.createMoney(amount1, Currency.WON);
        Money money2 = bank.createMoney(amount2, Currency.WON);

        assertThatThrownBy(() -> bank.subtractMoney(money1, money2))
                .isInstanceOf(NegativeArithmeticException.class)
                .hasMessageContaining("The result of the operation is negative", (amount1 - amount2));
    }


    @Test
    @DisplayName("원화에서 달러 환전 성공")
    void exchange_won_to_dollar_success() {
        double amount = 2500;
        Money money = bank.createMoney(amount, Currency.WON);

        Money dollar = bank.exchangeWonToForeignCurrency(money, Currency.DOLLAR);
        double result = Math.round((((amount - amount * 0.15) / Currency.DOLLAR.getRate())) * 100) / 100.0;

        assertThat(dollar).isNotNull();
        assertThat(dollar.getAmount()).isEqualTo(result);
    }

    @Test
    @DisplayName("원화에서 유로 환전 성공")
    void exchange_won_to_euro_success() {
        long amount = 2500;
        Money money = bank.createMoney(amount, Currency.WON);

        Money dollar = bank.exchangeWonToForeignCurrency(money, Currency.EURO);

        double result = Math.round((((amount - amount * 0.15) / Currency.EURO.getRate())) * 100) / 100.0;

        assertThat(dollar).isNotNull();
        assertThat(dollar.getAmount()).isEqualTo(result);
    }

    @Test
    @DisplayName("원화에서 같은 원화로 환전하려 했을 때 실패")
    void exchange_won_to_won_sameCurrencyException() {
        Money money = bank.createMoney(2500, Currency.WON);

        assertThatThrownBy(() -> bank.exchangeWonToForeignCurrency(money, Currency.WON))
                .isInstanceOf(SameCurrencyException.class)
                .hasMessageContaining("Same currency. Enable to exchange: ", Currency.WON);
    }

    @Test
    @DisplayName("달러에서 원화 환전 성공")
    void exchange_dollar_to_won_success() {
        double amount = 5.25;
        Money money = bank.createMoney(amount, Currency.DOLLAR);

        Money won = bank.exchangeForeignCurrencyToWon(money, Currency.DOLLAR);
        double result = Math.round((((amount - amount * 0.15) * Currency.DOLLAR.getRate())) / 10) * 10.0;

        assertThat(won).isNotNull();
        assertThat(won.getAmount()).isEqualTo(result);
    }

    @Test
    @DisplayName("유로에서 원화 환전 성공")
    void exchange_euro_to_won_success() {
        double amount = 52.5;
        Money money = bank.createMoney(amount, Currency.EURO);

        Money won = bank.exchangeForeignCurrencyToWon(money, Currency.EURO);
        double result = Math.round((((amount - amount * 0.15) * Currency.EURO.getRate())) / 10) * 10.0;

        assertThat(won).isNotNull();
        assertThat(won.getAmount()).isEqualTo(result);
    }


    @Test
    @DisplayName("환전 수수료 부과 확인")
    void exchangeFee_charged() {
        double amount = 52.5;
        Money money = bank.createMoney(amount, Currency.DOLLAR);

        double amountAfterFeeDeduction = bank.calculateExchangeFee(money);

        double exchangeFee = amount - amount * 0.15;

        assertThat(amountAfterFeeDeduction).isEqualTo(exchangeFee);

    }

}
