package main;

import beans.graph.Network;
import preprocessor.constractor.NetworkConstractor;
import generator.ItemGenerator;
import generator.PairItemGenerator;
import generator.TransGenerator;
import util.Constants;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Main {

    private Network network;

    public void init() {
        try {
            this.network = NetworkConstractor.constract(Constants._src);

            ItemGenerator.generateItems();
            PairItemGenerator.generatePairItems();
            TransGenerator.generateTrans();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
