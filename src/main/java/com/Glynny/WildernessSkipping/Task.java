package com.Glynny.WildernessSkipping;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import javax.annotation.Nullable;
import java.util.Map;

@Getter
public enum Task
{
    BANSHEES("Banshees", new WorldPoint[]{new WorldPoint(3442,3542,0)}, "Morytania Slayer Tower", new String[]{"Slayer Ring: Morytania Slayer Tower"}, ""),

    ;
    private static final Map<String, Task> tasks;

    private final String name;
    private final WorldPoint[] worldPoints; // Both surface and underground
    private final String location;
    private final String[] teleports;
    private final String info;

    static
    {
        ImmutableMap.Builder<String, Task> builder = new ImmutableMap.Builder<>();

        for (Task task : values())
        {
            builder.put(task.getName().toLowerCase(), task);
        }

        tasks = builder.build();
    }

    Task(String name, WorldPoint[] worldPoints, String location, String[] teleports, String info)
    {
        this.name = name;
        this.worldPoints = worldPoints;
        this.location = location;
        this.teleports = teleports;
        this.info = info;
    }

    @Nullable
    static Task getTask(String taskName)
    {
        return tasks.get(taskName.toLowerCase());
    }
}