package com.example.ufoodlibrary.utilities;

import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Luigi on 27/04/2016.
 */
public class RestaurantComparator implements Comparator<G3RestaurantObj> {

    private List<Comparator<G3RestaurantObj>> listComparators;

    public RestaurantComparator(Comparator<G3RestaurantObj>... comparators) {

        this.listComparators = Arrays.asList(comparators);

    }

    @Override
    public int compare(G3RestaurantObj res1, G3RestaurantObj res2) {

        for (Comparator<G3RestaurantObj> comparator : listComparators) {
            int result = comparator.compare(res1, res2);
            if (result != 0) {
                return result;
            }
        }

        return 0;
    }

    public static class NameComparator implements Comparator<G3RestaurantObj> {

        @Override
        public int compare(G3RestaurantObj lhs, G3RestaurantObj rhs) {

            return lhs.getName().compareTo(rhs.getName());

        }

    }

    public static class DistanceComparator implements Comparator<G3RestaurantObj> {

        private LatLng currentPos;

        public DistanceComparator(LatLng currentPos) {
            this.currentPos = currentPos;
        }

        @Override
        public int compare(G3RestaurantObj lhs, G3RestaurantObj rhs) {

            return Double.compare(
                    RestaurantFilters.calculateDistance(currentPos,
                            new LatLng(lhs.getAddressObj().getLatitude(), lhs.getAddressObj().getLongitude())),
                    RestaurantFilters.calculateDistance(currentPos,
                            new LatLng(rhs.getAddressObj().getLatitude(), rhs.getAddressObj().getLongitude()))
            );

        }

    }

    public static class PriceComparator implements Comparator<G3RestaurantObj> {

        @Override
        public int compare(G3RestaurantObj lhs, G3RestaurantObj rhs) {

//            G3MenuObj lhsMenu = lhs.getMenu();
//            G3MenuObj rhsMenu = rhs.getMenu();
//
//            if (lhsMenu == null && rhsMenu != null)
//                return 1;
//            if (rhsMenu != null && rhsMenu == null)
//                return -1;
//            if (lhsMenu != null && rhsMenu != null) {
//                return Double.compare(lhsMenu.getAveragePrice(), rhsMenu.getAveragePrice());
//            }
//
//            return 0;
            return Integer.valueOf(lhs.getPriceRating()).compareTo(rhs.getPriceRating());

        }

    }

    public static class RankingComparator implements Comparator<G3RestaurantObj> {

        @Override
        public int compare(G3RestaurantObj lhs, G3RestaurantObj rhs) {

            return Double.compare(calculateBayesanAvg(rhs), calculateBayesanAvg(lhs)); // Descending order!

        }

        private double calculateBayesanAvg(G3RestaurantObj res) {

            final double MIN_REVIEWS = 10;
            final double LOWER_BOUND = 1;

            double bayesanAvgScore = 0;

            double avgScore = res.getReviewsAvgScore();
            int numOfReviews = res.getNumOfReviews();

            if(numOfReviews != 0 && avgScore != 0) {
                bayesanAvgScore = (numOfReviews / (numOfReviews + MIN_REVIEWS)) * avgScore +
                        (MIN_REVIEWS / (numOfReviews + MIN_REVIEWS)) * LOWER_BOUND;
            }

            return bayesanAvgScore;

        }

    }

}
