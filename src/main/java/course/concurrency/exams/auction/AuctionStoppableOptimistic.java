package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;
    private volatile boolean isStopped = false;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
        latestBid = new AtomicMarkableReference<>(new Bid(-1l, -1l, -1l), isStopped);
    }

    private final AtomicMarkableReference<Bid> latestBid;

    public boolean propose(Bid bid) {

        Bid currentBid;
        do {
            currentBid = latestBid.getReference();
            if (bid.getPrice() <= currentBid.getPrice()) {
                return false;
            }
        } while (latestBid.compareAndSet(currentBid, bid, isStopped, isStopped));

        return false;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public synchronized Bid stopAuction() {
        isStopped = true;
        return latestBid.getReference();
    }
}
