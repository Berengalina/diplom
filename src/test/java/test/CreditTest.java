package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import page.InitialPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }


    @Test
    public void shouldSuccessCreditPayment() { //1.4 Оплата в кредит - успешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val creditPage = initialPage.chooseCreditPage();
        creditPage.checkCreditHeading();
        creditPage.inputCardData(card);
        creditPage.continuePayment();
        creditPage.checkSuccessOperation();
        val actual = SQLHelper.getCreditStatusFromDB();
        assertEquals("APPROVED", actual);
    }

    @Test
    public void shouldRejectCreditPaymentDeclinedCard() { //1.5 Оплата в кредит - неуспешная операция вариант 1
        Card card = new Card();
        card.setNumber(DataHelper.rejectedCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val creditPage = initialPage.chooseCreditPage();
        creditPage.checkCreditHeading();
        creditPage.inputCardData(card);
        creditPage.continuePayment();
        creditPage.checkRejectOperation();
        val actual = SQLHelper.getCreditStatusFromDB();
        assertEquals("DECLINED", actual);
    }


    @Test
    public void shouldRejectCreditPaymentNotExistCard() { //1.6 Оплата в кредит - неуспешная операция вариант 2
        Card card = new Card();
        card.setNumber(DataHelper.notExistCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val creditPage = initialPage.chooseCreditPage();
        creditPage.checkCreditHeading();
        creditPage.inputCardData(card);
        creditPage.continuePayment();
        creditPage.checkRejectOperation();
        val actual = SQLHelper.getCreditStatusFromDB();
        assertEquals(null, actual);
    }


    @AfterEach
    public void shouldCleanData() {
        SQLHelper.cleaningDB();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

}
