package com.hellokoding.account.model;

import javax.persistence.*;

@Entity
@Table(name = "games", schema = "accounts")
public class GamesEntity {
    private int id;
    private Integer boardSize;
    private Integer idPlayerOne;
    private Integer idPlayerTwo;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "boardSize", nullable = true)
    public Integer getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(Integer boardSize) {
        this.boardSize = boardSize;
    }

    @Basic
    @Column(name = "idPlayerOne", nullable = true)
    public Integer getIdPlayerOne() {
        return idPlayerOne;
    }

    public void setIdPlayerOne(Integer idPlayerOne) {
        this.idPlayerOne = idPlayerOne;
    }

    @Basic
    @Column(name = "idPlayerTwo", nullable = true)
    public Integer getIdPlayerTwo() {
        return idPlayerTwo;
    }

    public void setIdPlayerTwo(Integer idPlayerTwo) {
        this.idPlayerTwo = idPlayerTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamesEntity that = (GamesEntity) o;

        if (id != that.id) return false;
        if (boardSize != null ? !boardSize.equals(that.boardSize) : that.boardSize != null) return false;
        if (idPlayerOne != null ? !idPlayerOne.equals(that.idPlayerOne) : that.idPlayerOne != null) return false;
        if (idPlayerTwo != null ? !idPlayerTwo.equals(that.idPlayerTwo) : that.idPlayerTwo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (boardSize != null ? boardSize.hashCode() : 0);
        result = 31 * result + (idPlayerOne != null ? idPlayerOne.hashCode() : 0);
        result = 31 * result + (idPlayerTwo != null ? idPlayerTwo.hashCode() : 0);
        return result;
    }
}
