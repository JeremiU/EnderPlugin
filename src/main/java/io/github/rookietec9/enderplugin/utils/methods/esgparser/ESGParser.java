package io.github.rookietec9.enderplugin.utils.methods.esgparser;

import com.google.gson.Gson;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 23.6.3
 * @since 23.2.8
 */
public class ESGParser {

    public static List<Pair<Material, Integer>> loot() {
        Gson gson = new Gson();
        ParsingObjects parsingObjects;
        try {
            parsingObjects = gson.fromJson(new BufferedReader(new FileReader("E:\\MCD\\[BUKKIT] 1.8\\SurvivalGames\\data\\loot_tables\\minecraft\\chests\\loot_table.json")), ParsingObjects.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        List<Pair<Material, Integer>> finalEntries = new ArrayList<>();
        List<Pair<Material, Integer>> entries = new ArrayList<>();
        for (ParsingObjects.Pool pool : parsingObjects.pools) {
            entries.clear();
            int poolCounter = Java.getRandom(pool.rolls.min, pool.rolls.max);

            for (int i = 0; i < poolCounter; i++) {
                for (ParsingObjects.Entries entry : pool.entries) {
                    int count = entry.functions != null ? (entry.functions.get(0).counts != null ? Java.getRandom(entry.functions.get(0).counts.min, entry.functions.get(0).counts.max) : entry.functions.get(0).count) : 1;
                    if (count == 0) continue;
                    Pair<Material, Integer> pair = Pair.of(Material.valueOf(entry.name.substring("minecraft:".length()).replace("en_", "_").toUpperCase()), count);
                    for (int j = 0; j < entry.weight; j++) entries.add(pair);
                }
            }
            if (entries.size() == 0) continue;
            finalEntries.add(entries.get(Java.getRandom(0, entries.size() - 1)));
        }
        return finalEntries;
    }

}
//    public static void print(String... args) {
//        Gson gson = new Gson();
//        ParsingObjects parsingObjects;
//        try {
//            parsingObjects = gson.fromJson(new BufferedReader(new FileReader("F:\\MCD\\[BUKKIT] 1.8\\SurvivalGames\\data\\loot_tables\\minecraft\\chests\\loot_table.json")), ParsingObjects.class);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        for (ParsingObjects.Pool pool : parsingObjects.pools) {
//            //            System.out.println(pool.rolls.min + "min");
//            //            System.out.println(pool.rolls.max + "max");
//            //System.out.println(pool.entries.size());
//            for (ParsingObjects.Entries entries : pool.entries) {
//                System.out.println(entries.name.substring("minecraft:".length()));
//                System.out.println(entries.weight);
//                if (entries.functions != null) {
//                    if (entries.functions.get(0).counts != null) {
//                        System.out.println(entries.functions.get(0).counts.min + " item min");
//                        System.out.println(entries.functions.get(0).counts.max + " item max");
//                    }
//                    if (entries.functions.get(0).count != 0) System.out.println(entries.functions.get(0).count + " item count");
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    public static void count(String... args) {
//        Gson gson = new Gson();
//        ParsingObjects parsingObjects;
//        try {
//            parsingObjects = gson.fromJson(new BufferedReader(new FileReader("F:\\MCD\\[BUKKIT] 1.8\\SurvivalGames\\data\\loot_tables\\minecraft\\chests\\loot_table.json")), ParsingObjects.class);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        }
//        for (int i = 0; i <= 100; i++) {
//            for (ParsingObjects.Pool pool : parsingObjects.pools) {
//                int count = Java.getRandom(pool.rolls.min, pool.rolls.max);
//                System.out.print(count + "\t");
//            }
//            System.out.println();
//        }
//    }