package page;

import com.codeborne.selenide.Condition;
import static com.codeborne.selenide.Condition.visible;

public class PaymentPage extends InitialPage {

    public void checkPaymentHeading () {
        headingPaymentPayment.shouldBe(visible);
    }

    public void checkSuccessOperation() {
        notificationSuccess.waitUntil(Condition.visible, 20000);
    }

    public void checkRejectOperation() {
        notificationError.waitUntil(Condition.visible, 20000);
    }
}

