package server.api;

import commons.Expense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpenseRepository;

import java.util.*;
import java.util.function.Function;

public class TestExpenseRepository implements ExpenseRepository {
    private final List<Expense> expenses = new ArrayList<>();

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Returns all the expenses in the database
     */
    @Override
    public List<Expense> findAll() {
        return expenses;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public List<Expense> findAllById(Iterable<Long> longs) {
        return null;
    }
    /**
     * Implementation required by the interface
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Returns the expense with the provided id from the database
     */
    @Override
    public Optional<Expense> findById(Long id) {
        return expenses.stream().filter(e -> e.getExpenseId() == id).findFirst();
    }

    /**
     * Saves the expense in the database
     */
    @Override
    public Expense save(Expense expense) {
        expenses.add(expense);
        return expense;
    }

    /**
     * Removes the expense with the provided id from the database
     */
    @Override
    public void deleteById(Long id) {
        expenses.removeIf(expense -> expense.getExpenseId() == id);
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void delete(Expense entity) {

    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAll(Iterable<? extends Expense> entities) {

    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAll() {

    }
    /**
     * just to pass checkstyle
     */
    @Override
    public boolean existsById(Long id) {
        return expenses.stream().anyMatch(e -> e.getExpenseId() == id);
    }
    /**
     * Returns all the expenses in the database
     * @return a list of all expenses
     */
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void flush() {

    }
    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAllInBatch(Iterable<Expense> entities) {

    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAllInBatch() {

    }
    /**
     * Implementation required by the interface
     */
    @Override
    public Expense getOne(Long aLong) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public Expense getById(Long aLong) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public Expense getReferenceById(Long aLong) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Expense, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>,
                                                   R> queryFunction) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public List<Expense> findAll(Sort sort) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return null;
    }
}


