package com.Glynny.WildernessSkipping;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class WildnerssSkippingOverlay extends OverlayPanel
{
    private final WildernessSkippingPlugin plugin;
    private final WildernessSkippingConfig config;

    @Inject
    public WildnerssSkippingOverlay(WildernessSkippingPlugin plugin, WildernessSkippingConfig config)
    {
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setPreferredSize(new Dimension(25, 350));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.displayInfo() || plugin.getTask() == null)
        {
            return null;
        }

        panelComponent.getChildren().add(LineComponent.builder().left(plugin.getTask().getLocation()).build());

        for (String teleport : plugin.getTask().getTeleports())
        {
            panelComponent.getChildren().add(LineComponent.builder().left("- " + teleport).leftColor(Color.LIGHT_GRAY).build());
        }

        if (!plugin.getTask().getInfo().isEmpty())
        {
            panelComponent.getChildren().add(LineComponent.builder().left(plugin.getTask().getInfo()).leftColor(Color.LIGHT_GRAY).build());
        }

        return super.render(graphics);
    }
}