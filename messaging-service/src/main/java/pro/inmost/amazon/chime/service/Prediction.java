package pro.inmost.amazon.chime.service;

@FunctionalInterface
public interface Prediction<U, C, V> {
    boolean make(U u, C c, V v);
}
