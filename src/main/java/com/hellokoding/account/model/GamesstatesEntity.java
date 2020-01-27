package com.hellokoding.account.model;

import javax.persistence.*;

@Entity
@Table(name = "gamesstates", schema = "accounts")
public class GamesstatesEntity {
    private int id;
    private Integer gameId;
    private String grid;
    private String lastMove;
    private String penultimateMove;
    private String nextMove;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "gameID", nullable = true)
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Basic
    @Column(name = "grid", nullable = true, length = 361)
    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    @Basic
    @Column(name = "lastMove", nullable = true, length = 361)
    public String getLastMove() {
        return lastMove;
    }

    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }

    @Basic
    @Column(name = "penultimateMove", nullable = true, length = 361)
    public String getPenultimateMove() {
        return penultimateMove;
    }

    public void setPenultimateMove(String penultimateMove) {
        this.penultimateMove = penultimateMove;
    }

    @Basic
    @Column(name = "nextMove", nullable = true, length = 1)
    public String getNextMove() {
        return nextMove;
    }

    public void setNextMove(String nextMove) {
        this.nextMove = nextMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamesstatesEntity that = (GamesstatesEntity) o;

        if (id != that.id) return false;
        if (gameId != null ? !gameId.equals(that.gameId) : that.gameId != null) return false;
        if (grid != null ? !grid.equals(that.grid) : that.grid != null) return false;
        if (lastMove != null ? !lastMove.equals(that.lastMove) : that.lastMove != null) return false;
        if (penultimateMove != null ? !penultimateMove.equals(that.penultimateMove) : that.penultimateMove != null);
        if (nextMove != null ? !nextMove.equals(that.nextMove) : that.nextMove != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (gameId != null ? gameId.hashCode() : 0);
        result = 31 * result + (grid != null ? grid.hashCode() : 0);
        result = 31 * result + (lastMove != null ? lastMove.hashCode() : 0);
        result = 31 * result + (penultimateMove != null ? penultimateMove.hashCode() : 0);
        result = 31 * result + (nextMove != null ? nextMove.hashCode() : 0);
        return result;
    }


}
