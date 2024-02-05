package io.github.rookietec9.enderplugin.utils.methods.esgparser;

import java.util.List;

/**
 * @author Jeremi
 * @version 23.2.8
 * @since 23.2.8
 */
public class ParsingObjects {

    public List<Pool> pools;

    public static class Pool {
        public Rolls rolls;
        public List<Entries> entries;
    }

    public static class Rolls {
        public int min, max;
    }

    public static class Entries {
        public int weight;
        public String name;
        public List<Function> functions;
    }

    public static class Function {
        public Rolls counts;
        public int count;
    }
}