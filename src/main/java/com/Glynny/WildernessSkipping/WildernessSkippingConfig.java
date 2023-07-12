package com.Glynny.WildernessSkipping;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("turaelskipping")
public interface WildernessSkippingConfig extends Config
{
	@ConfigItem(
			keyName = "displayMapIcon",
			name = "Display Map Icon",
			description = "Displays an icon on the world map where the task is located",
			position = 0
	)
	default boolean displayMapIcon()
	{
		return true;
	}

	@ConfigItem(
		keyName = "displayInfo",
		name = "Display Info",
		description = "Displays an infobox containing task information",
		position = 1
	)
	default boolean displayInfo()
	{
		return true;
	}
}