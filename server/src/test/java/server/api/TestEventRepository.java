package server.api;

import commons.Event;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.EventRepository;

import java.util.*;
import java.util.function.Function;

public class TestEventRepository implements EventRepository {
    private final List<Event> events = new ArrayList<>();

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Returns all the expenses in the database
     */
    @Override
    public List<Event> findAll() {
        return events;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public List<Event> findAllById(Iterable<Long> longs) {
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
    public Optional<Event> findById(Long id) {
        return events.stream().filter(e -> e.getEventId() == id).findFirst();
    }

    /**
     * Saves the expense in the database
     */
    @Override
    public Event save(Event event) {
        events.add(event);
        return event;
    }

    /**
     * Removes the expense with the provided id from the database
     */
    @Override
    public void deleteById(Long id) {
        events.removeIf(expense -> expense.getEventId() == id);
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void delete(Event entity) {

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
    public void deleteAll(Iterable<? extends Event> entities) {

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
        return events.stream().anyMatch(e -> e.getEventId() == id);
    }
    /**
     * Returns all the expenses in the database
     * @return a list of all expenses
     */
    public List<Event> getAllExpenses() {
        return new ArrayList<>(events);
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
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

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
    public Event getOne(Long aLong) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public Event getById(Long aLong) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public Event getReferenceById(Long aLong) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public <S extends Event, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>,
                                                   R> queryFunction) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }

    /**
     * Implementation required by the interface
     */
    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }
}


