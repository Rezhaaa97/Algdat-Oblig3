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
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi,q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        endringer++;
        antall++;                                // én verdi mer i treet
        return true;
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
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) p = p.venstre;      // går til venstre
            else if (cmp > 0)  p = p.høyre;   // går til høyre

            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> barn = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) rot = barn;
            else if (p == p.forelder.venstre) p.forelder.venstre = barn;
            else p.forelder.høyre = barn;

            if (barn != null) barn.forelder = p.forelder;
        } else {
            Node<T> parent = p, høyreBarn = p.høyre;   // finner neste i inorden
            while (høyreBarn.venstre != null)
            {
                parent = høyreBarn;    // p er forelder til høyrebarn
                høyreBarn = høyreBarn.venstre;
            }

            p.verdi = høyreBarn.verdi;   // kopierer verdien i r til p
            if (parent != p) parent.venstre = høyreBarn.høyre;
            else parent.høyre = høyreBarn.høyre;
            if (høyreBarn.høyre != null) høyreBarn.høyre.forelder = parent;
        }

        endringer++;
        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        int antallVerdi = 0;
        while(fjern(verdi)){
            antallVerdi++;
        }

        return antallVerdi;
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        Node<T> p = rot;
        int antallVerdi = 0;

        while (p != null)
        {
            int cmp = comp.compare(verdi,p.verdi);
            if (cmp < 0) p = p.venstre;
            else
            {
                if (cmp == 0) antallVerdi++;
                p = p.høyre;
            }
        }
        return antallVerdi;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }


    @Override
    public void nullstill() {
        if (!tom()) nullstill(rot);  // nullstiller
        rot = null; antall = 0;      // treet er nå tomt
    }
    private void nullstill(Node<T> p)
    {
        if (p.venstre != null)
        {
            nullstill(p.venstre);      // venstre subtre
            p.venstre = null;          // nuller peker
        }
        if (p.høyre != null)
        {
            nullstill(p.høyre);        // høyre subtre
            p.høyre = null;            // nuller peker
        }
        p.verdi = null;              // nuller verdien
    }


    private static <T> Node<T> nesteInorden(Node<T> p) {
        if (p.høyre != null) {
            p = p.høyre;
            while (p.venstre != null) {
                p = p.venstre;

            }
            return p;
        }else {
            while (p.forelder != null && p.forelder.høyre == p){
                p = p.forelder;
            }
            return p.forelder;

        }


    }





    @Override

    public String toString() {
        if (tom()){
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<T> p = rot;
        // Finn første node inorden
        if (p != null) while (p.venstre !=null) p = p.venstre;
        while (p != null){
            sb.append(p.verdi);
            Node<T> neste = nesteInorden(p);
            if (neste != null) sb.append(", ");
            p = neste;
        }
        sb.append("]");
        return sb.toString();

    }


    public String omvendtString() {
        if (tom()){
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");

        Node<T> p = rot;
        while (p.høyre != null) p = p.høyre;
        sb.append(p.verdi);

        while (true)
        {
            if (p.venstre != null)
            {
                p = p.venstre;
                while (p.høyre != null) p = p.høyre;
            }
            else
            {
                while (p.forelder != null && p.forelder.venstre == p)
                {
                    p = p.forelder;
                }
                p = p.forelder;
            }
            if (p == null) break;
            sb.append(',').append(' ').append(p.verdi);
        }
        sb.append(']');

        return sb.toString();
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
