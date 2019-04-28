package net.nickchen450.player

/**
 * 播放器狀態監聽器
 * */
interface IPlayerListener {
    /**
     * 緩衝中
     * */
    fun buffering()
    /**
     * 閒置
     * */
    fun idel()
    /**
     * 播放完成
     * */
    fun end()
    /**
     * 播放中
     * @param position 當前播放進度(毫秒)
     * */
    fun playing(position: Long)
    /**
     * 加載來源成功，可以準備播放
     * */
    fun ready()

    /**
     * 加載/播放失敗
     * */
    fun fail(message: String?)
}