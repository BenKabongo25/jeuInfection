/**
 * Implémentation de Alphabeta
 */
public class Alphabeta extends Algorithm {

    /**
     * Alphabeta
     * @param player joueur qui raisonne
     * @param depth profondeur de raisonnement
     */
    public Alphabeta(String player, int depth) {
        super(player, depth);
    }

    /**
     * Implémentation de alphabeta negamax
     * @param state état évalué
     * @param depth profondeur de raisonnement
     * @return évaluation de l'état
     */
    public float alphabeta(State state, float a, float b, int depth) {
        nbNodes++; // le nombre de noeuds visités est incrémenté à chaque appel de la méthode
        if (depth == 0 || state.isOver()) {
            float m = state.getScore(state.getCurrentPlayer());
            return (state.getCurrentPlayer().equals(player)) ? m : -m;
        }
        for (Move move : state.getMoves(state.getCurrentPlayer())) {
            State nextState = state.play(move);
            a = Math.max(a, -alphabeta(nextState, -b, -a, depth-1));
            if (a >= b) return a;
        } return a;
    }

    /**
     * Retrouve le meilleur mouvement correspondant à
     * la meilleure évaluation alphabeta
     * @param state état à évaluer
     * @return Mouvement correspondant à la meilleure évaluation
     */
    @Override public Move getBestMove(State state) {
        Move bestMove = null;
        float bestValue = Float.NEGATIVE_INFINITY;
        for (Move move : state.getMoves(player)) {
            State nextState = state.play(move);
            float value = alphabeta(nextState, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, depth);
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        } return bestMove;
    }
}
