import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Jeu utilisateur - ordinateur
 */
public class MainUser {

    public static void main(String[] args) {
        PrintStream original = System.out;
        Scanner sc = new Scanner(System.in);
        String line;

        System.out.println("JEU D'INFECTION (User Vs Computer) ");

        do {
            System.out.println("Nouvelle partie !\n");
            State state = new State();

            // choix des joueurs
            System.out.print("Jouer en premier ? (o/n) : ");
            line = sc.nextLine();
            String user, computer;
            if (line.length() > 0 && line.toLowerCase().trim().charAt(0) == 'o') {
                user = State.PLAYER1;
                computer = State.PLAYER2;
            } else {
                user = State.PLAYER2;
                computer = State.PLAYER1;
            }

            // choix du niveau de difficulté
            int depth;
            do {
                System.out.print("Choisissez le niveau de difficulté (1 - 5) : ");
                line = sc.nextLine();
                try {
                    depth = Integer.parseInt(line);
                } catch (Exception e) {
                    depth = -1;
                }
            } while (depth < 1 || depth > 5);

            // algo de raisonnement de l'ordinateur
            Alphabeta alphabeta = new Alphabeta(computer, depth);

            System.out.println("\n" + state.gridToString());
            System.out.println(State.PLAYER1 + " : " + state.getNbPlayer1() + " pions");
            System.out.println(State.PLAYER2 + " : " + state.getNbPlayer2() + " pions");
            System.out.println();

            do {
                Move move;

                // si c'est à l'utilisateur de jouer
                if (state.getCurrentPlayer().equals(user)) {
                    List<Move> moves = state.getMoves(user);
                    // choix du mouvement à effectuer
                    System.out.println("Les mouvements que vous pouvez effectuer sont :");
                    for (int i = 0; i < moves.size(); i++)
                        System.out.println(i + " -- un " + moves.get(i));
                    System.out.println();
                    int choice;
                    do {
                        System.out.print("Indiquez le numéro du mouvement à effectuer : (0-" +
                                (moves.size()-1) + ") : ");
                        line = sc.nextLine();
                        try {
                            choice = Integer.parseInt(line);
                        } catch (Exception e) {
                            choice = -1;
                        }
                    } while (choice < 0 || choice >= moves.size());
                    move = moves.get(choice);
                    System.out.println("\nVous avez fait un " + move);
                }

                // si c'est à l'ordinateur de jouer
                else {
                    // On désactive les system.out.println pendant les raisonnements
                    System.setOut(new PrintStream(new OutputStream() { public void write(int b) {}}));
                    move = alphabeta.getBestMove(state);
                    // On rétablit les system.out.prinln original
                    System.setOut(original);
                    System.out.println(state.getCurrentPlayer() + " a effectué un " + move);
                }

                state = state.play(move);
                System.out.println(state.gridToString());
                System.out.println(State.PLAYER1 + " : " + state.getNbPlayer1()
                        + " pions. Score :" + state.getScore(State.PLAYER1));
                System.out.println(State.PLAYER2 + " : " + state.getNbPlayer2()
                        + " pions. Score :" + state.getScore(State.PLAYER2));
                System.out.println();

                if (state.getCurrentPlayer().equals(user)) {
                    System.out.println("Appuyez sur ENTER pour continuer ...");
                    sc.nextLine();
                }

            } while (!state.isOver());

            String winner = state.getWinner();
            if (winner == null)
                System.out.println("Match nul !\n");
            else if (winner.equals(user))
                System.out.println("Vous avez gagné !\n");
            else
                System.out.println("Vous avez perdu !\n");

            System.out.print("Souhaitez-vous recommencer ? (o/n) : ");
            line = sc.nextLine();

        } while (line.length() > 0 && line.toLowerCase().trim().charAt(0) == 'o');

    }

}
