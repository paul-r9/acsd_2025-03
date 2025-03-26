package gildedrose;

class GildedRose {
    public static final String AGED_BRIE = "Aged Brie";
    public static final String PASSES = "Backstage passes to a TAFKAL80ETC concert";
    public static final String HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    public static final int LOWEST_QUALITY = 0;
    public static final int HIGHEST_QUALITY = 50;
    public static final int ITEM_EXPIRES_SOON = 11;
    public static final int ITEM_EXPIRES_SOONER = 6;
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = LOWEST_QUALITY; i < items.length; i++) {
            Item item = items[i];
            if (isConjuredItem(item)) {
                if (item.sellWithinDays == 0){
                    item.quality = item.quality - 4;
                }
                else{
                    item.quality = item.quality - 2;
                }
                item.sellWithinDays = item.sellWithinDays - 1;
            }
            else{
                if (!item.name.equals(AGED_BRIE)
                        && !item.name.equals(PASSES)) {
                    compareQuality(item);
                } else {
                    updateQualityForAgedBrieAndBackstagePasses(item);
                }

                if (!item.name.equals(HAND_OF_RAGNAROS)) {
                    item.sellWithinDays = item.sellWithinDays - 1;
                }

                if (item.sellWithinDays < LOWEST_QUALITY) {
                    if (!item.name.equals(AGED_BRIE)) {
                        if (!item.name.equals(PASSES)) {
                            compareQuality(item);
                        } else {
                            item.quality = item.quality - item.quality;
                        }
                    } else {
                        if (item.quality < HIGHEST_QUALITY) {
                            item.quality = item.quality + 1;
                        }
                    }
                }
            }

        }
    }

    private static void compareQuality(Item item) {
        if (item.quality > LOWEST_QUALITY) {
            if (!item.name.equals(HAND_OF_RAGNAROS)) {
                item.quality = item.quality - 1;
            }
        }
    }


    private static void updateQualityForAgedBrieAndBackstagePasses(Item item) {
        if (item.quality < HIGHEST_QUALITY) {
            item.quality = item.quality + 1;

            if (item.name.equals(PASSES)) {
                if (item.sellWithinDays < ITEM_EXPIRES_SOON) {
                    if (item.quality < HIGHEST_QUALITY) {
                        item.quality = item.quality + 1;
                    }
                }

                if (item.sellWithinDays < ITEM_EXPIRES_SOONER) {
                    if (item.quality < HIGHEST_QUALITY) {
                        item.quality = item.quality + 1;
                    }

                }
            }
        }
    }
    private static boolean isConjuredItem(Item item) {
        return item.name.contains("Conjured");

    }
}
