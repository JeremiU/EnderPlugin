package io.github.rookietec9.enderplugin;

import io.github.rookietec9.enderplugin.commandgroups.*;
import io.github.rookietec9.enderplugin.commands.advFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.ChatClearCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.HelpCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.ESGCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.GetKitCommand;
import io.github.rookietec9.enderplugin.commands.games.SelectBladeCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.HatCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.RepairCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.SkullCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.SkullLinkCommand;
import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.configs.associates.Associate;
import io.github.rookietec9.enderplugin.configs.associates.Lang;
import io.github.rookietec9.enderplugin.entities.*;
import io.github.rookietec9.enderplugin.entities.targetHandlers.TargetRedirectEvent;
import io.github.rookietec9.enderplugin.events.games.SumoFailEvent;
import io.github.rookietec9.enderplugin.events.games.booty.*;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFChestCheck;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFJoinEvent;
import io.github.rookietec9.enderplugin.events.games.esg.ESGBushEvent;
import io.github.rookietec9.enderplugin.events.games.esg.ESGEggEvent;
import io.github.rookietec9.enderplugin.events.games.esg.ESGStartChooseEvent;
import io.github.rookietec9.enderplugin.events.games.hidenseek.FindEvent;
import io.github.rookietec9.enderplugin.events.games.murder.DeathEvent;
import io.github.rookietec9.enderplugin.events.games.murder.EscapeEvent;
import io.github.rookietec9.enderplugin.events.games.parkour.Click;
import io.github.rookietec9.enderplugin.events.games.run.Bows;
import io.github.rookietec9.enderplugin.events.games.spleef.CraftEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.JoinEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.MineEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.WinEvent;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsBlades;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsSign;
import io.github.rookietec9.enderplugin.events.hub.BookOpenEvent;
import io.github.rookietec9.enderplugin.events.hub.DoubleJump;
import io.github.rookietec9.enderplugin.events.hub.HubShieldEvent;
import io.github.rookietec9.enderplugin.events.hub.TogglerEvent;
import io.github.rookietec9.enderplugin.events.inventoryclickers.CTFSwitchKitEvent;
import io.github.rookietec9.enderplugin.events.inventoryclickers.TeleporterClickEvent;
import io.github.rookietec9.enderplugin.events.inventoryclickers.WizardClickEvent;
import io.github.rookietec9.enderplugin.events.main.*;
import io.github.rookietec9.enderplugin.scoreboards.HubBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.*;
import io.github.rookietec9.enderplugin.utils.methods.FinderBase;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jeremi
 * @version 22.8.3
 * @since 0.0.1
 */
public class EnderPlugin extends JavaPlugin {

    public static FinderBase hoodBase = new FinderBase(false);
    public static FinderBase murderBase = new FinderBase(true);
    public static Associate games = null;
    private static EnderPlugin instance;
    private static Lang lang;
    private static Scheduler scheduler;

    public static EnderPlugin getInstance() {
        return instance;
    }

    public static Lang serverLang() {
        return lang;
    }

    public static Scheduler scheduler() {
        return scheduler;
    }

    private static void registerEntity(MobInfo mobInfo) {
        registerEntity(mobInfo.getName(), mobInfo.getType().getTypeId(), mobInfo.getIClass());
        System.out.print("Registered entity " + mobInfo.getName());
    }

