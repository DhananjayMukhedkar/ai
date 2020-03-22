
import java.util.*;

public class Minimax {

    private static   int[][] HEURISTIC_SCORES = { { 1, -10, -100, -1000 }, { 10, 0, 0, 0 }, { 100, 0, 0, 0 },
    { 1000, 0, 0, 0 } };
    private   int WHITE_KING = Constants.CELL_WHITE | Constants.CELL_KING;
    private   int RED_KING = Constants.CELL_RED | Constants.CELL_KING;

    /* Constants for evaluation function */
    private   int PIECE_WEIGHT = 1;
    private   int KING_WEIGHT = 5;

    private static HashMap< Integer,  Integer> history = new HashMap< Integer,  Integer>();

    public BestAction<GameState,  Integer> alphaBeta(  GameState gameState,   int player, int depth,  Integer alpha,
       Integer beta) {


            if (gameState.isRedWin()) { // Terminal state

                return new BestAction<GameState,  Integer>(gameState,  Integer.MIN_VALUE);

            } else if (gameState.isWhiteWin()) {

                return new BestAction<GameState,  Integer>(gameState,  Integer.MAX_VALUE);
            } else if (gameState.isDraw()) {

                return new BestAction<GameState,  Integer>(gameState,  (Integer)0);
            }

            if (depth == 0) {
                return new BestAction<GameState,  Integer>(gameState, evalUtil(gameState, player));
            }

            Vector<GameState> nextStates = new Vector<GameState>();
            gameState.findPossibleMoves(nextStates);
            Collections.shuffle(nextStates);
    //     history.put(gameState.hashCode(), evalUtil(gameState, player));

        if (player == Constants.CELL_WHITE) {// max Constants.CELL_WHITE
            return maxValue(nextStates, Constants.CELL_RED, depth, alpha, beta,gameState);// max for other player

        } else { // min CELL_REDO
            return minValue(nextStates, Constants.CELL_WHITE, depth, alpha, beta,gameState); // min for other player

        }

    }

    private  Integer evalUtil(GameState gameState, int player) {

        int red_count = 0;
        int cell = 0;
        int white_count = 0;
        int white_kings = 0;
        int red_kings = 0;
        int total_whites =0;
        int total_reds =0;
        int whiteMoves=0;
        int redMoves=0;
        int whiteKmoves=0;
        int redKmoves=0;
        int whiteOff=0;
        int redOff=0;
        int whiteNbr=0;
        int redNbr=0;
        int n=0;
        Vector<Move> moves = new Vector<Move> ();


        for (int i = 0; i < GameState.NUMBER_OF_SQUARES; i++) {
            cell = gameState.get(i);
            if (cell != Constants.CELL_INVALID && cell != Constants.CELL_EMPTY) {
                if (cell == Constants.CELL_RED) {//red player
                    red_count++;    //peice count                
                    moves.clear();
                    gameState.tryMove(moves, cell, Boolean.FALSE);
                    redMoves+=moves.size(); //add possible moves
                    redOff+=GameState.cellToCol(cell);
                    //check neighbours count from preceding rows and adjacent columns
                    n=gameState.get(GameState.cellToRow(cell)-1, GameState.cellToCol(cell)-1 );
                    if(n== Constants.CELL_RED){
                        redNbr++;
                    }
                    n=gameState.get(GameState.cellToRow(cell)-1, GameState.cellToCol(cell)+1 );
                    if(n== Constants.CELL_RED){
                        redNbr++;
                    }
                } else if (cell == Constants.CELL_WHITE) { //white player
                    white_count++; //piece count
                    moves.clear();
                    gameState.tryMove(moves, cell, Boolean.FALSE);
                    whiteMoves+=moves.size(); //add possible moves

                    whiteOff+=8-GameState.cellToCol(cell); //add offence score
                    //check neighbours from preceding rows
                    n=gameState.get(GameState.cellToRow(cell)+1, GameState.cellToCol(cell)-1 );
                    if(n== Constants.CELL_WHITE){
                     whiteNbr++;
                     }
                     n=gameState.get(GameState.cellToRow(cell)+1, GameState.cellToCol(cell)+1 );
                     if(n== Constants.CELL_WHITE){
                        whiteNbr++;
                    }
            } else if (cell == WHITE_KING ) { //check kings
                white_kings++;
                moves.clear();
                gameState.tryMove(moves, cell, Boolean.TRUE);
                whiteKmoves+=moves.size();

            } else if (cell ==RED_KING) {
                red_kings++;
                moves.clear();
                gameState.tryMove(moves, cell, Boolean.TRUE);
                redKmoves+=moves.size();
               // redOff+=GameState.cellToCol(cell);
            }
        }
    }

    //calculate total heuristic values        
    total_whites =    white_count * PIECE_WEIGHT + white_kings * KING_WEIGHT + whiteMoves+2* whiteKmoves+ 2*whiteNbr+(int)0.5* whiteOff;
    total_reds =  red_count * PIECE_WEIGHT + red_kings * KING_WEIGHT+ redMoves+2* redKmoves+ 2*redNbr+ (int)0.5* redOff;


    if (player == Constants.CELL_WHITE) {
            //System.err.println(total_whites - total_reds);
        return (total_whites - total_reds);
    }

    return (total_reds - total_whites );

    
}

public BestAction<GameState,  Integer> minValue( Vector<GameState> nextStates,   int player, int depth,
   Integer alpha,  Integer beta,GameState gameState){

   Integer bestUtil =  Integer.MAX_VALUE;
   GameState bestState = nextStates.elementAt(0);
   for (int i = 0; i < nextStates.size(); i++) {

      BestAction<GameState,  Integer> state = alphaBeta(nextStates.elementAt(i), player, depth - 1, alpha,
        beta);

      if (state.value < bestUtil) {
                // bestState = i;
        bestState = nextStates.elementAt(i);
        bestUtil = state.value;

    }
    beta = Math.min(beta, bestUtil); // max
    if (beta <= alpha) //prune
        break;
   
    }
    
    if(bestState==null){
            System.err.println("is null");
            bestState=nextStates.elementAt(0);
        }

    return new BestAction<GameState,  Integer>(bestState, bestUtil);
    
    }

    public BestAction<GameState,  Integer> maxValue( Vector<GameState> nextStates,   int player, int depth,
       Integer alpha,  Integer beta,GameState gameState) {

       Integer bestUtil =  Integer.MIN_VALUE;
       GameState bestState = nextStates.elementAt(0);

       for (int i = 0; i < nextStates.size(); i++) {

          BestAction<GameState,  Integer> state = alphaBeta(nextStates.elementAt(i), player, depth - 1, alpha,
            beta);

          if (state.value > bestUtil ) {
                // bestState = i;
            bestState = nextStates.elementAt(i);
            bestUtil = state.value;

        }

        alpha = Math.max(alpha, bestUtil);// max
        if (beta <= alpha) //prune
            break;
        }

        if(bestState==null){
            System.err.println("is null");
            bestState=nextStates.elementAt(0);
        }

        return new BestAction<GameState,  Integer>(bestState, bestUtil);

    }

}