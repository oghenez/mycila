package com.mycila.cards;

/**
 * @author Mathieu Carbou
 */
public final class HandFilters {

    private static final HandFilter ALL = new HandFilter() {
        public boolean accept(Hand hand) {
            return true;
        }
    };

    private static final HandFilter NONE = new HandFilter() {
        public boolean accept(Hand hand) {
            return false;
        }
    };

    private HandFilters() {
    }

    public static HandFilter all() {
        return ALL;
    }

    public static HandFilter none() {
        return NONE;
    }

}
