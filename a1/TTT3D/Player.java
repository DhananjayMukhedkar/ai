import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        //Random random = new Random();
        Minimax algo =new Minimax();        
      
        //return nextStates.elementAt(algo.alphaBeta(gameState, gameState.getNextPlayer(),3,Integer.MIN_VALUE,Integer.MAX_VALUE).state);
        //return nextStates.elementAt(random.nextInt(nextStates.size()));
        //HashMap<GameState, Integer> map=new HashMap<GameState, Integer>();
        return (algo.alphaBeta(gameState, gameState.getNextPlayer(),3,Integer.MIN_VALUE,Integer.MAX_VALUE).state);
    }    
}
