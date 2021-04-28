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
    public void shouldSuccessDebetPayment() {
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
    public void shouldRejectDebetPaymentDeclinedCard() {
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
    public void shouldRejectDebetPaymentNotExistCard() {
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
    public void shouldSuccessCreditPayment() {
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
    public void shouldRejectCreditPaymentDeclinedCard() {
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
        creditPage.checkSuccessOperation();
        val actual = SQLHelper.getCreditStatusFromDB();
        assertEquals("DECLINED", actual);
    }


    @Test
    public void shouldRejectCreditPaymentNotExistCard() {
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


    @Test
    public void shouldNotSendData() {
        val initialPage = new InitialPage();
        val paymentPage = initialPage.choosePaymentPage();
        paymentPage.continuePayment();
        paymentPage.emptyDataCheck();
        val actual = SQLHelper.getPaymentStatusFromDB();
        assertEquals(null, actual);
    }


    @Test
    public void shouldNotValidCard() {
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
    public void shouldNotValidNullMonth() {
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
    public void shouldNotValidNotExistMonth() {
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
    public void shouldNotValidOldYear() {
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
    public void shouldNotValidExpireMonth() {
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
    public void shouldSuccessDebetPaymentCurrentMonth() {
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
    public void shouldNotValidName() {
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
    public void shouldNotValidCvc() {
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
