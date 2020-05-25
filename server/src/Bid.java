public class Bid extends BidId {
    private int id;
    private int auctionId;
    private float bidAmount;
    private int bidderId;

    public Bid(float bidAmountValue, int bidderIdValue) {
        this.bidAmount = bidAmountValue;
        this.bidderId = bidderIdValue;
    }
    public void setId(int idValue) {
        this.id = idValue;
    }
    /*Method Used to get the Auction Id*/
    public void setAuctionId(int auctionIdValue) {
        this.auctionId = auctionIdValue;
    }

    /*Method Used to set the Bid Amount*/
    public void setBidAmount(float bidAmountValue) {
        this.bidAmount = bidAmountValue;
    }

    /*Method Used to set the Bidder Id*/
    public void setBidderId(int bidderIdValue) {
        this.bidderId = bidderIdValue;
    }

    /*Method Used to get the Bid Id*/
    public int getId() {
        return this.id;
    }

    /*Method Used to get the Auction Id*/
    public int getAuctionId() {
        return this.auctionId;
    }

    /*Method Used to get the Bid Amount*/
    public float getBidAmount() {
        return this.bidAmount;
    }

    /*Method Used to get the Bidder Id*/
    public int getBidderId() {
        return this.bidderId;
    }
}

