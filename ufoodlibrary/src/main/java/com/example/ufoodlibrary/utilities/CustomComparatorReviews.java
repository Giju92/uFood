package com.example.ufoodlibrary.utilities;

import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;

import java.util.Comparator;

/**
 * Created by Mattia on 10/05/2016.
 */
public class CustomComparatorReviews implements Comparator<G3ReviewObj>{

    @Override
    public int compare(G3ReviewObj lhs, G3ReviewObj rhs) {

        return rhs.getDate().compareTo(lhs.getDate());

    }
}
