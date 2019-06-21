package net.blacklab.lmr.entity.ai;

public interface IEntityAILM {

	//実行可能判定
	/**
	 * WARNING: This method have not better be called from outside this mod. If you do, you are responsible to make the flag back.
	 * @return Whether this ai works
	 */
    boolean getEnable();

	/**
     * WARNING: This method have not better be called from outside this mod. If you do, you are responsible to make the flag back.
     * @param pFlag Whether this ai works
     */
    void setEnable(boolean pFlag);
	/*
	  モードチェンジ実行時に設定される動作状態。
	 */
//	public void setDefaultEnable();

}
