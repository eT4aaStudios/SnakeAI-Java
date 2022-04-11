package com.snake.ai;

public interface AndroidConnection {
    void startService();

    void stopService();

    void updateNotification(int population);

    boolean isMyServiceRunning();

    void toast(String text);
}
