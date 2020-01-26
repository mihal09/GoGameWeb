package com.hellokoding.account.GameLogic;

public class MoveValidation {
    private static Grid makeGrid(String boardState, int boardSize){
        Grid grid = new Grid(boardSize);
        for(int i=0; i< boardSize; i++){
            for(int j=0; j<boardSize; j++){
                State state;
                switch (boardState.charAt(i*boardSize+j)){
                    case 'W':
                        state = State.WHITE;
                        break;
                    default:
                        state = State.BLACK;
                        break;
                }
                grid.addStone(i,j,state);
            }
        }
        return grid;
    }
    public static boolean IsMoveValid(Grid grid, int x, int y, State playerToMove){
        if(grid.isSafe(x,y,playerToMove)){
            return true;
        }
        else{
            return false;
        }

    }
    public static String MakeMove(String boardState, int boardSize, int x, int y, char color) {
        Grid grid = makeGrid(boardState, boardSize);
        State playerToMove;
        switch (color) {
            case 'W':
                playerToMove = State.WHITE;
                break;
            default:
                playerToMove = State.BLACK;
                break;
        }
        if (IsMoveValid(grid, x, y, playerToMove)) {
            grid.addStone(x, y, playerToMove);
            return grid.toString();
        }
        return boardState;
    }
}
