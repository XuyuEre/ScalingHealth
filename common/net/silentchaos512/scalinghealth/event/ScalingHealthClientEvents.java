package net.silentchaos512.scalinghealth.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.scalinghealth.config.ConfigScalingHealth;
import net.silentchaos512.scalinghealth.lib.EnumAreaDifficultyMode;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler.PlayerData;

public class ScalingHealthClientEvents {

  @SubscribeEvent
  public void renderTick(RenderGameOverlayEvent.Post event) {

    if (Minecraft.getMinecraft().theWorld == null || event.getType() != ElementType.ALL)
      return;

    if (!ConfigScalingHealth.DEBUG_MODE)
      return;

    ScaledResolution res = event.getResolution();
    int width = res.getScaledWidth();
    int height = res.getScaledHeight();

    FontRenderer fontRender = Minecraft.getMinecraft().fontRendererObj;

    GL11.glPushMatrix();

    String text = getDebugText();
    int y = 3;
    for (String line : text.split("\n")) {
      String[] array = line.split("=");
      if (array.length == 2) {
        fontRender.drawString(array[0].trim(), 3, y, 0xFFFFFF);
        fontRender.drawString(array[1].trim(), 90, y, 0xFFFFFF);
      } else {
        fontRender.drawStringWithShadow(line, 3, y, 0xFFFFFF);
      }
      y += 10;
    }

    GL11.glPopMatrix();
  }

  private String getDebugText() {

    World world = Minecraft.getMinecraft().theWorld;
    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    PlayerData data = SHPlayerDataHandler.get(player);
    EnumAreaDifficultyMode areaMode = ConfigScalingHealth.AREA_DIFFICULTY_MODE;
    if (data == null)
      return "Player data is null!";

    String ret = "";

    ret += String.format("Area Difficulty = %.4f (%s)\n",
        areaMode.getAreaDifficulty(world, player.getPosition()), areaMode.name());
    ret += String.format("Player Difficulty = %.4f\n", data.getDifficulty());
    ret += "Player Health = " + player.getHealth() + " / " + player.getMaxHealth() + "\n";
    int regenTimer = PlayerBonusRegenHandler.INSTANCE.getTimerForPlayer(player);
    ret += String.format("Regen Timer = %d (%ds)", regenTimer, regenTimer / 20);

    return ret;
  }
}
