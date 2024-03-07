package app.user.artistFeatures;

import lombok.Getter;
import lombok.Setter;


/**
 * A class encapsulating the monetization of an artist on the platform.
 */
@Getter
@Setter
public class ArtistAccount {
    private Double merchRevenue;
    private Double songRevenue;
    private int ranking;
    private String mostProfitableSong;

    public ArtistAccount() {
        songRevenue = 0.0;
        merchRevenue = 0.0;
        ranking = 0;
        mostProfitableSong = null;
    }

    /**
     * Add profit from bought merch to artist account.
     * @param price price of bought merch
     */
    public void increaseMerchRevenue(final Integer price) {
        merchRevenue += price;
    }

    /**
     * Add profit from played song to artist account.
     * @param money the revenue received
     */
    public void increaseSongRevenue(final Double money) {
        songRevenue += money;
    }
}
