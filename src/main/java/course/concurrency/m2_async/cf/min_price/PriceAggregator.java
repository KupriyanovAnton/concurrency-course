package course.concurrency.m2_async.cf.min_price;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceAggregator {

    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {

        List<CompletableFuture<Double>> futures = shopIds.stream()
                .map(shopId -> CompletableFuture.supplyAsync(() -> priceRetriever.getPrice(itemId, shopId)))
                .collect(Collectors.toList());

        try {
            CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[]{}))
                    .get(3, TimeUnit.SECONDS);

        } catch (Exception e) {}

        return futures.stream()
                .filter(Future::isDone)
                .map(CompletableFuture::join)
                .min(Double::compareTo)
                .orElse(Double.NaN);

    }
}