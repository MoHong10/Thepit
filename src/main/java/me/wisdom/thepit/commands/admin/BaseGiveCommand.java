package me.wisdom.thepit.commands.admin;

import me.wisdom.thepitapi.commands.AMultiCommand;

public class BaseGiveCommand extends AMultiCommand {
    public BaseGiveCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }
}
