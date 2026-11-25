package net.fabricmc.fabric.api.client.command.v2;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;

/** Compile-time bridge for Fabric API builds where ClientCommandManager was renamed. */
public final class ClientCommandManager {
    private ClientCommandManager() {}

    public static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return ClientCommands.literal(name);
    }

    public static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(
            String name, ArgumentType<T> type) {
        return ClientCommands.argument(name, type);
    }
}
