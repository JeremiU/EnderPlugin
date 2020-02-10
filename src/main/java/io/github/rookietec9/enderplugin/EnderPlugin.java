package io.github.rookietec9.enderplugin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.commands.advFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.editFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.dataFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.setFuncs.MuteCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.setFuncs.NickCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.ESGChestCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.ESGCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.GetKitCommand;
import io.github.rookietec9.enderplugin.commands.games.HubCommand;
import io.github.rookietec9.enderplugin.commands.games.SelectBladeCommand;
import io.github.rookietec9.enderplugin.events.games.KitKillEvent;
import io.github.rookietec9.enderplugin.events.games.Run.Bows;
import io.github.rookietec9.enderplugin.events.games.SumoFailEvent;
import io.github.rookietec9.enderplugin.events.games.booty.*;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFChestCheck;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFTeamClick;
import io.github.rookietec9.enderplugin.events.games.esg.*;
import io.github.rookietec9.enderplugin.events.games.hidenseek.FindEvent;
import io.github.rookietec9.enderplugin.events.games.hunger.*;
import io.github.rookietec9.enderplugin.events.games.murder.DeathEvent;
import io.github.rookietec9.enderplugin.events.games.murder.EscapeEvent;
import io.github.rookietec9.enderplugin.events.games.parkour.Click;
import io.github.rookietec9.enderplugin.events.games.parkour.Stomp;
import io.github.rookietec9.enderplugin.events.games.spleef.JoinEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.MineEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.WinEvent;
import io.github.rookietec9.enderplugin.events.games.tnt.TNTBreakEvent;
import io.github.rookietec9.enderplugin.events.games.tnt.TNTEffectEvent;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsBlades;
import io.github.rookietec9.enderplugin.events.hub.*;
import io.github.rookietec9.enderplugin.events.main.*;
import io.github.rookietec9.enderplugin.xboards.HubBoard;
import io.github.rookietec9.enderplugin.xboards.Log;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.rookietec9.enderplugin.EnderPlugin.Hashmaps.*;

/**
 * @author Jeremi
 * @version 16.7.4
 * @since 0.0.1
 */
public class EnderPlugin extends JavaPlugin {

    private static EnderPlugin instance;

    public static EnderPlugin getInstance() {
        return instance;
    }

    public void onEnable() {
        runUpdate();
        EnderPlugin.instance = this;
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        saveDefaultConfig();
        new Config(true, "", "data.yml", this).saveDefaultYaml();
        new Config(true, "", "games.yml", this).saveDefaultYaml();
        newRegisterWorlds();
        loadWeather();
        newerRegisterCommands();
        newerRegisterEvents();
        registerStands();

        for (Player player: Bukkit.getOnlinePlayers()) {
            for (HashMap<OfflinePlayer, Integer> hashMap : new HashMap[]
                    {tempBootyKills, tempBootyDeaths, tempWizardKills, tempWizardDeaths, tempCTFKills, tempCTFDeaths, tempSpleefRounds, tempSpleefBlocks, tempParkourAttempts, tempWizardStreak})
                hashMap.put(player, 0);

            tempParkourLevel.put(player, 1 + "/4");
            tempParkourBlock.put(player, 0 + "/17");

            tempWizardBlade.put(player, "none");
        }
    }

    private void registerStands() {
        for (int i = 0; i < Bukkit.getOfflinePlayers().length; i++) {
            ArmorStand armorStand = (ArmorStand) Bukkit.getWorld(Utils.Reference.Worlds.HUB).spawnEntity(new Location(Bukkit.getWorld(Utils.Reference.Worlds.HUB), -255,65,-67), EntityType.ARMOR_STAND);
            armorStand.setMarker(true);
        }
    }

