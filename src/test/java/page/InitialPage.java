package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.Card;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;


public class InitialPage {

    protected SelenideElement paymentButton = $(byText("Купить"));
    protected SelenideElement creditButton = $(byText("Купить в кредит"));
    private SelenideElement cardField = $(byText("Номер карты")).parent();
    private SelenideElement monthField = $(byText("Месяц")).parent();
    private SelenideElement yearField = $(byText("Год")).parent();
    private SelenideElement nameField = $(byText("Владелец")).parent();
    private SelenideElement cvcField = $(byText("CVC/CVV")).parent();
    private SelenideElement nextButton = $(byText("Продолжить"));
    protected SelenideElement headingPaymentPayment = $(byText("Оплата по карте"));
    protected SelenideElement headingCreditPayment = $(byText("Кредит по данным карты"));
    protected SelenideElement notificationSuccess = $(".notification_status_ok ").$(byText("Операция одобрена Банком."));
    protected SelenideElement notificationError = $(".notification_status_error").$(byText("Ошибка! Банк отказал в проведении операции."));


    public void inputCardData(Card card) {
        cardField.$(".input__control").setValue(card.getNumber());
        monthField.$(".input__control").setValue(card.getMonth());
        yearField.$(".input__control").setValue(card.getYear());
        nameField.$(".input__control").setValue(card.getName());
        cvcField.$(".input__control").setValue(card.getCvc());
    }

    public CreditPage chooseCreditPage() {
        CreditPage creditPage = page(CreditPage.class);
        creditPage.creditButton.click();
        return creditPage;
    }

    public PaymentPage choosePaymentPage() {
        PaymentPage paymentPage = page(PaymentPage.class);
        paymentPage.paymentButton.click();
        return paymentPage;
    }

    public void continuePayment() {
        nextButton.click();
    }

    public void emptyDataCheck() {
        cardField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        monthField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        yearField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        nameField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        cvcField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    public void invalidCardCheck() {
        cardField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void invalidNullMonthCheck() {
        monthField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void invalidNotExistMonthCheck() {
        monthField.$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void expireYearCheck() {
        yearField.$(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты"));
    }

    public void invalidNameCheck() {
        nameField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void invalidCvcCheck() {
        cvcField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

}
