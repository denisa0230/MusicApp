package app.user.artistFeatures;

import lombok.Setter;
import lombok.Getter;

/**
 * This class represents an item available for purchase.
 */
public class Merchandise {

    /**
     * The name of the merchandise.
     */
    @Setter
    @Getter
    private String name;

    /**
     * The description of the merchandise.
     */
    @Setter
    @Getter
    private String description;

    /**
     * The price of the merchandise.
     */
    @Getter
    @Setter
    private Integer price;

    public Merchandise(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Returns a string representation of the merchandise,
     * including its name, price, and description.
     * @return A string representation of the merchandise.
     */
    public String toString() {
        return name + " - " + price + ":\n\t" + description;
    }
}
