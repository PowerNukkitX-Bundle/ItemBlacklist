package me.petterim1.itemblacklist;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.ItemStackRequestActionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.request.NetworkMapping;
import cn.nukkit.network.protocol.types.itemstack.ContainerSlotType;
import cn.nukkit.network.protocol.types.itemstack.request.ItemStackRequestSlotData;
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestAction;
import cn.nukkit.network.protocol.types.itemstack.request.action.SwapAction;
import cn.nukkit.network.protocol.types.itemstack.request.action.TransferItemStackRequestAction;

public class BlockInventoryTransactions implements Listener {

    @EventHandler
    public void onInventoryTransaction(ItemStackRequestActionEvent event) {
        ItemStackRequestAction action = event.getAction();
        ItemStackRequestSlotData source = null;
        ItemStackRequestSlotData destination = null;
        if (action instanceof TransferItemStackRequestAction transferItemStackRequestAction) {
            source = transferItemStackRequestAction.getSource();
            destination = transferItemStackRequestAction.getDestination();
        } else if (action instanceof SwapAction swapAction) {
            source = swapAction.getSource();
            destination = swapAction.getDestination();
        }
        if (source != null && destination != null) {
            ContainerSlotType sourceSlotType = source.getContainer();
            ContainerSlotType destinationSlotType = destination.getContainer();
            Inventory sourceI = NetworkMapping.getInventory(event.getPlayer(), sourceSlotType);
            Inventory destinationI = NetworkMapping.getInventory(event.getPlayer(), destinationSlotType);
            int sourceSlot = sourceI.fromNetworkSlot(source.getSlot());
            int destinationSlot = destinationI.fromNetworkSlot(destination.getSlot());
            var sourItem = sourceI.getItem(sourceSlot);
            var destItem = destinationI.getItem(destinationSlot);
            String s = sourItem != null ? (sourItem.getId() + ":" + sourItem.getDamage()) : "";
            String s_ = sourItem != null ? (sourItem.getId() + ":*") : "";
            String t = destItem != null ? (destItem.getId() + ":" + destItem.getDamage()) : "";
            String t_ = destItem != null ? (destItem.getId() + ":*") : "";
            if (Plugin.blacklist.contains(s) || Plugin.blacklist.contains(s_) || Plugin.blacklist.contains(t) || Plugin.blacklist.contains(t_)) {
                event.setCancelled(true);
            }
        }
    }
}
