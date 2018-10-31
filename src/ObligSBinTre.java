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

        Node<T> p = rot, q = null;
        int cmp = 0;

        while (p != null)
        {
            q = p;
            cmp = comp.compare(verdi,p.verdi);
            p = cmp < 0 ? p.venstre : p.høyre;
        }



        p = new Node<>(verdi,q);

        if (q == null) rot = p;
        else if (cmp < 0) q.venstre = p;
        else q.høyre = p;

        endringer++;
        antall++;
        return true;
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null){
            return false;
        }
        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0){
                p = p.venstre;
            }
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null)
        {
            int cmp = comp.compare(verdi,p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0)  p = p.høyre;

            else break;
        }
        if (p == null) return false;

        if (p.venstre == null || p.høyre == null)
        {
            Node<T> barn = p.venstre != null ? p.venstre : p.høyre;
            if (p == rot) rot = barn;
            else if (p == p.forelder.venstre) p.forelder.venstre = barn;
            else p.forelder.høyre = barn;

            if (barn != null) barn.forelder = p.forelder;
        } else {
            Node<T> parent = p, høyreBarn = p.høyre;
            while (høyreBarn.venstre != null)
            {
                parent = høyreBarn;
                høyreBarn = høyreBarn.venstre;
            }

            p.verdi = høyreBarn.verdi;
            if (parent != p) parent.venstre = høyreBarn.høyre;
            else parent.høyre = høyreBarn.høyre;
            if (høyreBarn.høyre != null) høyreBarn.høyre.forelder = parent;
        }

        endringer++;
        antall--;
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
        if (!tom()) nullstill(rot);
        rot = null; antall = 0;
    }
    private void nullstill(Node<T> p)
    {
        if (p== null) return;
        if (p.venstre != null)
        {
            nullstill(p.venstre);
            p.venstre = null;
        }
        if (p.høyre != null)
        {
            nullstill(p.høyre);
            p.høyre = null;
        }
        p.verdi = null;

        p.forelder = null;
        endringer++;
        antall--;
        p.verdi = null;
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
        return "[" + subtre(rot) + "]";
    }

    private String subtre(Node<T> p){
        if (p == null){
            return "";
        }
        StringBuilder sb = new StringBuilder(p.verdi.toString());

        if (p.høyre != null){
            sb.append(", ");
            sb.append(subtre(p.høyre));
        }else if (p.venstre != null){
            sb.append(", ");
            sb.append(subtre(p.venstre));
        }

        return sb.toString();
    }


    public String lengstGren() {
        return "[" + lengstGrenSubtree(rot) + "]";
    }
    private String lengstGrenSubtree(Node<T> p){
        if (p == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(p.verdi);

        int venstreHoyde = hoydeSubtre(p.venstre);
        int hoyreHoyde = hoydeSubtre(p.høyre);

        String venstre = lengstGrenSubtree(p.venstre);
        String hoyre = lengstGrenSubtree(p.høyre);

        if (venstreHoyde > 0 || hoyreHoyde > 0) sb.append(", ");

        sb.append(hoyreHoyde > venstreHoyde ? hoyre : venstre);

        return sb.toString();
    }

    private int hoydeSubtre(Node<T> node) {

        if (node == null) {
            return 0;

        } else {
            return 1 + Math.max(hoydeSubtre(node.venstre), hoydeSubtre(node.høyre));
        }
    }

    // Oppgave 7

    public String[] grener() {

        String grenListe[] = new String[tellLeafNode(rot)];
        createGren(rot, grenListe, "[");
        return grenListe;

    }




    // Oppgave 7 hjelpemetode

    private void createGren(Node<T> p, String list[], String gren) {

        if (p == null){
            return;
        }
        gren += p.verdi;




        if (p.venstre == null && p.høyre == null) {

            gren += "]";
            LeggTilGren(gren, list);
        } else {
            gren += ", ";
        }
        createGren(p.venstre, list, gren);
        createGren(p.høyre, list, gren);
    }

    private void LeggTilGren(String gren, String grenListe[]) {

        int i = 0;
        while (i < grenListe.length - 1 && grenListe[i] != null) {
            i++;
        }
        grenListe[i] = gren;
    }

    private int tellLeafNode(Node<T> p) {

        if (p == null) {
            return 0;

        } else if (p.venstre == null && p.høyre == null) {
            return 1;

        } else {
            return tellLeafNode(p.høyre) + tellLeafNode(p.venstre);
        }
    }


    public String bladnodeverdier() {
        if (rot == null) return "[]";

        StringBuilder sb = new StringBuilder("[");
        ArrayDeque<Node<T>> deque = new ArrayDeque<>();
        deque.addFirst(rot);

        while (deque.size() > 0) {
            Node<T> p = deque.removeFirst();

            if (p.høyre != null)
                deque.addFirst(p.høyre);

            if (p.venstre != null)
                deque.addFirst(p.venstre);

            if (p.venstre == null && p.høyre == null) {
                sb.append(p.verdi);

                if (deque.size() > 0)
                    sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String postString() {
        if (rot == null){
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<T> p = rot;
        while (p.venstre != null || p.høyre != null) {
            if (p.venstre != null) p = p.venstre;
            else p = p.høyre;
        }
        for (int i = 0; i < antall; i++) {
            sb.append(p.verdi);

            if (p.forelder != null) {
                if (p == p.forelder.høyre || p.forelder.høyre == null) {
                    p = p.forelder;
                } else {
                    p = p.forelder.høyre;
                    while (p.venstre != null || p.høyre != null) {
                        if (p.venstre != null) p = p.venstre;
                        else p = p.høyre;
                    }
                }
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() {
            if (p != null) {
                while (p.venstre != null || p.høyre != null) {
                    if (p.venstre != null) p = p.venstre;
                    else p = p.høyre;
                }
            }
        }


    @Override
    public boolean hasNext() {
        return p != null; // Denne skal ikke endres!

    }

    @Override
    public T next() {
        if (iteratorendringer != endringer)
            throw new ConcurrentModificationException();

        if (p == null)
            throw new NoSuchElementException();

        T r = p.verdi;
        q = p;

        while (p.forelder != null &&
                (p == p.forelder.høyre || p.forelder.høyre == null)) {
                p = p.forelder;
        }
        if (p.forelder != null) {
            p = p.forelder.høyre;
            while (p.venstre != null || p.høyre != null) {
                if (p.venstre != null) p = p.venstre;
                else p = p.høyre;
            }
        } else {
            p = null;
        }
        removeOK = true;
        return r;
    }
    @Override
    public void remove() {
        if (!removeOK){
            throw new IllegalStateException();
        }
        Node<T> parent = q.forelder;
        if (parent != null) {


            if (q == parent.høyre) parent.høyre = null;
            else parent.venstre = null;
        } else {

            rot = null;

        }


        q.forelder = null;
        q.verdi = null;
        q = null;

        antall--;
        iteratorendringer++;
        endringer++;
        removeOK = false;

        }
    } // BladnodeIterator

} //ObligSBinTre
