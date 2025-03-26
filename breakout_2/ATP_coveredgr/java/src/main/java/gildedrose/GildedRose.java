package gildedrose;

class GildedRose {
    public static final String HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros";
    public static final String PASSES = "Backstage passes to a TAFKAL80ETC concert";
    public static final String AGED_BRIE = "Aged Brie";
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (!item.name.equals(AGED_BRIE)
                    && !item.name.equals(PASSES)) {
                if (item.quality > 0) {
                    doesSomething(item);
                }
            } else {
                if (item.quality < 50) {
                    extracted(item);
                }
            }

            if (!item.name.equals(HAND_OF_RAGNAROS)) {
                item.sellIn = item.sellIn - 1;
            }

            if (item.sellIn < 0) {
                if (!item.name.equals(AGED_BRIE)) {
                    if (!item.name.equals(PASSES)) {
                        if (item.quality > 0) {
                            doesSomething(item);
                        }
                    } else {
                        item.quality = item.quality - item.quality;
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        }
    }

    private static void extracted(Item item) {
        item.quality = item.quality + 1;

        if (item.name.equals(PASSES)) {
            if (item.sellIn < 11) {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }

            if (item.sellIn < 6) {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }
        }
    }

    private static void doesSomething(Item item) {
        if (!item.name.equals(HAND_OF_RAGNAROS)) {
            item.quality = item.quality - 1;
        }
    }
}
