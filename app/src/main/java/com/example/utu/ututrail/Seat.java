package com.example.utu.ututrail;

/**
 * Class for seat. 
 */
//todo: put order in array<seat> 
public class Seat {
    private String seatNro;
    private String status;
    //private int userID = 0;

    public Seat(String seatNro, String status) {
        this.seatNro = seatNro;
        this.status = status;
    }


    public void setSeatNro(String mSeatNumero) {
        seatNro = mSeatNumero;
    }
    public void setStatus (String mStatus) { status = mStatus;
    }


    public String getSeatNro () {
        return seatNro;
    }
    public String getStatus () {
        return status;
    }


}
