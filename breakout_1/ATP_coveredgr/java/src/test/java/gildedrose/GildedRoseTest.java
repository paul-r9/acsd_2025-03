package gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GildedRoseTest {

    public static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    public static final String CONJURED_ITEM = "Conjured Mana Bun";

    private Item[] createItemArray(String itemName, int sellIn, int quality) {
        return new Item[] { new Item(itemName, sellIn, quality) };
    }

    @Test
    void LegendaryItem_QualityDoesNotDecrease() {
        // Arrange
        GildedRose sut = new GildedRose(createItemArray("Sulfuras, Hand of Ragnaros", 20, 80));

        // Act
        sut.updateQuality();

        // Assert
        assertEquals(80, sut.items[0].quality, "Quality is not decreased for this legendary item");
    }

    @Test
    void LegendaryItem_NeverHasToBeSold() {
        GildedRose sut = new GildedRose(createItemArray("Sulfuras, Hand of Ragnaros", 1, 80));
        sut.updateQuality();
        assertEquals(1, sut.items[0].sellWithinDays, "SellIn is not decreased for this legendary item");
    }

    @Test
    void LegendaryItem_NegativeSellIn_QualityUnchanged() {
        // Covers the code path where sellIn is negative, quality is positive, and *is* Legendary
        GildedRose sut = new GildedRose(createItemArray("Sulfuras, Hand of Ragnaros", -1, 80));
        sut.updateQuality();
        assertEquals(-1, sut.items[0].sellWithinDays, "SellIn is not decreased for this legendary item");
    }

    @ParameterizedTest(name = "{0} item SellIn decreases each update")
    @CsvSource({"generic item", "Aged Brie", BACKSTAGE_PASS, CONJURED_ITEM})
    void NonLegendaryItem_SellInDate_Decreases(String itemName) {
        GildedRose sut = new GildedRose(createItemArray(itemName, 8, 10));
        sut.updateQuality();
        assertEquals(7, sut.items[0].sellWithinDays, "Item sellIn date should decrease by 1 each day");
    }

    @ParameterizedTest(name = "{0} item SellIn will be negative after sellIn date reached")
    @CsvSource({"generic item", "Aged Brie", BACKSTAGE_PASS})
    void NonLegendaryItem_SellInDate_CanBeNegative(String itemName) {
        GildedRose sut = new GildedRose(createItemArray(itemName, 0, 25));
        sut.updateQuality();
        assertEquals(-1, sut.items[0].sellWithinDays, "SellIn date will go negative once sellIn date is reached");
    }

    @Test
    void GenericItem_QualityDecreasesBeforeSellInDate() {
        GildedRose sut = new GildedRose(createItemArray("generic item", 5, 10));
        sut.updateQuality();
        assertEquals(9, sut.items[0].quality, "Item quality should only decrease by 1 each day");
    }

    @Test
    void GenericItem_QualityDecreasesTwiceAsFastAfterSellInDate() {
        GildedRose sut = new GildedRose(createItemArray("generic item", 0, 10));
        sut.updateQuality();
        assertEquals(8, sut.items[0].quality, "When sellIn date is 0 then quality decreases twice as fast");
    }

    @Test
    void GenericItem_QualityNeverGoesNegative() {
        GildedRose sut = new GildedRose(createItemArray("generic item", 0, 0));
        sut.updateQuality();
        assertEquals(0, sut.items[0].quality, "Quality will not go negative once it is zero");
    }

    @Test
    void AgedBrie_QualityIncreases() {
        GildedRose sut = new GildedRose(createItemArray("Aged Brie", 5, 30));
        sut.updateQuality();
        assertEquals(31, sut.items[0].quality, "Aged Brie increases quality with age");
    }

    @ParameterizedTest(name = "{0} item Quality is Capped at 50")
    @CsvSource({"Aged Brie", BACKSTAGE_PASS})
    void NonLegendaryItem_ThatImprovesWithAge_QualityIsCappedAt50(String itemName) {
        GildedRose sut = new GildedRose(createItemArray(itemName, 5, 50));
        sut.updateQuality();
        assertEquals(50, sut.items[0].quality, "Quality has an upper limit that is not exceeded");
    }


    @Test
    void AgedBrie_QualityIncreases_EvenAfterSellInDate() {
        GildedRose sut = new GildedRose(createItemArray("Aged Brie", -1, 20));
        sut.updateQuality();
        assertEquals(22, sut.items[0].quality, "Aged Brie improves twice as fast after sellIn date (BUG?)");
    }

    @Test
    void AgedBrie_QualityIsCappedAt50_EvenWhenReallyOld() {
        GildedRose sut = new GildedRose(createItemArray("Aged Brie", -99, 50));
        sut.updateQuality();
        assertEquals(50, sut.items[0].quality, "Quality has an upper limit, even when cheese is old");
    }

    @Test
    void BackstagePass_QualityIncreasesEachDay() {
        GildedRose sut = new GildedRose(createItemArray(BACKSTAGE_PASS, 30, 23));
        sut.updateQuality();
        assertEquals(24, sut.items[0].quality, "Backstage Pass increases quality with age");
    }

    @Test
    void BackstagePass_QualityIncreasesMoreAsConcertNears() {
        GildedRose sut = new GildedRose(createItemArray(BACKSTAGE_PASS, 10, 40));
        sut.updateQuality();
        assertEquals(42, sut.items[0].quality, "Backstage Pass quality increases more when concert is near");
    }

    @Test
    void BackstagePass_QualityIncreasesMuchMoreWhenConcertIsClose() {
        GildedRose sut = new GildedRose(createItemArray(BACKSTAGE_PASS, 5, 40));
        sut.updateQuality();
        assertEquals(43, sut.items[0].quality, "Backstage Pass quality increases even more when concert is almost here");
    }

    @Test
    void BackStagePass_QualityDropsToZeroWhenConcertPasses() {
        GildedRose sut = new GildedRose(createItemArray(BACKSTAGE_PASS, 0, 50));
        sut.updateQuality();
        assertEquals(0, sut.items[0].quality, "Backstage Pass is worthless when concert has passed");
    }

    @Test
    void BackstagePass_QualityIsCappedAtMaximum() {
        GildedRose sut = new GildedRose(createItemArray(BACKSTAGE_PASS, 5, 49));
        sut.updateQuality();
        assertEquals(50, sut.items[0].quality, "Backstage pass, very close to sellIn date, is still capped at maximum quality");
    }

    @Test
    void ShopContainsMultipleItems() {
        Item[] items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                                    new Item("generic item", 10, 5)};
        GildedRose sut = new GildedRose(items);

        sut.updateQuality();

        assertAll("Each item in the shop should be updated correctly",
                () -> assertEquals(0, items[0].sellWithinDays, "Legendary item sellIn date is not changed"),
                () -> assertEquals(9, items[1].sellWithinDays, "generic item sellIn is decreased")
        );
    }

    //TODO: NEW BEHAVIOR
    // conjured items

    @Test
    void ConjuredItem_QualityDecreasesTwiceEachDay() {
        GildedRose sut = new GildedRose(createItemArray(CONJURED_ITEM, 10, 5));
        sut.updateQuality();
        assertEquals(3, sut.items[0].quality, "Conjured item decreases quality twice per day");
    }
    @Test
    void ConjuredItem_QualityDecreasesFourTimesEachDayAfterSellIn(){
        GildedRose sut = new GildedRose(createItemArray(CONJURED_ITEM, 0, 10));
        sut.updateQuality();
        assertEquals(6, sut.items[0].quality, "Conjured item decreases quality four times per day after sell in");

    }
}
