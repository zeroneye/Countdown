package xieao.countdown;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import xieao.countdown.api.TimeData;
import xieao.countdown.command.MainCommand;
import xieao.countdown.config.Config;
import xieao.countdown.config.HudSettings;
import xieao.countdown.network.Packets;
import xieao.countdown.potion.IPotions;
import xieao.countdown.world.gen.IFeatures;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xieao.lib.Lollipop.addEventListener;
import static xieao.lib.Lollipop.addModListener;

@Mod(Countdown.MOD_ID)
public class Countdown {
    public static final String MOD_ID = "countdown";

    public Countdown() {
        Path dir = FMLPaths.CONFIGDIR.get();
        Path configDir = Paths.get(dir.toAbsolutePath().toString(), MOD_ID);
        try {
            Files.createDirectory(configDir);
        } catch (Exception ignored) {
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC, MOD_ID + "/common.toml");
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
        addEventListener(this::serverStarting);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
        IPotions.initBrew();
        IFeatures.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
        HudSettings.load();
    }

    void loadComplet(FMLLoadCompleteEvent event) {
        TimeData.globalDefault = Config.GENERAL.time.get();
    }

    void serverStarting(FMLServerStartingEvent evt) {
        MainCommand.register(evt.getCommandDispatcher());
    }
}