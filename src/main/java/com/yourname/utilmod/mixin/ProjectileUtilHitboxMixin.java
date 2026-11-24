package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.HitboxManager;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

/**
 * Expands the hitbox used by the client attack raycast without touching the
 * entity's real bounding box. Vanilla collision and physics boxes stay untouched,
 * which is what previously made large multipliers freeze the game.
 */
@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilHitboxMixin {
    @ModifyVariable(
        method = "collectPiercingCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/component/type/AttackRangeComponent;Ljava/util/function/Predicate;Lnet/minecraft/world/RaycastContext$ShapeType;)Lcom/mojang/datafixers/util/Either;",
        at = @At("HEAD"),
        argsOnly = true
    )
    private static AttackRangeComponent utilmod$expandHitboxMargin(
        AttackRangeComponent component, Entity attacker
    ) {
        if (!(attacker instanceof PlayerEntity) || !attacker.getEntityWorld().isClient()) {
            return component;
        }
        if (!HitboxManager.hasAnyMargin()) return component;
        double margin = HitboxManager.effectiveMargin(attacker.getUuid());
        if (margin <= 0.0) return component;

        return new AttackRangeComponent(
            component.minRange(),
            component.maxRange(),
            component.minCreativeRange(),
            component.maxCreativeRange(),
            component.hitboxMargin() + (float) margin,
            component.mobFactor()
        );
    }
}
