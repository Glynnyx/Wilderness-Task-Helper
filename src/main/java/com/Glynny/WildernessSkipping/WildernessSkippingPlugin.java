package com.Glynny.WildernessSkipping;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;
import net.runelite.client.util.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Wilderness Skipping"
)
public class WildernessSkippingPlugin extends Plugin
{
	private static final String TURAEL = "Turael";
	private static final String SPRIA = "Spria";

	// NPC messages
	private static final Pattern SLAYER_ASSIGN_MESSAGE = Pattern.compile(".*(?:Your new task is to kill \\d+) (?<name>.+)(?:.)");
	private static final Pattern SLAYER_CURRENT_MESSAGE = Pattern.compile(".*(?:You're still hunting) (?<name>.+)(?:[,;] you have \\d+ to go.)");

	private boolean worldPointSet = false;

	@Getter
	private Task task;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private WorldMapPointManager worldMapPointManager;

	@Inject
	private WildernessSkippingOverlay overlay;

	@Inject
	private WildernessSkippingConfig config;

	private void setTask(String taskName)
	{
		task = Task.getTask(taskName);
		createWorldPoint();
	}

	private void completeTask()
	{
		task = null;
		worldMapPointManager.removeIf(TaskWorldMapPoint.class::isInstance);
		worldPointSet = false;
	}

	private void createWorldPoint()
	{
		if (task != null && config.displayMapIcon() && !worldPointSet)
		{
			for (WorldPoint worldPoint : task.getWorldPoints())
			{
				worldMapPointManager.add(new TaskWorldMapPoint(worldPoint));
			}
			worldPointSet = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// Getting tasks
		Widget npcName = client.getWidget(WidgetInfo.DIALOG_NPC_NAME);
		Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
		if (npcDialog != null && npcName != null && (npcName.getText().equals(TURAEL) || npcName.getText().equals(SPRIA)))
		{
			String npcText = Text.sanitizeMultilineText(npcDialog.getText());
			final Matcher mAssign = SLAYER_ASSIGN_MESSAGE.matcher(npcText);
			final Matcher mCurrent = SLAYER_CURRENT_MESSAGE.matcher(npcText);

			if (mAssign.find())
			{
				String name = mAssign.group("name");
				setTask(name);
			}

			if (mCurrent.find())
			{
				String name = mCurrent.group("name");
				setTask(name);
			}
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		// Completing tasks
		if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		String chatMessage = Text.removeTags(event.getMessage());

		if (chatMessage.startsWith("You've completed") && (chatMessage.contains("Slayer master") || chatMessage.contains("Slayer Master")))
		{
			completeTask();
		}
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		task = null;
		worldMapPointManager.removeIf(TaskWorldMapPoint.class::isInstance);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("turaelskipping"))
		{
			worldMapPointManager.removeIf(TaskWorldMapPoint.class::isInstance);
			worldPointSet = false;

			createWorldPoint();
		}
	}

	@Provides
	WildernessSkippingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WildernessSkippingConfig.class);
	}
}