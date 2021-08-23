import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Etat du jeu
 */
public class State implements Cloneable {

    /**
     * joueur bleu
     */
    public static final String PLAYER1 = "\u001B[34m0\u001B[0m";

    /**
     * joueur rouge
     */
    public static final String PLAYER2 = "\u001B[31mO\u001B[0m";

    /**
     * nombre de pions du joueur 1
     */
    private int nbPlayer1;
    public final int getNbPlayer1() {
        return nbPlayer1;
    }

    /**
     * nombre de pions du joueur 2
     */
    private int nbPlayer2;
    public final int getNbPlayer2() {
        return nbPlayer2;
    }

    /**
     * joueur courant
     */
    private String currentPlayer;
    public final String getCurrentPlayer() {
        return currentPlayer;
    }
    public final void changePlayer() {
        currentPlayer = (currentPlayer.equals(PLAYER1)) ? PLAYER2 : PLAYER1;
    }

    /**
     * grille de jeu
     */
    private final String[][] grid;

    public State() {
        grid = new String[7][7];
        currentPlayer = PLAYER1;
        grid[0][0] = PLAYER2; grid[6][6] = PLAYER2; nbPlayer2 = 2;
        grid[0][6] = PLAYER1; grid[6][0] = PLAYER1; nbPlayer1 = 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;

        if (!currentPlayer.equals(state.currentPlayer)) return false;
        if (nbPlayer1 != state.nbPlayer1) return false;
        if (nbPlayer2 != state.nbPlayer2) return false;

        //Arrays.equals(grid, state.grid);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if ((grid[i][j] == null && state.grid[i][j] != null) ||
                        (grid[i][j] != null && !grid[i][j].equals(state.grid[i][j])))
                    return false;
            }
        } return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPlayer, nbPlayer1, nbPlayer2, Arrays.deepHashCode(grid));
    }

    @Override
    public State clone() {
        State state = new State();
        state.nbPlayer1 = nbPlayer1;
        state.nbPlayer2 = nbPlayer2;
        state.currentPlayer = currentPlayer;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                state.grid[i][j] = grid[i][j];
            }
        } return state;
    }

    /**
     * Liste tous les mouvements possibles pour un joueur donné
     * en fonction de l'état de jeu courant
     * @param player joueur dont on veut connaitre les mouvements
     * @return
     */
    public List<Move> getMoves(String player) {
        List<Move> moves = new ArrayList<>();
        int startLn, startCol, endLn, endCol, dl, dc;
        for (startLn = 0; startLn < 7; startLn++) { for (startCol = 0; startCol < 7; startCol++) {
            if (grid[startLn][startCol] == null || !grid[startLn][startCol].equals(player))
                continue;
            for (dl = -1; dl <= 1; dl++) { for (dc = -1; dc <= 1; dc++) {
                // clonage
                endLn = startLn + dl; endCol = startCol + dc;
                if (endLn >= 0 && endLn < 7 && endCol >= 0 && endCol < 7 && grid[endLn][endCol] == null)
                    moves.add(new Move(Move.CLONE, startLn, startCol, endLn, endCol));
                // saut
                endLn = startLn + 2 * dl; endCol = startCol + 2 * dc;
                if (endLn >= 0 && endLn < 7 && endCol >= 0 && endCol < 7 && grid[endLn][endCol] == null)
                    moves.add(new Move(Move.JUMP, startLn, startCol, endLn, endCol));
            }}
        }} return moves;
    }

    /**
     * Calcule le score d'un joueur en fonction de l'état de jeu courant
     * @param player joueur de qui calculer le score
     * @return
     */
    public float getScore(String player) {
        float n1, n2;
        n1 = (player.equals(PLAYER1)) ? nbPlayer1: nbPlayer2;
        n2 = (player.equals(PLAYER1)) ? nbPlayer2: nbPlayer1;
        ;
        return n1 / (n1 + n2);
    }

    /**
     * Effectue un mouvement pour le joueur courant en fonction de l'état de jeu
     * courant et retourne le nouvel état de jeu
     * @param move mouvement à effectuer
     * @return nouvel état de jeu
     */
    public State play(Move move) {
        State state = clone();
        if (move != null) {
            String otherPlayer = (currentPlayer.equals(PLAYER1)) ? PLAYER2 : PLAYER1;
            int nbCurrentPlayerWin = 0;
            int nbOtherPlayerLose = 0;

            int startLn, startCol, endLn, endCol, ln, col;
            startLn = move.getStartLn();
            startCol = move.getStartCol();
            endLn = move.getEndLn();
            endCol = move.getEndCol();

            // mouvement
            state.grid[endLn][endCol] = currentPlayer;
            if (move.getType() == Move.JUMP)
                // si le mouvement est un saut, on libère la cellule d'origine du pion
                state.grid[startLn][startCol] = null;
            else
                // le clonage augmente de 1 le nombre de pions du joueur courant
                nbCurrentPlayerWin++;

            // infection
            for (int dl = -1; dl <= 1; dl++) {
                for (int dc = -1; dc <= 1; dc++) {
                    ln = endLn + dl;
                    col = endCol + dc;
                    if (ln >= 0 && ln < 7 && col >= 0 && col < 7 &&
                            state.grid[ln][col] != null && state.grid[ln][col].equals(otherPlayer)) {
                        state.grid[ln][col] = currentPlayer;
                        nbCurrentPlayerWin++; // le joueur courant gagne les joueurs infectés
                        nbOtherPlayerLose--; // l'autre joueur perd ses joueurs infectés
                    }
                }
            }

            if (currentPlayer.equals(PLAYER1)) {
                state.nbPlayer1 += nbCurrentPlayerWin;
                state.nbPlayer2 += nbOtherPlayerLose;
            } else {
                state.nbPlayer2 += nbCurrentPlayerWin;
                state.nbPlayer1 += nbOtherPlayerLose;
            }
        }
        state.changePlayer();
        return state;
    }

    /**
     * Teste si la partie est terminée
     * On vérifie si l'un des deux joueurs n'a plus de pions
     * Ou si les deux joueurs ne peuvent plus jouer
     * Le cas où l'état en courant serait un état déjà joué est testé
     * dans le programme principal
     * @return
     */
    public boolean isOver() {
        if (nbPlayer1 == 0)
            System.out.println("Le joueur " + PLAYER1 + " n'a plus de pions");
        else if (nbPlayer2 == 0)
            System.out.println("Le joueur " + PLAYER2 + " n'a plus de pions");
        else if (getMoves(PLAYER1).isEmpty() && getMoves(PLAYER2).isEmpty())
            System.out.println("Les deux joueurs n'ont pas pu jouer");
        else return false;
        return true;
    }

    /**
     * Trouve le gagnant après une fin de partie
     * @return
     */
    public String getWinner() {
        if (nbPlayer1 == 0 && nbPlayer2 > 0)
            return PLAYER2;
        if (nbPlayer2 == 0 && nbPlayer1 > 0)
            return PLAYER1;
        if (nbPlayer1 == nbPlayer2)
            return null;
        return (nbPlayer1 > nbPlayer2) ? PLAYER1 : PLAYER2;
    }

    /**
     * Affiche la grille de jeu
     * @return
     */
    public String gridToString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (i == 0 || j == 0)
                    if (j != 0) s.append("| ").append(j).append("\t");
                    else if (i != 0) s.append(i).append("\t");
                    else s.append("\t");
                else
                    s.append("| ").append((grid[i - 1][j - 1] == null) ? " " : grid[i - 1][j - 1]).append("\t");
            }
            s.append("|\n");
        }
        return s.toString();
    }

}