    private static void registerEntity(String name, int id, Class<? extends EntityInsentient> customClass) {
        try {
            List<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }
            if ((dataMap.get(2)).containsKey(id)) {
                (dataMap.get(0)).remove(name);
                (dataMap.get(2)).remove(id);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEnable() {
        EnderPlugin.instance = this;
        EnderPlugin.games = new Associate(new Config(true, "", "games.yml", EnderPlugin.getInstance()));
        EnderPlugin.lang = new Lang();

        DataPlayer.registerPlayers();

        EnderPlugin.scheduler = new Scheduler();
        runUpdate();

        if (!getDataFolder().exists()) System.out.print("New folder: " + getDataFolder().mkdir());

        registerEntity(ESGSnowMan.mobInfo);
        registerEntity(ESGBlaze.mobInfo);
        registerEntity(SpleefSnowMan.mobInfo);
        registerEntity(ESGWolf.mobInfo);
        registerEntity(ESGHorse.mobInfo);

        saveDefaultConfig();
        new Config(true, "", "data.yml", this).saveDefaultYaml();
        new Config(true, "", "games.yml", this).saveDefaultYaml();
        newRegisterWorlds();
        loadWeather();
        newerRegisterCommands();
        newerRegisterEvents();
        runPathFinder();
        System.out.print("Successfully registered EnderPlugin v" + Minecraft.versionInfo(Minecraft.VerType.NUM_NO_DOTS) + " " + Minecraft.versionInfo(Minecraft.VerType.CYCLE) + " (" + (Bukkit.getOnlineMode() ? "online" : "offline") + ")");
    }

    private void newerRegisterCommands() {
        EndExecutor[] advFuncs = {new BirthdayCommand(), new CountCommand(), new HologramCommand(), new PluginsCommand(), new SignCommand(), new StalkCommand(), new WarpCommand(), new UpdateCommand(), new CrashCommands(), new BackCommand()};
        EndExecutor[] invFuncs = {new HatCommand(), new RepairCommand(), new InventoryOpenCommands(), new SkullCommand(), new SkullLinkCommand()};
        EndExecutor[] playerFuncs = {new GetChestCommand(), new LocCommand(), new WorldSpawnCommand(), new PartyCommand()};
        EndExecutor[] txtFuncs = {new EffectsListCommand(), new UUIDCommand(), new ChatClearCommand(), new HelpCommand()};
        EndExecutor[] gameFuncs = {new ESGCommand(), new GetKitCommand(), new SelectBladeCommand(),};
        EndExecutor[] multiFuncs = {new CrashCommands(), new InventoryOpenCommands(), new ItemChangeCommands(), new PlayerCommands(), new PlayerSetCommands(), new TeleportCommands(), new TextArgCommands(), new TextCommands()};

        EndExecutor[][] endExecutors = {advFuncs, invFuncs, playerFuncs, txtFuncs, gameFuncs, multiFuncs};

        for (EndExecutor[] endExecutor : endExecutors) {
            for (EndExecutor executor : endExecutor) {
                for (String commandName : executor.commandNames()) {
                    getCommand(commandName).setExecutor(executor);
                    getCommand(commandName).setTabCompleter(executor);
                }
            }
        }
        System.out.println("Registered commands!");
    }

    private void loadWeather() {
        for (World world : Bukkit.getWorlds()) world.setStorm(false);
    }

    private void newerRegisterEvents() {
        Listener[] mainListeners = {new MainJoinEvent(), new MainDeathEvent(), new MainTalkEvent(), new MainLeaveEvent(), new MainHungerEvent(), new ArrowShootEvent(), new TeleporterClickEvent(), new TeleportationEvent(), new WorldInventoryEvent()};
        Listener[] gameBootyListeners = {new BootyClick(), new BootyJump(), new BootyTeam(), new BootyFrame(), new BootyDeath()};
        Listener[] gameESGListeners = {new ESGBushEvent(), new ESGStartChooseEvent(), new SnowmanImmunityEvent(), new ESGEggEvent(),
                new TargetRedirectEvent(EntityType.SNOWMAN),
                new TargetRedirectEvent(EntityType.MAGMA_CUBE),
                new TargetRedirectEvent(EntityType.WOLF),
                new TargetRedirectEvent(EntityType.BLAZE)
        };
        Listener[] gameCTFListeners = {new CTFChestCheck(), new CTFJoinEvent(), new CTFSwitchKitEvent()};
        Listener[] gameWizardListeners = {new WizardsBlades(), new WizardsSign(), new WizardClickEvent()};
        Listener[] gameSpleefListeners = {new JoinEvent(), new MineEvent(), new WinEvent(), new CraftEvent(), new TargetRedirectEvent(EntityType.SNOWMAN)};
        Listener[] gameHideListeners = {new FindEvent(), new io.github.rookietec9.enderplugin.events.games.hidenseek.JoinEvent()};
        Listener[] gameMurderListeners = {new DeathEvent(), new io.github.rookietec9.enderplugin.events.games.murder.JoinEvent(), new EscapeEvent()};
        Listener[] gameOtherListeners = {new SumoFailEvent(), new Click(), new Bows()};
        Listener[] hubListeners = {new HubShieldEvent(), new DoubleJump(), new ScoreboardSetupEvent(), new BookOpenEvent(), new TogglerEvent()};
        Listener[][] listeners = {gameBootyListeners, gameESGListeners, gameCTFListeners, gameWizardListeners, gameSpleefListeners, gameHideListeners, gameMurderListeners, gameOtherListeners, mainListeners, hubListeners};

        for (int i = 0; i < listeners.length; i++) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < listeners[i].length; ++j) {
                stringBuilder.append(listeners[i][j].getClass().getName().substring(listeners[i][j].getClass().getName().lastIndexOf(".") + 1));
                stringBuilder.append(" , ");
                Bukkit.getPluginManager().registerEvents(listeners[i][j], this);
            }
            System.out.print("Registered Event Group # (" + (((String.valueOf((i + 1)).length() < 2 ? ("0" + (i + 1)) : String.valueOf((i + 1))) + "/" + listeners.length + ") : "
                    + stringBuilder.toString().substring(0, stringBuilder.toString().length() - " , ".length()))));
        }
    }

    private void newRegisterWorlds() {
        if (this.getConfig().getStringList("worlds") == null) return;
        for (final String world : this.getConfig().getStringList("worlds")) {
            try {
                if (world == null) continue;
                Bukkit.createWorld(new WorldCreator(world));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDisable() {
        DataPlayer.unregister();
        EnderPlugin.instance = null;
        EnderPlugin.lang = null;
        EnderPlugin.hoodBase = null;
        EnderPlugin.murderBase = null;
        EnderPlugin.games = null;
    }

    private void runUpdate() {
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) DataPlayer.get(player).getBoard(HubBoard.class).changeTicks();
        }, "HUB_UPDATE_TICKS", Math.abs((ManagementFactory.getRuntimeMXBean().getUptime() / 1000) - 1200) / 20.0, 30);
    }

    private void runPathFinder() {
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            for (LivingEntity ent : TargetMapper.keySet()) {
                TargetMapper mapper = TargetMapper.getTMP(ent);
                if (mapper.getOwner() == null || !(ent instanceof Horse)) continue;
                ((EntityInsentient) ((CraftEntity) ent).getHandle()).getNavigation().a(((CraftEntity) mapper.getOwner()).getHandle(), 2);
            }
        }, "MOB_PATH_FINDER", 0, 15);
    }
}