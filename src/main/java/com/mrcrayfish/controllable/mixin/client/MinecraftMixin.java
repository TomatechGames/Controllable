package com.mrcrayfish.controllable.mixin.client;

import com.mrcrayfish.controllable.Config;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.ButtonBindings;
import com.mrcrayfish.controllable.client.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Author: MrCrayfish
 */
@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @ModifyArgs(method = "processKeyBinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;sendClickBlockToController(Z)V"))
    private void sendClickBlockToController(Args args)
    {
        boolean leftClick = args.get(0);
        args.set(0, leftClick || isLeftClicking());
    }

    @Redirect(method = "processKeyBinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isHandActive()Z"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerController;onStoppedUsingItem(Lnet/minecraft/entity/player/PlayerEntity;)V")))
    private boolean onKeyDown(KeyBinding binding)
    {
        return binding.isKeyDown() || isRightClicking();
    }

    /**
     * Checks if a controller is connected and if the use item button is down.
     */
    private static boolean isRightClicking()
    {
        Controller controller = Controllable.getController();
        return controller != null && ButtonBindings.USE_ITEM.isButtonDown();
    }

    /**
     * Checks if a controller is connected and if the attack button is down. A special except is
     * added when virtual mouse is enabled and it will ignore if the mouse is grabbed or not.
     */
    private static boolean isLeftClicking()
    {
        Minecraft mc = Minecraft.getInstance();
        Controller controller = Controllable.getController();
        if(controller != null && ButtonBindings.ATTACK.isButtonDown())
        {
            boolean usingVirtualMouse = (Config.CLIENT.options.virtualMouse.get() && Controllable.getInput().getLastUse() > 0);
            return mc.currentScreen == null && (mc.mouseHelper.isMouseGrabbed() || usingVirtualMouse);
        }
        return false;
    }

}
