public class BidId extends BidRequiredProperties {
    private int id;

    public BidId() {
        super();
    }

    /*Method Used to Set the Bid Id*/
    public void setId(int idValue) {
        this.id = idValue;
    }

    /*Method Used to get the Bid Id*/
    public int getId() {
        return this.id;
    }
}

