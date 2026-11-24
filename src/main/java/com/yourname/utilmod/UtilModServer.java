package com.yourname.utilmod;

import net.fabricmc.api.DedicatedServerModInitializer;

/**
 * Dedicated-server entrypoint. Payload registration lives in the common
 * {@link UtilMod} entrypoint so it also applies to integrated servers.
 */
public class UtilModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        // Nothing extra: common initialization already registered networking.
    }
}
