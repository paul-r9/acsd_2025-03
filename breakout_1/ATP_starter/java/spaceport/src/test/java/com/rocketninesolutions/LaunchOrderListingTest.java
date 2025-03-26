package com.rocketninesolutions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class LaunchOrderListingTest {
    //TODO - Use the Stub Recipe to test that launches are sorted correctly
    @Test
    void LaunchesAre_SortedByDestination_DestinationsAreUnique() {
        // Step 1. Create LaunchInfoProviderStub (that implements ISpacelineLaunchInfoProvider)
        List<LaunchInfo> unorderedLaunches = new ArrayList<LaunchInfo>();
        List<LaunchInfo> expectedLaunches = new ArrayList<LaunchInfo>();

        LaunchInfo launchA = new LaunchInfo(UUID.randomUUID());
        LaunchInfo launchB = new LaunchInfo(UUID.randomUUID());

        launchA.setDestination("A");
        launchB.setDestination("B");

        unorderedLaunches.add(launchB); //explicitly in wrong order
        unorderedLaunches.add(launchA);

        expectedLaunches.add(launchA);
        expectedLaunches.add(launchB);

        // Step 2 & 3 & 4. Create SUT - SpaceportDepartureBoard, using Constructor Injection
        // Exercising this behavior happens during construction of the System Under Test
        ISpacelineLaunchInfoProvider mock = new MockProvider(unorderedLaunches);
        SpaceportDepartureBoard sut = new SpaceportDepartureBoard(mock);
        List<LaunchInfo> listToBeTested = sut.getLaunchList();

        // Step 5. Verify the results are sorted correctly
        Assertions.assertAll("expect to be sorted by destination",
                () -> Assertions.assertEquals("A", listToBeTested.get(0).getDestination()),
                () -> Assertions.assertEquals("B", listToBeTested.get(1).getDestination())
        );
    }

    private class MockProvider implements ISpacelineLaunchInfoProvider {
        private final List<LaunchInfo> launches;

        public MockProvider(List<LaunchInfo> unorderedLaunches) {
            this.launches = unorderedLaunches;
        }

        @Override
        public List<LaunchInfo> getCurrentLaunches() {
            return launches;
        }
    }
}
