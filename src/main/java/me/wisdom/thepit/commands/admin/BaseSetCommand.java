package me.wisdom.thepit.commands.admin;

import me.wisdom.thepitapi.commands.AMultiCommand;

public class BaseSetCommand extends AMultiCommand {
    public BaseSetCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }
}
