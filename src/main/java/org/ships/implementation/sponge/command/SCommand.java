package org.ships.implementation.sponge.command;

import org.core.CorePlugin;
import org.core.command.CommandLauncher;
import org.core.exceptions.NotEnoughArguments;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SCommand implements CommandCallable {

    private CommandLauncher commandLauncher;

    public SCommand(CommandLauncher commandLauncher){
        this.commandLauncher = commandLauncher;
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        org.core.source.command.CommandSource source2 = ((SpongePlatform)CorePlugin.getPlatform()).get(source);
        try {
            return this.commandLauncher.run(source2, convertArgs(arguments)) ? CommandResult.success() : CommandResult.empty();
        } catch (NotEnoughArguments notEnoughArguments) {
            return CommandResult.empty();
        } catch (Throwable e){
            e.printStackTrace();
        }
        return CommandResult.empty();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, Location<World> targetPosition) {
        org.core.source.command.CommandSource source2 = ((SpongePlatform)CorePlugin.getPlatform()).get(source);
        return this.commandLauncher.tab(source2, convertArgs(arguments));
    }

    @Override
    public boolean testPermission(CommandSource source) {
        SpongePlatform platform = (SpongePlatform)CorePlugin.getPlatform();
        return this.commandLauncher.hasPermission(platform.get(source));
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(((SText)CorePlugin.buildText(this.commandLauncher.getDescription())).toSponge());
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Text getUsage(CommandSource source) {
        org.core.source.command.CommandSource source2 = ((SpongePlatform)CorePlugin.getPlatform()).get(source);
        return ((SText)CorePlugin.buildText(this.commandLauncher.getUsage(source2))).toSponge();
    }

    public String[] convertArgs(String values){
        List<String> list = new ArrayList<>();
        String[] args = values.split(" ");
        for(int A = 0; A < args.length; A++){
            String arg = args[A];
            if(arg.length() == 0){
                continue;
            }
            boolean failed = false;
            for(int B = 0; B < arg.length(); B++) {
                char cha = arg.charAt(B);
                if (!Character.isLetterOrDigit(cha)) {
                    failed = true;
                    break;
                }
            }
            if(failed){
                continue;
            }
            list.add(arg);
        }
        return list.toArray(new String[list.size()]);
    }

}