    private void newerRegisterCommands() {
        EndExecutor[] advFuncs = {new BirthdayCommand(), new CountCommand(), new HologramCommand(), new KYSCommand(), new PluginsCommand(), new SignCommand(), new StalkCommand(), new WarpCommand(),};
        EndExecutor[] invFuncs = {new ClearCommand(), new HatCommand(), new HideFlagsCommand(), new PaintCommand(), new RenameCommand(), new RepairCommand(), new UnbreakableCommand(), new AnvilCommand(), new BreweryCommand(), new CraftCommand(), new EnchantCommand(), new EnderChestCommand(),};
        EndExecutor[] playerFuncs = {new ExtinguishCommand(), new FinishCommand(), new FlyCommand(), new GetChestCommand(), new GMCommand(), new GodCommand(), new HealCommand(), new JumpCommand(), new KickCommand(), new KillCommand(), new LocCommand(),};
        EndExecutor[] txtFuncs = {new DetailsCommand(), new EffectsListCommand(), new GetColorCommand(), new ListCommand(), new UUIDCommand(), new MuteCommand(), new NickCommand(), new AnonActionCommand(), new AnonCastCommand(), new AnonCommand(), new ChatClearCommand(), new ColorsCommand(), new FakeCommand(), new HelpCommand(), new HugCommand(), new TitleCommand(), new VersionCommand(), new YTCommand(),};
        EndExecutor[] gameFuncs = {new ESGChestCommand(), new ESGCommand(), new GetKitCommand(), new HubCommand(), new SelectBladeCommand(),};

        EndExecutor[][] endExecutors = {advFuncs, invFuncs, playerFuncs, txtFuncs, gameFuncs};

        for (int i = 0; i < endExecutors.length; ++i) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < endExecutors[i].length; ++j) {
                stringBuilder.append(endExecutors[i][j].getClass().getName().substring(endExecutors[i][j].getClass().getName().lastIndexOf(".") + 1));
                stringBuilder.append(" , ");

                this.getCommand(endExecutors[i][j].commandName()).setExecutor(endExecutors[i][j]);
                this.getCommand(endExecutors[i][j].commandName()).setTabCompleter(endExecutors[i][j]);
            }
            debug("Registered Command Group # (" + (((String.valueOf((i + 1)).length() < 2 ? ("0" + String.valueOf((i + 1))) : String.valueOf((i + 1))) + "/" + endExecutors.length + ") : "
                    + stringBuilder.toString().substring(0, stringBuilder.toString().length() - " , ".length()))));
        }
    }

    private void loadWeather() {
        for (World world : Bukkit.getWorlds()) world.setStorm(false);
    }

    private void newerRegisterEvents() {
        Listener[] mainListeners = {new MainJoinEvent(), new MainDeath(), new MainTalkEvent(), new MainRenameEvent(), new MainLeaveEvent(), new MainHungerEvent(), new ArrowShoot()};
        Listener[] gameBootyListeners = {new BootyClick(), new BootyJump(), new BootyTeam(), new BootyFrame(), new Stomp(), new BootyDeath()};
        Listener[] gameESGListeners = {new ESGShopEvent(), new ESGBushEvent(), new ESGStartChooseEvent(), new ESGTargetHitEvent(), new ESGTargetEvent(), new ESGSnowNoEvent(), new ESGEggEvent()};
        Listener[] gameHungerListeners = {new BroadcastEnchEvent(), new BroadcastKillEvent(), new BroadcastLavaEvent(), new BroadcastMineEvent(), new BroadcastSpawnEvent()};
        Listener[] gameTNTListeners = {new TNTEffectEvent(), new TNTBreakEvent()};
        Listener[] gameCTFListeners = {new CTFChestCheck(), new CTFTeamClick()};
        Listener[] gameWizardListeners = {new WizardsBlades()};
        Listener[] gameSpleefListeners = {new JoinEvent(), new MineEvent(), new WinEvent()};
        Listener[] gameHideListeners = {new FindEvent(), new io.github.rookietec9.enderplugin.events.games.hidenseek.JoinEvent()};
        Listener[] gameMurderListeners = {new DeathEvent(), new io.github.rookietec9.enderplugin.events.games.murder.JoinEvent(), new EscapeEvent()};
        Listener[] gameOtherListeners = {new SumoFailEvent(), new Click(), new KitKillEvent(), new Bows()};
        Listener[] hubListeners = {new HubShieldEvent(), new GoUp(), new StopWeather(), new NoDrop(), new DoubleJump(), new Toggle(), new Log()};
        Listener[][] listeners = {
                gameBootyListeners, gameESGListeners, gameHungerListeners, gameTNTListeners, gameCTFListeners, gameWizardListeners, gameSpleefListeners,
                gameHideListeners, gameMurderListeners, gameOtherListeners, mainListeners, hubListeners
        };

        for (int i = 0; i < listeners.length; ++i) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < listeners[i].length; ++j) {
                stringBuilder.append(listeners[i][j].getClass().getName().substring(listeners[i][j].getClass().getName().lastIndexOf(".") + 1));
                stringBuilder.append(" , ");
                Bukkit.getPluginManager().registerEvents(listeners[i][j], this);
            }
            debug("Registered Event Group # (" + (((String.valueOf((i + 1)).length() < 2 ? ("0" + String.valueOf((i + 1))) : String.valueOf((i + 1))) + "/" + listeners.length + ") : "
                    + stringBuilder.toString().substring(0, stringBuilder.toString().length() - " , ".length()))));
        }
    }

    private void newRegisterWorlds() {
        if (this.getConfig().getStringList("worlds") == null) {
            debug("Returning since no worlds were found!");
            return;
        }
        for (final String world : this.getConfig().getStringList("worlds")) {
            try {
                if (world == null) {
                    continue;
                }
                Bukkit.createWorld(new WorldCreator(world));
            } catch (NullPointerException e) {
                if (debug(world) != 1 && debug(world) != 4) {
                    Bukkit.getLogger().warning(world + " could not be loaded to due chunk/other errors!");
                    Bukkit.getLogger().info("Load" + world + " in vanilla, then copy it to resolve the issue. Note this might corrupt your world due to the errors.");
                    if (debug(world) != 0) {
                        debug("[WARNING] : " + world + " could not be loaded to due chunk/other errors!");
                        debug("Load" + world + " in vanilla, then copy it to resolve the issue. Note this might corrupt your world due to the errors.");
                    }
                } else {
                    debug("[WARNING] : " + world + " could not be loaded to due chunk/other errors!");
                    debug("Load" + world + " in vanilla, then copy it to resolve the issue. Note this might corrupt your world due to the errors.");
                }
            }
            debug("Loaded world : " + world);
        }
    }

    public void onDisable() {
        EnderPlugin.instance = null;
    }

    public int debug(final String msg) {
        final int i = this.getConfig().getInt("debug");
        if (i == 0) return i;
        if (i == 1) {
            Bukkit.getLogger().info(ChatColor.stripColor(msg));
            return i;
        }
        if (i == 2) {
            for (final OfflinePlayer p : Bukkit.getOperators()) {
                if (p.isOnline()) {
                    ((Player) p).sendMessage(ChatColor.AQUA + msg);
                }
            }
            return i;
        }
        if (i == 3) {
            Bukkit.broadcastMessage(ChatColor.AQUA + msg);
            return i;
        }
        if (i == 4) {
            for (final OfflinePlayer p : Bukkit.getOperators()) {
                if (p.isOnline()) {
                    ((Player) p).sendMessage(ChatColor.AQUA + msg);
                }
            }
            Bukkit.getLogger().info(ChatColor.stripColor(msg));
            return i;
        }
        return -2600;
    }

    private void runUpdate() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            long newTicks = ManagementFactory.getRuntimeMXBean().getUptime();
            for (Player player : Bukkit.getWorld(Utils.Reference.Worlds.HUB).getPlayers()) {
                HubBoard board = new HubBoard(player);
                board.changeTicks(newTicks);
            }
        }, Math.abs((ManagementFactory.getRuntimeMXBean().getUptime() / 1000) - 1200), 600L);
    }

    public static class Hashmaps {
        public static Multimap<Player, EntityDamageEvent> damageList = HashMultimap.create();
        public static Map<EntityDamageEvent, Long> damageTimeList = new HashMap<>();

        public static final HashMap<OfflinePlayer, Integer> tempBootyKills = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempBootyDeaths = new HashMap<>();

        public static final HashMap<OfflinePlayer, Integer> tempCTFKills = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempCTFDeaths = new HashMap<>();

        public static final HashMap<OfflinePlayer, String> tempParkourLevel = new HashMap<>();
        public static final HashMap<OfflinePlayer, String> tempParkourBlock = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempParkourAttempts = new HashMap<>();


        public static final HashMap<OfflinePlayer, String> tempWizardBlade = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempWizardKills = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempWizardDeaths = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempWizardStreak = new HashMap<>();

        public static final HashMap<OfflinePlayer, Integer> tempSpleefBlocks = new HashMap<>();
        public static final HashMap<OfflinePlayer, Integer> tempSpleefRounds = new HashMap<>();


        public static OfflinePlayer ctfBlueHolder = null;
        public static OfflinePlayer ctfRedHolder = null;
        public static boolean ctfRedSafe = true;
        public static boolean ctfBlueSafe = true;

        public static int hoodHideTicks = 60;
        public static int hoodFindTicks = 180;
        public static int prisonTicks = 180;
        public static int spleefTicks = 0;

        public static boolean hoodGame = false;
        public static boolean hoodHiding = false;

        public static int spleefLeft = 0;

        public static int redScore = 0;
        public static int blueScore = 0;

        public static boolean spleefEvent1 = false;
        public static boolean spleefEvent2 = false;

        public static int spleefEv1ID = 0;
        public static int spleefEv2ID = 0;

        public static List<Player> prisonCaughtList = new ArrayList<>();
        public static List<Player> prisonEscapeList = new ArrayList<>();
    }
}