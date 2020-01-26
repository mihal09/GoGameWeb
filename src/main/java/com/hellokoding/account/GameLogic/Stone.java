package com.hellokoding.account.GameLogic;

class Stone {

    State state;
    int row;
    int col;

    Stone(int row, int col, State state) {
        this.state = state;
        this.row = row;
        this.col = col;
    }
    public String toString(){
        switch(state){
            case BLACK:
                return "B";
            case WHITE:
                return "W";
            default:
                return "N";
        }
    }
}