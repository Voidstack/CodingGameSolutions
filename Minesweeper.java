import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
* Faible chance de réussite (proche de 5%)
* Compliqué de faire mieux
* Le site retourne "timeout!" fréquement, j'imagine que l'orienté object fais que l'algo est trop lent ... (edit : remplacer le premier move par une valeur plus proche de "0 0";)
*/
class Player {
    public static final String regex = "^[1-8]$";
    public static final int row = 16;
    public static final int col = 30;
    public static Scanner in = null;
    public static Pattern pattern = null;

    public static void main(String args[]) {
        in = new Scanner(System.in);
        pattern = Pattern.compile(regex);

        Set<Case> tableauCase = new HashSet<Case>();
        Set<Case> tableauBomb = new HashSet<Case>();

        boolean firstMove = true;

        // game loop
        while (true) {
            tableauCase = recupererTableau();
            if(firstMove){
                firstMove = false;
                System.out.println(3 + " " + 3);
            }else{
                analyseNeighbors(tableauCase);
                tableauBomb.addAll(trouverLesBombes(tableauCase));
                tableauCase.addAll(tableauBomb); // On rajoute les bombes dans le tableau
                if(deminer(tableauCase)) unstuck(tableauCase);
            }
        }
    }

    public static void unstuck(Set<Case> tableauCase){
        Case meilleurCase = null;
        int meilleurScore = 10;
        for (Case case2 : tableauCase) {
            // caseValue - bomb + nmbDinconnue
            if(pattern.matcher(case2.type).matches()){
                int unknownCount = case2.findCharInNeighbors("?").size();

                int score = Integer.valueOf(case2.type) - unknownCount;
                if(unknownCount >= Integer.valueOf(case2.type) 
                && score < meilleurScore){
                    meilleurScore = score;
                    meilleurCase = case2;
                }
            }
        }
        System.err.println("Stuck -> démine -> ");
        for (Case case1 : meilleurCase.findCharInNeighbors("?")) {
            System.out.println(case1.x + " " + case1.y);
            return;        
        }

    }

    public static boolean deminer(Set<Case> tableauCase){
        for (Case case1 : tableauCase) {
            if(pattern.matcher(case1.type).matches()){
                Set<Case> bombs = case1.findCharInNeighbors("x");
                if(bombs.size() == Integer.valueOf(case1.type)){
                    case1.neighbors.removeAll(bombs);
                    for (Case case2 : case1.neighbors) {
                        System.out.println(case2.x + " " + case2.y);
                        return false;
                    }
                }
            }
        }
        return true;
    } 

    public static Set<Case> trouverLesBombes(Set<Case> tableauCase){
        Set<Case> bombs = new HashSet<Case>(8);
        for (Case caseDuTableau : tableauCase) {
            if(caseDuTableau.type.matches(regex)) 
                bombs.addAll(caseDuTableau.findBombInNeighbors());
        }
        return bombs;
    }

    public static Set<Case> recupererTableau(){
        Set<Case> tableauCase = new HashSet<Case>(row*col);
        for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    String cell = in.next(); // '?' if unknown, '.' if no mines nearby, '1'-'8' otherwise
                    if(cell.matches(regex) || cell.equals("?"))
                        tableauCase.add(new Case(j, i, cell));
                }
            }
        return tableauCase;
    }

    public static void analyseNeighbors(Set<Case> tableauCase){
        for (Case case1 : tableauCase) {
            if(case1.type.matches(regex)) 
                case1.analyseNeighbors(tableauCase);
        }
    }

    public static class Case{
        public int x, y;
        String type;
        Set<Case> neighbors;

        Case(int x, int y, String type){
            this.x = x;
            this.y = y;
            this.type = type;
            this.neighbors = new HashSet<Case>();
        }

        public Set<Case> findCharInNeighbors(String e){
            return neighbors.stream()
            .filter(neighbor -> neighbor.type.equals(e))
           .collect(Collectors.toCollection(HashSet::new));
        }

        public Set<Case> findBombInNeighbors(){
            Set<Case> unknow = this.findCharInNeighbors("?");
            Set<Case> bomb = this.findCharInNeighbors("x");

            int totalNeighbors = unknow.size() + bomb.size();
    
            if (totalNeighbors == Integer.valueOf(this.type)) // If the number of unknowns equals the type
                return unknow.stream()
                .peek(e -> e.type = "x")
                .collect(Collectors.toSet());
            else return new HashSet<>(); // No bombs found
        }

        public void analyseNeighbors(Set<Case> tableauCase){
            for (Vecteur vecteur : Vecteur.values()) {
                Case caseVecteur = new Case(this.x + vecteur.x, this.y + vecteur.y, "");
                for (Case caseDuTableau : tableauCase) {
                    if (caseDuTableau.equals(caseVecteur) && !pattern.matcher(caseDuTableau.type).matches()) {
                        neighbors.add(caseDuTableau);
                    }
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Case autreObjet = (Case) obj;
        return this.x == autreObjet.x && this.y == autreObjet.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static enum Vecteur{
        UP(0, 1),
        DOWN(0, -1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP_LEFT(-1, 1),
        UP_RIGHT(1, 1),
        DOWN_LEFT(-1, -1),
        DOWN_RIGHT(1, -1);
        
        public int x, y;

        Vecteur(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
}
