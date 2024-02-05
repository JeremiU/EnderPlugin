package io.github.rookietec9.enderplugin;

import io.github.rookietec9.enderplugin.commandgroups.*;
import io.github.rookietec9.enderplugin.commands.advFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.HatCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.RepairCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.SkullCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.invFuncs.SkullLinkCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs.*;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.ChatClearCommand;
import io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.HelpCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.ESGChestCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.ESGCommand;
import io.github.rookietec9.enderplugin.commands.games.ESG.GetKitCommand;
import io.github.rookietec9.enderplugin.commands.games.OBSTPhotoCommand;
import io.github.rookietec9.enderplugin.commands.games.SelectBladeCommand;
import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.configs.associates.Associate;
import io.github.rookietec9.enderplugin.configs.associates.Lang;
import io.github.rookietec9.enderplugin.entities.*;
import io.github.rookietec9.enderplugin.entities.targetHandlers.TargetRedirectEvent;
import io.github.rookietec9.enderplugin.events.games.obst.BoxingEvent;
import io.github.rookietec9.enderplugin.events.games.obst.ObstFallEvent;
import io.github.rookietec9.enderplugin.events.games.obst.StoneRespawnEvent;
import io.github.rookietec9.enderplugin.events.games.ParkourEvents;
import io.github.rookietec9.enderplugin.events.games.SumoFailEvent;
import io.github.rookietec9.enderplugin.events.games.booty.*;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFFlagEvent;
import io.github.rookietec9.enderplugin.events.games.ctf.CTFJoinEvent;
import io.github.rookietec9.enderplugin.events.games.esg.ESGBushEvent;
import io.github.rookietec9.enderplugin.events.games.esg.ESGEggEvent;
import io.github.rookietec9.enderplugin.events.games.esg.ESGStartChooseEvent;
import io.github.rookietec9.enderplugin.events.games.murder.EscapeEvent;
import io.github.rookietec9.enderplugin.events.games.murder.MurderStartEvent;
import io.github.rookietec9.enderplugin.events.games.run.Bows;
import io.github.rookietec9.enderplugin.events.games.spleef.SpleefCraftEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.SpleefJoinEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.SpleefMineEvent;
import io.github.rookietec9.enderplugin.events.games.spleef.SpleefWinEvent;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsBladesEvent;
import io.github.rookietec9.enderplugin.events.games.wizards.WizardsStartEvent;
import io.github.rookietec9.enderplugin.events.hub.*;
import io.github.rookietec9.enderplugin.events.inventoryclickers.*;
import io.github.rookietec9.enderplugin.events.main.*;
import io.github.rookietec9.enderplugin.scoreboards.HubBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.*;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.FinderBase;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * @author Jeremi
 * @version 25.7.3
 * @since 0.0.1
 */
public class EnderPlugin extends JavaPlugin {

    public static FinderBase hoodBase = new FinderBase(false);
    public static FinderBase murderBase = new FinderBase(true);
    public static Associate games = null;
    private static EnderPlugin instance;
    private static Lang lang;
    private static Scheduler scheduler, snowballScheduler;

    public static EnderPlugin getInstance() {
        return instance;
    }

    public static Lang serverLang() {
        return lang;
    }

    public static Scheduler scheduler() {
        return scheduler;
    }

