package VRPCore.Commands

import VRPCore.VRPCore
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.velocity.CommandHandler.annotations.CHArgument
import org.velocity.CommandHandler.annotations.CHCommand
import org.velocity.CommandHandler.models.CommandArg
import org.velocity.CommandHandler.models.CommandBase

@CHCommand(Name="profile")
class ProfileCommand : CommandBase<VRPCore> {
    companion object {
        lateinit var core : VRPCore;
    }

    override fun execute(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?) {

    }

    override fun executeFinal(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        return true;
    }

    @CHArgument(Name="", Index=0, IsVariable = true)
    class ProfileUserCommand : CommandArg {
        override fun execute(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?, arg: String?) {

        }

        override fun executeFinal(
            sender: CommandSender?,
            command: Command?,
            label: String?,
            args: Array<out String>?,
            arg: String?
        ): Boolean {
            sender!!.sendMessage("Getting user info for $arg");

            return true;
        }

    }

    override fun init(plugin: VRPCore) {
        core = plugin;
    }

}