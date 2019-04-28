package net.nickchen450.player

/**
 * 定義 PlayerController 所有功能接口
 * */
public interface IPlayerController {
    /**
     * 設定媒體資料來源Url
     * @param url 網址
     * @param autoPlayWhenReady 媒體來源加載後，自動播放
     * */
    fun setMediaResourceUrl(url: String, autoPlayWhenReady: Boolean)

    /**
     * 從頭播放
     * */
    fun play()

    /**
     * 暫停
     * */
    fun pause()

    /**
     * 回復當前播放進度
     * */
    fun resume()

    /**
     * 停止
     * */
    fun stop()

    /**
     * 從某個時間點，開始播放
     * */
    fun seekToPosition(position: Long)

    /**
     * 釋放所有播放資源，須在 Destroy view 時，釋放
     * */
    fun release()

    /**
     * 是否正在播放中
     * */
    fun isPlaying(): Boolean

    /**
     * 取得媒體播放時間總長度
     */
    fun getEntireTime(): Long

    /**
     * 取得當前播放軸時間
     * */
    fun getCurrentPointTime(): Long

    /**
     * 監聽播放器狀態
     * @see IPlayerListener
     * */
    fun setPlayerListener(listener: IPlayerListener)
}