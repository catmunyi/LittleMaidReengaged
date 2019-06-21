package net.blacklab.lmr.entity.littlemaid.mode;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.TextureBoxBase;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagList;

public class EntityMode_BookDecorder extends EntityModeBase {

	public EntityMode_BookDecorder(EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 8000;
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
	}

	@Override
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		if (pitemstack.getItem() instanceof ItemWritableBook) {
			if (ItemWritableBook.isNBTValid(pitemstack.getTagCompound())) {
				NBTTagList llist = pitemstack.getTagCompound().getTagList("pages", 8);
                StringBuilder ls = new StringBuilder();
				for (int li = 0; li < llist.tagCount(); li++) {
//					NBTTagString ltex = (NBTTagString)llist.tagAt(li);
                    ls.append(llist.getStringTagAt(li));
				}

                String[] lcommands = ls.toString().split(";");
                String[] lcom;
                TextureBoxBase[] lboxs = new TextureBoxBase[]{
						owner.textureData.textureBox[0],
						owner.textureData.textureBox[1]
				};
				int lcolor = owner.getColor();
				boolean lflag = false;
				for (String lt : lcommands) {
					lcom = lt.split(":");
					if (lcom.length > 1) {
						lcom[0] = lcom[0].trim();
						lcom[1] = lcom[1].trim();
						
						try {
                            switch (lcom[0]) {
                                case "color":
                                    lcolor = Integer.valueOf(lcom[1]) & 0x0f;
                                    lflag = true;
                                    break;
                                case "texture": {
                                    TextureBoxBase lbox = ModelManager.instance.getTextureBox(lcom[1]);
                                    if (lbox != null) {
                                        lboxs[0] = lbox;
                                        lflag = true;
                                    }
                                    break;
                                }
                                case "armor": {
                                    TextureBoxBase lbox = ModelManager.instance.getTextureBox(lcom[1]);
                                    if (lbox != null) {
                                        lboxs[1] = lbox;
                                        lflag = true;
                                    }
                                    break;
                                }
                                case "dominantArm":
                                    int li = Integer.valueOf(lcom[1]);
                                    owner.setDominantArm(li);
                                    break;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (pentityplayer.world.isRemote) {
					if (lflag) {
//						MMM_TextureManager.instance.postSetTexturePack(owner, lcolor, lboxs);
					}
				}
				return true;
			}
		}
		return false;
	}

}
