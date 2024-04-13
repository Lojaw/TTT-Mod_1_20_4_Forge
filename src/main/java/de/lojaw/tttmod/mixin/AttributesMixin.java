package de.lojaw.tttmod.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Attributes.class)
public class AttributesMixin {

    @ModifyConstant(
            constant = @Constant(doubleValue = 1024.0D),
            method = "<clinit>")
    private static double modifyMaxHealth(double original) {
        return Double.MAX_VALUE;
    }
}