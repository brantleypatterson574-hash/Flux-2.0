package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.StringSetting;
import net.minecraft.client.sound.SoundInstance;

/** Mutes selected sound IDs locally. The server never sees a difference. */
public class SoundBlockerModule extends Module {
    public static SoundBlockerModule INSTANCE;

    private final StringSetting sounds = new StringSetting("Sounds",
        "experience_orb.pickup,block.anvil.land")
        .description("Comma-separated sound IDs or path fragments to mute on your client only.");
    private final BooleanSetting exactMatch = new BooleanSetting("Exact Match", false)
        .description("Matches the whole sound ID instead of any part of its path.");

    public SoundBlockerModule() {
        super("Sound Blocker", "Mutes chosen sounds on your client. Nothing changes on the server.",
            Category.UTIL);
        INSTANCE = this;
        addSetting(sounds);
        addSetting(exactMatch);
    }

    /** Called from the client sound mixin before the sound is queued. */
    public static boolean shouldBlock(SoundInstance sound) {
        SoundBlockerModule module = INSTANCE;
        if (module == null || !module.isEnabled() || sound == null) return false;
        String id = sound.getId().toString();
        String path = sound.getId().getPath();
        for (String raw : module.sounds.getValue().split(",")) {
            String token = raw.trim().toLowerCase(java.util.Locale.ROOT);
            if (token.isEmpty()) continue;
            if (module.exactMatch.isEnabled()) {
                if (id.equalsIgnoreCase(token)) return true;
            } else if (path.toLowerCase(java.util.Locale.ROOT).contains(token)) {
                return true;
            }
        }
        return false;
    }
}
