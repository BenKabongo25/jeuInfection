import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Jeu aléatoire
 */
public class MainRandom {

    public static void main(String[] args) {
        Random random = new Random(new Date().getTime());
        Scanner sc = new Scanner(System.in);

        State state = new State();
        List<State> states = new ArrayList<>();

        System.out.println("JEU D'INFECTION\n");
        System.out.println(state.gridToString());
        System.out.println("Appuyez sur ENTER pour continuer ..."); sc.nextLine();

        do {
            states.add(state);

            String player = state.getCurrentPlayer();
            List<Move> moves = state.getMoves(player);
            Move move = (moves.size() == 0) ? null : moves.get(random.nextInt(moves.size()));

            state = state.play(move);
            System.out.println("Le joueur " + player + ((move == null) ? " n'a pas pu jouer": " a fait un " + move));
            System.out.println(state.gridToString());
            System.out.println(State.PLAYER1 + " a " + state.getNbPlayer1() + " pions");
            System.out.println(State.PLAYER2 + " a " + state.getNbPlayer2() + " pions");
            System.out.println();
            System.out.println("Appuyez sur ENTER pour continuer ..."); sc.nextLine();
        } while (!state.isOver() || !states.contains(state));

        System.out.println((state.getWinner() == null) ? "Match null" : "Gagnant : " + state.getWinner());

    }

}
