package com.shanebeestudios.skbee.elements.other.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skbee.elements.other.sections.SecRunTaskLater;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Name("Task - Cancel Task")
@Description({"Stop tasks.",
    "`stop all tasks` = Will stop all currently running tasks created with a task section.",
    "`stop current task` = Will stop the task section this effect is in."})
@Examples({"run 0 ticks later repeating every second:",
    "\tset {-id} to current task id",
    "\tadd 1 to {_a}",
    "\tif {_a} > 10:",
    "\t\tstop current task",
    "",
    "on unload:",
    "\tstop all tasks"})
@Since("3.3.0")
public class EffTaskStop extends Effect {
    static {
        Skript.registerEffect(EffTaskStop.class,
            "(stop|cancel) all tasks", "(stop|cancel) current task");
    }

    private int pattern;
    private Expression<Number> ids;
    private SecRunTaskLater currentTask;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.pattern = matchedPattern;
        if (matchedPattern == 1) {
            List<SecRunTaskLater> currentSections = getParser().getCurrentSections(SecRunTaskLater.class);
            if (currentSections.isEmpty()) {
                Skript.error("'" + parseResult.expr + "' can only be used in a run task section.");
                return false;
            }
            this.currentTask = currentSections.getLast();
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        switch (this.pattern) {
            case 0 -> SecRunTaskLater.cancelTasks();
            case 1 -> this.currentTask.stopCurrentTask();
        }
    }

    @Override
    public @NotNull String toString(Event e, boolean d) {
        return switch (this.pattern) {
            case 1 -> "stop current task";
            default -> "stop all tasks";
        };
    }

}
