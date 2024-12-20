package zone.vao.nexoAddon.classes.bedrockbreak;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.utils.breaker.ModernBreakerManager;
import com.nexomc.nexo.mechanics.breakable.BreakableMechanic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BedrockBreakMechanicManager {

    public BedrockBreakMechanicManager(BedrockBreakMechanicFactory factory) {
        ModernBreakerManager.MODIFIERS.add(new HardnessModifier() {

            @Override
            public boolean isTriggered(Player player, Block block, ItemStack tool) {
                if (block.getType() != Material.BEDROCK) return false;

                String itemID = NexoItems.idFromItem(tool);
                boolean disableFirstLayer = !factory.isDisabledOnFirstLayer() || block.getY() > (block.getWorld().getMinHeight());
                return !factory.isNotImplementedIn(itemID) && disableFirstLayer;

            }

            @Override
            public void breakBlock(Player player, Block block, ItemStack tool) {
                String itemID = NexoItems.idFromItem(tool);
                BedrockBreakMechanic mechanic = (BedrockBreakMechanic) factory.getMechanic(itemID);
                World world = block.getWorld();
                Location loc = block.getLocation();

                if (mechanic == null) return;
                if (mechanic.bernouilliTest())
                    world.dropItemNaturally(loc, new ItemStack(Material.BEDROCK));

                block.breakNaturally(true);
            }

            @Override
            public long getPeriod(Player player, Block block, ItemStack tool) {
                String itemID = NexoItems.idFromItem(tool);
                BedrockBreakMechanic mechanic = (BedrockBreakMechanic) factory.getMechanic(itemID);
                return mechanic.getPeriod();
            }
        });
    }

}