class Item {
    constructor(name, sellIn, quality) {
        this.name = name;
        this.sellIn = sellIn;
        this.quality = quality;
    }
}

class Shop {
    // static #STANDARD_MAX_QUALITY = 50;
    constructor(items = []) {
        this.items = items;
        this.STANDARD_MAX_QUALITY = 50;
        this.BRIE = "Aged Brie";
        this.BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert";
    }

    decreaseConjuredQuality(i) {
        if (this.items[i].sellIn > 0) {
            this.items[i].quality -= 2;
        } else {
            this.items[i].quality -= 4;
        }
    }
    updateQuality() {
        for (let i = 0; i < this.items.length; i++) {
            if (this.items[i].name === "conjured item") {
                this.decreaseConjuredQuality(i);
            } else {
                this.updateStandardItems(i);
                if (this.items[i].name != "Sulfuras, Hand of Ragnaros") {
                    this.items[i].sellIn = this.items[i].sellIn - 1;
                }
                if (this.items[i].sellIn < 0) {
                    if (this.items[i].name != this.BRIE) {
                        if (this.items[i].name != this.BACKSTAGE) {
                            this.decreaseQuality(i);
                        } else {
                            this.items[i].quality = 0;
                        }
                    } else {
                        this.increaseQualityLessThan50(i);
                    }
                }
            }
        }

        return this.items;
    }

    decreaseQuality(i) {
        if (this.items[i].quality > 0) {
            if (this.items[i].name != "Sulfuras, Hand of Ragnaros") {
                this.items[i].quality = this.items[i].quality - 1;
            }
        }
    }

    updateStandardItems(i) {
        if (
            this.items[i].name != this.BRIE &&
            this.items[i].name != "Backstage passes to a TAFKAL80ETC concert"
        ) {
            this.decreaseQuality(i);
        } else {
            if (this.items[i].quality < this.STANDARD_MAX_QUALITY) {
                this.items[i].quality = this.items[i].quality + 1;
                if (
                    this.items[i].name ==
                    "Backstage passes to a TAFKAL80ETC concert"
                ) {
                    if (this.items[i].sellIn < 11) {
                        this.increaseQualityLessThan50(i);
                    }
                    if (this.items[i].sellIn < 6) {
                        this.increaseQualityLessThan50(i);
                    }
                }
            }
        }
    }

    increaseQualityLessThan50(i) {
        if (this.items[i].quality < this.STANDARD_MAX_QUALITY) {
            this.items[i].quality = this.items[i].quality + 1;
        }
    }
}

module.exports = {
    Item,
    Shop,
};
