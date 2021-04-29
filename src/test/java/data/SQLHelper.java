package data;

import lombok.SneakyThrows;
import lombok.val;
import java.sql.DriverManager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class SQLHelper {

    private static String getHost() {
        return System.getProperty("test.db.url");
    }

    @SneakyThrows
    public static String getPaymentStatusFromDB() {
        val runner = new QueryRunner();
        val statusSQL = "SELECT p.status FROM payment_entity p left join order_entity o " +
                "on p.transaction_id = o.payment_id " +
                "where p.amount = 4500000 and o.payment_id is not NULL;";
        val conn = DriverManager.getConnection(
                getHost(), "app", "pass");
        val status = runner.query(conn, statusSQL, new ScalarHandler<>());
        return (String) status;
    }

    @SneakyThrows
    public static String getCreditStatusFromDB() {
        val runner = new QueryRunner();
        val statusSQL = "SELECT c.status FROM credit_request_entity c left join order_entity o " +
                "on c.bank_id = o.credit_id " +
                "where o.credit_id is not NULL;";
        val conn = DriverManager.getConnection(
                getHost(), "app", "pass");
        val status = runner.query(conn, statusSQL, new ScalarHandler<>());
        return (String) status;
    }

    @SneakyThrows
    public static void cleaningDB() {
        val runner = new QueryRunner();
        String deleteOrderEntitySQL = "DELETE FROM order_entity";
        String deletePaymentEntitySQL = "DELETE FROM payment_entity";
        String deleteCreditRequestEntitySQL = "DELETE FROM credit_request_entity";
        try (val conn = DriverManager.getConnection(
                getHost(), "app", "pass");
        ) {
            runner.update(conn, deleteOrderEntitySQL);
            runner.update(conn, deletePaymentEntitySQL);
            runner.update(conn, deleteCreditRequestEntitySQL);
        }
    }

}
