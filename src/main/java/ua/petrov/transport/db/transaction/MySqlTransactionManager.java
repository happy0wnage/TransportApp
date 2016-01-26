package ua.petrov.transport.db.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.petrov.transport.db.connection.IConnectionPool;
import ua.petrov.transport.db.connection.MySQLConnection;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.exception.TranscationException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Vladyslav
 */
@Service
public class MySqlTransactionManager implements TransactionManager {

    @Qualifier("connectionPool")
    @Autowired
    private IConnectionPool<Connection> connectionPool;

    @Override
    public <T> T doInTransaction(TransactionOperation<T> transactionOperation) {
        Connection connection = MySQLConnection.getWebInstance();
        connectionPool.put(connection);

        T result = transactionOperation.apply();

        try {
            connection.commit();
            connection.close();
        } catch (SQLException | DBLayerException e) {
            rollback(connection);
            throw new TranscationException("Transaction not committed.", e);
        } finally {
            connectionPool.remove();
        }
        return result;
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TranscationException("Transaction not rollbacked.", e);
        }
    }
}
