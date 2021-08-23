import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    /*
    Modes de lancement du programme en ligne de commande
    1.  java Main <int a> <int b> <boolean alphabetaFlag>
        @param a: indique la profondeur de raisonnement du premier joueur
        @param b: indique la profondeur de raisonnement du second joueur
        @param alphabetaFlag: si True, on utilise Alphabeta au lieu de Negamax
    2.  java Main <int a> <int b>
        Equivalent à java Main a b true
        On utilise alphabeta
    3.  java Main <int a>
        Equivalent à java Main a a true
        Les deux joueurs ont la même profondeur de raisonnement
        On utilise alphabeta
    4.  java Main
        Equivalent à java Main 3 3 true
        Les deux joueurs ont la même profondeur de raisonnement, par défaut 3
     */

    public static void main(String[] args) {
        PrintStream original = System.out;
        Scanner sc = new Scanner(System.in);

        // profondeur de raisonnement
        int depth1 = 3, depth2 = 3;
        // indique si on utilise ou non alphabeta
        boolean alphabetaFlag = true;

        if (args.length >= 1) {
            int a; // profondeur du premier joueur
            try {
                a = Integer.parseInt(args[0]);
                if (a < 1) throw new IllegalArgumentException();
            } catch (Exception e) {
                a = depth1;
            }
            depth1 = a;
            depth2 = a;
            if (args.length >= 2) {
                int b; // profondeur du second joueur
                try {
                    b = Integer.parseInt(args[1]);
                    if (b < 1) throw new IllegalArgumentException();
                } catch (Exception e) {
                    b = depth2;
                }
                depth2 = b;
            }
            if (args.length >= 3) {
                alphabetaFlag = args[2].toLowerCase().trim().equals("true");
            }
        }

        // algorithmes de raisonnement
        Algorithm algorithm1, algorithm2;
        if (alphabetaFlag) {
            algorithm1 = new Alphabeta(State.PLAYER1, depth1);
            algorithm2 = new Alphabeta(State.PLAYER2, depth2);
        } else {
            algorithm1 = new Negamax(State.PLAYER1, depth1);
            algorithm2 = new Negamax(State.PLAYER2, depth2);
        }

        // initialistaion du jeu
        System.out.println("JEU D'INFECTION\n");
        System.out.println("L'algorithme de décision utilisé est " +
                        ((alphabetaFlag) ? "alphabeta" : "negamax") + "...");
        System.out.println("Le joueur " + State.PLAYER1 + " raisonne avec une profondeur de " + depth1 + "...");
        System.out.println("Le joueur " + State.PLAYER2 + " raisonne avec une profondeur de " + depth2 + "...");

        // tous les états de la partie
        List<State> states = new ArrayList<>();
        // état courant
        State currentState = new State();
        // indicateur de fin de partie
        boolean isOver;

        System.out.println(currentState.gridToString());
        System.out.println(State.PLAYER1 + " a " + currentState.getNbPlayer1() + " pions");
        System.out.println(State.PLAYER2 + " a " + currentState.getNbPlayer2() + " pions");
        //System.out.print("\nAppuyez sur ENTER pour continuer..."); sc.nextLine();

        do {
            states.add(currentState);

            // On désactive les system.out.println pendant les raisonnements
            System.setOut(new PrintStream(new OutputStream() { public void write(int b) {}}));
            // choix du joueur courant
            Move move = (currentState.getCurrentPlayer().equals(State.PLAYER1)) ?
                    algorithm1.getBestMove(currentState) : algorithm2.getBestMove(currentState);

            // On rétablit les system.out.prinln original
            System.setOut(original);
            System.out.println("Le joueur " + currentState.getCurrentPlayer() +
                    ((move == null) ? " n'a pas pu jouer" : " a effectué un " + move));
            currentState = currentState.play(move);

            System.out.println(currentState.gridToString());
            System.out.println(State.PLAYER1 + " a " + currentState.getNbPlayer1() + " pions");
            System.out.println(State.PLAYER2 + " a " + currentState.getNbPlayer2() + " pions");
            //System.out.print("\nAppuyez sur ENTER pour continuer..."); sc.nextLine();

            // Tests de fin de partie
            // La méthode affiche du texte selon que l'état est une fin de jeu
            isOver = currentState.isOver();
            if (states.contains(currentState)) {
                System.out.println("Retour dans un état de jeu déjà joué !");
                isOver = true;
            }
        } while (!isOver);

        String winner = currentState.getWinner();
        if (State.PLAYER1.equals(winner)) {
            System.out.println("Gagnant : " + State.PLAYER1);
        } else if (State.PLAYER2.equals(winner)) {
            System.out.println("Gagnant : " + State.PLAYER2);
        } else {
            System.out.println("Match nul !");
        }

        // Statistiques
        System.out.println("\nSTATISTIQUES...");
        System.out.println("Algorithme de raisonnement utilisé : " +
                ((alphabetaFlag) ? "alphabeta" : "negamx"));
        System.out.println("\nPour le joueur " + State.PLAYER1 + ":");
        System.out.println("\tScore \t\t\t\t\t\t: " + currentState.getScore(State.PLAYER1));
        System.out.println("\tProfondeur de raisonnement\t: " + depth1);
        System.out.println("\tNombre de noeuds visités\t: " + algorithm1.getNbNodes());
        System.out.println("\nPour le joueur " + State.PLAYER2 + ":");
        System.out.println("\tScore \t\t\t\t\t\t: " + currentState.getScore(State.PLAYER2));
        System.out.println("\tProfondeur de raisonnement\t: " + depth2);
        System.out.println("\tNombre de noeuds visités\t: " + algorithm2.getNbNodes());

    }
}
