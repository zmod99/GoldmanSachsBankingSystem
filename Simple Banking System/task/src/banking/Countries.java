package banking;

public enum Countries {

    CROATIA(11),
    SLOVENIA(13),
    BULGARIA(10);

    public final int idCardLength;


    Countries(int idCardLength) {
        this.idCardLength = idCardLength;
    }



    @Override
    public String toString() {
        return this.name();
    }
}


