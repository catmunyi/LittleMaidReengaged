package net.blacklab.lmr.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * モード切り替え用トリガーアイテムのコンテナ。
 * マルチ対策用。
 * データの読み込みはIFFで行っている。
 */
public class TriggerSelect {

    public static List<String> selector = new ArrayList<>();
    public static Map<UUID, Map<Integer, List<Item>>> usersTrigger = new HashMap<>();
    public static Map<Integer, List<Item>> defaultTrigger = new HashMap<>();


	public static Map<Integer, List<Item>> getUserTrigger(UUID pUsername) {
		if (pUsername == null) {
			return defaultTrigger;
		}
		// 存在チェック、無かったら追加
		if (!usersTrigger.containsKey(pUsername)) {
            Map<Integer, List<Item>> lmap = new HashMap<>(defaultTrigger);
			usersTrigger.put(pUsername, lmap);
		}

		return usersTrigger.get(pUsername);
	}

	public static List<Item> getuserTriggerList(UUID pUsername, String pSelector) {
		if (!selector.contains(pSelector)) {
			selector.add(pSelector);
		}
		int lindex = selector.indexOf(pSelector);
		Map<Integer, List<Item>> lmap = getUserTrigger(pUsername);
		List<Item> llist;
		if (lmap.containsKey(lindex)) {
			llist = lmap.get(lindex);
		} else {
            llist = new ArrayList<>();
			lmap.put(lindex, llist);
		}
		return llist;
	}


	/**
	 * ユーザー毎にトリガーアイテムを設定する。
	 */
	public static void appendTriggerItem(UUID pUsername, String pSelector, String pIndexstr) {
		// トリガーアイテムの追加
		appendWeaponsIndex(pIndexstr, getuserTriggerList(pUsername, pSelector));
	}

	/**
	 * トリガーアイテムを解析して登録。
	 */
	private static void appendWeaponsIndex(String indexstr, List<Item> indexlist) {
		if (indexstr.isEmpty()) return;
		String[] s = indexstr.split(",");
		for (String t : s) {
            Item o = Item.REGISTRY.getObject(new ResourceLocation(t));
			if(o instanceof Item)
			{
                indexlist.add(o);
			}
		}
	}

	/**
	 * アイテムが指定されたトリガーに登録されているかを判定
	 */
	@Deprecated
	public static boolean checkWeapon(UUID pUsername, String pSelector, ItemStack pItemStack) {
		if (pItemStack.isEmpty()) return false;
		return checkTrigger(pUsername, pSelector, pItemStack.getItem());
	}

	public static boolean checkTrigger(UUID pUuid, String pSelector, Item pItem) {
		if (!selector.contains(pSelector)) {
			return false;
		}
//		if (CommonHelper.isLocalPlay()) {
//			return getuserTriggerList(null, pSelector).contains(pItemStack.getItem());
//		}
		if (!usersTrigger.containsKey(pUuid)) {
			return false;
		}
		return getuserTriggerList(pUuid, pSelector).contains(pItem);
	}

}
