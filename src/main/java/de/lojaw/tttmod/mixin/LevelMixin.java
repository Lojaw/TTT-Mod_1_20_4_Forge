package de.lojaw.tttmod.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow
    protected abstract LevelEntityGetter<Entity> getEntities();
}
