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
        if (p== null) return;
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

        String branchList[] = new String[countLeafs(rot)];

        createBranch(rot, branchList, "[");

        return branchList;

    }




    // Oppgave 7 hjelpemetode

    private void createBranch(Node<T> node, String list[], String branch) {

        if (node == null) return;




        branch += node.verdi;




        if (node.venstre == null && node.høyre == null) {

            branch += "]";

            addBranch(branch, list);




        } else {

            branch += ", ";

        }




        createBranch(node.venstre, list, branch);

        createBranch(node.høyre, list, branch);

    }




    // Oppgave 7 hjelpemetode

    private void addBranch(String branch, String branchList[]) {

        int i = 0;

        while (i < branchList.length - 1 && branchList[i] != null)

            i++;




        branchList[i] = branch;

    }




    // Oppgave 7 hjelpemetode

    private int countLeafs(Node<T> node) {

        if (node == null) {

            return 0;




        } else if (node.venstre == null && node.høyre == null) {

            return 1;




        } else {

            return countLeafs(node.høyre) + countLeafs(node.venstre);

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
        if (rot == null) return "[]";
        StringBuilder stringBuilder = new StringBuilder("[");
        Node<T> p = rot;
        while (p.venstre != null || p.høyre != null) {
            if (p.venstre != null) p = p.venstre;
            else p = p.høyre;
        }
        for (int i = 0; i < antall; i++) {
            stringBuilder.append(p.verdi);

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
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
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

}