    public static Scheduler snowballScheduler() {
        return snowballScheduler;
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
        Bukkit.getLogger().setFilter(record -> {
            if (Java.containsAllIgnoreCase(record.getMessage(), "Sending up to", "chunks per packet")) return false;
            return !Java.containsIgnoreCase(record.getMessage(), " -------- World Settings For [", "Arrow Despawn Rate:", "Item Despawn Rate:", "Item Merge Radius:", "Allow Zombie Pigmen to spawn from portal blocks:", "Zombie Aggressive Towards Villager:",
                    "Chunks to Grow per Tick:", "Clear tick list:", "Experience Merge Radius:", "Max Entity Collisions:", "Custom Map Seeds:", "Tile Max Tick Time:", "Max TNT Explosions:", "Anti X-Ray:", "Engine Mode:", "Hidden Blocks:", "Replace Blocks:", "Nerfing mobs spawned from spawners:",
                    "Mob Spawn Range:", "Cactus Growth Modifier:", "Cane Growth Modifier:", "Melon Growth Modifier:", "Mushroom Growth Modifier:", "Pumpkin Growth Modifier:", "Sapling Growth Modifier:", "Wheat Growth Modifier:", "NetherWart Growth Modifier:", "Entity Activation Range:", "Entity Tracking Range:",
                    "Random Lighting Updates:", "Structure Info Saving:", "Hopper Transfer:", "View Distance:", "Preparing start region for level", "Skipping Piece with id Iglu", "Preparing spawn area for", "Skipping BlockEntity with");
        });

        EnderPlugin.instance = this;
        EnderPlugin.games = new Associate(new Config(true, "", "games.yml", EnderPlugin.getInstance()));
        EnderPlugin.lang = new Lang();

        DataPlayer.registerPlayers();

        EnderPlugin.scheduler = new Scheduler();
        EnderPlugin.snowballScheduler = new Scheduler();
        runUpdate();

        if (!getDataFolder().exists()) System.out.print("New folder: " + getDataFolder().mkdir());

        registerEntity(ESGSnowMan.mobInfo);
        registerEntity(ESGBlaze.mobInfo);
        registerEntity(SpleefSnowMan.mobInfo);
        registerEntity(ESGWolf.mobInfo);
        registerEntity(ESGHorse.mobInfo);
        registerEntity(ESGMagmaCube.mobInfo);

        saveDefaultConfig();
        new Config(true, "", "data.yml", this).saveDefaultYaml();
        new Config(true, "", "games.yml", this).saveDefaultYaml();
        newRegisterWorlds();
        loadWeather();
        newerRegisterCommands();
        newerRegisterEvents();
        runPathFinder();
        if (Bukkit.getWorld(WIZARDS).getBlockAt(23, 12, -42).getState() instanceof Sign && ((Sign) Bukkit.getWorld(WIZARDS).getBlockAt(23, 12, -42).getState()).getLine(2).contains("ON")) runWizardPowerUps();
        else for (Entity entity : Bukkit.getWorld(WIZARDS).getEntitiesByClasses(ArmorStand.class)) entity.remove();

        PartySystem.resetPlayers();
        System.out.print("Successfully registered EnderPlugin v" + Minecraft.versionInfo(Minecraft.VerType.NUM_NO_DOTS) + " " + Minecraft.versionInfo(Minecraft.VerType.CYCLE) + " (" + (Bukkit.getOnlineMode() ? "online" : "offline") + ")");
    }

    private void newerRegisterCommands() {
        EndExecutor[] advFuncs = {new BirthdayCommand(), new CountCommand(), new HologramCommand(), new PluginsCommand(), new SignCommand(), new InfoCommand(), new UpdateCommand()};
        EndExecutor[] invFuncs = {new HatCommand(), new RepairCommand(), new InventoryOpenCommands(), new SkullCommand(), new SkullLinkCommand()};
        EndExecutor[] playerFuncs = {new GetChestCommand(), new LocCommand(), new PartyCommand()};
        EndExecutor[] txtFuncs = {new EffectsListCommand(), new UUIDCommand(), new ChatClearCommand(), new HelpCommand()};
        EndExecutor[] gameFuncs = {new ESGCommand(), new GetKitCommand(), new SelectBladeCommand(), new ESGChestCommand(), new OBSTPhotoCommand()};

        EndExecutor[] multiFuncs = {new CrashCommands(), new InventoryOpenCommands(), new ItemChangeCommands(), new PlayerCommands(), new PlayerSetCommands(), new TeleportCommands(), new TextArgCommands(), new TextCommands(), new WorldCommands(), new WarpCommands()};
        EndExecutor[][] endExecutors = {advFuncs, invFuncs, playerFuncs, txtFuncs, gameFuncs, multiFuncs};

        for (EndExecutor[] endExecutor : endExecutors) {
            for (EndExecutor executor : endExecutor) {
                for (String commandName : executor.commandNames()) {
                    getCommand(commandName).setExecutor(executor);
                    getCommand(commandName).setTabCompleter(executor);
                }
            }
        }
        System.out.print("Registered commands!");
    }

    private void loadWeather() {
        for (World world : Bukkit.getWorlds()) world.setStorm(false);
    }

