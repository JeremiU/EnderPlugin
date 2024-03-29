package io.github.rookietec9.enderplugin.configs;

/**
 * @author Jeremi
 * @version 25.2.0
 * @since 21.3.4
 */
public enum DataType {

    BOOTYKILLS("booty.kills.{PATH}", 0),
    BOOTYDEATHS("booty.deaths.{PATH}", 0),

    WIZARDKILLS("wizard.kills.{PATH}", 0),
    WIZARDDEATHS("wizard.deaths.{PATH}", 0),

    CTFKILLS("ctf.kills.{PATH}", 0),
    CTFDEATHS("ctf.deaths.{PATH}", 0),
    CTFCAPTURES("ctf.captures.{PATH}", 0),
    CTFPOINTS("ctf.points.{PATH}", 0),
    CTFWINS("ctf.wins.{PATH}", 0),
    CTFLOSSES("ctf.losses.{PATH}", 0),

    SPLEEFWINS("spleef.wins.{PATH}", 0),
    SPLEEFLOSSES("spleef.losses.{PATH}", 0),
    SPLEEFBLOCKS("spleef.blocks.{PATH}", 0),

    MURDERWINS("murder.wins.{PATH}", 0),
    MURDERLOSSES("murder.losses.{PATH}", 0),

    HOODWINS("hood.wins.{PATH}", 0),
    HOODLOSSES("hood.losses.{PATH}", 0);

    private final String path;
    private final Object def;

    DataType(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    public String getModifiedPath(String modVal) {
        return path.replace("{PATH}", modVal);
    }

    public Object getDef() {
        return def;
    }
}