package lv.id.bonne.reactionabuse;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Initial class
 */
@Mod("reactionabusemod")
public class ChatReactionAbuseMod
{
    public ChatReactionAbuseMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ChatReactionAbuseMod.instance = this;
    }


    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    }


    /**
     * This method process Client stuff. It registers new listener, that will process
     * speed typer message.
     * @param setupEvent FMLClientSetupEvent object.
     */
    private void doClientStuff(final FMLClientSetupEvent setupEvent)
    {
        // Add Client Chat Received Event listener that will check for speed typer
        // starting string.

        MinecraftForge.EVENT_BUS.<ClientChatReceivedEvent>addListener(event -> {

            ITextComponent component = event.getMessage();

            if (component != null &&
                component.getString().startsWith("[Speed Typer]") &&
                component.getString().endsWith(" wins!"))
            {
                String outputMessage = "";

                if (component.getStyle().getHoverEvent() != null &&
                    component.getStyle().getHoverEvent().getValue() != null)
                {
                	// Try to use hover element text as it is simpler.
                    ITextComponent hoverElement = component.getStyle().getHoverEvent().getValue();
                    outputMessage = hoverElement.getString();
                }

                if (outputMessage.isEmpty())
                {
                	// Use the simplest regex to remove start and the end by leaving only
					// usefull code.

                    outputMessage = component.getString();
                    outputMessage = outputMessage.replaceAll("(.*): ", "");
                    outputMessage = outputMessage.replaceAll("( wins!)", "");
                }

                if (!outputMessage.isEmpty() &&
                    setupEvent.getMinecraftSupplier() != null)
                {
                    // Send Speed Typer chat message.
                    setupEvent.getMinecraftSupplier().get().player.sendChatMessage(outputMessage);
                }
            }
        });

        // do something that can only be done on the client
    }


    // ---------------------------------------------------------------------
    // Section: Variables
    // ---------------------------------------------------------------------

    /**
     * Mod ID variable
     */
    public static final String modID = "reactionabusemod";

    /**
     * Logger variable
     */
    public static final Logger LOGGER = LogManager.getLogger(modID);

    /**
     * Instance variable
     */
    public static ChatReactionAbuseMod instance;
}