    private void newerRegisterEvents() {
        Listener[] mainListeners = {new MainJoinLeaveEvent(), new MainDeathEvent(), new MainTalkEvent(), new MainHungerEvent(), new ArrowShootEvent(), new TeleporterClickEvent(), new TeleportationEvent(), new WorldInventoryEvent()};
        Listener[] gameBootyListeners = {new BootyClickEvent(), new BootyJumpEvent(), new BootyTeamEvent(), new BootyFrameEvent(), new BootyDeathEvent()};
        Listener[] gameESGListeners = {new ESGBushEvent(), new ESGStartChooseEvent(), new SnowmanImmunityEvent(), new ESGEggEvent(), new TargetRedirectEvent(EntityType.SNOWMAN), new TargetRedirectEvent(EntityType.MAGMA_CUBE), new TargetRedirectEvent(EntityType.WOLF), new TargetRedirectEvent(EntityType.BLAZE)};
        Listener[] gameCTFListeners = {new CTFFlagEvent(), new CTFJoinEvent(), new CTFSwitchKitEvent()};
        Listener[] gameWizardListeners = {new WizardsBladesEvent(), new WizardsStartEvent(), new WizardClickEvent()};
        Listener[] gameSpleefListeners = {new SpleefJoinEvent(), new SpleefMineEvent(), new SpleefWinEvent(), new SpleefCraftEvent(), new TargetRedirectEvent(EntityType.SNOWMAN)};
        Listener[] gameMurderListeners = {new MurderStartEvent(), new EscapeEvent(), new MurderClickEvent()};
        Listener[] gameObstListeners = {new BoxingEvent(), new ObstFallEvent(), new StoneRespawnEvent()};
        Listener[] gameOtherListeners = {new SumoFailEvent(), new ParkourEvents(), new Bows(), new PartyClickEvent()};
        Listener[] hubListeners = {new HubShieldEvent(), new DoubleJumpEvent(), new ScoreboardSetupEvent(), new BookOpenEvent(), new TogglerEvent(), new PaintGunEvent(), new WorldEditEvent()};
        Listener[][] listeners = {gameBootyListeners, gameESGListeners, gameCTFListeners, gameWizardListeners, gameSpleefListeners, gameMurderListeners, gameObstListeners, gameOtherListeners, mainListeners, hubListeners};

        for (Listener[] listenerGroup : listeners) for (Listener listener : listenerGroup) Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void newRegisterWorlds() {
        if (Minecraft.getWorlds() == null) return;
        Minecraft.getWorlds().stream().filter(Objects :: nonNull).forEach(x -> Bukkit.createWorld(new WorldCreator(x)));
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
            if (Reference.upTimeSeconds() % 5 == 0) Bukkit.getWorld(HUB).getPlayers().forEach(x -> DataPlayer.get(x).getBoard(HubBoard.class).changeTicks());
        }, "HUB_UPDATE_TICKS", 0, 1, PREFIX_HUB);
    }

    private void runPathFinder() {
        EnderPlugin.scheduler().runRepeatingTask(() -> {
            for (LivingEntity ent : TargetMapper.keySet()) {
                TargetMapper mapper = TargetMapper.getTMP(ent);
                if (mapper.getOwner() == null || !(ent instanceof Horse)) continue;
                ((EntityInsentient) ((CraftEntity) ent).getHandle()).getNavigation().a(((CraftEntity) mapper.getOwner()).getHandle(), 2);
            }
        }, "MOB_PATH_FINDER", 0, 15, PREFIX_ESG);
    }

    public void runWizardPowerUps() {
        if (POWERUP_SLOW.getWorld() == null) POWERUP_SLOW.setWorld(Bukkit.getWorld(WIZARDS));
        if (POWERUP_BLIND.getWorld() == null) POWERUP_BLIND.setWorld(Bukkit.getWorld(WIZARDS));
        if (POWERUP_SPEED.getWorld() == null) POWERUP_SPEED.setWorld(Bukkit.getWorld(WIZARDS));
        if (POWERUP_HEALTH.getWorld() == null) POWERUP_HEALTH.setWorld(Bukkit.getWorld(WIZARDS));

        for (Entity entity : Bukkit.getWorld(WIZARDS).getEntitiesByClasses(ArmorStand.class)) entity.remove();

        DataPlayer.blockBelow(POWERUP_SLOW, 1).getBlock().setType(Material.PACKED_ICE);
        HologramCommand.spawnHologram(SLOW_HEADER, false, POWERUP_SLOW);
        HologramCommand.spawnHologram(SLOW_TEXT, true, POWERUP_SLOW);

        DataPlayer.blockBelow(POWERUP_BLIND, 1).getBlock().setType(Material.COAL_BLOCK);
        HologramCommand.spawnHologram(BLIND_HEADER, false, POWERUP_BLIND);
        HologramCommand.spawnHologram(BLIND_TEXT, true, POWERUP_BLIND);

        DataPlayer.blockBelow(POWERUP_SPEED, 1).getBlock().setType(Material.DIAMOND_BLOCK);
        HologramCommand.spawnHologram(SPEED_HEADER, false, POWERUP_SPEED);
        HologramCommand.spawnHologram(SPEED_TEXT, true, POWERUP_SPEED);

        DataPlayer.blockBelow(POWERUP_HEALTH, 1).getBlock().setType(Material.GOLD_BLOCK);
        HologramCommand.spawnHologram(HEALTH_HEADER, false, POWERUP_HEALTH);
        HologramCommand.spawnHologram(HEALTH_TEXT, true, POWERUP_HEALTH);
    }
}