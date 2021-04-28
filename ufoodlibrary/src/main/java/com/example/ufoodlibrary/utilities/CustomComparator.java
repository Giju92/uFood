package com.example.ufoodlibrary.utilities;


import com.example.ufoodlibrary.objects.G3OrderObj;
import java.util.Comparator;

/**
 * Created by Mattia on 12/04/2016.
 */
public class CustomComparator  implements Comparator<G3OrderObj>{
    @Override
    public int compare(G3OrderObj lhs, G3OrderObj rhs) {
        return lhs.getDate().compareTo(rhs.getDate());
    }
}

