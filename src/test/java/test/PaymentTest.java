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

public class PaymentTest {


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @Test
    public void shouldSuccessPayment() {  //1.1 Оплата по дебетовой карте - успешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.checkPaymentHeading();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.checkSuccessOperation();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals("APPROVED", actual);
    }

    @Test
    public void shouldRejectPaymentDeclinedCard() { //1.2 Оплата по дебетовой карте - неуспешная операция вариант 1
        Card card = new Card();
        card.setNumber(DataHelper.rejectedCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.checkRejectOperation();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals("DECLINED", actual);
    }

    @Test
    public void shouldRejectPaymentNotExistCard() { //1.3 Оплата по дебетовой карте - неуспешная операция вариант 2
        Card card = new Card();
        card.setNumber(DataHelper.notExistCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.checkRejectOperation();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotSendData() { //1.7 Отправка пустой формы - неуспешная операция
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.continuePayment();
        paymentPage.emptyDataCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotValidCard() {  //1.8 Ввод невалидного номера карты - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.invalidCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.invalidCardCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotValidNullMonth() {  //1.9 Ввод невалидного месяца вариант 1 - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.invalidNullMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.invalidNullMonthCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }

    @Test
    public void shouldNotValidNotExistMonth() {  //1.10 Ввод невалидного месяца вариант 2 - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.invalidNotExistMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.invalidNotExistMonthCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotValidOldYear() {  //1.11 Ввод неактуального месяца и года вариант 1 - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.actualMonth());
        card.setYear(DataHelper.lastYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.expireYearCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotValidExpireMonth() {  //1.12 Ввод неактуального месяца и года вариант 2 - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.lastMonth());
        card.setYear(DataHelper.actualYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.invalidNotExistMonthCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }

    @Test
    public void shouldSuccessPaymentCurrentMonth() {  //1.13 Ввод текущего месяца и года - успешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.actualMonth());
        card.setYear(DataHelper.actualYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.checkPaymentHeading();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.checkSuccessOperation();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals("APPROVED", actual);
    }


    @Test
    public void shouldNotValidName() {  //1.14 Ввод невалидного владельца - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("RU"));
        card.setCvc(DataHelper.generateByFakerRandomCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.invalidNameCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotValidCvc() {  //1.15 Ввод невалидного CVC - неуспешная операция
        Card card = new Card();
        card.setNumber(DataHelper.activeCard());
        card.setMonth(DataHelper.randomMonth());
        card.setYear(DataHelper.nextYear());
        card.setName(DataHelper.generateByFakerName("EN"));
        card.setCvc(DataHelper.invalidCVC());
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.inputCardData(card);
        paymentPage.continuePayment();
        paymentPage.invalidCvcCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
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
