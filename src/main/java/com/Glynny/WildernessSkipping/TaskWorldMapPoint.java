package com.Glynny.WildernessSkipping;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

public class TaskWorldMapPoint extends WorldMapPoint
{
    TaskWorldMapPoint(WorldPoint worldPoint)
    {
        super(worldPoint, null);

        BufferedImage taskWorldImage = ImageUtil.loadImageResource(WildernessSkippingPlugin.class, "/Krystilia_chathead.png");

        this.setSnapToEdge(true);
        this.setJumpOnClick(true);
        this.setName("Wilderness Task");
        this.setImage(taskWorldImage);
    }
}