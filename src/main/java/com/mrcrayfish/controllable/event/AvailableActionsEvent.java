package com.mrcrayfish.controllable.event;

import com.mrcrayfish.controllable.client.Action;
import com.mrcrayfish.controllable.client.ButtonBinding;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
@Deprecated
public class AvailableActionsEvent extends Event
{
    private Map<Integer, Action> actions;

    public AvailableActionsEvent(Map<Integer, Action> actions)
    {
        this.actions = actions;
    }

    public Map<Integer, Action> getActions()
    {
        return this.actions;
    }
}
