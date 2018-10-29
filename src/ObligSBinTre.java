import java.util.*;

public class ObligSBinTre <T> implements Beholder<T>{
    private static final class Node<T>{
        private T verdi;
        private Node<T> venstre, høyre;
        private Node<T> forelder;

        //konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder){
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder){
            this(verdi, null,null, forelder);
        }

        @Override
        public String toString(){ return ""+verdi;}
    }// Class Node

    private Node<T> rot;
    private int antall;
    private int endringer;

    private final Comparator<? super T> comp; // komparator

    public ObligSBinTre(Comparator<? super T> c){
        rot= null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi){
        throw new UnsupportedOperationException("Ikke kodet ennå !");
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) return false; Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi); if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String omvendtString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String høyreGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;
        private BladnodeIterator() { //konstruktør
        throw new UnsupportedOperationException("Ikke kodet ennå!");

    }

    @Override
    public boolean hasNext() {
        return p != null; // Denne skal ikke endres!

    }

    @Override
    public T next() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    } // BladnodeIterator

}
