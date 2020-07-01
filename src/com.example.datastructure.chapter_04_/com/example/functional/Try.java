package com.example.functional;

/**
 * Created by debasishc on 8/9/16.
 */
public abstract class Try<E> {
    public static <E> Try<E> of(
            NoArgumentExpressionWithException<E> expression) {
        try {
            return new Success<>(expression.evaluate());
        } catch (Exception ex) {
            return new Failure<>(ex);
        }
    }

    public abstract <R> Try<R> map(
            OneArgumentExpressionWithException<E, R> expression);

    public abstract <R> Try<R> flatMap(
            OneArgumentExpression<E, Try<R>> expression);

    public abstract E get();

    public abstract void forEach(
            OneArgumentStatement<E> statement);

    public abstract Try<E> processException(
            OneArgumentStatement<Exception> statement);

    protected static class Failure<E> extends Try<E> {
        protected Exception exception;

        public Failure(Exception exception) {
            this.exception = exception;
        }

        @Override
        public <R> Try<R> flatMap(
                OneArgumentExpression<E, Try<R>> expression) {
            return new Failure<>(exception);
        }

        @Override
        public <R> Try<R> map(
                OneArgumentExpressionWithException<E, R> expression) {
            return new Failure<>(exception);
        }

        @Override
        public E get() {
            throw new NoValueException(
                    "get method invoked on Failure");
        }

        @Override
        public void forEach(
                OneArgumentStatement<E> statement) {

        }

        @Override
        public Try<E> processException(
                OneArgumentStatement<Exception> statement) {
            statement.doSomething(exception);
            return this;
        }
    }

    protected static class Success<E> extends Try<E> {
        protected E value;

        public Success(E value) {
            this.value = value;
        }

        @Override
        public <R> Try<R> flatMap(
                OneArgumentExpression<E, Try<R>> expression) {
            return expression.compute(value);
        }

        @Override
        public <R> Try<R> map(
                OneArgumentExpressionWithException<E, R> expression) {
            try {
                return new Success<>(
                        expression.compute(value));
            } catch (Exception ex) {
                return new Failure<>(ex);
            }
        }

        @Override
        public E get() {
            return value;
        }

        @Override
        public void forEach(
                OneArgumentStatement<E> statement) {
            statement.doSomething(value);
        }

        @Override
        public Try<E> processException(
                OneArgumentStatement<Exception> statement) {
            return this;
        }
    }

}
