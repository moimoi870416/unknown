package unit;

public class Delay {
    private int count;
    private int countLimit;
    private boolean isPause;
    private boolean isLoop;

    public Delay(final int countLimit) {
        this.countLimit = countLimit;
        this.count = 0;
        this.isPause = true;
        this.isLoop = false;
    }

    public void play() {
        // 一次性執行
        this.isLoop = false;// 一次性執行所以isLoop設定為false
        this.isPause = false;// 將暫停取消 開始計時
    }

    public void loop() {
        // 反覆執行
        this.isLoop = true;// 反覆執行因此設定為true
        this.isPause = false;// 將暫停取消 開始計時
    }

    public void pause() {
        this.isPause = true;// 暫停
    }

    public void stop() {
        this.count = 0;// 如果是停止的話我們需要順便重置count
        this.isPause = true;
    }

    public boolean isStop() {
        return this.count == 0 && this.isPause;
    }

    public boolean isPause() {
        return this.isPause;
    }

    public boolean isPlaying() {
        return !this.isPause;
    }

    public boolean count() {
        if (this.isPause) {
            return false;
        }

        // 如果目前已經計算到需要計時的範圍，則回傳true通知外面可以執行事件
        if (this.count >= this.countLimit) {
            if (this.isLoop) {
                // 如果是週期性執行的情況就重新將count歸零繼續計算
                this.count = 0;
            } else {
                // 如果是一次性執行的情況我們呼叫stop()停止這個事件的進行
                this.stop();
            }
            return true;
        }
        this.count++;// 沒有算到要觸發 同時也 不是暫停的情況 計算次數增加
        return false;// 還沒觸發
    }

}
