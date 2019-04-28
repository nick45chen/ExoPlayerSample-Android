# ExoPlayerSample-Android

## How to Use it
在`xml`檔案下，使用`player`module裡面的`UIPlayerController`</br>

``` xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <net.nickchen450.player.UIPlayerController
        android:id="@+id/player_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

	<...>

</FrameLayout>
```

### 在Activity/Fragment中
* 設置媒體資料來源

``` java
ui_media_controller_view.setMediaResourceUrl(URL_MP3, false)
```

* 啟動

``` java
ui_media_controller_view.play()
```

* 暫停

``` java
ui_media_controller_view.pause()
```

* 回復播放

``` java
ui_media_controller_view.resume()
```

* 停止

``` java
ui_media_controller_view.stop()
```

* 釋放</br>
在`Activity`/`Fragment`中，或不使用`UIPlayerController`時，需銷毀視圖釋放播放器資源

``` java
override fun onDestroy() {
	super.onDestroy()
	ui_media_controller_view.release()
}
```

</br>

## Public Method

``` java
/**
* 設定媒體資料來源Url
* @param url 網址
* @param autoPlayWhenReady 媒體來源加載後，自動播放
**/
fun setMediaResourceUrl(url: String, autoPlayWhenReady: Boolean)

/**
* 從頭播放
**/
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
```
