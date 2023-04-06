package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private AtomicReference<Bid> latestBidAtomic = new AtomicReference<>(new Bid(-1l, -1l, -1l));

    public boolean propose(Bid bid) {

        Bid latestBid;
        boolean successfullySet = false;
        do {
            latestBid = latestBidAtomic.get();
            if (bid.getPrice() > latestBid.getPrice()) {
                notifier.sendOutdatedMessage(latestBid);
                successfullySet = true;
            } else {
                successfullySet = false;
                break;
            }
        } while (!latestBidAtomic.compareAndSet(latestBid, bid));


        return successfullySet;
    }

    public Bid getLatestBid() {
        return latestBidAtomic.get();
    }
}
