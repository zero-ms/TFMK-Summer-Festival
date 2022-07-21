package ms.zero.tfmk.tfmkhotel.handler;

import ms.zero.tfmk.tfmkhotel.Objects.HotelManger;
import ms.zero.tfmk.tfmkhotel.Objects.Room;
import static ms.zero.tfmk.tfmkhotel.miscellaneous.Util.*;
import static ms.zero.tfmk.tfmkhotel.miscellaneous.GlobalVariable.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("room")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("confirm")) {
                    if (args.length == 2) {
                        switch(HotelManger.assignNewRoom(p, Integer.parseInt(args[1]))) {
                            case REQUEST_SUCCESS:
                                p.sendMessage(translate("&a[TFMK] &7방을 할당받았습니다!"));
                                p.sendMessage(String.format(translate("&a[TFMK] &7방 번호: &6%d"), HotelManger.get(p.getUniqueId()).getNumber()));
                                break;
                            case ALREADY_ASSIGNED_PLAYER:
                                p.sendMessage(translate("&a[TFMK] &7이미 방이 있습니다."));
                                break;
                            case ROOM_INSUFFICIENT:
                                p.sendMessage(translate("&a[TFMK] &7방이 &c부족&7합니다. 관리자에게 문의하세요."));
                                break;
                            case REQUEST_FAILED:
                                p.sendMessage(translate("&a[TFMK] &7방 배정에 실패하였습니다."));
                                p.sendMessage(translate("&a[TFMK] &7호텔 건물을 잘못 입력했을 확률이 높습니다."));
                                break;
                            default:
                                p.sendMessage(translate("&c[ERROR] &7ReturnType error. 관리자에게 문의하세요."));
                                break;
                        }
                        return true;
                    } else {
                        p.sendMessage(translate("&c[ERROR] &7/room confirm &6(호텔건물:1~2)"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (p.isOp()) {
                        Iterator<Integer> roomIter = HotelManger.getList();
                        while (roomIter.hasNext()) {
                            Integer key = roomIter.next();
                            p.sendMessage(translate("====================="));
                            p.sendMessage(String.format(translate("&6방 번호: &f%d"), key));
                            p.sendMessage(String.format(translate("&cUUID: &f%s"), HotelManger.get(key).getUUID().toString()));
                            p.sendMessage(String.format(translate("&enickname: &f%s"), Bukkit.getOfflinePlayer(HotelManger.get(key).getUUID()).getName()));
                            p.sendMessage(translate("====================="));
                        }
                        return true;
                    } else {
                        p.sendMessage(translate("&c[ERROR] &7권한이 없습니다."));
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
