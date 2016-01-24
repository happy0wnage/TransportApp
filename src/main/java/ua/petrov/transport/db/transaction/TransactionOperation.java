package ua.petrov.transport.db.transaction;

/**
 * @author Vladyslav
 */
public interface TransactionOperation<T> {
    T apply();
}
