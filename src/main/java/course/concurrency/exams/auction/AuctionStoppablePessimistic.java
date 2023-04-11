package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private  Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(-1l, -1l, -1l);
    private volatile boolean stopped = false;

    private final Object lock = new Object();

    public boolean propose(Bid bid) {
        if (bid.getPrice() > latestBid.getPrice() && !stopped) {
            synchronized (lock) {
                if (bid.getPrice() > latestBid.getPrice() && !stopped) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        stopped = true;
        return latestBid;
    }
}
